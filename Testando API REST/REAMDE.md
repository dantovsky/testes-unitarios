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

## JUnit

-> Questão - Diferença entre erros e falhas?

Falhas (um erro que não passou): ocorrem quando é lançado uma exceção do tipo "Asssertion Error".  
Ocorrem quando o teste é executado sem problemas, porém, alguma condição que era esperada não foi atendida (representado na forma de assertivas).

Erros (qualquer outra exceção): o teste encontra um problema sem nem chegar nas assertivas.  
Um erro acontece quando algum problema durante a execução do teste impede que o mesmo seja concluído, ou seja, quando ocorre alguma exceção não esperada e não tratada.

```java
package br.ce.wcaquino.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;

public class OlaMundoTest {

    @Test
    public void testOlaMundo() {
        Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me:80/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue("O status code deveria ser 201", response.statusCode() == 200);
        Assert.assertEquals(200, response.statusCode());

        // throw new RuntimeException();
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }
}
```

## Aula 8 - Modo Fluente

Modo Fluente (não é tão simples como o modo simples, mas dá mais legibilidade e organização).  
Essa forma utiliza uma sintaxe parecida com o Gerkin, utilizado no Cucumber

### Given When Then
- Give (dado)      :: uma pré-condião que queremos aplicar
- When (quando)    :: ação de fato
- Then (entao)     :: serão as assertivas que serão feitas

```java
public class OlaMundoTest {
    
    @Test // Outros modos de se trabalhar com o REST-assured
    public void devoConhecerOutrasFormasRestAssured() {

        // Modo simples
        get("https://restapi.wcaquino.me/ola").then().statusCode(200);

        // Modo fluente
        RestAssured
        .given()    // Pré condições
        .when()     // Ação
            .get("https://restapi.wcaquino.me/ola")
        .then()     // Assertivas
            // .assertThat() // pode usar o assertThat(), mas não muda nada (só leitura)
            .statusCode(200);
    }
}
```

## Aula 9 - Hamcrest

Biblioteca que é uma dependência do JUnit, e o REST-assured também trabalha com esta lib.

Documentação » org.hamcrest » Class Matchers  
http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html

```java
public class OlaMundoTest {

    @Test // Aula 9 - Hamcrest
    public void deveConhecerMatchersHamcrest() {
        // Assertions sem import static de Assert e Matchers
        // Assert.assertThat("Maria", Matchers.is("Maria")); // atual, matcher

        assertThat("Maria", is("Maria")); // atual, matcher
        assertThat(128, is(128));
        assertThat(128, isA(Integer.class));
        assertThat(128d, isA(Double.class));
        assertThat(128f, greaterThan(120f));
        assertThat(128f, lessThan(130f));

        List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
        assertThat(impares, hasSize(5));
        assertThat(impares, contains(1, 3, 5, 7, 9)); // tem que ter os mesmos nums e na mesma ordem
        assertThat(impares, containsInAnyOrder(1, 9, 7, 5, 3)); // tem que ter os mesmos nums em qualquer ordem
        assertThat(impares, hasItem(5));
        assertThat(impares, hasItems(1, 5));

        // Vários Matchers aninhados
        assertThat("Maria", is("Maria"));
        assertThat("Maria", is(not("João")));
        assertThat("Maria", anyOf(is("Maria"), is("Joaquina"))); // OR
        assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui"))); // AND
    }
}
```

## Aula 10 - Validar o body :: Aplicando o Hamcrest ao REST-assured

```java
public class OlaMundoTest {

    @Test // Aula 10 - Validar o body :: Aplicando o Hamcrest ao REST-assured
    public void devoValidarBody() {
        RestAssured
            .given()    // Pré condições
            .when()     // Ação
                .get("https://restapi.wcaquino.me/ola")
            .then()     // Assertivas
                .statusCode(200)
                .body(is("Ola Mundo!"))  // Matchers.is("Olá Mundo!")
                .body(containsString("Mundo"))
                .body(is(not(nullValue())));
    }
}
```
## Nova API users

Resultado da chamada à API: https://restapi.wcaquino.me/users
```json
[
  {
    "id": 1,
    "name": "João da Silva",
    "age": 30,
    "salary": 1234.5678
  },
  {
    "id": 2,
    "name": "Maria Joaquina",
    "endereco": {
      "rua": "Rua dos bobos",
      "numero": 0
    },
    "age": 25,
    "salary": 2500
  },
  {
    "id": 3,
    "name": "Ana Júlia",
    "age": 20,
    "filhos": [
      {
        "name": "Zezinho"
      },
      {
        "name": "Luizinho"
      }
    ]
  }
]
```

## Aula 11 - JSON primeiro nível

Endpoint do user 1: https://restapi.wcaquino.me/users/1
```json
{
    "id": 1,
    "name": "João da Silva",
    "age": 30,
    "salary": 1234.5678
}
```

Exemplos:
```java
public class UserJsonTest {

    @Test
    public void deveVerificarPrimeiroNivel() {
        given()
        .when()
            .get("https://restapi.wcaquino.me/users/1")
        .then()
            .statusCode(200)
            .body("id", is(1))
            .body("name", containsString("Silva"))
            .body("age", greaterThan(18))
        ;
    }

    @Test // Outras formas :: extrair o response e a partir dele fazer alguns tratamentos
    public void deveVerificarPrimeiroNivelOutrasFormas() {
        Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/users/1");

        // path
        // System.out.println(Integer.toString(response.path("id")));
        Assert.assertEquals(new Integer(1), response.path("id"));
        Assert.assertEquals(new Integer(1), response.path("%s", "id")); // enviando via parametro

        // JSONPath ::
        JsonPath jpath = new JsonPath(response.asString());
        Assert.assertEquals(1, jpath.getInt("id"));

        // from :: método estático do próprio JSONPath
        int id = JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1, id);
    }
}
```

## Aula 12 - JSON segundo nível

Endpoint API: https://restapi.wcaquino.me/users/2
```json
{
    "id": 2,
    "name": "Maria Joaquina",
    "endereco": {
      "rua": "Rua dos bobos",
      "numero": 0
    }
}
```

Validando uma key com mais de um nível:
```java
public class UserJsonTest {
    
    @Test
    public void deveVerificarSegundoNivel() {
        given()
        .when()
            .get("https://restapi.wcaquino.me/users/2")
        .then()
            .statusCode(200)
            .body("name", containsString("Joaquina"))
            .body("endereco.rua", is("Rua dos bobos"))
        ;
    }
}
```

## Aula 13 - JSON com lista

Endpoint API: https://restapi.wcaquino.me/users/3
```json
{
    "id": 3,
    "name": "Ana Júlia",
    "age": 20,
    "filhos": [
        {
          "name": "Zezinho"
        },
        {
          "name": "Luizinho"
        }
    ]
}
```

Validando uma key com mais de um nível:
```java
public class UserJsonTest {

    @Test // Aula 13 - JSON com lista
    public void deveVerificarLista() {
        given()
        .when()
            .get("https://restapi.wcaquino.me/users/3")
        .then()
            .statusCode(200)
            .body("name", containsString("Ana"))
            .body("filhos", hasSize(2))
            .body("filhos", hasSize(2))
            .body("filhos[0].name", is("Zezinho"))
            .body("filhos[1].name", is("Luizinho"))
            .body("filhos.name", hasItem("Luizinho")) // filhos.name traz uma coleção com os nomes de filhos que encontrar
            .body("filhos.name", hasItems("Zezinho", "Luizinho"))
            .body("filhos.name", hasSize(2))
        ;
    }
}
```
