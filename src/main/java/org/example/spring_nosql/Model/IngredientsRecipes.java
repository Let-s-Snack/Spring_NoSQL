package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

public class IngredientsRecipes {
    @Schema(description = "Id do ingrediente da receita", example = "66f2dfdfb310eeeabd300dc3")
    @Field(name = "ingredient_id")
    private String ingredientId;

    @NotNull(message = "O tipo de medição não deve ser nulo")
    @Schema(description = "Tipo de medição", example = "gr")
    @Field(name = "medition_type")
    private String meditionType;

    @Schema(description = "Quantidade", example = "100")
    @NotNull(message = "A quantidade não deve ser nula")
    private double quantity;

    public IngredientsRecipes(){}

    public IngredientsRecipes(String ingredientId, String meditionType, double quantity) {
        this.ingredientId = ingredientId;
        this.meditionType = meditionType;
        this.quantity = quantity;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getMeditionType() {
        return meditionType;
    }

    public void setMeditionType(String meditionType) {
        this.meditionType = meditionType;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "IngredientsRecipes{" +
                "ingredientId=" + ingredientId +
                ", meditionType='" + meditionType + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
