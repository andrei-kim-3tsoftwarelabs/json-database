package server;

import com.google.gson.JsonObject;
import shared.Response;

public class Database {
    private JsonObject data;

    Database() {
        data = new JsonObject();
    }

    protected Response set(String key, String value) {
        data.addProperty(key, value);
        return new Response(Response.STATUS.OK);
    }

    protected Response get(String key) {
        if (data.has(key)) {
            Response response = new Response(Response.STATUS.OK);
            response.setValue(data.get(key).toString());
            return response;
        } else {
            Response response = new Response(Response.STATUS.ERROR);
            response.setReason("No such key");
            return response;
        }
    }

    protected Response delete(String key) {
        if (data.has(key)) {
            data.remove(key);
            return new Response(Response.STATUS.OK);
        } else {
            Response response = new Response(Response.STATUS.ERROR);
            response.setReason("No such key");
            return response;
        }
    }
}
