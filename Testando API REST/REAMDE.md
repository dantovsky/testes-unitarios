# Curso Testando API REST com REST-assured

Testando API Rest com REST-assured  
Aprenda a acessar e validar requisições de APIs Rest, utilizando JAVA, trabalhando com os formatos Json e XML  
https://www.udemy.com/course/testando-api-rest-com-rest-assured/

## Olá Mundo! :: API utilizada:

- https://restapi.wcaquino.me/ola
- https://restapi.wcaquino.me:80/ola (equivalente, explicitando a porta 80, padrão do protocolo HTTP)

Partes do endpoint:
- https://              => Protocolo
- restapi.wcaquino.me   => Endereço do server
- /ola                  => Recurso

RESPONSE :: resposta do servidor, a um pedido efetuado
- body: corpo da mensagem
- header: cabeçalho contendo o status code

Task: utilizando o REST-assured, obter a resposta da API.

```java
package br.ce.wcaquino.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class OlaMundo {

    public static void main(String[] args) {
        Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
        System.out.println(response.getBody().asString());
        System.out.println(response.statusCode());
    }
}
```

## Busca por IP - Solucionando problemas

Havendo erro no acesso a uma API, pode ser necessário:
- adicionar a porta
- obter o IP respectivo (fazer ping do endereço DNS)

## Status Code

São códigos que a resposta da requisição vai trazer.

HTTP response status codes  
https://developer.mozilla.org/en-US/docs/Web/HTTP/Status

Exemplo de alguns dos principais status code:

Respostas de sucesso
- 200 OK :: requisição bem sucedida
- 201 Created :: req bem sucedida e um novo recurso foi criado como resultado de um POST
- 204 No Content :: atendeu a req mas não tem mais nada a dizer

Respostas de erro do Cliente
- 404 Not Found :: quando a URL não existe
- 400 Bad Request :: o recurso existe mas não foi passado todos os dados necessários para atender o pedido
- 401 Unauthorized :: o cliente deve se autenticar para obter a resposta solicitada
- 403 Forbidden :: a identidade é reconhecida, porém o cliente não tem direito de acesso ao conteúdo solicitado, portanto o server rejeita em dar a resposta

Respostas de erro do Servidor
- 500 Internal server Error :: O server encontrou uma situação com a qual não sabe lidar
