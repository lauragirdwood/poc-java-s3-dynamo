import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeradorArquivoJsonObjetos {

    @SerializedName("lista")
    private List<Objeto> listaObjeto;

    public List<Objeto> getListaObjeto() {
        return listaObjeto;
    }

    public void setListaObjeto(List<Objeto> listaObjeto) {
        this.listaObjeto = listaObjeto;
    }

    public static void main(String[] args) {

        int numObjetos = 100000;
        List<Objeto> listaObjeto = new ArrayList<>();
        for (int i = 0; i < numObjetos; i++) {
            Objeto objeto = new Objeto();
            listaObjeto.add(objeto);
        }
        GeradorArquivoJsonObjetos geradorArquivoJsonObjetos = new GeradorArquivoJsonObjetos();
        geradorArquivoJsonObjetos.setListaObjeto(listaObjeto);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(geradorArquivoJsonObjetos);
        try {
            FileWriter writer = new FileWriter("teste.json");
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class Objeto {
        @SerializedName("id")
        private String id = "123e4567-e89b-12d3-a456-426655440000";
        private String nome = "ALEATORIO";
        public String getId() {
            return id;
        }
        public void setClearingId(String id) {
            this.id = id;
        }
        public String getNome() {
            return nome;
        }
        public void setNome(String nome) {
            this.nome = nome;
        }
    }

}
