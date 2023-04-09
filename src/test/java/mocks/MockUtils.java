package mocks;

import application.domain.Pedido;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MockUtils {

    private static final String idPedido1 = UUID.randomUUID().toString();
    private static final String idPedido2 = UUID.randomUUID().toString();
    private static final String acaoCancelar = "CANCELAR";
    private static final String acaoconfirmar = "CONFIRMAR";

    public static List<Pedido> getListaPedidos() {
        List<Pedido> listaPedidos = new ArrayList<>();

        Pedido pedido1 = new Pedido();
        pedido1.setIdPedido(idPedido1);
        pedido1.setAcao(acaoCancelar);
        listaPedidos.add(pedido1);

        Pedido pedido2 = new Pedido();
        pedido2.setIdPedido(idPedido2);
        pedido2.setAcao(acaoconfirmar);
        listaPedidos.add(pedido2);

        return listaPedidos;
    }


    public static byte[] getArquivoPedidosEmBytes() {
        return ("{\n" +
                "  \"pedidos\": [\n" +
                "    {\n" +
                "      \"pedido_id\": \""+idPedido1+",\n" +
                "      \"acao\": \""+acaoCancelar+"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"pedido_id\": \""+idPedido2+",\n" +
                "      \"acao\": \""+acaoconfirmar+"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}").getBytes();
    }
}
