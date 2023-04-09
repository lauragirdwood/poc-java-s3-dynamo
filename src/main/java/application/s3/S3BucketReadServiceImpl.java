package application.s3;

import application.configuration.ObjectMapperFactory;
import application.configuration.S3ClientFactory;
import application.domain.Pedido;
import application.exceptions.ObjectMapperCreationException;
import application.exceptions.S3BucketServiceCreationException;
import application.exceptions.S3ClientCreationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.List;

public class S3BucketReadServiceImpl implements S3BucketReadServiceInterface {

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;

    public S3BucketReadServiceImpl(S3ClientFactory s3ClientFactory, ObjectMapperFactory objectMapperFactory) {
        if (s3ClientFactory == null && objectMapperFactory == null)
            throw new S3BucketServiceCreationException("s3ClientFactory and objectMapperFactory are null");
        else if (s3ClientFactory == null) {
            throw new S3ClientCreationException("s3ClientFactory is null");
        }
        else if (objectMapperFactory == null) {
            throw new ObjectMapperCreationException("objectMapperFactory is null");
        }
        this.s3Client = s3ClientFactory.createS3Client();
        this.objectMapper = objectMapperFactory.createObjectMapper();
    }

    @Override
    public List<Pedido> getArquivoPedidos() {

        List<Pedido> pedidosList;

        try {
            byte[] s3FileContent = readS3Object();
            pedidosList = objectMapper.readValue(s3FileContent, new TypeReference<List<Pedido>>() {});

        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getCause().getMessage());
            System.err.println(e.getCause().getCause().getMessage());
            throw new RuntimeException(e);
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            throw e;
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getCause().getMessage());
            System.err.println(e.getCause().getCause().getMessage());
            throw new RuntimeException("null pointer aqui nao", e);
        } finally {
            s3Client.close();
        }

        return pedidosList;
    }

    private byte[] readS3Object() throws IOException {

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .key("teste/teste.json")
                .bucket("testes-laura")
                .build();

        ResponseInputStream<GetObjectResponse> objectResponse = s3Client.getObject(objectRequest);

        return objectResponse.readAllBytes();
    }

}
