package org.example.spring_nosql.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "Adm")
public class Adm {
    @Id
    @Schema(description = "Id do administrador", example = "60d5f4832f8fb814b56fa2f5")
    @Field(name = "_id")
    private ObjectId id;

    @Schema(description = "E-mail do administrador", example = "jose@gmail.com")
    @Email(message = "E-mail deve ser válido")
    @NotBlank(message = "E-mail não deve ser nulo!")
    private String email;

    @Schema(description = "Senha do usuário em HASH", example = "$2a$10$7EqJtq98hPqEX7fNZaFWoO7BiEXLYAK9Lk8rClTl6l5povF9QIJFu")
    @Size(min = 8, message = "Senha deve ter 8 caracteres ou mais")
    @NotBlank(message = "Senha não deve ser nula!")
    private String password;

    @Schema(description = "Nome do usuário", example = "Jose")
    @Size(max = 200, message = "Nome do usuário não deve ter mais de 200 caracteres")
    @NotBlank(message = "Nome do usuário não deve ser nulo")
    private String name;

    @Schema(description = "Indica se o usuário foi excluido ou não", example = "true")
    @Field(name = "is_deleted")
    @NotNull(message = "Informativo de deleção não pode ser nulo")
    private boolean isDeleted;

    @Schema(description = "Data de criação do usuário", example = "2024/08/27")
    @Field(name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Date creationDate;

    public Adm(ObjectId id, String email, String password, String name, boolean isDeleted, Date creationDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.isDeleted = isDeleted;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id.toHexString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
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

    public String toString() {
        return "Adm{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                ", creationDate=" + creationDate +
                '}';
    }
}
