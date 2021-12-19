package modulos.produtos;

import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.ComponentePojo;
import pojo.ProdutoPojo;
import pojo.UsuarioPojo;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes de API Rest modulo de produto")
public class ProdutoTest {
    private String token;

    @BeforeEach
    public void beforEach(){
        //Configurando os dados da API Rest da Lojinha
        baseURI = "http://165.227.93.41";
        basePath = "/lojinha";




        //Obter o token do usuario admin
        this.token = given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.criarUsuarioAdministrador())
            .when()
                .post("/v2/login")
            .then()
                .extract()
                    .path("data.token");






    }

    @Test
    @DisplayName("Validar que o valor do produto igual a zero não é permitido")
    public void testValidarLimiteZeradoProibidoValorProduto (){

        //Testar  inseriri um produto com valor 0.00 a validar que a mensagem de erro foi apresentada e o
        // Status code retornado foi 422


        given()
                .contentType(ContentType.JSON)
                .header("token",this.token)
                .body(ProdutoDataFactory.criarProdutoComumComOValorIgualA(0.00))
        .when()
                .post("/v2/produtos")
        .then()
                .assertThat()
                    .body("error",equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                    .statusCode(422);

    }
    @Test
    @DisplayName("Validar que o valor do produto igual a 7000,01 não é permitido")
    public void testValidarLimiteMaiorSeteMilProibidoValorProduto (){

        //Testar  inserir  um produto com valor 7000,01 a validar que a mensagem de erro foi apresentada e o
        // Status code retornado foi 422

        given()
                .contentType(ContentType.JSON)
                .header("token",this.token)
                .body(ProdutoDataFactory.criarProdutoComumComOValorIgualA(7000.01))
            .when()
                .post("/v2/produtos")
            .then()
                .assertThat()
                 .body("error",equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                 .statusCode(422);

    }


}
