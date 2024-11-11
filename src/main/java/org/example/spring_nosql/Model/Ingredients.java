package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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

    @Schema(description = "Indica se o usuário foi excluido ou não", example = "true")
    @Field(name = "is_deleted")
    @NotNull(message = "Informativo de deleção não pode ser nulo")
    private boolean isDeleted;

    @Field(name = "creation_date")
    @Schema(description = "Data de criação do ingrediente", example = "2024/08/12")
    private Date creationDate;


    @Field(name = "broken_restrictions")
    @Schema(description = "Lista de restrições que bloqueam o ingrediente", example = "Pescetariano")
    private List<ObjectId> brokenRestrictions;

    @Field (name = "is_swift")
    @Schema(description = "Indica se o ingrediente é da swift ou não", example = "true")
    private boolean isSwift;

    public Ingredients(ObjectId id, String name, String description, boolean isDeleted, Date creationDate, List<ObjectId> brokenRestrictions, boolean isSwift) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brokenRestrictions = brokenRestrictions;
        this.creationDate = creationDate;
        this.isDeleted = isDeleted;
        this.isSwift = isSwift;
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

    public List<String> getBrokenRestrictions() {
        return (brokenRestrictions == null)
                ? List.of()
                : brokenRestrictions.stream()
                .map(ObjectId::toHexString)
                .collect(Collectors.toList());
    }

    public void setBrokenRestrictions(List<ObjectId> brokenRestrictions) {
        this.brokenRestrictions = brokenRestrictions;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getIsSwift() {
        return this.isSwift;
    }

    public void setIsSwift(boolean isSwift) {
        this.isSwift = isSwift;
    }

    public String toString() {
        return "Ingredients{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isDeleted=" + isDeleted +
                ", creationDate=" + creationDate +
                ", brokenRestrictions=" + brokenRestrictions +
                ", isSwift=" + isSwift +
                '}';
    }
}