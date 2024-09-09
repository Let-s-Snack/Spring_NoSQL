package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "Persons")
public class Ingredients {
    @Id
    @Schema(name = "Id da receita ", example = "1")
    @Field(name = "ingredient_id")
    private int ingredientId;

    @NotNull(message = "O tipo de medição não deve ser nulo")
    @Schema(name = "Tipo de medição", example = "gr")
    private String meditionType;

    @Schema(name = "Quantidade", example = "100")
    @NotNull(message = "A quantidade não deve ser nula")
    private Double quantity;

    public Ingredients() { }

    public Ingredients(int ingredientId, String meditionType, Double quantity) {
        this.ingredientId = ingredientId;
        this.meditionType = meditionType;
        this.quantity = quantity;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public @NotNull(message = "O tipo de medição não deve ser nulo") String getMeditionType() {
        return meditionType;
    }

    public void setMeditionType(@NotNull(message = "O tipo de medição não deve ser nulo") String meditionType) {
        this.meditionType = meditionType;
    }

    public @NotNull(message = "A quantidade não deve ser nula") Double getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull(message = "A quantidade não deve ser nula") Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "ingredientId=" + ingredientId +
                ", meditionType='" + meditionType + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
