package shared;

import com.beust.jcommander.Parameter;
import com.google.gson.JsonElement;

public class Arguments {
    @Parameter(names = "-t", description = "specifies the type of request")
    private String type;
    @Parameter(names = "-k", description = "specifies the key", converter = JsonConverter.class)
    private JsonElement key;
    @Parameter(names = "-v", description = "specifies the value (only needed for set requests)", converter = JsonConverter.class)
    private JsonElement value;
    @Parameter(names = "-in", description = "specifies the input file for request")
    private String filename;

    public String getType() {
        return type;
    }

    public JsonElement getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }

    public boolean isFileForRequestProvided() {
        return filename != null;
    }

    public String getFilename() {
        return filename;
    }
}
