package com.mycompany.studentrecordsystem;

import java.sql.*;
import org.json.JSONObject;

public class AdminActionExecutor {

    private static final String ACTIVE_STATUS = "Active";
    private static final String ARCHIVED_STATUS = "Archived";

    private static final java.util.Set<String> ALLOWED_TABLES = java.util.Set.of(
            "students",
            "courses",
            "curriculum",
            "departments",
            "enrollments",
            "grades",
            "instructors",
            "rooms",
            "schedules",
            "school_years",
            "sections",
            "semesters",
            "subjects",
            "users",
            "admins",
            "items"
    );

    public static String execute(JSONObject actionJson) {
        String intent = actionJson.optString("intent", "").trim();

        try {
            if ("execute_sql".equals(intent)) {
                return executeSqlOperation(actionJson);
            }

            JSONObject data = actionJson.optJSONObject("data");

            if (intent.isEmpty() || data == null) {
                return "Invalid AI response: missing intent or data.";
            }

            return switch (intent) {
                case "get_table_records" -> getTableRecords(data);
                case "get_record_by_id" -> getRecordById(data);
                case "get_active_records" -> getRecordsByStatus(data, ACTIVE_STATUS);
                case "get_archived_records" -> getRecordsByStatus(data, ARCHIVED_STATUS);
                case "count_records" -> countRecords(data, null);
                case "count_active_records" -> countRecords(data, ACTIVE_STATUS);
                case "count_archived_records" -> countRecords(data, ARCHIVED_STATUS);
                case "create_record" -> createRecord(data);
                case "update_record" -> updateRecord(data);
                case "delete_record" -> deleteRecord(data);
                case "archive_record" -> archiveRecord(data);
                case "archive_record_by_name", "delete_record_by_name" -> archiveRecordByName(data);
                case "restore_record_by_name" -> restoreRecordByName(data);
                case "restore_record" -> restoreRecord(data);

                case "create_student" -> createStudent(data);
                case "get_all_students" -> getAllStudents();
                case "get_students_by_section" -> getStudentsBySection(data);
                case "get_student_by_id" -> getStudentById(data);
                case "update_student" -> updateStudent(data);
                case "get_active_students" -> getActiveStudents();
                case "get_archived_students" -> getArchivedStudents();
                case "get_regular_students" -> getRegularStudents();
                case "get_irregular_students" -> getIrregularStudents();
                case "archive_student" -> archiveStudent(data);
                case "restore_student" -> restoreStudent(data);
                case "delete_student" -> deleteStudent(data);

                case "unknown_intent" -> "I understood the message, but I do not know which CRUD operation to perform. Please include the table, action, and record id if needed.";
                default -> "Unsupported intent: " + intent;
            };
        } catch (Exception e) {
            return "Execution error: " + e.getMessage();
        }
    }

    private static void validateTable(String table) {
        if (table == null || !ALLOWED_TABLES.contains(table)) {
            throw new IllegalArgumentException("Invalid table.");
        }
    }

    private static void validateColumnName(String column) {
        if (column == null || !column.matches("[A-Za-z0-9_]+")) {
            throw new IllegalArgumentException("Invalid column name: " + column);
        }
    }

    private static String getPrimaryKey(String table) {
        return switch (table) {
            case "students" -> "student_id";
            case "courses" -> "course_id";
            case "curriculum" -> "curriculum_id";
            case "departments" -> "department_id";
            case "enrollments" -> "enrollment_id";
            case "grades" -> "grade_id";
            case "instructors" -> "instructor_id";
            case "rooms" -> "room_id";
            case "schedules" -> "schedule_id";
            case "school_years" -> "school_year_id";
            case "sections" -> "section_id";
            case "semesters" -> "semester_id";
            case "subjects" -> "subject_id";
            case "users" -> "user_id";
            case "admins" -> "admin_id";
            case "items" -> "item_id";
            default -> "id";
        };
    }

