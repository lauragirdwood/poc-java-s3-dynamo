package application.dynamo;

import application.domain.Pedido;
import application.domain.PedidoEntity;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class DynamoDBService {

    static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .credentialsProvider(ProfileCredentialsProvider.create())
            .region(Region.US_EAST_2)
            .build();

    public static List<PedidoEntity> getAllPedidosFromDynamoDB() {

        List<PedidoEntity> pedidosList = new ArrayList<>();

        try {
            ScanRequest request = ScanRequest.builder()
                    .tableName("pedidos")
                    .build();

            ScanResponse response = dynamoDbClient.scan(request);

            for (Map<String, AttributeValue> item : response.items()) {
                PedidoEntity pedido = new PedidoEntity();
                pedido.setPedidoId(item.get("pedido_id").s());
                pedido.setStatus(item.get("status").s());
                pedidosList.add(pedido);
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }

        return pedidosList;
    }

    public static void updateStatusPedido(String idPedido, String novoStatus) {

        HashMap<String,AttributeValue> itemKey = new HashMap<>();
        itemKey.put("pedido_id", AttributeValue.builder()
                .s(idPedido)
                .build());

        HashMap<String,AttributeValueUpdate> updatedValues = new HashMap<>();
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
        }

        System.out.println("The Amazon DynamoDB table was updated!");
    }
}


