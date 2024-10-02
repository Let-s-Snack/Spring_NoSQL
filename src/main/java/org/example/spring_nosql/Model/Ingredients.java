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
    @Schema(description = "Id da receita ", example = "") //adicionar exemplo
    @Field(name = "_id")
    private ObjectId id;

    @NotBlank(message = "Nome não deve ser nulo")
    @Schema(name = "Nome do ingrediente", example = "Camarão")
    @Min(value = 1, message = "Nome do ingrediente deve ter mais de 1 caracter")
    @Max(value = 45, message = "Nome do ingrediente não deve ter mais de 45 caracteres")
    private String name;

    @Field(name = "broken_restrictions")
    @Schema(name = "Lista de restrições que bloqueam o ingrediente", example = "Pescetariano")
    private List<Restrictions> brokenRestrictions;

    @Field(name = "creation_date")
    @Schema(name = "Data de criação do ingrediente", example = "2024/08/12")
    private Date creationDate;

    @NotNull(message = "O tipo de medição não deve ser nulo")
    @Schema(description = "Tipo de medição", example = "gr")
    @Field(name = "medition_type")
    private String meditionType;

    @Schema(description = "Quantidade", example = "100")
    @NotNull(message = "A quantidade não deve ser nula")
    private Double quantity;

    public Ingredients(ObjectId id, String name, List<Restrictions> brokenRestrictions, Date creationDate, String meditionType, Double quantity) {
        this.id = id;
        this.name = name;
        this.brokenRestrictions = brokenRestrictions;
        this.creationDate = creationDate;
        this.meditionType = meditionType;
        this.quantity = quantity;
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
        return "Ingredients{" +
                "id=" + id.toHexString() +
                ", name='" + name + '\'' +
                ", brokenRestrictions=" + brokenRestrictions +
                ", creationDate=" + creationDate +
                ", meditionType='" + meditionType + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
