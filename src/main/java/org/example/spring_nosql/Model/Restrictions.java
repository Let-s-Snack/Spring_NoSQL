package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "Restrictions")
public class Restrictions {
    @Id
    @Schema(name = "ID da restrição", example = "4")
    private int id;
    @Schema(name = "Nome da restrição", example = "Pescetariano")
    @NotNull
    @Min(value = 3, message = "Nome da restrição deve ter mais de 3 caracteres")
    @Max(value = 45, message = "Nome da restrição não deve ter mais de 45 caracteres")
    private String name;
    @NotNull
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
}
