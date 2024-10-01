package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class PersonsRestrictions {
    @Field(name = "restrictions_id")
    @Schema(description = "ID da restrição ", example = "") // Adicionar exemplo
    @NotBlank(message = "ID da restrição não deve ser nulo")
    private String restrictionId;

    @Schema(name = "Nome da restrição", example = "Pescetariano")
    @NotBlank(message = "Nome não deve ser nulo ou vazio")
    @Min(value = 3, message = "Nome da restrição deve ter mais de 3 caracteres")
    @Max(value = 45, message = "Nome da restrição não deve ter mais de 45 caracteres")
    private String name;
    @NotBlank(message = "Descrição não deve ser nula ou vazio")
    @Min(value = 3, message = "Descrição da restrição deve ter mais de 3 caracteres")
    @Max(value = 1000, message = "Descrição da restrição não deve ter mais de 1000 caracteres")
    @Schema(name = "Descrição da restrição", example = "Pescetariano é um regime alimentar que inclui peixes e frutos do mar, mas exclui a carne de outros animais.")
    private String description;
    @Field(name = "url_photo")
    @Schema(name = "URL da Photo", example = "") //Adicionar exemplo de URL de foto
    private String urlPhoto;
    @Field(name = "creation_date")
    @Schema(name = "Data de criação da restrição", example = "2024/08/12")
    private Date creationDate;

    public PersonsRestrictions() {this.creationDate = new Date();}
    public PersonsRestrictions(String restrictionId) {
        this.restrictionId = restrictionId;
        this.creationDate = new Date();
    }

    public PersonsRestrictions(String name, String description, String urlPhoto) {
        this.name = name;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.creationDate = new Date();
    }

    public PersonsRestrictions(String restrictionId, String name, String description, String urlPhoto) {
        this.restrictionId = restrictionId;
        this.name = name;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.creationDate = new Date();
    }

    public String getRestrictionId() {
        return restrictionId;
    }

    public void setRestrictionId(String restrictionId) {
        this.restrictionId = restrictionId;
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

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "PersonsRestrictions{" +
                "restrictionId='" + restrictionId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
