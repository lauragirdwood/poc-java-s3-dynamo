## Passos realizados:


OK acessar minha conta da aws a partir da IDE (rodo o rolê das credenciais)

OK configurar os sdks de java da aws na IDE

    - QUAL SDK pra S3?
        - quais opções que tem?
            1. software.amazon.awssdk.crt ****SÓ DESGRAÇA ASYNC COMPLETABLE FUTURE E OS CARALHO***
                https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/crt-based-s3-client.html
                https://docs.aws.amazon.com/pt_br/sdk-for-java/latest/developer-guide/lambda-optimize-starttime.html
                https://aws.amazon.com/blogs/developer/introducing-crt-based-s3-client-and-the-s3-transfer-manager-in-the-aws-sdk-for-java-2-x/

                        // Create client
                        S3AsyncClient s3AsyncClient =
                                S3AsyncClient.crtBuilder()
                                        .credentialsProvider(DefaultCredentialsProvider.create())
                                        .region(Region.US_EAST_2)
                                        .targetThroughputInGbps(20.0)
                                        .minimumPartSizeInBytes(8L)
                                        .build();

                        // Download an object from Amazon S3 to a local file
                        GetObjectResponse getObjectResponse =
                                s3AsyncClient.getObject(req -> req.bucket("testes-laura")
                                                        .key("/teste/teste.json"),
                                                AsyncResponseTransformer.toFile(Paths.get("/download.json")))
                                        .join();

                    }

            2. aws-java-sdk-s3 ***O NORMALZAO QUE FUNCIONA - clássico do sdk v1***
            https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/java/example_code/s3/src/main/java/aws/example/s3/GetObject.java
            https://docs.aws.amazon.com/pt_br/sdk-for-java/v1/developer-guide/examples-s3-objects.html

            3. software.amazon.awssdk ***O NORMALZAO QUE FUNCIONA - só que do sdk v2 + recente***

    - Qual SDK para dynamo?
            https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb
            1. software.amazon.awssdk ***O NORMALZAO QUE FUNCIONA - sdk v2***
                DynamoDbClient - entre outros, dá acesso ao métodos CRUD do DynamoDB, como putItem(), getItem(), updateItem() e deleteItem(),
                DynamoDbEnhancedClient - abstração do DynamoDbClient, entre outros, provê anotações de mapeamento de uma classe Java para uma tabela do DynamoDB.


OK criar um bucket s3 com uma pasta e um json dentro (criado na mão mesmo via console)

OK ler o arquivo json de um bucket s3 e armazenar o resultado em uma lista 
           OK - 1º testes lendo e escrevendo em arquivo json local

OK criar um dynamo com 1 tabela cujo id (hash) seja o campo pedido_id
 - script aws-cli para criação da tabela:

        aws dynamodb create-table  --table-name pedidos --attribute-definitions AttributeName=pedido_id,AttributeType=S --key-schema AttributeName=pedido_id,KeyType=HASH --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1


OK fazer um select em uma tabela e armazenar o resultado em uma lista
           OK - 1º fazer inserts de pedidos na tabela via aws-cli
    
   scripts para fazer insert de 01 item via cli:
   (como tem aspas duplas, se rodar no cmd windows precisa por barra invertida pra fazer o escape do caractere, no linux/git bash dá bom)
    
    aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"8ca89336-fb15-4af2-b2be-5f8af8d16bb2"},"status":{"S":"false"}}'
    aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"31067442-1a52-4a67-8e40-94330c69e68a"},"status":{"S":"false"}}'
    aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"8c00b8cc-ee53-43e1-b49e-6b890fceb5dd"},"status":{"S":"false"}}'
    aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"64ee848a-d210-40b9-87f3-6b8c3a5a6601"},"status":{"S":"false"}}'
    aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"f6309ab5-6585-4c71-a435-a94a7c4f6f83"},"status":{"S":"false"}}'
    aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"9f6dd60c-df71-4e03-9609-76895801e0dd"},"status":{"S":"false"}}'
    aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"24ffb2af-0d5d-4aa9-b950-abda46a2b766"},"status":{"S":"false"}}'


OK comparar cada registro da lista retornada pelo select com todos com pedidos da lista do json

OK caso encontre algum registro igual, verificar se um campo do json contém uma string "CANCELAR",

OK caso tenha essa string, fazer um update nesse registro marcando ele como TRUE.



# TODOs

- testes unitários
- classe utils para fazer upload de arquivo no s3
- classe utils para gerar querys de insert automatizadas para o dynamo
