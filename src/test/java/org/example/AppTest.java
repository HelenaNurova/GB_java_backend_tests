package org.example;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class AppTest
{
        private final String apiKey = "8f24a42de3cd40cb84c540b661c1ac91";

//GET
//1
    @Test
    void getSearchRecipesMainCoursePositiveTest() {
        given()
                .queryParam("apiKey", apiKey)
                .queryParam("type", "main course")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .then()
                .statusCode(200);
    }


//2

    @Test
    void getIncludeIngredientsPositiveTest() {
        given()
                .queryParam("apiKey", apiKey)
                .queryParam("query", "cheese, tomatoes, olives")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("results[0].title")
                .toString()
                .contains("cheese, tomatoes, olives");
    }

//3

    @Test
    void getCuisineItalian(){
        given()
                .queryParam("apiKey", apiKey)
                .queryParam("cuisine", "italian")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .then()
                .statusCode(200);
    }

//4

    @Test
    void getVegeterianExcludeEggsPositiveTest() {
        given()
                .queryParam("apiKey", apiKey)
                .queryParam("diet", "vegetarian")
                .queryParam("excludeIngredients", "eggs")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .then()
                .statusCode(200);
    }


//5

    @Test
    void getSearchRecipesQuery(){
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParam("query", "pasta")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("totalResults"),equalTo(223));
    }

//POST

//1
    @Test
    void classifyCuisineWithoutQueryParametersTest() {
        given()
                .queryParam("apiKey", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .then()
                .assertThat().body("cuisine", equalTo("Mediterranean"))
                .assertThat().body("confidence", equalTo(0.0F))
                .statusCode(200);
    }

//2
    @Test
    void classifyCuisineMediterraneanTypeTest() {
        given()
                .queryParam("apiKey", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Italian Seafood Stew")
                .param("language", "en")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .then()
                .assertThat().body("cuisine", equalTo("Mediterranean"))
                .assertThat().body("confidence", equalTo(0.95F))
                .statusCode(200);
    }

//3
    @Test
    void classifyCuisineAfricanTypeTest() {
        given()
                .queryParam("apiKey", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "African Bean Soup")
                .param("ingredientList", "")
                .param("language", "en")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .then()
                .assertThat().body("cuisine", equalTo("African"))
                .assertThat().body("confidence", equalTo(0.85F))
                .statusCode(200);
    }

//4
    @Test
    void classifyCuisineKoreanTypeTest() {
        given()
                .queryParam("apiKey", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Winter Kimchi")
                .param("language", "en")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .then()
                .assertThat().body("cuisine", equalTo("Korean"))
                .assertThat().body("confidence", equalTo(0.85F))
                .statusCode(200);
    }

//5
    @Test
    void classifyCuisineSeafoodNewburgTest() {
        given()
                .queryParam("apiKey", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .param("title", "Seafood Newburg")
                .param("ingredientList", "¼ lb Scallops, ⅓ lb Shrimp, ½ lb white cod")
                .param("language", "en")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .prettyPeek()
                .then()
                .assertThat().body("cuisine", equalTo("Mediterranean"))
                .statusCode(200);
    }



    @Test
    void ConnectUser(){
        String hash = given()
                .queryParam("apiKey", apiKey)
                .body("{\n" +
                                "    \"username\": \"HelenaNur\",\n" +
                                "    \"firstName\": \"Helena\",\n" +
                                "    \"lastName\": \"Nurova\",\n" +
                                "    \"email\": \"helenn70@yandex.ru\"\n" +
                                "}")
                .when()
                .post("https://api.spoonacular.com/users/connect")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .prettyPeek()
                .get("hash")
                .toString();
    }

    @Test
    void mealPlanTest() {
        String hash = "099c8fd8160bc21a3ee692550ed47641b3349660";
        String id = given()
                .queryParam("hash", hash)
                .queryParam("apiKey", apiKey)
                .body("{\n"
                        + " \"date\": 1589500800,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"RECIPE\",\n"
                        + " \"value\": {\n"
                        + " \"id\": 1171485,\n"
                        + " \"servings\": 8,\n"
                        + " \"title\": \"Chocolate & Pistachio Naked Layer Cake\",\n"
                        + " \"imageType\": \"jpg\"\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/HelenaNur/items/")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .prettyPeek()
                .get("id")
                .toString();

        given()
                .queryParam("hash", hash)
                .queryParam("apiKey", apiKey)
                .delete("https://api.spoonacular.com/mealplanner/HelenaNur/items/" + id)
                .then()
                .statusCode(200);
    }

}



