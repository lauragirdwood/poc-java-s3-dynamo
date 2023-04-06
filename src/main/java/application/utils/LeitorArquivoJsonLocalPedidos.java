package application.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import application.domain.Pedido;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LeitorArquivoJsonLocalPedidos {

    public static void main(String[] args) {

        Gson gson = new Gson();
        try {
            // Ler o arquivo JSON
            List<Pedido> listaPedidos = gson.fromJson(
                    new FileReader("teste.json"),
                    new TypeToken<List<Pedido>>() {
                    }.getType()
            );

            // Iterar a lista de pedidos e imprimir cada um
            for (Pedido pedido : listaPedidos) {
                System.out.println("ID Pedido: " + pedido.getIdPedido());
                System.out.println("Nome Pedido: " + pedido.getAcao());
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