    private static boolean tableHasColumn(Connection conn, String table, String columnName) throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM INFORMATION_SCHEMA.COLUMNS
            WHERE TABLE_SCHEMA = DATABASE()
              AND TABLE_NAME = ?
              AND COLUMN_NAME = ?
        """;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, table);
            pst.setString(2, columnName);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private static String resolveColumnName(Connection conn, String table, String requestedColumn) throws SQLException {
        validateColumnName(requestedColumn);

        if (tableHasColumn(conn, table, requestedColumn)) {
            return requestedColumn;
        }

        String key = requestedColumn.toLowerCase().trim();
        java.util.List<String> candidates = new java.util.ArrayList<>();

        switch (key) {
            case "name", "fullname", "full_name", "username", "user_name" -> {
                candidates.add("full_name");
                candidates.add(tableSingular(table) + "_name");
                candidates.add("name");
                candidates.add("first_name");
            }
            case "firstname", "first" -> candidates.add("first_name");
            case "middlename", "middle" -> candidates.add("middle_name");
            case "lastname", "last", "surname" -> candidates.add("last_name");
            case "email", "mail" -> {
                candidates.add("email");
                candidates.add("user_email");
                candidates.add("admin_email");
            }
            case "password", "pass" -> {
                candidates.add("password");
                candidates.add("user_password");
                candidates.add("admin_password");
            }
            case "role", "type" -> {
                candidates.add("role");
                candidates.add("user_role");
                candidates.add("admin_role");
            }
            case "status", "state", "active_status", "record_status" -> {
                candidates.add("status");
                candidates.add("user_status");
                candidates.add("admin_status");
                candidates.add("student_status");
            }
            case "studentstatus", "student_status", "regularity" -> candidates.add("student_status");
            case "number", "studentnumber", "student_no", "studentnum" -> candidates.add("student_number");
            case "contact", "phone", "phone_number", "contactnumber" -> candidates.add("contact_number");
            case "birthdate", "birthday", "dob" -> candidates.add("birth_date");
            case "address", "location" -> candidates.add("address");
            case "gender", "sex" -> candidates.add("gender");
            case "year", "yearlevel", "level" -> candidates.add("year_level");
            case "course", "program" -> {
                candidates.add("course_id");
                candidates.add("course_name");
                candidates.add("course_code");
            }
            case "section", "block" -> {
                candidates.add("section_id");
                candidates.add("section_name");
                candidates.add("section_code");
            }
            case "subject" -> {
                candidates.add("subject_id");
                candidates.add("subject_name");
                candidates.add("subject_code");
            }
            case "semester", "sem" -> {
                candidates.add("semester_id");
                candidates.add("semester_name");
            }
            case "schoolyear", "school_year", "sy" -> {
                candidates.add("school_year_id");
                candidates.add("school_year");
            }
            case "instructor", "teacher", "faculty" -> candidates.add("instructor_id");
            case "room", "classroom", "lab", "laboratory" -> {
                candidates.add("room_id");
                candidates.add("room_name");
                candidates.add("room_number");
            }
            case "code" -> {
                candidates.add(tableSingular(table) + "_code");
                candidates.add("code");
            }
            case "description", "desc" -> candidates.add("description");
            case "price", "cost", "amount" -> {
                candidates.add("price");
                candidates.add("amount");
                candidates.add("cost");
            }
            case "capacity", "slots", "seat", "seats" -> candidates.add("capacity");
            default -> {
                String snake = toSnakeCase(key);
                candidates.add(snake);
                candidates.add(tableSingular(table) + "_" + snake);
            }
        }

        for (String candidate : candidates) {
            if (candidate != null && tableHasColumn(conn, table, candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private static String tableSingular(String table) {
        if (table == null || table.isBlank()) return "";
        if (table.equals("curriculum")) return "curriculum";
        if (table.endsWith("ies")) return table.substring(0, table.length() - 3) + "y";
        if (table.endsWith("s")) return table.substring(0, table.length() - 1);
        return table;
    }

    private static String toSnakeCase(String value) {
        return value == null ? "" : value
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .replaceAll("[^A-Za-z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "")
                .toLowerCase();
    }

    private static String requireStatusColumn(Connection conn, String table) throws SQLException {
        if (resolveColumnName(conn, table, "status") == null) {
            return "Validation failed: this table has no status column, so active/archived operations cannot be used.";
        }
        return null;
    }

    private static String getTableRecords(JSONObject data) throws Exception {
        String table = data.optString("table", "").trim();
        validateTable(table);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM " + table + " LIMIT 100";

            try (PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {
                return formatResultSet("Records from " + table + ":", rs);
            }
        }
    }

    private static String getRecordById(JSONObject data) throws Exception {
        String table = data.optString("table", "").trim();
        int id = data.optInt("id", 0);

        validateTable(table);

        if (id <= 0) {
            return "Validation failed: id is required.";
        }

        String primaryKey = getPrimaryKey(table);
        StringBuilder result = new StringBuilder();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM " + table + " WHERE " + primaryKey + " = ? LIMIT 1";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, id);

                try (ResultSet rs = pst.executeQuery()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int columnCount = meta.getColumnCount();

                    if (!rs.next()) {
                        return "Record not found.";
                    }

                    result.append("Record from ").append(table).append(":\n");

                    for (int i = 1; i <= columnCount; i++) {
                        result.append(meta.getColumnName(i))
                                .append(": ")
                                .append(rs.getString(i) == null ? "" : rs.getString(i))
                                .append("\n");
                    }
                }
            }
        }

        return result.toString();
    }

    private static String getRecordsByStatus(JSONObject data, String status) throws Exception {
        String table = data.optString("table", "").trim();
        validateTable(table);

        try (Connection conn = DBConnection.getConnection()) {
            String statusError = requireStatusColumn(conn, table);
            if (statusError != null) {
                return statusError;
            }

            String statusColumn = resolveColumnName(conn, table, "status");
            String sql = "SELECT * FROM " + table + " WHERE LOWER(`" + statusColumn + "`) = LOWER(?) LIMIT 100";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, status);

                try (ResultSet rs = pst.executeQuery()) {
                    return formatResultSet(status + " records from " + table + ":", rs);
                }
            }
        }
    }

    private static String countRecords(JSONObject data, String status) throws Exception {
        String table = data.optString("table", "").trim();
        validateTable(table);

        try (Connection conn = DBConnection.getConnection()) {
            String sql;

            if (status == null) {
                sql = "SELECT COUNT(*) AS total FROM " + table;

                try (PreparedStatement pst = conn.prepareStatement(sql);
                     ResultSet rs = pst.executeQuery()) {
                    return rs.next()
                            ? "Total records in " + table + ": " + rs.getInt("total")
                            : "Count failed.";
                }
            }

            String statusError = requireStatusColumn(conn, table);
            if (statusError != null) {
                return statusError;
            }

            String statusColumn = resolveColumnName(conn, table, "status");
            sql = "SELECT COUNT(*) AS total FROM " + table + " WHERE LOWER(`" + statusColumn + "`) = LOWER(?)";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, status);

                try (ResultSet rs = pst.executeQuery()) {
                    return rs.next()
                            ? status + " records in " + table + ": " + rs.getInt("total")
                            : "Count failed.";
                }
            }
        }
    }

    private static String createRecord(JSONObject data) throws Exception {
        String table = data.optString("table", "").trim();
        JSONObject values = data.optJSONObject("values");

        validateTable(table);

        if (values == null || values.isEmpty()) {
            return "Validation failed: values are required.";
        }

        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        java.util.List<Object> params = new java.util.ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            for (String key : values.keySet()) {
                validateColumnName(key);

                String actualColumn = resolveColumnName(conn, table, key);
                if (actualColumn == null) {
                    return "Validation failed: column '" + key + "' does not exist in " + table + ".";
                }

                columns.append("`").append(actualColumn).append("`, ");
                placeholders.append("?, ");
                params.add(values.opt(key));
            }

            columns.setLength(columns.length() - 2);
            placeholders.setLength(placeholders.length() - 2);

            String sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + placeholders + ")";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                for (int i = 0; i < params.size(); i++) {
                    pst.setObject(i + 1, params.get(i));
                }

                int rows = pst.executeUpdate();
                return rows > 0
                        ? "Record created successfully in " + table + "."
                        : "Record creation failed.";
            }
        }
    }

    private static String updateRecord(JSONObject data) throws Exception {
        String table = data.optString("table", "").trim();
        int id = data.optInt("id", 0);
        JSONObject values = data.optJSONObject("values");

        validateTable(table);

        if (id <= 0) {
            return "Validation failed: id is required.";
        }

        if (values == null || values.isEmpty()) {
            return "Validation failed: values are required.";
        }

        String primaryKey = getPrimaryKey(table);
        StringBuilder setClause = new StringBuilder();
        java.util.List<Object> params = new java.util.ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            for (String key : values.keySet()) {
                validateColumnName(key);

                String actualColumn = resolveColumnName(conn, table, key);
                if (actualColumn == null) {
                    return "Validation failed: column '" + key + "' does not exist in " + table + ".";
                }

                setClause.append("`").append(actualColumn).append("` = ?, ");
                params.add(values.opt(key));
            }

            setClause.setLength(setClause.length() - 2);
            params.add(id);

            String sql = "UPDATE " + table + " SET " + setClause + " WHERE " + primaryKey + " = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                for (int i = 0; i < params.size(); i++) {
                    pst.setObject(i + 1, params.get(i));
                }

                int rows = pst.executeUpdate();
                return rows > 0
                        ? "Record updated successfully in " + table + "."
                        : "Record update failed.";
            }
        }
    }

    private static String deleteRecord(JSONObject data) throws Exception {
        /*
         * Soft delete:
         * Admin commands that say "delete/remove" should not permanently remove records.
         * They are archived by changing the status column to Archived.
         */
        return updateRecordStatus(data, ARCHIVED_STATUS, "archived");
    }

    private static String archiveRecordByName(JSONObject data) throws Exception {
        String table = data.optString("table", "").trim();
        String targetName = data.optString("name", data.optString("target_name", "")).trim();

        validateTable(table);

        if (targetName.isEmpty()) {
            return "Validation failed: name is required.";
        }

        try (Connection conn = DBConnection.getConnection()) {
            String statusError = requireStatusColumn(conn, table);
            if (statusError != null) {
                return statusError;
            }

            String primaryKey = getPrimaryKey(table);
            String statusColumn = resolveColumnName(conn, table, "status");
            String nameExpression = getSearchableNameExpression(conn, table);

            if (nameExpression == null || nameExpression.isBlank()) {
                return "Validation failed: I cannot search " + table + " by name because no name column was found.";
            }

            java.util.List<RecordNameMatch> matches = findActiveRecordsByName(
                    conn, table, primaryKey, statusColumn, nameExpression, targetName
            );

            if (matches.isEmpty()) {
                return "No active matching record found for \"" + targetName + "\" in " + table + ".";
            }

            if (matches.size() > 1) {
                StringBuilder result = new StringBuilder();
                result.append("Multiple active records matched \"").append(targetName).append("\" in ")
                        .append(table).append(". Please include the ID:\n");

                for (RecordNameMatch match : matches) {
                    result.append("ID: ").append(match.id)
                            .append(" | ").append(match.displayName)
                            .append("\n");
                }

                return result.toString();
            }

            RecordNameMatch match = matches.get(0);
            String sql = "UPDATE " + table + " SET `" + statusColumn + "` = ? WHERE " + primaryKey + " = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, ARCHIVED_STATUS);
                pst.setObject(2, match.id);

                int rows = pst.executeUpdate();

                return rows > 0
                        ? "Record archived successfully in " + table + ": " + match.displayName + " (ID: " + match.id + ")."
                        : "Record archive failed.";
            }
        }
    }

    private static String restoreRecordByName(JSONObject data) throws Exception {
        String table = data.optString("table", "").trim();
        String targetName = data.optString("name", data.optString("target_name", "")).trim();

        validateTable(table);

        if (targetName.isEmpty()) {
            return "Validation failed: name is required.";
        }

        try (Connection conn = DBConnection.getConnection()) {
            String statusError = requireStatusColumn(conn, table);
            if (statusError != null) {
                return statusError;
            }

            String primaryKey = getPrimaryKey(table);
            String statusColumn = resolveColumnName(conn, table, "status");
            String nameExpression = getSearchableNameExpression(conn, table);

            if (nameExpression == null || nameExpression.isBlank()) {
                return "Validation failed: I cannot search " + table + " by name because no name column was found.";
            }

            java.util.List<RecordNameMatch> matches = findRecordsByNameAndStatus(
                    conn, table, primaryKey, statusColumn, nameExpression, targetName, ARCHIVED_STATUS
            );

            if (matches.isEmpty()) {
                return "No archived matching record found for \"" + targetName + "\" in " + table + ".";
            }

            if (matches.size() > 1) {
                StringBuilder result = new StringBuilder();
                result.append("Multiple archived records matched \"").append(targetName).append("\" in ")
                        .append(table).append(". Please include the ID:\n");

                for (RecordNameMatch match : matches) {
                    result.append("ID: ").append(match.id)
                            .append(" | ").append(match.displayName)
                            .append("\n");
                }

                return result.toString();
            }

            RecordNameMatch match = matches.get(0);
            String sql = "UPDATE " + table + " SET `" + statusColumn + "` = ? WHERE " + primaryKey + " = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, ACTIVE_STATUS);
                pst.setObject(2, match.id);

                int rows = pst.executeUpdate();

                return rows > 0
                        ? "Record restored successfully in " + table + ": " + match.displayName + " (ID: " + match.id + ")."
                        : "Record restore failed.";
            }
        }
    }

    private static java.util.List<RecordNameMatch> findActiveRecordsByName(
            Connection conn,
            String table,
            String primaryKey,
            String statusColumn,
            String nameExpression,
            String targetName
    ) throws SQLException {
        java.util.List<RecordNameMatch> matches = new java.util.ArrayList<>();
        String[] words = targetName.toLowerCase().trim().split("\\s+");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(primaryKey).append(" AS record_id, ")
                .append(nameExpression).append(" AS display_name ")
                .append("FROM ").append(table)
                .append(" WHERE LOWER(`").append(statusColumn).append("`) <> LOWER(?) ");

        for (int i = 0; i < words.length; i++) {
            sql.append(" AND LOWER(").append(nameExpression).append(") LIKE ? ");
        }

        sql.append(" LIMIT 20");

        try (PreparedStatement pst = conn.prepareStatement(sql.toString())) {
            pst.setString(1, ARCHIVED_STATUS);

            for (int i = 0; i < words.length; i++) {
                pst.setString(i + 2, "%" + words[i] + "%");
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    matches.add(new RecordNameMatch(
                            rs.getObject("record_id"),
                            rs.getString("display_name")
                    ));
                }
            }
        }

        return matches;
    }

    private static java.util.List<RecordNameMatch> findRecordsByNameAndStatus(
            Connection conn,
            String table,
            String primaryKey,
            String statusColumn,
            String nameExpression,
            String targetName,
            String requiredStatus
    ) throws SQLException {
        java.util.List<RecordNameMatch> matches = new java.util.ArrayList<>();
        String[] words = targetName.toLowerCase().trim().split("\\s+");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(primaryKey).append(" AS record_id, ")
                .append(nameExpression).append(" AS display_name ")
                .append("FROM ").append(table)
                .append(" WHERE LOWER(`").append(statusColumn).append("`) = LOWER(?) ");

        for (int i = 0; i < words.length; i++) {
            sql.append(" AND LOWER(").append(nameExpression).append(") LIKE ? ");
        }

        sql.append(" LIMIT 20");

        try (PreparedStatement pst = conn.prepareStatement(sql.toString())) {
            pst.setString(1, requiredStatus);

            for (int i = 0; i < words.length; i++) {
                pst.setString(i + 2, "%" + words[i] + "%");
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    matches.add(new RecordNameMatch(
                            rs.getObject("record_id"),
                            rs.getString("display_name")
                    ));
                }
            }
        }

        return matches;
    }

    private static String getSearchableNameExpression(Connection conn, String table) throws SQLException {
        if (tableHasColumn(conn, table, "first_name") && tableHasColumn(conn, table, "middle_name") && tableHasColumn(conn, table, "last_name")) {
            return "CONCAT_WS(' ', first_name, NULLIF(middle_name, ''), last_name)";
        }

        if (tableHasColumn(conn, table, "first_name") && tableHasColumn(conn, table, "last_name")) {
            return "CONCAT_WS(' ', first_name, last_name)";
        }

        String[] candidates = {
                "full_name",
                "name",
                tableSingular(table) + "_name",
                "student_name",
                "instructor_name",
                "subject_name",
                "room_name",
                "course_name",
                "section_name",
                "department_name",
                "semester_name",
                "username",
                "email"
        };

        for (String column : candidates) {
            if (column != null && !column.isBlank() && tableHasColumn(conn, table, column)) {
                return "`" + column + "`";
            }
        }

        return null;
    }

    private static class RecordNameMatch {
        Object id;
        String displayName;

        RecordNameMatch(Object id, String displayName) {
            this.id = id;
            this.displayName = displayName == null ? "" : displayName;
        }
    }

    private static String archiveRecord(JSONObject data) throws Exception {
        return updateRecordStatus(data, ARCHIVED_STATUS, "archived");
    }

    private static String restoreRecord(JSONObject data) throws Exception {
        return updateRecordStatus(data, ACTIVE_STATUS, "restored");
    }

    private static String updateRecordStatus(JSONObject data, String status, String actionWord) throws Exception {
        String table = data.optString("table", "").trim();
        int id = data.optInt("id", 0);

        validateTable(table);

        if (id <= 0) {
            return "Validation failed: id is required.";
        }

        String primaryKey = getPrimaryKey(table);

        try (Connection conn = DBConnection.getConnection()) {
            String statusError = requireStatusColumn(conn, table);
            if (statusError != null) {
                return statusError;
            }

            String statusColumn = resolveColumnName(conn, table, "status");
            String sql = "UPDATE " + table + " SET `" + statusColumn + "` = ? WHERE " + primaryKey + " = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, status);
                pst.setInt(2, id);

                int rows = pst.executeUpdate();

                return rows > 0
                        ? "Record " + actionWord + " successfully in " + table + "."
                        : "Record " + actionWord + " failed.";
            }
        }
    }

    private static String executeSqlOperation(JSONObject actionJson) throws Exception {
        String sql = extractSqlFromAction(actionJson);
        String cleanedSql = cleanAndValidateSql(sql);
        String firstWord = firstSqlWord(cleanedSql);

        try (Connection conn = DBConnection.getConnection()) {
            if ("select".equals(firstWord)) {
                String selectSql = addLimitIfNeeded(cleanedSql);
                try (PreparedStatement pst = conn.prepareStatement(selectSql);
                     ResultSet rs = pst.executeQuery()) {
                    return formatResultSet("Requested information:", rs);
                }
            }

            /*
             * Safety rule:
             * Even if the AI returns DELETE SQL, this system treats delete/remove as soft delete.
             * The DELETE is converted to UPDATE status = 'Archived' when the table supports status.
             */
            if ("delete".equals(firstWord)) {
                return archiveDeleteSql(conn, cleanedSql);
            }

            try (PreparedStatement pst = conn.prepareStatement(cleanedSql)) {
                int rows = pst.executeUpdate();
                String operation = switch (firstWord) {
                    case "insert" -> "created";
                    case "update" -> "updated";
                    default -> "changed";
                };

                return rows > 0
                        ? "SQL operation executed successfully. Records " + operation + ": " + rows + "."
                        : "SQL operation completed, but no records were changed.";
            }
        }
    }

    private static String archiveDeleteSql(Connection conn, String deleteSql) throws Exception {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "(?is)^\\s*DELETE\\s+FROM\\s+`?([A-Za-z0-9_]+)`?\\s+WHERE\\s+(.+?)\\s*$"
        );
        java.util.regex.Matcher matcher = pattern.matcher(deleteSql == null ? "" : deleteSql.trim());

        if (!matcher.find()) {
            throw new IllegalArgumentException("Validation failed: DELETE could not be converted to archive.");
        }

        String table = matcher.group(1).toLowerCase().trim();
        String whereClause = matcher.group(2).trim();

        validateTable(table);

        String statusError = requireStatusColumn(conn, table);
        if (statusError != null) {
            return statusError + " Permanent delete was not performed.";
        }

        String statusColumn = resolveColumnName(conn, table, "status");
        String sql = "UPDATE " + table + " SET `" + statusColumn + "` = ? WHERE " + whereClause;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, ARCHIVED_STATUS);
            int rows = pst.executeUpdate();

            return rows > 0
                    ? "SQL delete request was safely archived instead. Records archived: " + rows + "."
                    : "SQL delete request was converted to archive, but no records were changed.";
        }
    }


    private static String extractSqlFromAction(JSONObject actionJson) {
        String sql = actionJson.optString("sql", "").trim();
        if (!sql.isBlank()) return sql;

        JSONObject data = actionJson.optJSONObject("data");
        if (data != null) {
            return data.optString("sql", "").trim();
        }

        return "";
    }

    private static String cleanAndValidateSql(String sql) {
        if (sql == null || sql.trim().isBlank()) {
            throw new IllegalArgumentException("Validation failed: SQL is required.");
        }

        String trimmed = sql.trim();
        if (trimmed.endsWith(";")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1).trim();
        }

        if (trimmed.contains(";")) {
            throw new IllegalArgumentException("Validation failed: only one SQL statement is allowed.");
        }

        String lower = trimmed.toLowerCase().replaceAll("\\s+", " ").trim();

        if (lower.contains("--") || lower.contains("/*") || lower.contains("*/") || lower.contains("#")) {
            throw new IllegalArgumentException("Validation failed: SQL comments are not allowed.");
        }

        if (lower.matches(".*\\b(drop|truncate|alter|create|replace|grant|revoke|call|execute|load|outfile|infile|rename|use|lock|unlock)\\b.*")) {
            throw new IllegalArgumentException("Validation failed: dangerous SQL command is not allowed.");
        }

        if (lower.matches(".*\\b(information_schema|mysql|performance_schema|sys)\\b.*")) {
            throw new IllegalArgumentException("Validation failed: system database access is not allowed.");
        }

        String firstWord = firstSqlWord(trimmed);
        if (!(firstWord.equals("select") || firstWord.equals("insert") || firstWord.equals("update") || firstWord.equals("delete"))) {
            throw new IllegalArgumentException("Validation failed: only SELECT, INSERT, UPDATE, and DELETE are allowed.");
        }

        if ((firstWord.equals("update") || firstWord.equals("delete")) && !lower.matches(".*\\bwhere\\b.*")) {
            throw new IllegalArgumentException("Validation failed: UPDATE and DELETE must include a WHERE clause.");
        }

        if (lower.contains("where 1=1") || lower.contains("where true")) {
            throw new IllegalArgumentException("Validation failed: broad WHERE clauses are not allowed.");
        }

        java.util.Set<String> referencedTables = extractReferencedTables(trimmed);
        if (referencedTables.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: no valid table was found.");
        }

        for (String table : referencedTables) {
            validateTable(table);
        }

        return trimmed;
    }

    private static String firstSqlWord(String sql) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("^\\s*([a-zA-Z]+)")
                .matcher(sql == null ? "" : sql);
        return matcher.find() ? matcher.group(1).toLowerCase() : "";
    }

    private static java.util.Set<String> extractReferencedTables(String sql) {
        java.util.Set<String> tables = new java.util.LinkedHashSet<>();

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "(?i)\\b(?:from|join|into|update)\\s+`?([a-zA-Z0-9_]+)`?(?:\\.`?([a-zA-Z0-9_]+)`?)?"
        );

        java.util.regex.Matcher matcher = pattern.matcher(sql == null ? "" : sql);
        while (matcher.find()) {
            String table = matcher.group(2) != null ? matcher.group(2) : matcher.group(1);
            if (table != null && !table.isBlank()) {
                tables.add(table.toLowerCase());
            }
        }

        return tables;
    }

    private static String addLimitIfNeeded(String sql) {
        String lower = sql.toLowerCase();
        if (!lower.startsWith("select")) return sql;
        if (lower.matches(".*\\blimit\\s+\\d+.*")) return sql;
        if (lower.matches(".*\\bcount\\s*\\(.*")) return sql;
        return sql + " LIMIT 100";
    }

    private static String formatResultSet(String title, ResultSet rs) throws SQLException {
        StringBuilder result = new StringBuilder();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        result.append(title).append("\n");
        result.append("COLUMNS: ");

        for (int i = 1; i <= columnCount; i++) {
            result.append(meta.getColumnName(i));
            if (i < columnCount) result.append(" | ");
        }

        result.append("\n");

        boolean found = false;

        while (rs.next()) {
            found = true;
            result.append("ROW: ");

            for (int i = 1; i <= columnCount; i++) {
                result.append(rs.getString(i) == null ? "" : rs.getString(i));
                if (i < columnCount) result.append(" | ");
            }

            result.append("\n");
        }

        if (!found) {
            return "No records found.";
        }

        return result.toString();
    }

    private static String getRegularStudents() throws Exception {
        return getStudentsByStudentStatus("Regular", "Regular students:");
    }

    private static String getIrregularStudents() throws Exception {
        return getStudentsByStudentStatus("Irregular", "Irregular students:");
    }

    private static String getAllStudents() throws Exception {
        StringBuilder result = new StringBuilder();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                SELECT s.student_id, s.student_number, s.first_name, s.last_name,
                       s.student_status, s.status, sec.section_name
                FROM students s
                LEFT JOIN sections sec ON sec.section_id = s.section_id
                ORDER BY s.last_name, s.first_name
            """;

