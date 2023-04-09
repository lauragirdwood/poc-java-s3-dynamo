package application.dynamo;

import application.configuration.DynamoDBClientFactory;
import application.domain.PedidoEntity;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamoDBReadServiceImpl implements DynamoDBReadServiceInterface {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDBReadServiceImpl(DynamoDBClientFactory dynamoDBClientFactory) {
        this.dynamoDbClient = dynamoDBClientFactory.createDynamoClient();
    }

    @Override
    public List<PedidoEntity> getAllPedidos() {
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
            throw e;
        } finally {
            System.out.println("Closing the DynamoDB client");
            dynamoDbClient.close();
        }

        return pedidosList;
    }
}
