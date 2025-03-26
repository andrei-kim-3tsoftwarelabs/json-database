package shared;

import com.beust.jcommander.Parameter;

public class Arguments {
    @Parameter(names = "-t", description = "specifies the type of request")
    private String type;
    @Parameter(names = "-k", description = "specifies the key")
    private String key;
    @Parameter(names = "-v", description = "specifies the value (only needed for set requests)")
    private String value;

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
