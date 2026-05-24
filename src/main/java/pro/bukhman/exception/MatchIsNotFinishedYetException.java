package pro.bukhman.exception;

public class MatchIsNotFinishedYetException extends AppException {
    public MatchIsNotFinishedYetException(String message) {
        super(message);
    }

    public MatchIsNotFinishedYetException(String message, Throwable cause) {
        super(message, cause);
    }
}
