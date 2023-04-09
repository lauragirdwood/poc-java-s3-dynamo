package application.exceptions;

public class DynamoDBClientCreationException extends RuntimeException {

    public DynamoDBClientCreationException(String message) {
        super(message);
    }
}