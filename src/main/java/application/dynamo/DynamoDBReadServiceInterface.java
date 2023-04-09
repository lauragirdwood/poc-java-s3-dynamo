package application.dynamo;

import application.domain.PedidoEntity;

import java.util.List;

public interface DynamoDBReadServiceInterface {
    List<PedidoEntity> getAllPedidos();
}
