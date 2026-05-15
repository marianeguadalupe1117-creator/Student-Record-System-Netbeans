package com.mycompany.studentrecordsystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;

public class OllamaAdminService {

    private static final String OLLAMA_URL = "http://localhost:11434/api/chat";
    private static final String OLLAMA_MODEL = "qwen2.5-coder:7b";

    private static final String[] ALLOWED_TABLE_NAMES = {
            "students", "courses", "curriculum", "departments", "enrollments", "grades",
            "instructors", "rooms", "schedules", "school_years", "sections", "semesters",
            "subjects", "users", "admins", "items"
    };

    private static final Set<String> ALLOWED_TABLES = Set.of(ALLOWED_TABLE_NAMES);

    public static JSONObject getAdminAction(String adminPrompt) throws Exception {
        return getAdminAction(adminPrompt, null, 0, null);
    }

    public static JSONObject getAdminAction(String adminPrompt, Consumer<String> progressCallback) throws Exception {
        return getAdminAction(adminPrompt, null, 0, progressCallback);
    }

    public static JSONObject getAdminAction(String adminPrompt, String selectedTable, int selectedId) throws Exception {
        return getAdminAction(adminPrompt, selectedTable, selectedId, null);
    }

    public static JSONObject getAdminAction(String adminPrompt, String selectedTable, int selectedId, Consumer<String> progressCallback) throws Exception {
        /*
         * Hybrid flow:
         * 1. Local intent runs first so commands like "delete Ana Esteban" can use the currently opened table.
         * 2. If local intent cannot solve it, direct SQL is allowed as a fallback.
         * 3. All delete/remove actions are treated as soft delete/archive, not permanent DELETE.
         */
        notifyProgress(progressCallback, "Ana is analyzing your request...");

        JSONObject localIntent = detectIntentLocally(adminPrompt, selectedTable, selectedId);
        if (isUsableIntentAction(localIntent)) {
            localIntent.put("analysis_layer", "intent");
            return localIntent;
        }

        notifyProgress(progressCallback, "Ana is checking the database operation...");

        JSONObject directSqlAction = tryDirectSql(adminPrompt, selectedTable, selectedId);
        if (isUsableDirectSqlAction(directSqlAction)) {
            directSqlAction.put("analysis_layer", "direct_ai");
            return directSqlAction;
        }

        JSONObject aiIntent = tryOllamaIntent(adminPrompt, selectedTable, selectedId);
        if (isUsableIntentAction(aiIntent)) {
            aiIntent.put("analysis_layer", "intent");
            return aiIntent;
        }

        if (isUnknownIntent(localIntent)) {
            localIntent.put("analysis_layer", "intent");
            return localIntent;
        }

        if (isUnknownIntent(aiIntent)) {
            aiIntent.put("analysis_layer", "intent");
            return aiIntent;
        }

        if (isUnknownIntent(directSqlAction)) {
            directSqlAction.put("analysis_layer", "direct_ai");
            return directSqlAction;
        }

        return unknown("Invalid information.").put("analysis_layer", "intent");
    }

    private static void notifyProgress(Consumer<String> progressCallback, String message) {
        if (progressCallback != null) {
            progressCallback.accept(message);
        }
    }

    private static JSONObject tryDirectSql(String adminPrompt, String selectedTable, int selectedId) {
        try {
            return callOllamaDirectSql(adminPrompt, selectedTable, selectedId);
        } catch (Exception e) {
            return null;
        }
    }

