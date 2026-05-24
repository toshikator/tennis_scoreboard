package pro.bukhman.exception;

public class MatchIsAlreadyFinishedException extends AppException {

    public MatchIsAlreadyFinishedException(String message) {
        super(message);
    }

    public MatchIsAlreadyFinishedException(String message, Throwable cause) {
        super(message, cause);
    }
}
