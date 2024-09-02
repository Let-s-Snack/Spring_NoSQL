package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Genders")
public class Gender {
    @Id
    @Schema(description = "ID do gênero", example = "12")
    private int id;

    @Size(min=3, message = "Gênero não deve conter menos de 3 caracteres!")
    @Size(max=45, message = "Gênero não pode conter mais de 45 caracteres!")
    @NotNull(message="Gênero não deve ser nulo!")
    @Schema(description = "Nome dos gêneros", example="Masculino")
    private String name;

    @Column(name="creation_date")
    @Schema(description = "Data de criação do gênero", example = "2024/08/12")
    private Date creationDate;

    public Gender(){}

    public Gender(String name) {
        this.name = name;
    }

    public Gender(int id, String name, Date creationDate) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


}