    private static JSONObject tryOllamaIntent(String adminPrompt, String selectedTable, int selectedId) {
        try {
            return callOllama(adminPrompt, selectedTable, selectedId);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isUsableIntentAction(JSONObject action) {
        if (action == null) return false;
        String intent = action.optString("intent", "").trim();
        if (intent.isEmpty() || intent.equals("unknown_intent") || intent.equals("execute_sql")) return false;
        return action.optJSONObject("data") != null;
    }

    private static boolean isUnknownIntent(JSONObject action) {
        return action != null && "unknown_intent".equals(action.optString("intent", ""));
    }

    private static boolean isUsableDirectSqlAction(JSONObject action) {
        if (action == null) return false;
        if (!"execute_sql".equals(action.optString("intent", "").trim())) return false;

        String sql = extractSqlFromAction(action);
        if (sql.isBlank()) return false;

        String firstWord = firstSqlWord(sql);
        if (!(firstWord.equals("select") || firstWord.equals("insert") || firstWord.equals("update") || firstWord.equals("delete"))) {
            return false;
        }

        String operation = action.optString("operation", "").trim().toUpperCase();
        if (operation.isBlank()) {
            operation = switch (firstWord) {
                case "select" -> "READ";
                case "insert" -> "CREATE";
                case "update" -> "UPDATE";
                case "delete" -> "DELETE";
                default -> "UNKNOWN";
            };
            action.put("operation", operation);
        }

        if (!(operation.equals("READ") || operation.equals("CREATE") || operation.equals("UPDATE") || operation.equals("DELETE"))) {
            return false;
        }

        return looksSafeEnoughForFirstLayer(sql);
    }

    private static JSONObject detectIntentLocally(String prompt, String selectedTable, int selectedId) {
        String text = normalize(prompt);
        String original = prompt == null ? "" : prompt.trim();

        String table = detectTable(text);
        if (table == null && selectedTable != null && !selectedTable.isBlank()) {
            /*
             * Use the currently opened dashboard table when the user gives a natural command
             * without saying the table name, for example: "delete Ana Esteban".
             */
            table = selectedTable.trim();
        }

        int id = extractIdForTable(text, table);
        if (id <= 0 && referencesSelectedRecord(text) && selectedId > 0) {
            id = selectedId;
        }

        boolean isRead = containsAny(text,
                "show", "list", "view", "get", "find", "display", "select", "see", "open", "fetch", "give me");

        boolean isCreate = containsAny(text,
                "create", "add", "insert", "new", "register", "save new", "make new", "encode", "record new");

        boolean isUpdate = containsAny(text,
                "update", "change", "edit", "set", "modify", "rename", "correct", "revise", "make", "turn");

        boolean isDelete = containsAny(text,
                "delete", "remove", "drop", "erase", "destroy", "permanently remove");

        boolean isArchivedRead = containsAny(text,
                "archived", "archive list", "inactive", "deactivated", "disabled", "not active");

        boolean isActiveRead = containsAny(text,
                "active", "enabled", "current") && !isArchivedRead;

        boolean isArchiveAction = containsAny(text,
                "archive", "deactivate", "disable", "mark inactive", "set inactive", "move to archive", "soft delete");

        boolean isRestoreAction = containsAny(text,
                "restore", "unarchive", "reactivate", "activate again", "recover", "bring back", "set active");

        boolean isCount = containsAny(text,
                "count", "how many", "total number", "number of");

        JSONObject enrollIntent = detectEnrollmentIntent(original, text);
        if (enrollIntent != null) {
            return enrollIntent;
        }

        JSONObject statusIntent = detectStatusUpdateIntent(original, text, table, id, isUpdate, isArchiveAction, isRestoreAction);
        if (statusIntent != null) {
            return statusIntent;
        }

        if (table != null && isRestoreAction && id > 0) {
            return makeIdIntent("restore_record", table, id);
        }

        if (table != null && isArchivedRead && isRead && id <= 0) {
            if (table.equals("students")) {
                return new JSONObject().put("intent", "get_archived_students").put("data", new JSONObject());
            }
            return new JSONObject().put("intent", "get_archived_records").put("data", new JSONObject().put("table", table));
        }

        if (table != null && isActiveRead && isRead && id <= 0) {
            return new JSONObject().put("intent", "get_active_records").put("data", new JSONObject().put("table", table));
        }

        if (table != null && isArchiveAction && id > 0 && !isArchivedRead) {
            return makeIdIntent("archive_record", table, id);
        }

        if (table != null && isDelete && id > 0) {
            return makeIdIntent("archive_record", table, id);
        }

        if (table != null && (isDelete || isArchiveAction) && id <= 0 && !isArchivedRead) {
            String targetName = extractRecordNameTarget(original, table);

            if (!targetName.isEmpty()) {
                return new JSONObject()
                        .put("intent", "archive_record_by_name")
                        .put("data", new JSONObject()
                                .put("table", table)
                                .put("name", targetName));
            }
        }

        if (table != null && isRestoreAction && id <= 0) {
            String targetName = extractRecordNameTarget(original, table);

            if (!targetName.isEmpty()) {
                return new JSONObject()
                        .put("intent", "restore_record_by_name")
                        .put("data", new JSONObject()
                                .put("table", table)
                                .put("name", targetName));
            }
        }

        if (table != null && isCreate) {
            JSONObject values = extractValues(original, text, table);

            if (values.length() == 0) {
                return unknown("I understand that you want to create a record, but the needed values are missing.");
            }

            return new JSONObject()
                    .put("intent", "create_record")
                    .put("data", new JSONObject()
                            .put("table", table)
                            .put("values", values));
        }

        if (table != null && isUpdate && id > 0) {
            JSONObject values = extractValues(original, text, table);

            if (values.length() == 0) {
                return null;
            }

            return new JSONObject()
                    .put("intent", "update_record")
                    .put("data", new JSONObject()
                            .put("table", table)
                            .put("id", id)
                            .put("values", values));
        }

        if (table != null && isCount && isArchivedRead) {
            return new JSONObject().put("intent", "count_archived_records").put("data", new JSONObject().put("table", table));
        }

        if (table != null && isCount && isActiveRead) {
            return new JSONObject().put("intent", "count_active_records").put("data", new JSONObject().put("table", table));
        }

        if (table != null && isCount) {
            return new JSONObject().put("intent", "count_records").put("data", new JSONObject().put("table", table));
        }

        if (table != null && isRead && id > 0) {
            return makeIdIntent("get_record_by_id", table, id);
        }

        if (table != null && isRead) {
            return new JSONObject().put("intent", "get_table_records").put("data", new JSONObject().put("table", table));
        }

        return null;
    }

    private static JSONObject detectEnrollmentIntent(String original, String text) {
        if (!containsAny(text, "enroll", "enrolled", "enrollment", "enrol")) {
            return null;
        }

        int studentId = extractLabeledId(text, "student", "learner");
        if (studentId <= 0) {
            return unknown("Please include the student id to enroll the student.");
        }

        JSONObject values = new JSONObject();
        values.put("student_id", studentId);

        putIfPositive(values, "course_id", extractLabeledId(text, "course", "program"));
        putIfPositive(values, "section_id", extractLabeledId(text, "section", "block"));
        putIfPositive(values, "subject_id", extractLabeledId(text, "subject"));
        putIfPositive(values, "semester_id", extractLabeledId(text, "semester", "sem"));
        putIfPositive(values, "school_year_id", extractLabeledId(text, "school year", "school_year", "sy"));
        putIfPositive(values, "schedule_id", extractLabeledId(text, "schedule"));

        String status = extractStatusValue(original, text);
        if (!status.isEmpty()) {
            values.put("status", status);
        }

        return new JSONObject()
                .put("intent", "create_record")
                .put("data", new JSONObject()
                        .put("table", "enrollments")
                        .put("values", values));
    }

    private static JSONObject detectStatusUpdateIntent(String original, String text, String table, int id,
                                                       boolean isUpdate, boolean isArchiveAction, boolean isRestoreAction) {
        boolean talksAboutStatus = containsAny(text, "status", "state", "active", "inactive", "archived", "archive", "deactivate", "reactivate", "disable", "enable");

        if (table == null || id <= 0 || !talksAboutStatus) {
            return null;
        }

        if (isArchiveAction && !containsAny(text, "status to", "status as", "change status", "set status")) {
            return makeIdIntent("archive_record", table, id);
        }

        if (isRestoreAction && !containsAny(text, "status to", "status as", "change status", "set status")) {
            return makeIdIntent("restore_record", table, id);
        }

        if (!isUpdate && !containsAny(text, "status", "make", "turn")) {
            return null;
        }

        String status = extractStatusValue(original, text);
        if (status.isEmpty()) {
            return null;
        }

        return new JSONObject()
                .put("intent", "update_record")
                .put("data", new JSONObject()
                        .put("table", table)
                        .put("id", id)
                        .put("values", new JSONObject().put("status", status)));
    }

    private static String extractRecordNameTarget(String original, String table) {
        if (original == null) {
            return "";
        }

        String value = original.trim();

        value = value.replaceFirst("(?i)^\\s*(delete|remove|archive|deactivate|disable|soft\\s+delete|restore|unarchive|reactivate|recover)\\s+", "");

        String singular = tableSingular(table);
        if (singular != null && !singular.isBlank()) {
            value = value.replaceFirst("(?i)^\\s*" + java.util.regex.Pattern.quote(singular) + "s?\\s+", "");
        }

        if (table != null && !table.isBlank()) {
            value = value.replaceFirst("(?i)^\\s*" + java.util.regex.Pattern.quote(table.replace("_", " ")) + "\\s+", "");
            value = value.replaceFirst("(?i)^\\s*" + java.util.regex.Pattern.quote(table) + "\\s+", "");
        }

        value = value.replaceFirst("(?i)^\\s*(record|row|entry)\\s+", "");
        value = value.replaceAll("(?i)\\b(id|#|number|no\\.?|record id)\\s*\\d+\\b", "");
        value = value.replaceAll("(?i)\\b(from|in)\\s+[a-zA-Z_ ]+$", "");
        value = value.replaceAll("[,;]+$", "");
        value = value.replaceAll("\\s+", " ").trim();

        if (value.matches("\\d+")) {
            return "";
        }

        return value;
    }

    private static JSONObject extractValues(String original, String normalizedText, String table) {
        JSONObject values = new JSONObject();
        String text = original == null ? "" : original.trim();

        extractExplicitKeyValues(text, values);

        String status = extractStatusValue(original, normalizedText);
        if (!status.isEmpty()) {
            values.put("status", status);
        }

        String email = extractEmail(text);
        if (!email.isEmpty()) {
            values.put("email", email);
        }

        putIfNotEmpty(values, "password", extractSingleValue(text, "password"));
        putIfNotEmpty(values, "role", extractSingleValue(text, "role"));
        putIfNotEmpty(values, "gender", extractSingleValue(text, "gender"));
        putIfNotEmpty(values, "birth_date", extractSingleValue(text, "birth_date"));
        putIfNotEmpty(values, "birth_date", extractSingleValue(text, "birthday"));
        putIfNotEmpty(values, "address", extractSingleValue(text, "address"));
        putIfNotEmpty(values, "contact_number", extractSingleValue(text, "contact"));
        putIfNotEmpty(values, "student_number", extractSingleValue(text, "student_number"));
        putIfNotEmpty(values, "student_status", extractSingleValue(text, "student_status"));

        putIfPositive(values, "student_id", extractLabeledId(normalizedText, "student", "learner"));
        putIfPositive(values, "course_id", extractLabeledId(normalizedText, "course", "program"));
        putIfPositive(values, "section_id", extractLabeledId(normalizedText, "section", "block"));
        putIfPositive(values, "subject_id", extractLabeledId(normalizedText, "subject"));
        putIfPositive(values, "instructor_id", extractLabeledId(normalizedText, "instructor", "teacher", "faculty"));
        putIfPositive(values, "room_id", extractLabeledId(normalizedText, "room", "classroom", "lab"));
        putIfPositive(values, "semester_id", extractLabeledId(normalizedText, "semester", "sem"));
        putIfPositive(values, "school_year_id", extractLabeledId(normalizedText, "school year", "school_year", "sy"));
        putIfPositive(values, "schedule_id", extractLabeledId(normalizedText, "schedule"));
        putIfPositive(values, "year_level", extractLabeledId(normalizedText, "year level", "year"));

        extractNaturalName(text, normalizedText, table, values);
        extractNaturalNumberValues(normalizedText, table, values);

        if (table.equals("users")) {
            if (!values.has("status")) values.put("status", "Active");
            if (!values.has("role")) values.put("role", "user");
        }

        if (table.equals("admins")) {
            if (!values.has("status")) values.put("status", "Active");
            if (!values.has("role")) values.put("role", "admin");
        }

        return values;
    }

    private static void extractExplicitKeyValues(String text, JSONObject values) {
        Pattern keyValuePattern = Pattern.compile(
                "([a-zA-Z_][a-zA-Z0-9_]*)\\s*(?:=|:|\\bto\\b|\\bas\\b)\\s*([^,;]+?)(?=\\s+(?:and\\s+)?[a-zA-Z_][a-zA-Z0-9_]*\\s*(?:=|:|\\bto\\b|\\bas\\b)|[,;]|$)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = keyValuePattern.matcher(text == null ? "" : text);

        while (matcher.find()) {
            String key = normalizeKey(matcher.group(1));
            String value = cleanValue(matcher.group(2));

            if (isIgnoredValueKey(key) || value.isEmpty()) {
                continue;
            }

            values.put(key, value);
        }
    }

    private static void extractNaturalName(String original, String normalizedText, String table, JSONObject values) {
        String name = "";

        Pattern namedPattern = Pattern.compile(
                "(?i)\\b(?:named|called)\\s+([a-zA-ZñÑ. '\\-]+?)(?=\\s+(?:with|email|role|password|status|gender|course|section|year|as)\\b|[,;.]|$)"
        );
        Matcher namedMatcher = namedPattern.matcher(original == null ? "" : original);
        if (namedMatcher.find()) {
            name = namedMatcher.group(1).replaceAll("\\s+", " ").trim();
        }

        if (name.isEmpty()) {
            Pattern nameToPattern = Pattern.compile("(?i)\\b(?:name|rename)\\s*(?:to|as|=|:)\\s+([^,;]+)");
            Matcher m = nameToPattern.matcher(original == null ? "" : original);
            if (m.find()) {
                name = cleanValue(m.group(1));
            }
        }

        if (name.isEmpty()) {
            return;
        }

        switch (table) {
            case "students" -> putPersonName(values, name);
            case "instructors" -> putPersonName(values, name);
            case "users", "admins" -> values.put("full_name", name);
            case "courses" -> values.put("course_name", name);
            case "subjects" -> values.put("subject_name", name);
            case "rooms" -> values.put("room_name", name);
            case "sections" -> values.put("section_name", name);
            case "departments" -> values.put("department_name", name);
            case "semesters" -> values.put("semester_name", name);
            case "items" -> values.put("item_name", name);
            default -> values.put("name", name);
        }
    }

    private static void extractNaturalNumberValues(String normalizedText, String table, JSONObject values) {
        String price = extractMoneyValue(normalizedText);
        if (!price.isEmpty() && (table.equals("items") || containsAny(normalizedText, "price", "cost", "amount"))) {
            values.put("price", price);
        }

        String capacity = extractNumberAfterWords(normalizedText, "capacity", "slots", "seats");
        if (!capacity.isEmpty()) {
            values.put("capacity", capacity);
        }

        String units = extractNumberAfterWords(normalizedText, "units", "unit");
        if (!units.isEmpty()) {
            values.put("units", units);
        }
    }

    private static String extractMoneyValue(String text) {
        Pattern pattern = Pattern.compile(
                "(?:price|cost|amount)?\\s*(?:to|as|=|is|for)?\\s*(?:php|p|peso|pesos|₱)?\\s*(\\d+(?:\\.\\d{1,2})?)\\s*(?:php|p|peso|pesos)?",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(text == null ? "" : text);

        String last = "";
        while (matcher.find()) {
            last = matcher.group(1);
        }
        return last == null ? "" : last.trim();
    }

    private static String extractNumberAfterWords(String text, String... words) {
        for (String word : words) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\s*(?:to|as|=|is|:)??\\s*(\\d+)\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text == null ? "" : text);
            if (matcher.find()) {
                return matcher.group(1).trim();
            }
        }
        return "";
    }

    private static String extractStatusValue(String original, String normalizedText) {
        String value = extractSingleValue(original, "status");
        if (!value.isEmpty()) {
            return normalizeStatus(value);
        }

        Pattern statusPattern = Pattern.compile("(?i)\\b(?:make|turn|mark|set)\\s+[^,;]*?\\b(active|inactive|archived|disabled|enabled|deactivated|reactivated)\\b");
        Matcher statusMatcher = statusPattern.matcher(original == null ? "" : original);
        if (statusMatcher.find()) {
            return normalizeStatus(statusMatcher.group(1));
        }

        if (containsAny(normalizedText, "archive", "archived", "inactive", "deactivate", "disabled", "not active")) {
            return "Archived";
        }

        if (containsAny(normalizedText, "active", "activate", "reactivate", "enabled")) {
            return "Active";
        }

        return "";
    }

    private static String normalizeStatus(String value) {
        String v = value == null ? "" : value.toLowerCase().replaceAll("[^a-z ]", "").trim();
        if (v.contains("archive") || v.contains("inactive") || v.contains("disable") || v.contains("deactivate") || v.contains("not active")) {
            return "Archived";
        }
        if (v.contains("active") || v.contains("enable") || v.contains("reactivate")) {
            return "Active";
        }
        return value == null ? "" : value.trim();
    }

    private static int extractIdForTable(String text, String table) {
        if (table != null) {
            String singular = tableSingular(table);
            int labeled = extractLabeledId(text, singular, table);
            if (labeled > 0) {
                return labeled;
            }
        }

        Pattern explicitId = Pattern.compile("\\b(?:id|#|record|record id|no|number)\\s*#?\\s*(\\d+)\\b");
        Matcher explicitMatcher = explicitId.matcher(text == null ? "" : text);
        if (explicitMatcher.find()) {
            return parseIntSafe(explicitMatcher.group(1));
        }

        if (table != null) {
            return 0;
        }

        Pattern generalNumber = Pattern.compile("\\b(\\d+)\\b");
        Matcher generalMatcher = generalNumber.matcher(text == null ? "" : text);
        if (generalMatcher.find()) {
            return parseIntSafe(generalMatcher.group(1));
        }

        return 0;
    }

    private static int extractLabeledId(String text, String... labels) {
        String safeText = text == null ? "" : text;

        for (String label : labels) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(label.toLowerCase()) + "s?\\s*(?:id|#|no|number)?\\s*#?\\s*(\\d+)\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(safeText);
            if (matcher.find()) {
                return parseIntSafe(matcher.group(1));
            }
        }

        return 0;
    }

    private static String detectTable(String text) {
        if (containsAny(text, "school year", "school_year", "school years", "school_years", "sy")) return "school_years";
        if (containsAny(text, "student", "students", "learner", "learners")) return "students";
        if (containsAny(text, "enroll", "enrollment", "enrollments", "enrolled")) return "enrollments";
        if (containsAny(text, "course", "courses", "program", "programs")) return "courses";
        if (containsAny(text, "curriculum", "curricula")) return "curriculum";
        if (containsAny(text, "department", "departments", "dept")) return "departments";
        if (containsAny(text, "grade", "grades", "mark", "marks")) return "grades";
        if (containsAny(text, "instructor", "instructors", "teacher", "teachers", "faculty")) return "instructors";
        if (containsAny(text, "room", "rooms", "classroom", "classrooms", "laboratory", "lab")) return "rooms";
        if (containsAny(text, "schedule", "schedules", "timetable", "time table")) return "schedules";
        if (containsAny(text, "section", "sections", "block", "blocks")) return "sections";
        if (containsAny(text, "semester", "semesters", "sem")) return "semesters";
        if (containsAny(text, "subject", "subjects", "course subject", "course subjects")) return "subjects";
        if (containsAny(text, "admin account", "admin accounts", "admin")) return "admins";
        if (containsAny(text, "user", "users", "account", "accounts")) return "users";
        if (containsAny(text, "item", "items", "product", "products", "menu item", "menu items")) return "items";

        return null;
    }

    private static JSONObject makeIdIntent(String intent, String table, int id) {
        return new JSONObject()
                .put("intent", intent)
                .put("data", new JSONObject()
                        .put("table", table)
                        .put("id", id));
    }

    private static JSONObject unknown(String message) {
        return new JSONObject()
                .put("intent", "unknown_intent")
                .put("data", new JSONObject().put("message", message));
    }

    private static void putIfPositive(JSONObject obj, String key, int value) {
        if (value > 0) {
            obj.put(key, value);
        }
    }

    private static void putIfNotEmpty(JSONObject obj, String key, String value) {
        if (value != null && !value.trim().isEmpty()) {
            obj.put(key, value.trim());
        }
    }

    private static void putPersonName(JSONObject values, String name) {
        String[] parts = name.trim().replaceAll("\\s+", " ").split(" ");
        if (parts.length == 1) {
            values.put("first_name", parts[0]);
            return;
        }

        values.put("first_name", parts[0]);
        values.put("last_name", parts[parts.length - 1]);

        if (parts.length > 2) {
            StringBuilder middle = new StringBuilder();
            for (int i = 1; i < parts.length - 1; i++) {
                if (middle.length() > 0) middle.append(" ");
                middle.append(parts[i]);
            }
            values.put("middle_name", middle.toString());
        }
    }

    private static String extractEmail(String text) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        Matcher matcher = pattern.matcher(text == null ? "" : text);
        return matcher.find() ? matcher.group().trim() : "";
    }

