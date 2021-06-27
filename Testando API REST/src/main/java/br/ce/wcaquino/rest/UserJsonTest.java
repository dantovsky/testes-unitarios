package br.ce.wcaquino.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {

    // JSON PRIMEITO NIVEL

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

    // JSON SEGUNDO NIVEL

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

    // JSON COM LISTA

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

    @Test // Mensagem de erro :: Caso em que o user não existe
    public void deveRetornarErroUsuarioUnexistente() {
        given()
        .when()
            .get("https://restapi.wcaquino.me/users/4")
        .then()
            .statusCode(404)
            .body("error", is("Usuário inexistente"))
        ;
    }

    @Test // Lista na raiz
    public void deveVerificarListaRaiz() {
        given()
        .when()
            .get("https://restapi.wcaquino.me/users")
        .then()
            .statusCode(200)
            .body("$", hasSize(3)) // "$" indica a raiz do documento
            .body("", hasSize(3)) // "" indica a raiz do documento
            .body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia")) // "name" recebe uma lista contendo todos os nomes
            .body("age[1]", is(25)) // a idade do 2º item (index 1) da lista de age é 25
            .body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho"))) // uma lista dentro da lista raiz
            .body("salary", contains(1234.5678f, 2500, null))
        ;
    }

    @Test // Aula 16 - Validações avançadas
    public void deveFazerVerificacoesAvancadas() {
        given()
        .when()
            .get("https://restapi.wcaquino.me/users")
        .then()
        .statusCode(200)
            .body("$", hasSize(3))
            .body("age.findAll{it <= 25}.size()", is(2)) // Quando users existem de até 25 anos? (it é a instância atual da idade)
            .body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina")) // retorna lista
            .body("findAll{it.age <= 25}[0].name", is("Maria Joaquina")) // retorna 1º registo de uma lista
            .body("findAll{it.age <= 25}[-1].name", is("Ana Júlia")) // retorna último registo de uma lista
            .body("find{it.age <= 25}.name", is("Maria Joaquina")) // retorna 1ª ocorrência
            .body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
            .body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina"))
            .body("name.collect{it.toUpperCase()}", hasItems("MARIA JOAQUINA")) // itera uma lista e faz transformacao em cima dela
            .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItems("MARIA JOAQUINA"))
            .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
            .body("age.collect{it * 2}", hasItems(60, 50, 40)) // multiplica cada idade por 2
            .body("id.max()", is(3)) // qual o maior que existe na coleção
            .body("salary.min()", is(1234.5678F)) // qual o menor salário
            .body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678F, 0.001))) // soma dos salários da coleção
            .body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d))) // soma dos salários da coleção
        ;
    }

    @Test // Unindo jsonpath com Java
    public void devoUnirJsonPathComJava() {

        // Recebe uma lista de nomes
        ArrayList<String> names =
        given()
        .when()
            .get("https://restapi.wcaquino.me/users")
        .then()
            .statusCode(200)
            .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
            .extract().path("name.findAll{it.startsWith('Maria')}")
        ;

        // Agora temos uma estrutura de "Java normal" para trabalhar
        Assert.assertEquals(1, names.size()); // tamanho da lista é 1
        Assert.assertTrue(names.get(0).equalsIgnoreCase("mArIa Joaquina"));
        Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
    }
}
