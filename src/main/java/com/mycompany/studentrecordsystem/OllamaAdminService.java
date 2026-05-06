package com.mycompany.studentrecordsystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class OllamaAdminService {

    private static final String OLLAMA_URL = "http://localhost:11434/api/chat";
    private static final String OLLAMA_MODEL = "qwen2.5-coder:7b";

    public static JSONObject getAdminAction(String adminPrompt) throws Exception {
        JSONObject fixedIntent = detectIntentLocally(adminPrompt);

        if (fixedIntent != null) {
            return fixedIntent;
        }

        return callOllama(adminPrompt);
    }

    private static JSONObject detectIntentLocally(String prompt) {
        String text = normalize(prompt);

        String table = detectTable(text);
        int id = extractId(text);

        boolean isRead = containsAny(text,
                "show", "list", "view", "get", "find", "display", "select", "see", "open", "fetch");

        boolean isCreate = containsAny(text,
                "create", "add", "insert", "new", "register", "save new", "make new");

        boolean isUpdate = containsAny(text,
                "update", "change", "edit", "set", "modify", "rename", "correct", "revise");

        boolean isDelete = containsAny(text,
                "delete", "remove", "drop", "erase", "destroy");

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

        if (table != null && isRestoreAction && id > 0) {
            return new JSONObject()
                    .put("intent", "restore_record")
                    .put("data", new JSONObject()
                            .put("table", table)
                            .put("id", id));
        }

        if (table != null && isArchivedRead && isRead && id <= 0) {
            if (table.equals("students")) {
                return new JSONObject()
                        .put("intent", "get_archived_students")
                        .put("data", new JSONObject());
            }

            return new JSONObject()
                    .put("intent", "get_archived_records")
                    .put("data", new JSONObject().put("table", table));
        }

        if (table != null && isActiveRead && isRead && id <= 0) {
            return new JSONObject()
                    .put("intent", "get_active_records")
                    .put("data", new JSONObject().put("table", table));
        }

        if (table != null && isArchiveAction && id > 0 && !isArchivedRead) {
            return new JSONObject()
                    .put("intent", "archive_record")
                    .put("data", new JSONObject()
                            .put("table", table)
                            .put("id", id));
        }

        if (table != null && isDelete && id > 0) {
            return new JSONObject()
                    .put("intent", "delete_record")
                    .put("data", new JSONObject()
                            .put("table", table)
                            .put("id", id));
        }

        if (table != null && isCreate) {
            JSONObject values = extractValues(prompt);

            return new JSONObject()
                    .put("intent", "create_record")
                    .put("data", new JSONObject()
                            .put("table", table)
                            .put("values", values));
        }

        if (table != null && isUpdate && id > 0) {
            JSONObject values = extractValues(prompt);

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
            return new JSONObject()
                    .put("intent", "count_archived_records")
                    .put("data", new JSONObject().put("table", table));
        }

        if (table != null && isCount && isActiveRead) {
            return new JSONObject()
                    .put("intent", "count_active_records")
                    .put("data", new JSONObject().put("table", table));
        }

        if (table != null && isCount) {
            return new JSONObject()
                    .put("intent", "count_records")
                    .put("data", new JSONObject().put("table", table));
        }

        if (table != null && isRead && id > 0) {
            return new JSONObject()
                    .put("intent", "get_record_by_id")
                    .put("data", new JSONObject()
                            .put("table", table)
                            .put("id", id));
        }

        if (table != null && isRead) {
            return new JSONObject()
                    .put("intent", "get_table_records")
                    .put("data", new JSONObject().put("table", table));
        }

        return null;
    }

    private static String normalize(String text) {
        if (text == null) {
            return "";
        }

        return text.toLowerCase()
                .replaceAll("[^a-z0-9_@.\\-#:/ ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static boolean containsAny(String text, String... words) {
        for (String word : words) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private static String detectTable(String text) {
        if (containsAny(text, "school year", "school_year", "school years", "school_years", "sy")) return "school_years";
        if (containsAny(text, "student", "students", "learner", "learners")) return "students";
        if (containsAny(text, "course", "courses", "program", "programs")) return "courses";
        if (containsAny(text, "curriculum", "curricula")) return "curriculum";
        if (containsAny(text, "department", "departments", "dept")) return "departments";
        if (containsAny(text, "enrollment", "enrollments", "enrolled")) return "enrollments";
        if (containsAny(text, "grade", "grades", "mark", "marks")) return "grades";
        if (containsAny(text, "instructor", "instructors", "teacher", "teachers", "faculty")) return "instructors";
        if (containsAny(text, "room", "rooms", "classroom", "classrooms", "laboratory", "lab")) return "rooms";
        if (containsAny(text, "schedule", "schedules", "timetable", "time table")) return "schedules";
        if (containsAny(text, "section", "sections", "block", "blocks")) return "sections";
        if (containsAny(text, "semester", "semesters", "sem")) return "semesters";
        if (containsAny(text, "subject", "subjects", "course subject", "course subjects")) return "subjects";

        return null;
    }

    private static int extractId(String text) {
        Pattern explicitId = Pattern.compile("\\b(?:id|#|record|record id|no|number)\\s*#?\\s*(\\d+)\\b");
        Matcher explicitMatcher = explicitId.matcher(text);

        if (explicitMatcher.find()) {
            return parseIntSafe(explicitMatcher.group(1));
        }

        Pattern generalNumber = Pattern.compile("\\b(\\d+)\\b");
        Matcher generalMatcher = generalNumber.matcher(text);

        if (generalMatcher.find()) {
            return parseIntSafe(generalMatcher.group(1));
        }

        return 0;
    }

    private static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static JSONObject extractValues(String prompt) {
        JSONObject values = new JSONObject();

        if (prompt == null || prompt.trim().isEmpty()) {
            return values;
        }

        String text = prompt.trim();

        Pattern keyValuePattern = Pattern.compile(
                "([a-zA-Z_][a-zA-Z0-9_]*)\\s*(?:=|:|\\bto\\b|\\bas\\b)\\s*([^,;]+?)(?=\\s+(?:and\\s+)?[a-zA-Z_][a-zA-Z0-9_]*\\s*(?:=|:|\\bto\\b|\\bas\\b)|[,;]|$)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = keyValuePattern.matcher(text);

        while (matcher.find()) {
            String key = matcher.group(1).trim().toLowerCase();
            String value = matcher.group(2).trim();

            if (isIgnoredValueKey(key)) {
                continue;
            }

            value = value.replaceAll("(?i)\\s+and$", "").trim();
            values.put(key, value);
        }

        return values;
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
                || key.equals("sections");
    }

    private static JSONObject callOllama(String adminPrompt) throws Exception {

        String systemInstruction = """
            You are an admin database CRUD intent classifier for a student record system.

            Your job is to understand the user's intention first before choosing a CRUD operation.
            Return exactly one JSON object only.
            No markdown. No explanation. No SQL.

            Admin can access these tables only:
            students, courses, curriculum, departments, enrollments, grades,
            instructors, rooms, schedules, school_years, sections, semesters, subjects

            Allowed intents:
            get_table_records,
            get_record_by_id,
            get_archived_students,
            get_archived_records,
            get_active_records,
            count_records,
            count_archived_records,
            count_active_records,
            create_record,
            update_record,
            delete_record,
            archive_record,
            restore_record,
            unknown_intent

            JSON formats:
            - Read all records: {"intent":"get_table_records","data":{"table":"students"}}
            - Read one record by id: {"intent":"get_record_by_id","data":{"table":"students","id":1}}
            - Read archived students: {"intent":"get_archived_students","data":{}}
            - Read archived records from other table: {"intent":"get_archived_records","data":{"table":"subjects"}}
            - Read active records: {"intent":"get_active_records","data":{"table":"students"}}
            - Count all records: {"intent":"count_records","data":{"table":"students"}}
            - Count archived records: {"intent":"count_archived_records","data":{"table":"students"}}
            - Count active records: {"intent":"count_active_records","data":{"table":"students"}}
            - Create: {"intent":"create_record","data":{"table":"courses","values":{"course_name":"BSIT"}}}
            - Update: {"intent":"update_record","data":{"table":"courses","id":1,"values":{"course_name":"BSCS"}}}
            - Delete permanently: {"intent":"delete_record","data":{"table":"courses","id":1}}
            - Archive or deactivate: {"intent":"archive_record","data":{"table":"subjects","id":1}}
            - Restore or unarchive: {"intent":"restore_record","data":{"table":"subjects","id":1}}
            - Unclear command: {"intent":"unknown_intent","data":{"message":"Please specify the table, id, and action."}}

            Intent rules:
            - Understand meaning, not exact words.
            - show/list/view/get/display/select/see/fetch means READ.
            - create/add/insert/new/register means CREATE.
            - update/change/edit/set/modify/rename/correct means UPDATE.
            - delete/remove/drop/erase means DELETE only if the user wants permanent deletion.
            - archive/deactivate/disable/mark inactive/soft delete means ARCHIVE, not permanent delete.
            - restore/unarchive/reactivate/recover/bring back means RESTORE.
            - count/how many/total number means COUNT.
            - archived students, show archived students, show all archived students, list inactive students, inactive students all mean get_archived_students.
            - active students, show active students, current students all mean get_active_records with table students.
            - If the user says archived/inactive without an id, they want to VIEW archived records, not archive a record.
            - If the user says archive/deactivate with a specific id, they want to ARCHIVE that record.
            - If the user says restore/unarchive/reactivate with a specific id, they want to RESTORE that record.
            - Use exact table names from the allowed table list.
            - Put changed or created column values inside data.values.
            - Unknown values must be "" or 0.
            - Never invent a table name.
            - Never return SQL.

            Examples:
            - "students" -> {"intent":"get_table_records","data":{"table":"students"}}
            - "show students" -> {"intent":"get_table_records","data":{"table":"students"}}
            - "show all students" -> {"intent":"get_table_records","data":{"table":"students"}}
            - "display student id 5" -> {"intent":"get_record_by_id","data":{"table":"students","id":5}}
            - "archived students" -> {"intent":"get_archived_students","data":{}}
            - "show all archived students" -> {"intent":"get_archived_students","data":{}}
            - "list inactive students" -> {"intent":"get_archived_students","data":{}}
            - "show archived subjects" -> {"intent":"get_archived_records","data":{"table":"subjects"}}
            - "active students" -> {"intent":"get_active_records","data":{"table":"students"}}
            - "how many students" -> {"intent":"count_records","data":{"table":"students"}}
            - "how many archived students" -> {"intent":"count_archived_records","data":{"table":"students"}}
            - "add course course_name BSIT" -> {"intent":"create_record","data":{"table":"courses","values":{"course_name":"BSIT"}}}
            - "create room room_name Lab 101 capacity 40" -> {"intent":"create_record","data":{"table":"rooms","values":{"room_name":"Lab 101","capacity":"40"}}}
            - "update room 1 room_name to Lab 101" -> {"intent":"update_record","data":{"table":"rooms","id":1,"values":{"room_name":"Lab 101"}}}
            - "change course id 2 course_name to BSCS" -> {"intent":"update_record","data":{"table":"courses","id":2,"values":{"course_name":"BSCS"}}}
            - "archive student 3" -> {"intent":"archive_record","data":{"table":"students","id":3}}
            - "deactivate subject id 9" -> {"intent":"archive_record","data":{"table":"subjects","id":9}}
            - "restore student 3" -> {"intent":"restore_record","data":{"table":"students","id":3}}
            - "unarchive subject 9" -> {"intent":"restore_record","data":{"table":"subjects","id":9}}
            - "delete subject 3" -> {"intent":"delete_record","data":{"table":"subjects","id":3}}
            """;

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
