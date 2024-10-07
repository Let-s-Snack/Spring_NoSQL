package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;

public class Coments {
    @Id
    @Schema(description = "Id do comentário ", example = "1")
    @Field(name = "coment_id")
    private ObjectId comentId;

    @Schema(name = "Id do dono do comentário ", example = "1")
    @Field(name = "persons_id")
    private String personsId;

    @NotNull(message = "Avaliação da receita não deve ser nula")
    @Schema(description = "Avaliação", example = "4")
    @Max(value = 5, message = "A avaliação deve ser entre 1 e 5")
    @Min(value = 1, message = "A avaliação deve ser entre 1 e 5")
    private int rating;

    @Schema(description = "Comentário sobre a receita", example = "Receita muito boa!")
    @Size(max = 200, message = "Mensagem não deve ter mais que 200 caracteres")
    private String message;

    @NotNull(message = "A data de criação do comentário não deve ser nula")
    @Schema(description = "Data de criação do comentário", example = "10/08/2024")
    @Field(name = "creation_date")
    private Date creationDate;

    @Field(name = "persons_name")
    @NotBlank(message = "Nome do usuário que avaliou não deve ser nulo")
    private String personsName;

    public Coments() { }

    public Coments(ObjectId comentId, String personsId, int rating, String message, Date creationDate, String personsName) {
        this.comentId = comentId;
        this.personsId = personsId;
        this.rating = rating;
        this.message = message;
        this.creationDate = creationDate;
        this.personsName = personsName;
    }

    public Coments(String personsId, int rating, String message) {
        this.personsId = personsId;
        this.rating = rating;
        this.message = message;
    }

    public String getComentId() {
        return comentId.toHexString();
    }

    public void setComentId(ObjectId comentId) {
        this.comentId = comentId;
    }

    public String getPersonsId() {
        return personsId;
    }

    public void setPersonsId(String personsId) {
        this.personsId = personsId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getPersonsName() {
        return personsName;
    }

    public void setPersonsName(String personsName) {
        this.personsName = personsName;
    }

    @Override
    public String toString() {
        return "Coments{" +
                "comentId=" + comentId.toHexString() +
                ", personsId='" + personsId + '\'' +
                ", rating=" + rating +
                ", message='" + message + '\'' +
                ", creationDate=" + creationDate +
                ", personsName='" + personsName + '\'' +
                '}';
    }
}
