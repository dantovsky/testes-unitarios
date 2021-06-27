package br.ce.wcaquino.rest;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class VerbosTest {

    @Test // Aula 25 - POST com JSON
    public void deveSalvarUsuario() {
        given()
            .log().all()
            .contentType("application/json") // O server trata como um objeto JSON
            .body("{ \"name\": \"Jose\", \"age\": 50 }")
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Jose"))
            .body("age", is(50));
    }

    /*
    Log recebido:

    "C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\bin\java.exe" -ea -Didea.test.cyclic.buffer.size=1048576 -javaagent:C:\Users\xotsa62\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\211.6693.111\lib\idea_rt.jar=54424:C:\Users\xotsa62\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\211.6693.111\bin -Dfile.encoding=UTF-8 -classpath "C:\Users\xotsa62\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\211.6693.111\lib\idea_rt.jar;C:\Users\xotsa62\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\211.6693.111\plugins\junit\lib\junit5-rt.jar;C:\Users\xotsa62\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\211.6693.111\plugins\junit\lib\junit-rt.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\charsets.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\access-bridge-64.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\cldrdata.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\dnsns.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\jaccess.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\localedata.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\nashorn.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\sunec.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\sunjce_provider.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\sunmscapi.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\sunpkcs11.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\ext\zipfs.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\jce.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\jfr.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\jsse.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\management-agent.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\resources.jar;C:\Program Files\AdoptOpenJDK\jdk-8.0.282.8-hotspot\jre\lib\rt.jar;C:\Users\xotsa62\Documents\Cursos\Testes Unitatios\Testando API REST\target\classes;C:\Users\xotsa62\.m2\repository\io\rest-assured\rest-assured\4.0.0\rest-assured-4.0.0.jar;C:\Users\xotsa62\.m2\repository\org\codehaus\groovy\groovy\2.5.6\groovy-2.5.6.jar;C:\Users\xotsa62\.m2\repository\org\codehaus\groovy\groovy-xml\2.5.6\groovy-xml-2.5.6.jar;C:\Users\xotsa62\.m2\repository\org\apache\httpcomponents\httpclient\4.5.3\httpclient-4.5.3.jar;C:\Users\xotsa62\.m2\repository\org\apache\httpcomponents\httpcore\4.4.6\httpcore-4.4.6.jar;C:\Users\xotsa62\.m2\repository\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;C:\Users\xotsa62\.m2\repository\commons-codec\commons-codec\1.9\commons-codec-1.9.jar;C:\Users\xotsa62\.m2\repository\org\apache\httpcomponents\httpmime\4.5.3\httpmime-4.5.3.jar;C:\Users\xotsa62\.m2\repository\org\hamcrest\hamcrest-core\2.1\hamcrest-core-2.1.jar;C:\Users\xotsa62\.m2\repository\org\hamcrest\hamcrest\2.1\hamcrest-2.1.jar;C:\Users\xotsa62\.m2\repository\org\hamcrest\hamcrest-library\2.1\hamcrest-library-2.1.jar;C:\Users\xotsa62\.m2\repository\org\ccil\cowan\tagsoup\tagsoup\1.2.1\tagsoup-1.2.1.jar;C:\Users\xotsa62\.m2\repository\io\rest-assured\json-path\4.0.0\json-path-4.0.0.jar;C:\Users\xotsa62\.m2\repository\org\codehaus\groovy\groovy-json\2.5.6\groovy-json-2.5.6.jar;C:\Users\xotsa62\.m2\repository\io\rest-assured\rest-assured-common\4.0.0\rest-assured-common-4.0.0.jar;C:\Users\xotsa62\.m2\repository\io\rest-assured\xml-path\4.0.0\xml-path-4.0.0.jar;C:\Users\xotsa62\.m2\repository\org\apache\commons\commons-lang3\3.4\commons-lang3-3.4.jar;C:\Users\xotsa62\.m2\repository\javax\xml\bind\jaxb-api\2.2.12\jaxb-api-2.2.12.jar;C:\Users\xotsa62\.m2\repository\com\sun\xml\bind\jaxb-osgi\2.2.10\jaxb-osgi-2.2.10.jar;C:\Users\xotsa62\.m2\repository\org\apache\sling\org.apache.sling.javax.activation\0.1.0\org.apache.sling.javax.activation-0.1.0.jar;C:\Users\xotsa62\.m2\repository\javax\activation\activation\1.1.1\activation-1.1.1.jar;C:\Users\xotsa62\.m2\repository\junit\junit\4.12\junit-4.12.jar" com.intellij.rt.junit.JUnitStarter -ideVersion5 -junit4 br.ce.wcaquino.rest.VerbosTest,deveSalvarUsuario
    Request method:	POST
    Request URI:	https://restapi.wcaquino.me/users
    Proxy:			<none>
    Request params:	<none>
    Query params:	<none>
    Form params:	<none>
    Path params:	<none>
    Headers:		Accept=* / *
        Content-Type=application/json; charset=UTF-8
        Cookies:		<none>
        Multiparts:		<none>
        Body:
        {
            "name": "Jose",
                "age": 50
        }
        HTTP/1.1 201 Created
        Server: nginx/1.12.1 (Ubuntu)
        Date: Sun, 27 Jun 2021 19:45:55 GMT
        Content-Type: application/json; charset=utf-8
        Content-Length: 43
        Connection: keep-alive
        X-Powered-By: Express
        ETag: W/"2b-d3eDZUckyv0wEvE4/yYAoGsbsXY"

        {
            "name": "Jose",
                "age": 50,
                "id": 1624823155571
        }

        Process finished with exit code 0
    */

    @Test
    public void naoDeveSalvarUsuarioSemNome() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"age\": 50 }")
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(400)
            .body("id", is(nullValue()))
            .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test // Aula 27 - POST com XML
    public void deveSalvarUsuarioViaXML() {
        given()
            .log().all()
            .contentType(ContentType.XML) // O server trata como um objeto JSON
            .body("<user><name>Jose</name><age>50</age></user>")
        .when()
            .post("https://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .body("user.@id", is(notNullValue()))
            .body("user.name", is("Jose"))
            .body("user.age", is("50"))
        ;
    }

    @Test // Aula 28 - Alterando com PUT
    public void deveAlterarUsuario() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"User alterado\", \"age\": 80 }")
        .when()
            .put("https://restapi.wcaquino.me/users/1")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("User alterado"))
            .body("age", is(80))
            .body("salary", is(1234.5678f))
        ;
    }

    @Test // Aula 29 - URL Parametrizável (parte 1)
    public void devoCustomizarURL() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"User alterado\", \"age\": 80 }")
        .when()
            .put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("User alterado"))
            .body("age", is(80))
            .body("salary", is(1234.5678f))
        ;
    }

    @Test // Aula 29 - URL Parametrizável (parte 2)
    public void devoCustomizarURLParte2() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"User alterado\", \"age\": 80 }")
            .pathParam("entidade", "users")
            .pathParam("userId", 1)
        .when()
            .put("https://restapi.wcaquino.me/{entidade}/{userId}")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("User alterado"))
            .body("age", is(80))
            .body("salary", is(1234.5678f))
        ;
    }

    @Test // Aula 30 - Removendo com DELETE
    public void deveRemoverUsuario() {
        given()
            .log().all()
        .when()
            .delete("https://restapi.wcaquino.me/users/1")
        .then()
            .log().all()
            .statusCode(204)
        ;
    }

    @Test // Aula 30 - Removendo com DELETE
    public void naoDeveRemoverUsuarioInexistente() {
        given()
            .log().all()
        .when()
            .delete("https://restapi.wcaquino.me/users/1000")
        .then()
            .log().all()
            .statusCode(400)
            .body("error", is("Registro inexistente"))
        ;
    }
}