    private static String extractSingleValue(String text, String key) {
        Pattern pattern = Pattern.compile(
                "(?i)\\b" + Pattern.quote(key) + "\\s*(?:=|:|to|as|is)?\\s*([^,;]+?)(?=\\s+(?:and\\s+)?[a-zA-Z_][a-zA-Z0-9_]*\\s*(?:=|:|to|as|is)|[,;]|$)"
        );
        Matcher matcher = pattern.matcher(text == null ? "" : text);
        return matcher.find() ? cleanValue(matcher.group(1)) : "";
    }

    private static String cleanValue(String value) {
        if (value == null) return "";
        return value.replaceAll("(?i)\\b(pesos|peso|php)\\b", "")
                .replaceAll("(?i)\\s+and$", "")
                .replace("₱", "")
                .trim();
    }

    private static String normalizeKey(String key) {
        if (key == null) return "";
        String k = key.trim().toLowerCase();
        return switch (k) {
            case "fullname", "full_name", "name" -> "name";
            case "firstname" -> "first_name";
            case "middlename" -> "middle_name";
            case "lastname", "surname" -> "last_name";
            case "studentnumber" -> "student_number";
            case "contact", "phone", "phonenumber" -> "contact_number";
            case "birthday", "birthdate" -> "birth_date";
            case "year", "yearlevel" -> "year_level";
            default -> k;
        };
    }

