package application;

import application.configuration.DynamoDBClientFactory;
import application.configuration.ObjectMapperFactory;
import application.configuration.S3ClientFactory;
import application.domain.Pedido;
import application.domain.PedidoEntity;
import application.dynamo.*;
import application.s3.S3BucketReadServiceImpl;
import application.s3.S3BucketReadServiceInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Application {

    public static void main(String[] args) {

        S3BucketReadServiceInterface s3BucketService = new S3BucketReadServiceImpl(
                new S3ClientFactory(), new ObjectMapperFactory()
        );

        DynamoDBReadServiceInterface dynamoReadService = new DynamoDBReadServiceImpl(
                new DynamoDBClientFactory()
        );

        DynamoDBWriteServiceInterface dynamoWriteService = new DynamoDBWriteServiceImpl(
                new DynamoDBClientFactory()
        );

        List<Pedido> pedidosFromS3 = s3BucketService.getArquivoPedidos();
        Set<String> idsPedidosFromDB = new HashSet<>();

        for (PedidoEntity pedidoDB : dynamoReadService.getAllPedidos())
            idsPedidosFromDB.add(pedidoDB.getPedidoId());

        for (Pedido pedidoS3 : pedidosFromS3) {
            if (idsPedidosFromDB.contains(pedidoS3.getIdPedido())) {

                if (pedidoS3.getAcao().equals("CANCELAR")) {
                    System.out.println(
                            "O pedido id " + pedidoS3.getIdPedido()
                            + " com ação marcada para " + pedidoS3.getAcao() +
                            " foi encontrado na lista do DynamoDB"
                    );
                    dynamoWriteService.updateStatusPedido(pedidoS3.getIdPedido(), "true");
                }
            }
        }
    }

}