            try (PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {

                result.append("All students:\n");

                boolean found = false;

                while (rs.next()) {
                    found = true;
                    result.append("ID: ").append(rs.getInt("student_id"))
                            .append(" | ")
                            .append(rs.getString("student_number"))
                            .append(" | ")
                            .append(rs.getString("first_name")).append(" ")
                            .append(rs.getString("last_name"))
                            .append(" | Student Status: ")
                            .append(rs.getString("student_status"))
                            .append(" | Record Status: ")
                            .append(rs.getString("status"))
                            .append(" | Section: ")
                            .append(rs.getString("section_name"))
                            .append("\n");
                }

                if (!found) {
                    return "No students found.";
                }
            }
        }

        return result.toString();
    }

    private static String getStudentsByStudentStatus(String studentStatus, String title) throws Exception {
        StringBuilder result = new StringBuilder();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                SELECT s.student_id, s.student_number, s.first_name, s.last_name, sec.section_name
                FROM students s
                LEFT JOIN sections sec ON sec.section_id = s.section_id
                WHERE LOWER(s.student_status) = LOWER(?)
                  AND LOWER(s.status) = LOWER('Active')
                ORDER BY s.last_name, s.first_name
            """;

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, studentStatus);

                try (ResultSet rs = pst.executeQuery()) {
                    result.append(title).append("\n");

                    boolean found = false;

                    while (rs.next()) {
                        found = true;
                        result.append("ID: ").append(rs.getInt("student_id"))
                                .append(" | ")
                                .append(rs.getString("student_number"))
                                .append(" | ")
                                .append(rs.getString("first_name")).append(" ")
                                .append(rs.getString("last_name"))
                                .append(" | Section: ")
                                .append(rs.getString("section_name"))
                                .append("\n");
                    }

                    if (!found) {
                        return "No " + studentStatus.toLowerCase() + " students found.";
                    }
                }
            }
        }

        return result.toString();
    }

    private static String getArchivedStudents() throws Exception {
        return getSimpleStudentList(ARCHIVED_STATUS, "Archived students:", "status");
    }

    private static String getActiveStudents() throws Exception {
        return getSimpleStudentList(ACTIVE_STATUS, "Active students:", "status");
    }

    private static String getSimpleStudentList(String value, String title, String column) throws Exception {
        validateColumnName(column);
        StringBuilder result = new StringBuilder();

        try (Connection conn = DBConnection.getConnection()) {
            if (!tableHasColumn(conn, "students", column)) {
                return "Validation failed: column '" + column + "' does not exist in students.";
            }

            String sql = """
                SELECT s.student_id, s.student_number, s.first_name, s.last_name, sec.section_name
                FROM students s
                LEFT JOIN sections sec ON sec.section_id = s.section_id
                WHERE LOWER(s.%s) = LOWER(?)
                ORDER BY s.last_name, s.first_name
            """.formatted(column);

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, value);

                try (ResultSet rs = pst.executeQuery()) {
                    result.append(title).append("\n");

                    boolean found = false;

                    while (rs.next()) {
                        found = true;
                        result.append("ID: ").append(rs.getInt("student_id"))
                                .append(" | ")
                                .append(rs.getString("student_number"))
                                .append(" | ")
                                .append(rs.getString("first_name")).append(" ")
                                .append(rs.getString("last_name"))
                                .append(" | Section: ")
                                .append(rs.getString("section_name"))
                                .append("\n");
                    }

                    if (!found) {
                        return "No records found.";
                    }
                }
            }
        }

        return result.toString();
    }

    private static String getStudentById(JSONObject data) throws Exception {
        int studentId = data.optInt("student_id", data.optInt("id", 0));

        if (studentId <= 0) {
            return "Validation failed: student_id is required.";
        }

        try (Connection conn = DBConnection.getConnection()) {
            return getStudentDetailsText(conn, studentId);
        }
    }

    private static String createStudent(JSONObject data) throws Exception {
        String studentNumber = data.optString("student_number", "").trim();
        String password = data.optString("password", "").trim();
        String firstName = data.optString("first_name", "").trim();
        String lastName = data.optString("last_name", "").trim();
        String gender = data.optString("gender", "").trim();
        String email = data.optString("email", "").trim();

        int courseId = data.optInt("course_id", 0);
        int sectionId = data.optInt("section_id", 0);
        int yearLevel = data.optInt("year_level", 0);

        if (studentNumber.isEmpty() || password.isEmpty() || firstName.isEmpty()
                || lastName.isEmpty() || gender.isEmpty() || email.isEmpty()
                || courseId <= 0 || sectionId <= 0 || yearLevel <= 0) {
            return "Validation failed: missing required student fields.";
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (existsByStudentNumber(conn, studentNumber)) {
                return "Validation failed: student number already exists.";
            }

            if (existsByEmail(conn, email)) {
                return "Validation failed: email already exists.";
            }

            String sql = """
                INSERT INTO students
                (student_number, first_name, middle_name, last_name, gender, birth_date, address,
                 contact_number, email, password, course_id, section_id, year_level, student_status, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Active')
            """;

            try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, studentNumber);
                pst.setString(2, firstName);
                pst.setString(3, data.optString("middle_name", "").trim());
                pst.setString(4, lastName);
                pst.setString(5, gender);
                pst.setString(6, emptyToNull(data.optString("birth_date", "").trim()));
                pst.setString(7, data.optString("address", "").trim());
                pst.setString(8, data.optString("contact_number", "").trim());
                pst.setString(9, email);
                pst.setString(10, password);
                pst.setInt(11, courseId);
                pst.setInt(12, sectionId);
                pst.setInt(13, yearLevel);
                pst.setString(14, data.optString("student_status", "Regular").trim());

                int rows = pst.executeUpdate();

                if (rows <= 0) {
                    return "Student creation failed.";
                }

                int newStudentId = 0;

                try (ResultSet keys = pst.getGeneratedKeys()) {
                    if (keys.next()) {
                        newStudentId = keys.getInt(1);
                    }
                }

                return "Student created successfully.\n\nCreated student information:\n"
                        + getStudentDetailsText(conn, newStudentId);
            }
        }
    }

    private static String getStudentsBySection(JSONObject data) throws Exception {
        String sectionName = data.optString("section_name", "").trim();

        if (sectionName.isEmpty()) {
            return "Validation failed: section_name is required.";
        }

        StringBuilder result = new StringBuilder();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                SELECT s.student_id, s.student_number, s.first_name, s.last_name, sec.section_name
                FROM students s
                JOIN sections sec ON sec.section_id = s.section_id
                WHERE LOWER(sec.section_name) = LOWER(?)
                  AND LOWER(s.status) = LOWER('Active')
                ORDER BY s.last_name, s.first_name
            """;

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, sectionName);

                try (ResultSet rs = pst.executeQuery()) {
                    result.append("Students in ").append(sectionName).append(":\n");

                    boolean found = false;

                    while (rs.next()) {
                        found = true;
                        result.append("ID: ").append(rs.getInt("student_id"))
                                .append(" | ")
                                .append(rs.getString("student_number"))
                                .append(" | ")
                                .append(rs.getString("first_name")).append(" ")
                                .append(rs.getString("last_name"))
                                .append(" | Section: ")
                                .append(rs.getString("section_name"))
                                .append("\n");
                    }

                    if (!found) {
                        return "No active students found in section " + sectionName + ".";
                    }
                }
            }
        }

        return result.toString();
    }

    private static String updateStudent(JSONObject data) throws Exception {
        int studentId = data.optInt("student_id", data.optInt("id", 0));

        if (studentId <= 0) {
            return "Validation failed: student_id is required.";
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (!studentExists(conn, studentId)) {
                return "Validation failed: student not found.";
            }

            StringBuilder sql = new StringBuilder("UPDATE students SET ");
            java.util.List<Object> params = new java.util.ArrayList<>();

            String[] fields = {
                    "student_number", "password", "first_name", "middle_name", "last_name",
                    "gender", "birth_date", "address", "contact_number", "email",
                    "course_id", "section_id", "year_level", "student_status", "status"
            };

            for (String field : fields) {
                if (data.has(field)) {
                    sql.append(field).append(" = ?, ");
                    params.add(field.equals("birth_date")
                            ? emptyToNull(data.optString(field, "").trim())
                            : data.opt(field));
                }
            }

            if (params.isEmpty()) {
                return "Validation failed: no fields to update.";
            }

            sql.setLength(sql.length() - 2);
            sql.append(" WHERE student_id = ?");
            params.add(studentId);

            try (PreparedStatement pst = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    pst.setObject(i + 1, params.get(i));
                }

                int rows = pst.executeUpdate();

                if (rows <= 0) {
                    return "Student update failed.";
                }

                return "Student updated successfully.\n\nUpdated student information:\n"
                        + getStudentDetailsText(conn, studentId);
            }
        }
    }

    private static String archiveStudent(JSONObject data) throws Exception {
        int studentId = data.optInt("student_id", data.optInt("id", 0));

        if (studentId <= 0) {
            return "Validation failed: student_id is required.";
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (!studentExists(conn, studentId)) {
                return "Validation failed: student not found.";
            }

            String studentInfo = getStudentDetailsText(conn, studentId);
            String sql = "UPDATE students SET status = ? WHERE student_id = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, ARCHIVED_STATUS);
                pst.setInt(2, studentId);

                int rows = pst.executeUpdate();

                return rows > 0
                        ? "Student archived successfully.\n\nArchived student information:\n" + studentInfo
                        : "Student archive failed.";
            }
        }
    }

    private static String restoreStudent(JSONObject data) throws Exception {
        int studentId = data.optInt("student_id", data.optInt("id", 0));

        if (studentId <= 0) {
            return "Validation failed: student_id is required.";
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (!studentExists(conn, studentId)) {
                return "Validation failed: student not found.";
            }

            String sql = "UPDATE students SET status = ? WHERE student_id = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, ACTIVE_STATUS);
                pst.setInt(2, studentId);

                int rows = pst.executeUpdate();

                return rows > 0
                        ? "Student restored successfully.\n\nRestored student information:\n" + getStudentDetailsText(conn, studentId)
                        : "Student restore failed.";
            }
        }
    }

    private static String deleteStudent(JSONObject data) throws Exception {
        /*
         * Soft delete for students:
         * Delete commands archive the student instead of permanently deleting the row.
         */
        return archiveStudent(data);
    }

    private static String getStudentDetailsText(Connection conn, int studentId) throws SQLException {
        String sql = """
            SELECT s.student_id, s.student_number, s.first_name, s.middle_name, s.last_name,
                   s.gender, s.birth_date, s.address, s.contact_number, s.email,
                   s.year_level, s.student_status, s.status,
                   sec.section_name
            FROM students s
            LEFT JOIN sections sec ON sec.section_id = s.section_id
            WHERE s.student_id = ?
            LIMIT 1
        """;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, studentId);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return "Student not found.";
                }

                String middleName = rs.getString("middle_name") == null ? "" : rs.getString("middle_name");

                return "Student ID: " + rs.getInt("student_id") + "\n"
                        + "Student Number: " + rs.getString("student_number") + "\n"
                        + "Name: " + rs.getString("first_name") + " "
                        + middleName + " "
                        + rs.getString("last_name") + "\n"
                        + "Gender: " + rs.getString("gender") + "\n"
                        + "Birth Date: " + rs.getString("birth_date") + "\n"
                        + "Address: " + rs.getString("address") + "\n"
                        + "Contact Number: " + rs.getString("contact_number") + "\n"
                        + "Email: " + rs.getString("email") + "\n"
                        + "Section: " + rs.getString("section_name") + "\n"
                        + "Year Level: " + rs.getInt("year_level") + "\n"
                        + "Student Status: " + rs.getString("student_status") + "\n"
                        + "Record Status: " + rs.getString("status");
            }
        }
    }

    private static boolean existsByStudentNumber(Connection conn, String studentNumber) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE student_number = ? LIMIT 1";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, studentNumber);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static boolean existsByEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE email = ? LIMIT 1";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, email);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static boolean studentExists(Connection conn, int studentId) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE student_id = ? LIMIT 1";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, studentId);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
