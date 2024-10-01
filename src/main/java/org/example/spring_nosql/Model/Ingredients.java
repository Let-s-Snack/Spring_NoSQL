package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "Persons")
public class Ingredients {
    @Id
    @Schema(description = "Id da receita ", example = "") //adicionar exemplo
    @Field(name = "ingredient_id")
    @NotBlank(message = "ID do ingrediente não deve ser nulo")
    private ObjectId ingredientId;

    @NotNull(message = "O tipo de medição não deve ser nulo")
    @Schema(description = "Tipo de medição", example = "gr")
    @Field(name = "medition_type")
    private String meditionType;

    @Schema(description = "Quantidade", example = "100")
    @NotNull(message = "A quantidade não deve ser nula")
    private Double quantity;

    public Ingredients() { }

    public Ingredients(ObjectId ingredientId, String meditionType, Double quantity) {
        this.ingredientId = ingredientId;
        this.meditionType = meditionType;
        this.quantity = quantity;
    }

    public String getIngredientId() {
        return ingredientId.toHexString();
    }

    public void setIngredientId(ObjectId ingredientId) {
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
                "ingredientId=" + ingredientId.toHexString() +
                ", meditionType='" + meditionType + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
