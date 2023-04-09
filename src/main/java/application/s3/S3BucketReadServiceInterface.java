package application.s3;

import application.domain.Pedido;

import java.util.List;

public interface S3BucketReadServiceInterface {

    List<Pedido> getArquivoPedidos();
}
