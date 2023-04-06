package application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Pedido {

    @SerializedName("pedido_id") //necessario para gerar arquivo Json
    @JsonProperty("pedido_id") //necessario para ler arquivo json
    private String idPedido;
    private String acao;

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }
}
