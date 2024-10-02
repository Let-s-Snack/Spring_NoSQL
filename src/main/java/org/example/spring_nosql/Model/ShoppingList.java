package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document("shopping_list")
public class ShoppingList {
    @Field(name = "recipes_id")
    @Schema(description = "ID da receita da shopping list", example = "60d5f4832f8fb814b56fa2f5")
    @NotBlank(message = "ID da receita da shopping list não deve ser nulo")
    private String recipesId;

    @Field(name = "recipe_name")
    @NotNull(message = "Nome da receita não deve ser nulo")
    @Schema(name = "Nome da receita", example = "Bobó de camarão")
    private String recipeName;

    @Field(name = "ingredients")
    @Schema(description = "Id dos ingredientes salvos", example = "[\n" +
            "        {\n" +
            "          \"ingredientId\": \"60d5f4832f8fb814b56fa229\",\n" +
            "          \"isChecked\": false,\n" +
            "          \"ingredientName\": null,\n" +
            "          \"meditionType\": null,\n" +
            "          \"quantity\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ingredientId\": \"60d5f4832f8fb814b56fa221\",\n" +
            "          \"isChecked\": false,\n" +
            "          \"ingredientName\": null,\n" +
            "          \"meditionType\": null,\n" +
            "          \"quantity\": null\n" +
            "        }\n" +
            "      ]")
    @NotBlank(message = "Shoppinglist deve ter ingredientes salvos")
    private List<IngredientsShoppingList> ingredients;
    @Schema(description = "Data de criação da shopping list", example = "2024/08/27")
    @Field(name = "creation_date")
    private Date creationDate;

    public ShoppingList() {this.creationDate = new Date();}

    public ShoppingList(String recipesId, List<IngredientsShoppingList> ingredients) {
        this.recipesId = recipesId;
        this.ingredients = ingredients;
    }

    public ShoppingList(String recipesId, String recipeName, List<IngredientsShoppingList> ingredients, Date creationDate) {
        this.recipesId = recipesId;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.creationDate = creationDate;
    }

    public String getRecipesId() {
        return this.recipesId;
    }

    public void setRecipesId(String recipesId) {
        this.recipesId = recipesId;
    }

    public List<IngredientsShoppingList> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(List<IngredientsShoppingList> ingredients) {
        this.ingredients = ingredients;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "recipesId='" + recipesId + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", ingredients=" + ingredients +
                ", creationDate=" + creationDate +
                '}';
    }
}
