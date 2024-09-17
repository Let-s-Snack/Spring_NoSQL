package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "Restrictions")
public class Restrictions {
    @Id
    @Schema(name = "ID da restrição", example = "4")
    @Field(name = "_id")
    private ObjectId id;
    @NotBlank(message = "Nome da restrição não deve ser nulo")
    @Min(value = 3, message = "Nome da restrição deve ter mais de 3 caracteres")
    @Max(value = 45, message = "Nome da restrição não deve ter mais de 45 caracteres")
    @Schema(name = "Nome da restrição", example = "Pescetariano")
    private String name;
    @NotBlank(message = "Descrição da restrição não deve ser nulo")
    @Min(value = 3, message = "Descrição da restrição deve ter mais de 3 caracteres")
    @Max(value = 1000, message = "Descrição da restrição não deve ter mais de 1000 caracteres")
    @Schema(name = "Descrição da restrição", example = "Pescetariano é um regime alimentar que inclui peixes e frutos do mar, mas exclui a carne de outros animais.")
    private String description;
    @Schema(name = "URL da Photo", example = "") //Adicionar exemplo de URL de foto
    @Field(name = "url_photo")
    private String urlPhoto;
    @Field(name = "creation_date")
    @Schema(name = "Data de criação da restrição", example = "2024/08/12")
    private Date creationDate;

    public Restrictions() {}

    public ObjectId getId() {
        return id;
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
        final StringBuffer sb = new StringBuffer("Restrictions{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", urlPhoto='").append(urlPhoto).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append('}');
        return sb.toString();
    }
}
