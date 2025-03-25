package client;

import java.util.List;
import java.util.ArrayList;

import com.beust.jcommander.Parameter;

public class Arguments {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = "-t", description = "the type of the request")
    String type;
    @Parameter(names = "-i", description = "the index of the cell")
    int index;
    @Parameter(names = "-m", description = "message to save in the database (only needed for set requests)")
    String message = "";
}
