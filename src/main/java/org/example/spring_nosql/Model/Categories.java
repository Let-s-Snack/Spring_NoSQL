package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "Categories")
public class Categories {
    @Id
    @Schema(description = "Id da categoria ", example = "672d94bedb6a04681a5844c6")
    @Field(name = "_id")
    private ObjectId id;

    @NotBlank(message = "Nome não deve ser nulo")
    @Schema(description = "Nome da categoria", example = "Swift")
    @Min(value = 1, message = "Nome da categoria deve ter mais de 1 caracter")
    @Max(value = 45, message = "Nome da categoria não deve ter mais de 45 caracteres")
    private String name;

    @NotBlank(message = "Descrição não deve ser nula")
    @Schema(description = "Descrição da categoria", example = "Empresa de alimentos do grupo J&F")
    @Min(value = 1, message = "Descrição da categoria deve ter mais de 1 caracter")
    @Max(value = 100, message = "Descrição da categoria não deve ter mais de 100 caracteres")
    private String description;

    @Schema(description = "Indica se o usuário foi excluido ou não", example = "true")
    @Field(name = "is_deleted")
    @NotNull(message = "Informativo de deleção não pode ser nulo")
    private boolean isDeleted;

    @Field(name = "creation_date")
    @Schema(description = "Data de criação do ingrediente", example = "2024/08/12")
    private Date creationDate;

    @Schema(description = "URL da foto da receita", example = "https://i.pinimg.com/236x/c6/fa/68/c6fa68d10f6929de2b764484aa835310.jpg")
    @Field(name = "url_photo")
    @NotBlank(message = "A foto da receita não deve ser nula")
    private String urlPhoto;

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

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String toString() {
        return "Categories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isDeleted=" + isDeleted +
                ", creationDate=" + creationDate +
                ", urlPhoto='" + urlPhoto + '\'' +
                '}';
    }
}
