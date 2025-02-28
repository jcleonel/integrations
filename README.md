# Desafio Integrations

Projeto foi desenvolvido com as tecnologias:
- Java 17
- Spring Boot
- Spring Security
- JWT
- JPA
- Swagger
- HETEOAS
- Testes Unitários
- PostgreSQL
- RabbitMQ
- Docker
- Flyway

OBS: Alguns dados de Product, Customer e User já estão sendo inseridos por migrations do Flyway

## Pré-requisitos
- Docker instalado
- Docker Compose instalado

## Imagem no Docker Hub
- https://hub.docker.com/r/jcleonel/desafiointegrations

## Como Executar
- Crie uma pasta no seu local de preferencia (preferencialmente na raiz do sistema, Ex: C:)
- Dentro da pasta crie o arquivo docker-compose.yml e coloque o script que vou escrever em seguida
- Execute algum terminal dentro desta pasta
- Execute o comando `docker-compose up` e aguarde todos os containers subirem

## Script para o arquivo docker-compose.yml
```
version: "3.8"
services:
  app:
    image: jcleonel/desafiointegrations:1.0
    ports:
      - "8080:8080"
    depends_on:
      - db
      - rabbitmq
    environment:
      SPRING_PROFILES_ACTIVE: docker
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/postgres
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: 1234
    networks:
      - app-network

  db:
    image: postgres:17
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 1234
      POSTGRES_DB: postgres
    ports:
      - "5432:5432"
    networks:
      - app-network

  rabbitmq:
    image: rabbitmq:4.0-management
    container_name: rabbitmq
    restart: always
    ports:
      - "5672:5672"
      - "15672:15672"
    volumes:
      - ./dados:/var/lib/rabbitmq/
    networks:
      - app-network

networks:
  app-network:
    driver: bridge
```

## Acessando os Serviços
Após a execução, os seguintes serviços estarão disponíveis:

- API: http://localhost:8080
- Documentação Swagger: http://localhost:8080/swagger-ui.html
- RabbitMQ Management:
  - URL: http://localhost:15672
  - Usuário: guest
  - Senha: guest
- PostgreSQL:
  - Host: localhost
  - Porta: 5432
  - Banco: postgres
  - Usuário: postgres
  - Senha: 1234

## Fazendo uma Request

Acesse o Swagger: http://localhost:8080/swagger-ui.html

Procure a Tag Authentication e o endpoint POST `/auth/signin`
![image](https://github.com/user-attachments/assets/88f54456-dbbf-4bf5-9c49-8f7119152c8d)

OBS: Atuamente não temos um endpoint para criar User, então temos alguns padrão com o Role de ADMIN
```
username: admin123
password: admin123

OU

username: admin234
password: admin234
```

OBS2: Atualmente não temos vínculo de User com Customer. Então a autenticação é apenas para bloquear as requests e fazer testes com o Spring Security.

Após logar na aplicação, pegue o token e autorize a API
![image](https://github.com/user-attachments/assets/a0cc37e5-b991-4ae2-bc47-238aad285fe9)

Após autenticar façam vários testes de Customers, Product e Order.

Crie um pedido de exemplo `POST /orders`
```
{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 1
    },
    {
      "productId": 2,
      "quantity": 1
    },
    {
      "productId": 3,
      "quantity": 1
    },
    {
      "productId": 4,
      "quantity": 1
    },
    {
      "productId": 5,
      "quantity": 1
    }
  ]
}
```
Todo pedido é criado com Status PENDING.

Exietem três status PENDING, PAID, CANCELED

Use o endepoint PUT `/orders/{id}` para cancelar um pedido
(O ideal é /orders/{id}/cancel)

Ou o endpoint PUT `/orders/{id}/status` para alterar entre os status, de acordo com as regras de negócios

________________________________________________________________________________________

Após alterar um status acesse o painel do RabbitMQ para ver as mensagens
  - URL: http://localhost:15672
  - Usuário: guest
  - Senha: guest

Após logar acesse a aba Queues and Streams para ver as mensagens da fila de alteração de status
Clique na fila `status.order`
![RabbitMQ0](https://github.com/user-attachments/assets/09a6c911-8824-4ea1-85cf-d3e9489fd069)

Clique em `Get messages` para ver as mensagens
![RabbitMQ1](https://github.com/user-attachments/assets/46f9ed75-d721-481d-896f-8d8901ec624d)

_____________________________________________________________________________________

Caso necessário configure o banco em algum gerenciador

_____________________________________________________________________________________

Se encontrar algum problema:

Certifique-se de que as portas 8080, 5432 e 5672/15672 não estão sendo usadas
Tente remover os containers e volumes antigos




