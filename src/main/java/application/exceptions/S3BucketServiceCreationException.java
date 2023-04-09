package application.exceptions;

public class S3BucketServiceCreationException extends RuntimeException {

    public S3BucketServiceCreationException(String message) {
        super(message);
    }
}
