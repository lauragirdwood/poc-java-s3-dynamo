package application.utils;

import application.domain.Pedido;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GeradorArquivoJsonLocalPedidos {

    //TODO gerador de arquivo no S3

    public static void main(String[] args) {

        int numPedidos = 10;
        List<Pedido> listaPedidos = new ArrayList<>();

        for (int i = 0; i < numPedidos; i++) {
            Pedido pedido = new Pedido();
            pedido.setIdPedido(UUID.randomUUID().toString());

            if (i % 3 == 0)
                pedido.setAcao("CANCELAR");
            else
                pedido.setAcao("CONFIRMAR");

            listaPedidos.add(pedido);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(listaPedidos);

        try {
            FileWriter fileWriter = new FileWriter("teste.json");
            fileWriter.write(json);
            System.out.println("Arquivo gerado!");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
