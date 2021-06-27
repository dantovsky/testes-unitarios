package br.ce.wcaquino.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class OlaMundoTest {

    @Test
    public void testOlaMundo() {
        Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
        assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        assertTrue(response.statusCode() == 200);
        assertTrue("O status code deveria ser 201", response.statusCode() == 200);
        assertEquals(200, response.statusCode());

        // throw new RuntimeException();
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }

    @Test // Aula 8 - Outros modos de se trabalhar com o REST-assured
    public void devoConhecerOutrasFormasRestAssured() {

        get("https://restapi.wcaquino.me/ola").then().statusCode(200);

        // Modo Fluente (não é tão simples como acima, mas dá mais legibilidade e organização)
        // Essa forma utiliza uma sintaxe parecida com o Gerkin, utilizado no Cucumber
        // => Given When Then
        // Give (dado)      :: uma pré-condião que queremos aplicar
        // When (quando)    :: ação de fato
        // Then (entao)     :: serão as assertivas que serão feitas

        RestAssured
        .given()    // Pré condições
        .when()     // Ação
            .get("https://restapi.wcaquino.me/ola")
        .then()     // Assertivas
            // .assertThat() // pode usar o assertThat(), mas não muda nada (só leitura)
            .statusCode(200);
    }

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
