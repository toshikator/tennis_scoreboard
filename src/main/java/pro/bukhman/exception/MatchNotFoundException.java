package pro.bukhman.exception;

public class MatchNotFoundException extends AppException {
    public MatchNotFoundException(String message) {
        super(message);
    }

    public MatchNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
