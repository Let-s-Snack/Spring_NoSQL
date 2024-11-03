package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "Recipes")
public class Recipes {
    @Id
    @Schema(description = "Id da receita ", example = "66f2dfdfb310eeeabd300dc5")
    @Field(name = "_id")
    private ObjectId id;

    @NotBlank(message = "Nome da receita não deve ser nulo")
    @Schema(description = "Nome da receita", example = "Bobó de camarão")
    private String name;

    @Schema(description = "Descrição da receita", example = "Receita feita com temperos especiais e frutos do mar, trazendo uma harmonia ao paladar.")
    @NotBlank(message = "A descrição da receita não deve ser nula")
    private String description;

    @Schema(description = "URL da foto da receita", example = "https://i.pinimg.com/236x/c6/fa/68/c6fa68d10f6929de2b764484aa835310.jpg")
    @Field(name = "url_photo")
    @NotBlank(message = "A foto da receita não deve ser nula")
    private String urlPhoto;

    @NotNull(message = "A lista de ingredientes não deve ser nula")
    @Schema(description = "Lista de receitas favoritas", example = "{\n" +
            "        \"ingredientId\": \"66f2dfdfb310eeeabd300dc0\",\n" +
            "        \"meditionType\": \"unidade\",\n" +
            "        \"quantity\": 2\n" +
            "      }")
    private List<IngredientsRecipes> ingredients;

    @Schema(description = "Lista de comentários", example = "Teste") //Adicionar um exemplo
    private List<Coments> coments;

    @NotNull(message = "A lista de passos não deve ser nula")
    @Schema(description = "Passos de preparação da receita", example = "[\n" +
            "      \"1. Quebre os ovos;\",\n" +
            "      \"2. Bata os ovos com sal;\",\n" +
            "      \"3. Aqueça a frigideira;\",\n" +
            "      \"4. Despeje os ovos e cozinhe até dourar\"\n" +
            "    ]")
    @Field(name = "preparation_methods")
    private List<String> preparationMethods;

    @NotNull(message = "A lista de restrições não deve ser nula")
    @Schema(description = "Lista de alimentos restritos", example = "Teste") //Adicionar um exemplo
    @Field(name = "broken_restrictions")
    private List<ObjectId> brokenRestrictions;

    @NotNull(message = "A data de criação da receita não deve ser nula")
    @Schema(description = "Data de criação da receita", example = "10/08/2024")
    @Field(name = "creation_date")
    private Date creationDate;

    @Schema(description = "Média das avaliações", example = "4.5")
    @Max(value = 5, message = "A avaliação deve ser entre 1 e 5")
    @Min(value = 1, message = "A avaliação deve ser entre 1 e 5")
    private Double rating;

    @Schema(description = "Indica se a receita está favoritada pelo usuário ou não", example = "true")
    @NotNull(message = "Receita deve ser favorita ou não")
    private Boolean isFavorite;

    @Schema(description = "Indica se o usuário foi excluido ou não", example = "true")
    @Field(name = "is_deleted")
    @NotNull(message = "Informativo de deleção não pode ser nulo")
    private boolean isDeleted;

    public Recipes() { }

    public Recipes(ObjectId id, String name, String description, String urlPhoto, List<IngredientsRecipes> ingredients, List<Coments> coments, List<String> preparationMethods, List<ObjectId> brokenRestrictions, Date creationDate, Double rating, Boolean isFavorite, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.ingredients = ingredients;
        this.coments = coments;
        this.preparationMethods = preparationMethods;
        this.brokenRestrictions = brokenRestrictions;
        this.creationDate = creationDate;
        this.rating = rating;
        this.isFavorite = isFavorite;
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id.toHexString();
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

    public @NotNull(message = "Nome da receita não deve ser nulo") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Nome da receita não deve ser nulo") String name) {
        this.name = name;
    }

    public @NotNull(message = "A descrição da receita não deve ser nula") String getDescription() {
        return description;
    }

    public void setDescription(@NotNull(message = "A descrição da receita não deve ser nula") String description) {
        this.description = description;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public @NotNull(message = "A lista de ingredientes não deve ser nula") List<IngredientsRecipes> getIngredients() {
        return ingredients;
    }

    public void setIngredients(@NotNull(message = "A lista de ingredientes não deve ser nula") List<IngredientsRecipes> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Coments> getComents() {
        return coments;
    }

    public void setComents(List<Coments> coments) {
        this.coments = coments;
    }

    public @NotNull(message = "A lista de passos não deve ser nula") List<String> getPreparationMethods() {
        return preparationMethods;
    }

    public void setPreparationMethods(@NotNull(message = "A lista de passos não deve ser nula") List<String> preparationMethods) {
        this.preparationMethods = preparationMethods;
    }

    public @NotNull(message = "A lista de restricoes não deve ser nula") List<String> getBrokenRestrictions() {
        return (brokenRestrictions == null)
                ? List.of()
                : brokenRestrictions.stream()
                .map(ObjectId::toHexString)
                .collect(Collectors.toList());
    }

    public void setBrokenRestrictions(@NotNull(message = "A lista de restricoes não deve ser nula") List<ObjectId> brokenRestrictions) {
        this.brokenRestrictions = brokenRestrictions;
    }

    public @NotNull(message = "A data de criação da receita não deve ser nula") Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(@NotNull(message = "A data de criação da receita não deve ser nula") Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getIsFavorite() {
        return this.isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getCommentCount() {
        return this.coments.size();
    }
    @Override
    public String toString() {
        return "Recipes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                ", ingredients=" + ingredients +
                ", coments=" + coments +
                ", preparationMethods=" + preparationMethods +
                ", brokenRestrictions=" + brokenRestrictions +
                ", creationDate=" + creationDate +
                ", rating=" + rating +
                ", isFavorite=" + isFavorite +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
