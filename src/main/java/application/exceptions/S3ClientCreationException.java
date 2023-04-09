package application.exceptions;

public class S3ClientCreationException extends RuntimeException {

    public S3ClientCreationException(String message) {
        super(message);
    }
}