package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.is;

public class BookStoreTest {

    @BeforeAll
    static void setup(){
        RestAssured.baseURI = "https://demoqa.com";
    }

    @DisplayName("Получение информации о книгах")
    @Test
    void informationAboutBooksTest(){

        given()
                .filter(withCustomTemplates())
                .log().uri()
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("books.isbn[1]",is("9781449331818"))
                .body("books.author[1]",is("Addy Osmani"))
                .body("books.pages[1]",is(254));
    }

    @DisplayName("Получение информации о книге по ISBN")
    @Test
    void informationAboutOneBookTest(){

        given()
                .filter(withCustomTemplates())
                .params("ISBN","9781449365035")
                .log().uri()
                .when()
                .get("/BookStore/v1/Book")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("title",is("Speaking JavaScript"))
                .body("author",is("Axel Rauschmayer"));
    }

    @DisplayName("Генерация токена")
    @Test
    void generateTokenTest(){
        String data = "{ \"userName\": \"SvetlanaV\", \"password\": \"Qwe1234!\" }";

        given()
                .contentType(JSON)
                .body(data)
                .log().uri()
                .log().body()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status",is("Success"))
                .body("result",is("User authorized successfully."));
    }

    @DisplayName("Генерация токена с AllureRestAssured")
    @Test
    void generateTokenWithAllureListenerTest(){
        String data = "{ \"userName\": \"SvetlanaV\", \"password\": \"Qwe1234!\" }";

        given()
                .filter(new AllureRestAssured())
                .contentType(JSON)
                .body(data)
                .log().uri()
                .log().body()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("status",is("Success"))
                .body("result",is("User authorized successfully."));
    }

    /*@DisplayName("Генерация токена с CustomAllureListener")
    @Test
    void generateTokenWithCustomAllureListenerTest(){
        String data = "{ \"userName\": \"SvetlanaV\", \"password\": \"Qwe1234!\" }";

        given()
                .filter(withCustomTemplates())
                .contentType(JSON)
                .body(data)
                .log().uri()
                .log().body()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("shemas/generateToken_response_shema.json"))
                .body("status",is("Success"))
                .body("result",is("User authorized successfully."));*/
    }
}
