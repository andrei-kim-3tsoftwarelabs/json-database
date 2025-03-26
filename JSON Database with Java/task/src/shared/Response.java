package shared;

public class Response {
    public enum STATUS {
        OK,
        ERROR,
    }

    STATUS response;
    String value;
    String reason;

    public Response(STATUS response) {
        this.response = response;
    }

    public void setResponse(STATUS value) {
        this.response = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setReason(String value) {
        this.reason = value;
    }
}
