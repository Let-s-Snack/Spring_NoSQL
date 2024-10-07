package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;


@Document(collection = "Ingredients")
public class Ingredients {
    @Id
    @Schema(description = "Id da receita ", example = "66f2dfdfb310eeeabd300dc3")
    @Field(name = "_id")
    private ObjectId id;

    @NotBlank(message = "Nome não deve ser nulo")
    @Schema(description = "Nome do ingrediente", example = "Camarão")
    @Min(value = 1, message = "Nome do ingrediente deve ter mais de 1 caracter")
    @Max(value = 45, message = "Nome do ingrediente não deve ter mais de 45 caracteres")
    private String name;

    @NotBlank(message = "Descrição não deve ser nula")
    @Schema(description = "Descrição do ingrediente", example = "Camarão é um fruto do mar")
    @Min(value = 1, message = "Descrição do ingrediente deve ter mais de 1 caracter")
    @Max(value = 100, message = "Descrição do ingrediente não deve ter mais de 100 caracteres")
    private String description;

    @Field(name = "broken_restrictions")
    @Schema(description = "Lista de restrições que bloqueam o ingrediente", example = "Pescetariano")
    private List<Restrictions> brokenRestrictions;

    @Field(name = "creation_date")
    @Schema(description = "Data de criação do ingrediente", example = "2024/08/12")
    private Date creationDate;


    public Ingredients(ObjectId id, String name, String description, List<Restrictions> brokenRestrictions, Date creationDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brokenRestrictions = brokenRestrictions;
        this.creationDate = creationDate;
    }

    public Ingredients() {}

    public String getId() {
        return id.toHexString();
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public List<Restrictions> getBrokenRestrictions() {
        return brokenRestrictions;
    }

    public void setBrokenRestrictions(List<Restrictions> brokenRestrictions) {
        this.brokenRestrictions = brokenRestrictions;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", brokenRestrictions=" + brokenRestrictions +
                ", creationDate=" + creationDate +
                '}';
    }
}
