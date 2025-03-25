package server;

import java.util.ArrayList;
import java.util.Arrays;

public class Database {
    private final int ARRAY_SIZE = 1000;
    private String[] data;

    Database() {
        data = new String[ARRAY_SIZE];
        Arrays.fill(data, "");
    }

    protected String set(int index, String value) {
        if (boundCheck(index)) {
            data[index] = value;
            return "OK";
        } else {
            return "ERROR";
        }
    };

    protected String get(int index) {
        if (boundCheck(index)) {
            return data[index];
        } else {
            return "ERROR";
        }
    };

    protected String delete(int index) {
        if (boundCheck(index)) {
            data[index] = "";
            return "OK";
        } else {
            return "ERROR";
        }
    };

    private boolean boundCheck(int index) {
        if (index < 0 || index >= ARRAY_SIZE) {
            return false;
        }

        return !"".equals(data[index]);
    }
}
