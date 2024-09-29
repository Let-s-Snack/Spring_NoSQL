package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

public class IngredientsShoppingList {
    @Field(name = "ingredient_id")
    @Schema(description = "Id da receita ", example = "") //adicionar exemplo
    @NotBlank(message = "ID do ingrediente não deve ser nulo")
    private String ingredientId;

    @Field(name = "is_checked")
    @Schema(description = "Indica se o ingrediente foi selecionado ou não", example = "false")
    @NotNull(message = "Ingrediente deve ser indicado como selecionado ou não!")
    private Boolean isChecked;

    @Schema(name = "Id do ingrediente ", example = "1")
    @Field(name = "_id")
    @NotBlank(message = "ID do ingrediente não deve ser nula ou vazia")
    private String id;

    @NotBlank(message = "Nome da receita não deve ser nulo")
    @Schema(name = "Nome da receita", example = "Bobó de camarão")
    private String name;

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
    public IngredientsShoppingList(String ingredientId, Boolean isChecked, String id, String name, String meditionType, Double quantity) {
        this.ingredientId = ingredientId;
        this.isChecked = isChecked;
        this.id = id;
        this.name = name;
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

    public void setIsChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "ingredientId=" + ingredientId +
                ", isChecked=" + isChecked +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", meditionType='" + meditionType + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
