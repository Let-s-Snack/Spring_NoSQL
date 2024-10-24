package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Field;

public class IngredientsShoppingList {
    @Field(name = "ingredient_id")
    @Schema(description = "Id da receita ", example = "60d5f4832f8fb814b56fa287")
    @NotBlank(message = "ID do ingrediente não deve ser nulo")
    private String ingredientId;

    @Field(name = "is_checked")
    @Schema(description = "Indica se o ingrediente foi selecionado ou não", example = "false")
    @NotNull(message = "Ingrediente deve ser indicado como selecionado ou não!")
    private boolean isChecked;

    @Field(name = "ingredient_name")
    @NotBlank(message = "Nome do ingrediente não deve ser nulo")
    @Schema(name = "Nome do ingrediente", example = "Bobó de camarão")
    private String ingredientName;

    @NotNull(message = "O tipo de medida não deve ser nulo")
    @Schema(description = "Tipo de medida", example = "gr")
    @Field(name = "medition_type")
    private String meditionType;

    @Schema(description = "Quantidade", example = "100")
    @NotNull(message = "A quantidade não deve ser nula")
    private Double quantity;

    public IngredientsShoppingList(){}

    public IngredientsShoppingList(String ingredientId, Boolean isChecked) {
        this.ingredientId = ingredientId;
        this.isChecked = isChecked;
    }

    public IngredientsShoppingList(String ingredientId, Boolean isChecked, String ingredientName, String meditionType, Double quantity) {
        this.ingredientId = ingredientId;
        this.isChecked = isChecked;
        this.ingredientName = ingredientName;
        this.meditionType = meditionType;
        this.quantity = quantity;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getMeditionType() {
        return meditionType;
    }

    public void setMeditionType(String meditionType) {
        this.meditionType = meditionType;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "IngredientsShoppingList{" +
                "ingredientId='" + ingredientId + '\'' +
                ", isChecked=" + isChecked +
                ", ingredientName='" + ingredientName + '\'' +
                ", meditionType='" + meditionType + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
