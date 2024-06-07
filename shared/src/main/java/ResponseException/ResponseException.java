package ResponseException;

public class ResponseException extends Exception{
    private final int status;
    public ResponseException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
