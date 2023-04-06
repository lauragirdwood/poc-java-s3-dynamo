package application;

import application.domain.Pedido;
import application.domain.PedidoEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static application.dynamo.DynamoDBService.getAllPedidosFromDynamoDB;
import static application.dynamo.DynamoDBService.updateStatusPedido;
import static application.s3.S3BucketService.getArquivoPedidos;

public class Main {

    public static void main(String[] args) {

        List<Pedido> pedidosFromS3 = getArquivoPedidos();
        Set<String> idsPedidosFromDynamoDB = new HashSet<>();

        for (PedidoEntity pedidoDB : getAllPedidosFromDynamoDB()) {
            idsPedidosFromDynamoDB.add(pedidoDB.getPedidoId());
        }

        for (Pedido pedidoS3 : pedidosFromS3) {
            // verifica se o pedido_id do S3 está na lista de pedidos do DynamoDB
            if (idsPedidosFromDynamoDB.contains(pedidoS3.getIdPedido())) {
                // se esse pedido_id encontrado estiver com ação 'cancelar' no arquivo do S3
                // faz update do status desse pedido_id na base de dados (dynamo)
                if (pedidoS3.getAcao().equals("CANCELAR")) {
                    System.out.println("O pedido id " + pedidoS3.getIdPedido() + "com ação marcada para " +pedidoS3.getAcao()+ " foi encontrado na lista do DynamoDB");
                    updateStatusPedido(pedidoS3.getIdPedido(), "true");
                }
            }
        }

        System.out.println("\nstatus final da base de dados:\n");

        getAllPedidosFromDynamoDB().forEach(System.out::println);
    }


}


