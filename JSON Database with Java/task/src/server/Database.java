package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    protected Response set(String key, String value) {
        database.addProperty(key, value);
        persistDatabase();
        return new Response(Response.STATUS.OK);
    }

    protected Response get(String key) {
        if (database.has(key)) {
            Response response = new Response(Response.STATUS.OK);
            response.setValue(database.get(key).toString());
            return response;
        } else {
            Response response = new Response(Response.STATUS.ERROR);
            response.setReason("No such key");
            return response;
        }
    }

    protected Response delete(String key) {
        if (database.has(key)) {
            database.remove(key);
            persistDatabase();
            return new Response(Response.STATUS.OK);
        } else {
            Response response = new Response(Response.STATUS.ERROR);
            response.setReason("No such key");
            return response;
        }
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
