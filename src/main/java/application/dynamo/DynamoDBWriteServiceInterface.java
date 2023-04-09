package application.dynamo;

public interface DynamoDBWriteServiceInterface {
    void updateStatusPedido(String idPedido, String novoStatus);
}
