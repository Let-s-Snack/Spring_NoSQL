package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class Coments {
    @Id
    @Schema(name = "Id do comentário ", example = "1")
    @Field(name = "coment_id")
    private int comentId;

    @Schema(name = "Id do dono do comentário ", example = "1")
    @Field(name = "person_id")
    private int personId;

    @NotNull(message = "Avaliação da receita não deve ser nula")
    @Schema(name = "Avaliação", example = "4")
    @Max(value = 5, message = "A avaliação deve ser entre 0 e 5")
    private int rating;

    @Schema(name = "Comentário sobre a receita", example = "Receita muito boa!")
    private String message;

    @NotNull(message = "A data de criação do comentário não deve ser nula")
    @Schema(name = "Data de criação do comentário", example = "10/08/2024")
    @Field(name = "creation_date")
    private Date creationDate;

    public Coments() { }

    public Coments(int comentId, int personId, int rating, String message, Date creationDate) {
        this.comentId = comentId;
        this.personId = personId;
        this.rating = rating;
        this.message = message;
        this.creationDate = creationDate;
    }

    public int getComentId() {
        return comentId;
    }

    public void setComentId(int comentId) {
        this.comentId = comentId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @NotNull(message = "Avaliação da receita não deve ser nula")
    @Max(value = 5, message = "A avaliação deve ser entre 0 e 5")
    public int getRating() {
        return rating;
    }

    public void setRating(@NotNull(message = "Avaliação da receita não deve ser nula") @Max(value = 5, message = "A avaliação deve ser entre 0 e 5") int rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public @NotNull(message = "A data de criação do comentário não deve ser nula") Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(@NotNull(message = "A data de criação do comentário não deve ser nula") Date creationDate) {
        this.creationDate = creationDate;
    }
}