package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class Favorites {

    @Field(name = "recipes_id")
    @Schema(description = "ID da receita favorita", example = "") // Adicionar exemplo
    @NotBlank(message = "ID da receita favorita não deve ser nulo")
    private String recipesId;

    @Field(name = "creation_date")
    @Schema(description = "Data de criação das receitas favoritas", example = "2024/08/27")
    private Date creationDate;
    public Favorites(){this.creationDate = new Date();}

    public Favorites(String recipesId) {
        this.recipesId = recipesId;
        this.creationDate = new Date();
    }

    public Favorites(String recipesId, Date creationDate) {
        this.recipesId = recipesId;
        this.creationDate = creationDate;
    }

    public String getRecipesId() {
        return this.recipesId;
    }

    public void setRecipesId(String recipesId) {
        this.recipesId = recipesId;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "recipesId=" + recipesId +
                ", creationDate=" + creationDate +
                '}';
    }
}
