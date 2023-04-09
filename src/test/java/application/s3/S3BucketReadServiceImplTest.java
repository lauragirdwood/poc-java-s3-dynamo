package application.s3;

import application.configuration.ObjectMapperFactory;
import application.configuration.S3ClientFactory;
import application.domain.Pedido;
import application.exceptions.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mocks.MockUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class S3BucketReadServiceImplTest {

    @Mock
    private S3ClientFactory s3ClientFactory;

    @Mock
    private S3Client s3Client;

    @Mock
    private ObjectMapperFactory objectMapperFactory;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResponseInputStream<GetObjectResponse> objectResponse;

    private S3BucketReadServiceImpl s3BucketReadServiceImpl;

    private final byte[] s3FileContent = MockUtils.getArquivoPedidosEmBytes();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(s3ClientFactory.createS3Client()).thenReturn(s3Client);
        when(objectMapperFactory.createObjectMapper()).thenReturn(objectMapper);
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(objectResponse);
    }

    @Test
    @DisplayName("Test getArquivoPedidos with success")
    public void shouldGetArquivoPedidos() throws IOException {
        List<Pedido> expected = MockUtils.getListaPedidos();

        when(objectResponse.readAllBytes()).thenReturn(s3FileContent);
        when(objectMapper.readValue(eq(s3FileContent), any(TypeReference.class))).thenReturn(expected);

        s3BucketReadServiceImpl = new S3BucketReadServiceImpl(s3ClientFactory, objectMapperFactory);
        List<Pedido> actual = s3BucketReadServiceImpl.getArquivoPedidos();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertDoesNotThrow(() -> s3BucketReadServiceImpl.getArquivoPedidos());
    }


    @Test
    @DisplayName("Test fail to instantiate S3BucketReadServiceImpl with any null dependencies")
    public void shouldHandleNullPointerOnConstructor() {
        assertThrows(
                S3BucketServiceCreationException.class,
                () -> new S3BucketReadServiceImpl(null, null),
                "Dependencies s3ClientFactory and objectMapperFactory are null"
        );
        assertThrows(
                S3ClientCreationException.class,
                () -> new S3BucketReadServiceImpl(null, objectMapperFactory),
                "Dependency s3ClientFactory is null"
        );
        assertThrows(
                ObjectMapperCreationException.class,
                () -> new S3BucketReadServiceImpl(s3ClientFactory, null),
                "Dependency objectMapperFactory is null"
        );
    }

    @Test
    @DisplayName("Test fail to getArquivoPedidos with S3Exception")
    public void shouldHandleS3Exception() {
        S3Exception s3Exception =
                (S3Exception) S3Exception.builder()
                        .awsErrorDetails(AwsErrorDetails.builder()
                                .errorMessage("s3 exception error message simulation").build())
                        .build();

        s3BucketReadServiceImpl = new S3BucketReadServiceImpl(s3ClientFactory, objectMapperFactory);
        when(s3Client.getObject(any(GetObjectRequest.class))).thenThrow(s3Exception);

        assertThrows(S3Exception.class, () -> s3BucketReadServiceImpl.getArquivoPedidos());
    }

    @Test
    @DisplayName("Test fail to getArquivoPedidos with NoSuchKeyException")
    public void shouldHandleNoSuchKeyException() {
        NoSuchKeyException noSuchKeyException =
                NoSuchKeyException.builder()
                        .awsErrorDetails(AwsErrorDetails.builder()
                                .errorMessage("no such key error message simulation").build())
                        .build();

        s3BucketReadServiceImpl = new S3BucketReadServiceImpl(s3ClientFactory, objectMapperFactory);
        when(s3Client.getObject(any(GetObjectRequest.class))).thenThrow(noSuchKeyException);

        assertThrows(NoSuchKeyException.class, () -> s3BucketReadServiceImpl.getArquivoPedidos());
    }

}
