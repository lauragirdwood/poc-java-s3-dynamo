package application.dynamo;

import application.configuration.DynamoDBClientFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;

public class DynamoDBWriteServiceImpl implements DynamoDBWriteServiceInterface {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDBWriteServiceImpl(DynamoDBClientFactory dynamoDBClientFactory) {
        this.dynamoDbClient = dynamoDBClientFactory.createDynamoClient();
    }

    @Override
    public void updateStatusPedido(String idPedido, String novoStatus) {

        HashMap<String, AttributeValue> itemKey = new HashMap<>();
        itemKey.put("pedido_id", AttributeValue.builder()
                .s(idPedido)
                .build());

        HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();
        updatedValues.put("status", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(novoStatus).build())
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName("pedidos")
                .key(itemKey)
                .attributeUpdates(updatedValues)
                .build();

        try {
            dynamoDbClient.updateItem(request);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            throw e;
        } finally {
            System.out.println("Closing the DynamoDB client");
            dynamoDbClient.close();
        }

        System.out.println("The Amazon DynamoDB table was updated!");
    }
}
