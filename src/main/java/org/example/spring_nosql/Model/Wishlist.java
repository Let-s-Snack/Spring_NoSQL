package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class Wishlist {
    private int recipesId;

    private Boolean isActive;

    private Date creationDate;

    public Wishlist(int recipesId, Boolean isActive, Date creationDate) {
        this.recipesId = recipesId;
        this.isActive = isActive;
        this.creationDate = creationDate;
    }

    public int getRecipesId() {
        return this.recipesId;
    }

    public void setRecipesId(int recipesId) {
        this.recipesId = recipesId;
    }

    public Boolean getActive() {
        return this.isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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
                ", isActive=" + isActive +
                ", creationDate=" + creationDate +
                '}';
    }
}
