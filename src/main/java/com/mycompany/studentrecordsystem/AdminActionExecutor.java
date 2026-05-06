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
            "subjects"
    );

    public static String execute(JSONObject actionJson) {
        String intent = actionJson.optString("intent", "").trim();
        JSONObject data = actionJson.optJSONObject("data");

        if (intent.isEmpty() || data == null) {
            return "Invalid AI response: missing intent or data.";
        }

        try {
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

    private static String requireStatusColumn(Connection conn, String table) throws SQLException {
        if (!tableHasColumn(conn, table, "status")) {
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

            String sql = "SELECT * FROM " + table + " WHERE LOWER(status) = LOWER(?) LIMIT 100";

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

            sql = "SELECT COUNT(*) AS total FROM " + table + " WHERE LOWER(status) = LOWER(?)";

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

                if (!tableHasColumn(conn, table, key)) {
                    return "Validation failed: column '" + key + "' does not exist in " + table + ".";
                }

                columns.append(key).append(", ");
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

                if (!tableHasColumn(conn, table, key)) {
                    return "Validation failed: column '" + key + "' does not exist in " + table + ".";
                }

                setClause.append(key).append(" = ?, ");
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
        String table = data.optString("table", "").trim();
        int id = data.optInt("id", 0);

        validateTable(table);

        if (id <= 0) {
            return "Validation failed: id is required.";
        }

        String primaryKey = getPrimaryKey(table);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM " + table + " WHERE " + primaryKey + " = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, id);

                int rows = pst.executeUpdate();
                return rows > 0
                        ? "Record deleted successfully from " + table + "."
                        : "Record delete failed.";
            }
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

            String sql = "UPDATE " + table + " SET status = ? WHERE " + primaryKey + " = ?";

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
        int studentId = data.optInt("student_id", data.optInt("id", 0));

        if (studentId <= 0) {
            return "Validation failed: student_id is required.";
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (!studentExists(conn, studentId)) {
                return "Validation failed: student not found.";
            }

            String studentInfo = getStudentDetailsText(conn, studentId);
            String sql = "DELETE FROM students WHERE student_id = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, studentId);

                int rows = pst.executeUpdate();

                return rows > 0
                        ? "Student deleted successfully.\n\nDeleted student information:\n" + studentInfo
                        : "Student delete failed.";
            }
        }
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
