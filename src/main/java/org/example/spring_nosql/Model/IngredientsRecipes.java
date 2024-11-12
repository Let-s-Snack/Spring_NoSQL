package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Nome não deve ser nulo")
    @Schema(name = "Nome do ingrediente", example = "Camarão")
    private String name;

    @NotBlank(message = "Descrição não deve ser nula")
    @Schema(description = "Descrição do ingrediente", example = "Camarão é um fruto do mar")
    private String description;

    @Field (name = "is_swift")
    @Schema(description = "Indica se o ingrediente é da swift ou não", example = "true")
    private boolean isSwift;

    public IngredientsRecipes(){}

    public IngredientsRecipes(String ingredientId, String meditionType, double quantity, String name, String description, boolean isSwift) {
        this.ingredientId = ingredientId;
        this.meditionType = meditionType;
        this.quantity = quantity;
        this.name = name;
        this.description = description;
        this.isSwift = isSwift;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsSwift() {
        return this.isSwift;
    }

    public void setIsSwift(boolean isSwift) {
        this.isSwift = isSwift;
    }

    public String toString() {
        return "IngredientsRecipes{" +
                "ingredientId='" + ingredientId + '\'' +
                ", meditionType='" + meditionType + '\'' +
                ", quantity=" + quantity +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isSwift=" + isSwift +
                '}';
    }
}