    private static boolean isIgnoredValueKey(String key) {
        return key.equals("id")
                || key.equals("student")
                || key.equals("students")
                || key.equals("course")
                || key.equals("courses")
                || key.equals("room")
                || key.equals("rooms")
                || key.equals("subject")
                || key.equals("subjects")
                || key.equals("instructor")
                || key.equals("instructors")
                || key.equals("schedule")
                || key.equals("schedules")
                || key.equals("section")
                || key.equals("sections")
                || key.equals("user")
                || key.equals("users")
                || key.equals("account")
                || key.equals("accounts")
                || key.equals("item")
                || key.equals("items")
                || key.equals("product")
                || key.equals("products")
                || key.equals("admin")
                || key.equals("admins");
    }

    private static boolean referencesSelectedRecord(String text) {
        return containsAny(text, "this", "selected", "current record", "current row", "that record", "this record", "this user", "this student");
    }

    private static String normalize(String text) {
        if (text == null) {
            return "";
        }

        return text.toLowerCase()
                .replaceAll("[^a-z0-9_@.\\-#:/₱ ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static boolean containsAny(String text, String... words) {
        String safeText = text == null ? "" : text;
        for (String word : words) {
            if (safeText.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String tableSingular(String table) {
        if (table == null || table.isBlank()) return "";
        if (table.equals("curriculum")) return "curriculum";
        if (table.equals("school_years")) return "school year";
        if (table.endsWith("ies")) return table.substring(0, table.length() - 3) + "y";
        if (table.endsWith("s")) return table.substring(0, table.length() - 1);
        return table;
    }

    private static JSONObject callOllamaDirectSql(String adminPrompt, String selectedTable, int selectedId) throws Exception {
        String contextText = "";
        if (selectedTable != null && !selectedTable.isBlank() && selectedId > 0) {
            contextText = "\nCurrent selected record context: table=" + selectedTable + ", id=" + selectedId + ". Use this only when the user says this/selected/current record.";
        }

        String schemaText = buildSchemaForPrompt();

        String systemInstruction = """
            You are the first-layer admin database operation planner for a Java Swing student record system.

            Convert the admin's natural language request into exactly one safe MySQL CRUD SQL operation.
            Return only one JSON object. No markdown. No explanation outside JSON.

            JSON format when you can answer:
            {"intent":"execute_sql","operation":"READ|CREATE|UPDATE|DELETE","sql":"SQL_HERE","requires_confirmation":true|false,"message":"short admin-friendly message"}

            JSON format when the request is missing important data or is not a database operation:
            {"intent":"unknown_intent","data":{"message":"short reason"}}

            Hard rules:
            - Use only SELECT, INSERT, UPDATE, or DELETE.
            - Never use DROP, TRUNCATE, ALTER, CREATE, REPLACE, GRANT, REVOKE, CALL, EXECUTE, LOAD, OUTFILE, or INFILE.
            - Return exactly one SQL statement only.
            - Do not use SQL comments.
            - For SELECT list/search commands, add LIMIT 100 unless the command asks for a count/total.
            - UPDATE and DELETE must include a clear WHERE clause using an id, code, exact email, exact name, or selected record context.
            - If the command says delete/remove/archive/deactivate/disable/soft delete, use UPDATE and set the status-like column to 'Archived'. Do not use DELETE unless the user explicitly says "permanently delete".
            - If the command says restore/unarchive/reactivate/activate, use UPDATE and set the status-like column to 'Active'.
            - If the table has user_status/admin_status/student_status instead of status, use the real column from the schema.
            - Do not invent columns. Use only columns that appear in the schema.
            - If a required id/value/table/column is unclear, return unknown_intent.
            - For READ operations, requires_confirmation must be false.
            - For CREATE, UPDATE, and DELETE operations, requires_confirmation must be true.
            - Do not use parameter placeholders like ?. Put the literal values directly in SQL and escape single quotes by doubling them.

            Examples:
            "show all students" -> {"intent":"execute_sql","operation":"READ","sql":"SELECT * FROM students LIMIT 100","requires_confirmation":false,"message":"Showing students."}
            "how many active students" -> {"intent":"execute_sql","operation":"READ","sql":"SELECT COUNT(*) AS total FROM students WHERE status = 'Active'","requires_confirmation":false,"message":"Counting active students."}
            "delete Ana Esteban" with selected table students -> {"intent":"execute_sql","operation":"UPDATE","sql":"UPDATE students SET status = 'Archived' WHERE LOWER(CONCAT_WS(' ', first_name, middle_name, last_name)) LIKE '%ana%' AND LOWER(CONCAT_WS(' ', first_name, middle_name, last_name)) LIKE '%esteban%'","requires_confirmation":true,"message":"Archive matching student instead of permanently deleting."}
            "archive student 5" -> {"intent":"execute_sql","operation":"UPDATE","sql":"UPDATE students SET status = 'Archived' WHERE student_id = 5","requires_confirmation":true,"message":"Archive student 5."}
            "restore room 3" -> {"intent":"execute_sql","operation":"UPDATE","sql":"UPDATE rooms SET status = 'Active' WHERE room_id = 3","requires_confirmation":true,"message":"Restore room 3."}
            "change room 4 capacity to 45" -> {"intent":"execute_sql","operation":"UPDATE","sql":"UPDATE rooms SET capacity = 45 WHERE room_id = 4","requires_confirmation":true,"message":"Update room capacity."}

            """ + schemaText + contextText;

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", OLLAMA_MODEL);
        requestBody.put("stream", false);
        requestBody.put("options", new JSONObject()
                .put("temperature", 0)
                .put("top_p", 0.1));

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", systemInstruction));
        messages.put(new JSONObject().put("role", "user").put("content", adminPrompt));
        requestBody.put("messages", messages);

        URL url = new URL(OLLAMA_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.toString().getBytes("utf-8"));
        }

        int status = conn.getResponseCode();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream(), "utf-8")
        );

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        if (status < 200 || status >= 300) {
            throw new RuntimeException("Ollama error: " + status + " - " + response);
        }

        JSONObject obj = new JSONObject(response.toString());
        String content = obj.getJSONObject("message").getString("content");
        content = stripCodeFences(content);
        content = extractJsonObject(content);
        return new JSONObject(content);
    }

    private static String buildSchemaForPrompt() {
        StringBuilder schema = new StringBuilder("\nAllowed database schema:\n");

        try (Connection conn = DBConnection.getConnection()) {
            for (String table : ALLOWED_TABLE_NAMES) {
                schema.append("- ").append(table).append("(");

                String sql = """
                    SELECT COLUMN_NAME
                    FROM INFORMATION_SCHEMA.COLUMNS
                    WHERE TABLE_SCHEMA = DATABASE()
                      AND TABLE_NAME = ?
                    ORDER BY ORDINAL_POSITION
                """;

                boolean hasColumn = false;
                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setString(1, table);
                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            if (hasColumn) schema.append(", ");
                            schema.append(rs.getString("COLUMN_NAME"));
                            hasColumn = true;
                        }
                    }
                }

                if (!hasColumn) {
                    schema.append("columns unknown");
                }

                schema.append(")\n");
            }
        } catch (Exception e) {
            schema.append("Allowed tables only: ");
            for (int i = 0; i < ALLOWED_TABLE_NAMES.length; i++) {
                schema.append(ALLOWED_TABLE_NAMES[i]);
                if (i < ALLOWED_TABLE_NAMES.length - 1) schema.append(", ");
            }
            schema.append("\n");
        }

        return schema.toString();
    }

    private static String extractSqlFromAction(JSONObject action) {
        String sql = action.optString("sql", "").trim();
        if (!sql.isBlank()) return sql;

        JSONObject data = action.optJSONObject("data");
        if (data != null) {
            return data.optString("sql", "").trim();
        }

        return "";
    }

    private static String firstSqlWord(String sql) {
        String trimmed = sql == null ? "" : sql.trim().toLowerCase();
        Matcher matcher = Pattern.compile("^([a-z]+)").matcher(trimmed);
        return matcher.find() ? matcher.group(1) : "";
    }

    private static boolean looksSafeEnoughForFirstLayer(String sql) {
        if (sql == null || sql.trim().isBlank()) return false;

        String trimmed = sql.trim();
        String withoutLastSemicolon = trimmed.endsWith(";") ? trimmed.substring(0, trimmed.length() - 1) : trimmed;
        if (withoutLastSemicolon.contains(";")) return false;

        String lower = withoutLastSemicolon.toLowerCase().replaceAll("\\s+", " ").trim();
        if (lower.contains("--") || lower.contains("/*") || lower.contains("*/") || lower.contains("#")) return false;

        if (lower.matches(".*\\b(drop|truncate|alter|create|replace|grant|revoke|call|execute|load|outfile|infile|rename|use|lock|unlock)\\b.*")) {
            return false;
        }

        String first = firstSqlWord(withoutLastSemicolon);
        if ((first.equals("update") || first.equals("delete")) && !lower.matches(".*\\bwhere\\b.*")) {
            return false;
        }

        if (lower.contains("where 1=1") || lower.contains("where true")) {
            return false;
        }

        Pattern tablePattern = Pattern.compile("(?i)\\b(?:from|join|into|update)\\s+`?([a-zA-Z0-9_]+)`?(?:\\.`?([a-zA-Z0-9_]+)`?)?");
        Matcher matcher = tablePattern.matcher(withoutLastSemicolon);
        boolean foundTable = false;

        while (matcher.find()) {
            String table = matcher.group(2) != null ? matcher.group(2) : matcher.group(1);
            if (table == null || !ALLOWED_TABLES.contains(table.toLowerCase())) {
                return false;
            }
            foundTable = true;
        }

        return foundTable;
    }

    private static JSONObject callOllama(String adminPrompt, String selectedTable, int selectedId) throws Exception {

        String contextText = "";
        if (selectedTable != null && !selectedTable.isBlank() && selectedId > 0) {
            contextText = "\nCurrent selected record context: table=" + selectedTable + ", id=" + selectedId + ". Use this only when the user says this/selected/current record.";
        }

        String systemInstruction = """
            You are an admin database CRUD intent classifier for a student record system.

            Your job is to understand human admin commands and translate them into exactly one JSON object.
            No markdown. No explanation. No SQL.

            You are not allowed to invent tables, ids, or unknown required values.
            When the command lacks the id or required values, return unknown_intent with a short message.

            Admin can access these tables only:
            students, courses, curriculum, departments, enrollments, grades,
            instructors, rooms, schedules, school_years, sections, semesters, subjects,
            users, admins, items

            Allowed intents:
            get_table_records, get_record_by_id, get_archived_students, get_archived_records,
            get_active_records, count_records, count_archived_records, count_active_records,
            create_record, update_record, delete_record, archive_record, archive_record_by_name, restore_record, unknown_intent

            JSON formats:
            Read all: {"intent":"get_table_records","data":{"table":"students"}}
            Read one: {"intent":"get_record_by_id","data":{"table":"students","id":5}}
            Create: {"intent":"create_record","data":{"table":"courses","values":{"course_name":"BSIT"}}}
            Update: {"intent":"update_record","data":{"table":"courses","id":2,"values":{"course_name":"BSCS"}}}
            Delete permanently: {"intent":"delete_record","data":{"table":"subjects","id":3}}
            Archive/deactivate: {"intent":"archive_record","data":{"table":"students","id":3}}
            Restore/reactivate: {"intent":"restore_record","data":{"table":"students","id":3}}
            Unknown: {"intent":"unknown_intent","data":{"message":"Please include the table, id, and values."}}

            Intent meaning:
            show/list/view/get/display/find/see/fetch = READ.
            create/add/insert/register/new/encode/save new = CREATE.
            change/update/edit/set/modify/rename/correct/revise/make/turn = UPDATE.
            delete/remove/erase/drop/destroy = ARCHIVE, not permanent delete.
            permanently remove = DELETE only when the admin explicitly asks for permanent deletion.
            archive/deactivate/disable/mark inactive/soft delete = ARCHIVE, not delete.
            restore/unarchive/reactivate/activate again/recover/bring back = RESTORE.
            count/how many/total number/number of = COUNT.
            enroll/enrol/enrolled student = CREATE record in enrollments table.

            Column meaning:
            status/state/active/inactive means status.
            name/named/called means full_name for users/admins, first_name/last_name for students/instructors, and *_name for normal tables.
            email/mail means email or user_email depending on table.
            role/type means role or user_role.
            price/cost/amount means price.
            capacity/seats/slots means capacity.
            course/program id means course_id.
            section/block id means section_id.
            subject id means subject_id.
            instructor/teacher/faculty id means instructor_id.
            room/classroom/lab id means room_id.
            semester/sem id means semester_id.
            school year/sy id means school_year_id.

            Examples:
            "show all students" -> {"intent":"get_table_records","data":{"table":"students"}}
            "display student id 5" -> {"intent":"get_record_by_id","data":{"table":"students","id":5}}
            "archived students" -> {"intent":"get_archived_students","data":{}}
            "show archived subjects" -> {"intent":"get_archived_records","data":{"table":"subjects"}}
            "how many active students" -> {"intent":"count_active_records","data":{"table":"students"}}
            "delete Ana Esteban" with selected table students -> {"intent":"archive_record_by_name","data":{"table":"students","name":"Ana Esteban"}}
            "archive student 3" -> {"intent":"archive_record","data":{"table":"students","id":3}}
            "deactivate subject id 9" -> {"intent":"archive_record","data":{"table":"subjects","id":9}}
            "restore student 3" -> {"intent":"restore_record","data":{"table":"students","id":3}}
            "delete subject 3 permanently" -> {"intent":"delete_record","data":{"table":"subjects","id":3}}
            "change user 5 status to Archived" -> {"intent":"update_record","data":{"table":"users","id":5,"values":{"status":"Archived"}}}
            "make this user active" with selected users id 7 -> {"intent":"update_record","data":{"table":"users","id":7,"values":{"status":"Active"}}}
            "change the price of item 5 to 500 pesos" -> {"intent":"update_record","data":{"table":"items","id":5,"values":{"price":"500"}}}
            "set room 4 capacity to 45" -> {"intent":"update_record","data":{"table":"rooms","id":4,"values":{"capacity":"45"}}}
            "register a new user named Juan Dela Cruz email juan@example.com role admin" -> {"intent":"create_record","data":{"table":"users","values":{"full_name":"Juan Dela Cruz","email":"juan@example.com","role":"admin","status":"Active"}}}
            "add course named BSIT code BSIT" -> {"intent":"create_record","data":{"table":"courses","values":{"course_name":"BSIT","code":"BSIT"}}}
            "enroll student 5 course 2 section 3 semester 1 school year 1" -> {"intent":"create_record","data":{"table":"enrollments","values":{"student_id":5,"course_id":2,"section_id":3,"semester_id":1,"school_year_id":1}}}
            "enroll this student" without selected student id -> {"intent":"unknown_intent","data":{"message":"Please include the student id to enroll the student."}}
            "change this user status" without selected context and no status value -> {"intent":"unknown_intent","data":{"message":"Please include the record id and status value."}}
            """ + contextText;

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", OLLAMA_MODEL);
        requestBody.put("stream", false);
        requestBody.put("options", new JSONObject()
                .put("temperature", 0)
                .put("top_p", 0.1));

        JSONArray messages = new JSONArray();

        messages.put(new JSONObject()
                .put("role", "system")
                .put("content", systemInstruction));

        messages.put(new JSONObject()
                .put("role", "user")
                .put("content", adminPrompt));

        requestBody.put("messages", messages);

        URL url = new URL(OLLAMA_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.toString().getBytes("utf-8"));
        }

        int status = conn.getResponseCode();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        status >= 200 && status < 300
                                ? conn.getInputStream()
                                : conn.getErrorStream(),
                        "utf-8"
                )
        );

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        if (status < 200 || status >= 300) {
            throw new RuntimeException("Ollama error: " + status + " - " + response);
        }

        JSONObject obj = new JSONObject(response.toString());
        String content = obj.getJSONObject("message").getString("content");

        content = stripCodeFences(content);
        content = extractJsonObject(content);

        return new JSONObject(content);
    }

    private static String stripCodeFences(String text) {
        text = text.trim();

        if (text.startsWith("```")) {
            text = text.replaceFirst("^```json\\s*", "");
            text = text.replaceFirst("^```\\s*", "");
            text = text.replaceFirst("\\s*```$", "");
        }

        return text.trim();
    }

    private static String extractJsonObject(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');

        if (start >= 0 && end > start) {
            return text.substring(start, end + 1).trim();
        }

        return text.trim();
    }
}
