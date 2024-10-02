package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class DirectionsWeek {
    @Field(name = "recipes_id")
    @Schema(description = "ID da receita da semana", example = "60d5f4832f8fb814b56fa287")
    @NotBlank(message = "ID da receita da semana não deve ser nulo")
    private String recipesId;

    @Field(name = "creation_date")
    @Schema(description = "Data de criação da receita da semana do usuário", example = "2024/08/27")
    private Date creationDate;

    public DirectionsWeek() {this.creationDate = new Date();}
    public DirectionsWeek(String recipesId) {
        this.recipesId = recipesId;
        this.creationDate = new Date();
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
