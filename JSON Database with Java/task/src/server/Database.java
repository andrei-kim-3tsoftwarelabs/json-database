package server;

import com.google.gson.*;
import shared.Response;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Database {
    private JsonObject database;

    private String FILENAME_TEST_ENVIRONMENT = System.getProperty("user.dir") + "/src/server/data/";
    private String FILENAME_LOCAL_ENVIRONMENT = System.getProperty("user.dir") + "/JSON Database with Java/task/src/server/data/";
    private final String databaseName = "db.json";

    Database() {
        database = restoreDatabase();
    }

    protected Response set(JsonElement key, JsonElement value) {
        if (key.isJsonPrimitive()) {
            database.add(key.getAsString(), value);
        } else {
            JsonArray keys = key.getAsJsonArray();
            JsonElement dbLevel = database;

            for (int i = 0; i < keys.size() - 1; i++) {
                if (!dbLevel.getAsJsonObject().has(keys.get(i).getAsString())) {
                    dbLevel.getAsJsonObject().add(keys.get(i).getAsString(), new JsonObject());
                }
                dbLevel = dbLevel.getAsJsonObject().get(keys.get(i).getAsString());
            }
            database.add(keys.get(keys.size() - 1).getAsString(), value);
        }

        persistDatabase();
        return new Response(Response.STATUS.OK);
    }

    protected Response get(JsonElement key) {
        if (key.isJsonPrimitive()) {
            return getPrimitiveValue(key);
        }
        return getJsonValue(key);
    }

    protected Response getPrimitiveValue(JsonElement key) {
        if (database.has(key.getAsString())) {
            Response response = new Response(Response.STATUS.OK);
            response.setValue(database.get(key.getAsString()).toString());
            return response;
        }
        return noKeyErrorResponse();
    }

    protected Response getJsonValue(JsonElement key) {
        JsonArray keys = key.getAsJsonArray();
        JsonElement dbLevel = database;

        for (JsonElement jsonKey : keys) {
            if (!dbLevel.isJsonPrimitive() && dbLevel.getAsJsonObject().has(jsonKey.getAsString())) {
                dbLevel = dbLevel.getAsJsonObject().get(jsonKey.getAsString());
            } else {
                return noKeyErrorResponse();
            }
        }

        Response response = new Response(Response.STATUS.OK);
        response.setValue(dbLevel.getAsString());
        return response;
    }

    protected Response delete(JsonElement key) {
        if (key.isJsonPrimitive()) {
            if (database.has(key.getAsString())) {
                database.remove(key.getAsString());
            } else {
                return noKeyErrorResponse();
            }
        } else {
            JsonArray keys = key.getAsJsonArray();
            JsonElement dbLevel = database;

            for (int i = 0; i < keys.size() - 1; i++) {
                if (dbLevel.getAsJsonObject().has(keys.get(i).getAsString())) {
                    dbLevel = dbLevel.getAsJsonObject().get(keys.get(i).getAsString());
                } else {
                    return noKeyErrorResponse();
                }
            }
            dbLevel.getAsJsonObject().remove(keys.get(keys.size() - 1).getAsString());
        }
        persistDatabase();
        return new Response(Response.STATUS.OK);
    }

    private Response noKeyErrorResponse() {
        Response response = new Response(Response.STATUS.ERROR);
        response.setReason("No such key");
        return response;
    }

    private void persistDatabase() {
        File file = new File(getPathToDatabase());
        if (!file.exists()) {
            createDatabaseFiles();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(database.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDatabaseFiles() {
        File dir = new File(getPathToDatabaseFolder());
        File file = new File(getPathToDatabase());
        try {
            boolean isDirCreated = dir.mkdirs();
            boolean createdNew = file.createNewFile();
            if (createdNew && isDirCreated) {
                System.out.println("The file was successfully created.");
            } else {
                System.out.println("The file already exists.");
            }
        } catch (IOException e) {
            System.out.println("Cannot create the file: " + file.getPath());
        }
    }

    private JsonObject restoreDatabase() {
        File file = new File(getPathToDatabase());

        JsonElement json = new JsonObject();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {
                json = JsonParser.parseReader(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return json.getAsJsonObject();
    }

    private String getPathToDatabaseFolder() {
        return FILENAME_LOCAL_ENVIRONMENT;
    }

    private String getPathToDatabase() {
        return FILENAME_LOCAL_ENVIRONMENT + databaseName;
    }
}
