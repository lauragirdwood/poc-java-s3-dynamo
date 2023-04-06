import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LeitorArquivoJsonObjetos {

    public static void main(String[] args) {
        Gson gson = new Gson();
        try {
            // Ler o arquivo JSON
            List<Objeto> listaObjeto = gson.fromJson(
                    new FileReader("teste.json"),
                    new TypeToken<List<Objeto>>() {
                    }.getType()
            );
             // Iterar sobre a lista de objetos e imprimir cada um--p0
            for (Objeto objeto : listaObjeto) {
                System.out.println("ID Objeto: " + objeto.getId());
                System.out.println("Nome Objeto: " + objeto.getNome());
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class Objeto {
        private String id;
        private String nome;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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
