package br.ce.wcaquino.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

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
}
