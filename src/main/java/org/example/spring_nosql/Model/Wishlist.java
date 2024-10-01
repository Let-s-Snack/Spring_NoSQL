package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class Wishlist {
    @Field(name = "recipes_id")
    @Schema(description = "ID da receita da wishlist", example = "") // Adicionar exemplo
    @NotBlank(message = "ID da receita da wishlist não deve ser nulo")
    private String recipesId;

//    @Field(name = "recipes_id")
//    @Schema(description = "ID da receita da wishlist", example = "") // Adicionar exemplo
//    @NotBlank(message = "ID da receita da wishlist não deve ser nulo")
//    private ObjectId recipesId;

    @Field(name = "creation_date")
    @Schema(description = "Data de criação da wishlist", example = "2024/08/27")
    private Date creationDate;
    public Wishlist(){this.creationDate = new Date();}

    public Wishlist(String recipesId) {
        this.recipesId = recipesId;
        this.creationDate = new Date();
    }

    public String getRecipesId() {
        return this.recipesId;
    }

    public void setRecipesId(String recipesId) {
        this.recipesId = recipesId;
    }

//
//    public Wishlist(ObjectId recipesId, Date creationDate) {
//        this.recipesId = recipesId;
//        this.creationDate = creationDate;
//    }
//
//    public ObjectId getRecipesId() {
//        return recipesId;
//    }
//
//    public void setRecipesId(ObjectId recipesId) {
//        this.recipesId = recipesId;
//    }

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
