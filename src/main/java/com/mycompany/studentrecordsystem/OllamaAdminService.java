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

    public static JSONObject getAdminAction(String adminPrompt) throws Exception {
        JSONObject fixedIntent = detectIntentLocally(adminPrompt);

        if (fixedIntent != null) {
            return fixedIntent;
        }

        return callOllama(adminPrompt);
    }

    private static JSONObject detectIntentLocally(String prompt) {
    String text = prompt.toLowerCase().trim();

    String table = detectTable(text);
    int id = extractId(text);

    boolean isRead = text.contains("show")
            || text.contains("list")
            || text.contains("view")
            || text.contains("get")
            || text.contains("find")
            || text.contains("display")
            || text.contains("select");

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

    if (table != null && id > 0 &&
            (text.contains("update")
                    || text.contains("change")
                    || text.contains("edit")
                    || text.contains("set")
                    || text.contains("modify"))) {

        JSONObject values = extractUpdateValues(text);

        return new JSONObject()
                .put("intent", "update_record")
                .put("data", new JSONObject()
                        .put("table", table)
                        .put("id", id)
                        .put("values", values));
    }

    if (table != null && id > 0 &&
            (text.contains("delete") || text.contains("remove"))) {
        return new JSONObject()
                .put("intent", "delete_record")
                .put("data", new JSONObject()
                        .put("table", table)
                        .put("id", id));
    }

    if (table != null && id > 0 && text.contains("archive")) {
        return new JSONObject()
                .put("intent", "archive_record")
                .put("data", new JSONObject()
                        .put("table", table)
                        .put("id", id));
    }
    
    if (text.contains("archived") && text.contains("student")) {
        return new JSONObject()
                .put("intent", "get_archived_students")
                .put("data", new JSONObject());
    }

    return null;
}
    private static String detectTable(String text) {
        if (text.contains("student")) return "students";
        if (text.contains("course")) return "courses";
        if (text.contains("curriculum")) return "curriculum";
        if (text.contains("department")) return "departments";
        if (text.contains("enrollment")) return "enrollments";
        if (text.contains("grade")) return "grades";
        if (text.contains("instructor")) return "instructors";
        if (text.contains("room")) return "rooms";
        if (text.contains("schedule")) return "schedules";
        if (text.contains("school year") || text.contains("school_year")) return "school_years";
        if (text.contains("section")) return "sections";
        if (text.contains("semester")) return "semesters";
        if (text.contains("subject")) return "subjects";

        return null;
    }

    private static int extractId(String text) {
        Pattern pattern = Pattern.compile("\\b(?:id\\s*)?(\\d+)\\b");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }
    

    private static JSONObject callOllama(String adminPrompt) throws Exception {

        String systemInstruction = """
            You are an admin database CRUD assistant.

            Return exactly one JSON object only.
            No markdown. No explanation. No SQL.

            Admin can access these tables only:
            students, courses, curriculum, departments, enrollments, grades,
            instructors, rooms, schedules, school_years, sections, semesters, subjects

            Allowed generic intents:
            get_table_records,
            get_record_by_id,
            create_record,
            update_record,
            delete_record
            archive_record 
                                   
            Generic CRUD JSON formats:
            - Show table: {"intent":"get_table_records","data":{"table":"students"}}
            - Show by id: {"intent":"get_record_by_id","data":{"table":"students","id":1}}
            - Create: {"intent":"create_record","data":{"table":"courses","values":{"course_name":"BSIT"}}}
            - Update: {"intent":"update_record","data":{"table":"courses","id":1,"values":{"course_name":"BSCS"}}}
            - Delete: {"intent":"delete_record","data":{"table":"courses","id":1}}
            - Archive: {"intent":"archive_record","data":{"table":"subjects","id":1}}

            Rules:
            - show/list/view/get/select/display table records = get_table_records
            - show/get/find/view a specific id = get_record_by_id
            - create/add/insert/new = create_record
            - update/change/edit/set/modify = update_record
            - delete/remove = delete_record
            - Use exact table names from the allowed list.
            - Put changed or created column values inside "values".
            - Unknown values must be "" or 0.
            - "archived students" means get_archived_students (NOT archive_record)

            Examples:
            - "show archived students" -> {"intent":"get_archived_students","data":{}}
            - "show all students" -> {"intent":"get_table_records","data":{"table":"students"}}
            - "show courses" -> {"intent":"get_table_records","data":{"table":"courses"}}
            - "show instructors" -> {"intent":"get_table_records","data":{"table":"instructors"}}
            - "show room 1" -> {"intent":"get_record_by_id","data":{"table":"rooms","id":1}}
            - "create course with course_name BSIT" -> {"intent":"create_record","data":{"table":"courses","values":{"course_name":"BSIT"}}}
            - "update room 1 room_name to Lab 101" -> {"intent":"update_record","data":{"table":"rooms","id":1,"values":{"room_name":"Lab 101"}}}
            - "delete subject 3" -> {"intent":"delete_record","data":{"table":"subjects","id":3}}
            """;
            

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "llama3.2:3b");
        requestBody.put("stream", false);

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

    private static JSONObject extractUpdateValues(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


  
}