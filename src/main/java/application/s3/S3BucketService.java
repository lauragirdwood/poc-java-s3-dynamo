package application.s3;

import application.domain.Pedido;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class S3BucketService {

    static final S3Client s3Client = S3Client.builder()
            .region(Region.US_EAST_2)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

    static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Pedido> getArquivoPedidos() {

        List<Pedido> pedidosList = new ArrayList<>();

        try {

            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key("teste/teste.json")
                    .bucket("testes-laura")
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
            byte[] s3FileContent = objectBytes.asByteArray();
            pedidosList = objectMapper.readValue(s3FileContent, new TypeReference<List<Pedido>>() {});

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }

        return pedidosList;
    }

}
