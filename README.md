## PoC¹ aplicação Java integrada com S3 e Dynamo usando SDKs² v2 da AWS
¹ PoC: Proof of Concept (Prova de Conceito)

² SDK: Software Development Kit (Kit de Desenvolvimento de Software)

## Tecnologias utilizadas
Java 11, Maven, AWS S3, AWS DynamoDB, JUnit 5 (Jupiter), Mockito

## Como esse repositório está organizado

| Tema       | Caminho da pasta ou arquivo (ou 'path')                           | Descrição |
|------------|-------------------------------------------------------------------|----------------------------------------------------------|
| Domain     | [/src/main/java/application/domain/](domain)                      |  Entidade DynamoDB e Classe POJO do domínio de Pedido    |
| Dynamo     | [/src/main/java/application/dynamo/DynamoDBService.java](dynamo)  |  Serviço para select e update com DynamoDbClient         |
| S3         | [/src/main/java/application/s3/S3BucketService.java](s3)          |  Serviço para getObject com S3Client e Object Mapper     |
| Utils      | [/src/main/java/application/utils/](utils)                        |  Classes utilitárias geradoras e leitoras de .json local |
| Main       | [/src/main/java/application/Application.java](main)               |  Início da execução via método main e regras de negócio  |


## Print do console com resultado da execução do código via método main:
![image](https://user-images.githubusercontent.com/8313184/230374806-9178a772-0970-4265-87c2-c69191b7ecfc.png)


## Pré-requisitos para rodar este projeto
- Conta na AWS criada e configurada com AWS IAM Identity Center
- Ambiente local de desenvolvimento configurado com JDK¹ 11 e Maven²
- *IDE com plugin*³ [AWS Toolkit](https://docs.aws.amazon.com/pt_br/toolkit-for-jetbrains/latest/userguide/welcome.html) instalado e conectado com a conta da AWS (ou com Docker Local simulando os serviços ex. Localstack)

¹ JDK: Java Development Kit (Kit de Dsenvolvimento Java)

² Maven: Apache Maven é uma ferramenta de gerenciamento de dependências e build de projetos em Java

³ IDE: Integrated Development Environment (Ambiente Integrado de Desenvolvimento) como a **Intellij Communitty Edition 2023.1**

³ Plugin: Se a IDE fosse um carro, instalar um plugin na IDE seria equivalente a equipar um carro com ar-condicionado, vidros elétricos etc.
  
## Passos realizados:

1. Acessar conta da aws a partir da IDE com ajuda do plugin AWS Toolkit para autenticação usando o arquivo local [~/.aws/credentials](https://docs.aws.amazon.com/pt_br/toolkit-for-jetbrains/latest/userguide/setup-credentials.html)

2. Configurar os sdks de java da aws no pom.xml e teste de instanciar os clients no main

    - QUAL SDK pra S3?
   
        1. software.amazon.awssdk.crt
            
            - https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/crt-based-s3-client.html
            - https://docs.aws.amazon.com/pt_br/sdk-for-java/latest/developer-guide/lambda-optimize-starttime.html
            - https://aws.amazon.com/blogs/developer/introducing-crt-based-s3-client-and-the-s3-transfer-manager-in-the-aws-sdk-for-java-2-x/
            
            (*devido ao uso de conceitos de comunicação assíncrona achei muito complexo para debugar quando dá problema =/*)

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

        2. aws-java-sdk-s3 *o clássico sdk v1*

            - https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/java/example_code/s3/src/main/java/aws/example/s3/GetObject.java
            - https://docs.aws.amazon.com/pt_br/sdk-for-java/v1/developer-guide/examples-s3-objects.html

        3. software.amazon.awssdk *sdk v2 + recente*

            - **OPÇÃO ESCOLHIDA**            
            - https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/s3
            - https://docs.aws.amazon.com/pt_br/sdk-for-java/latest/developer-guide/examples-s3-objects.html

    - QUAL SDK PARA DYNAMODB?
    
        1. software.amazon.awssdk *sdk v2 + recente*

            - **OPÇÃO ESCOLHIDA**         
            - https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/example_code/dynamodb
        
            *DynamoDbClient - entre outros, dá acesso ao métodos CRUD do DynamoDB, como putItem(), getItem(), updateItem() e deleteItem()*
            
            *DynamoDbEnhancedClient - abstração do DynamoDbClient, entre outros, provê anotações de mapeamento de uma classe Java para uma tabela do DynamoDB.*


3. Criar um bucket s3 com uma pasta e um arquivo json dentro (criado na mão mesmo via console)

4. Utilizar o client da da sdk do s3 para ler o arquivo json do bucket criado e armazenar o que foi lido em uma lista (S3BucketService.java)

    - *Primeiro foram criados métodos para ler e criar/escrever em um arquivo json local*

5. Criar uma tabela no DynamoDB cuja Partition Key (Hash) seja o campo pedido_id
 - script aws-cli para criação da tabela de pedidos:

        aws dynamodb create-table  --table-name pedidos --attribute-definitions AttributeName=pedido_id,AttributeType=S --key-schema AttributeName=pedido_id,KeyType=HASH --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1

 - [Guia referência de comandos AWs CLI](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/index.html)

6. Fazer um select all na tabela criada (foi feito um scan na verdade) e armazenar o resultado em uma lista (DynamoDbService.java)

    6a. Primeiro foram feitos inserts de pedidos para gerar massa de teste na tabela criada:
    - scripts para fazer insert de pedidos via cli:

            aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"8ca89336-fb15-4af2-b2be-5f8af8d16bb2"},"status":{"S":"false"}}'
            aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"31067442-1a52-4a67-8e40-94330c69e68a"},"status":{"S":"false"}}'
            aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"8c00b8cc-ee53-43e1-b49e-6b890fceb5dd"},"status":{"S":"false"}}'
            aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"64ee848a-d210-40b9-87f3-6b8c3a5a6601"},"status":{"S":"false"}}'
            aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"f6309ab5-6585-4c71-a435-a94a7c4f6f83"},"status":{"S":"false"}}'
            aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"9f6dd60c-df71-4e03-9609-76895801e0dd"},"status":{"S":"false"}}'
            aws dynamodb put-item --table-name pedidos --item '{"pedido_id":{"S":"24ffb2af-0d5d-4aa9-b950-abda46a2b766"},"status":{"S":"false"}}'
    
7. Comparar cada registro da lista retornada pelo select/scan com todos com pedidos da lista do json

    7.1. Caso encontre algum registro que exista nas duas listas, verificar se o campo 'acao' do json contém uma string "CANCELAR"

    7.2. Caso seja 'CANCELAR' mesmo, fazer um update no registro da tabela marcando o campo 'status' como TRUE.



# TODOs

- testes unitários
- classe utils para fazer upload de arquivo no s3
- classe utils para gerar querys de insert automatizadas para o dynamo
