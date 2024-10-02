package org.example.spring_nosql.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;
import java.util.List;

@Document(collection = "Recipes")
public class Recipes {
    @Id
    @Schema(name = "Id da receita ", example = "1")
    @Field(name = "_id")
    @NotBlank(message = "ID da receita não deve ser nula ou vazia")
    private ObjectId id;

    @NotNull(message = "Nome da receita não deve ser nulo")
    @Schema(name = "Nome da receita", example = "Bobó de camarão")
    private String name;

    @Schema(name = "Descrição da receita", example = "Receita feita com temperos especiais e frutos do mar, trazendo uma harmonia ao paladar.")
    @NotNull(message = "A descrição da receita não deve ser nula")
    private String description;

    @Schema(name = "URL da foto da receita", example = "https://i.pinimg.com/236x/c6/fa/68/c6fa68d10f6929de2b764484aa835310.jpg")
    @Field(name = "url_photo")
    private String urlPhoto;

    @NotNull(message = "A data de criação da receita não deve ser nula")
    @Schema(name = "Data de criação da receita", example = "10/08/2024")
    @Field(name = "creation_date")
    private Date creationDate;

    @NotNull(message = "A lista de ingredientes não deve ser nula")
    @Schema(name = "Lista de receitas favoritas", example = "Teste") //Adicionar um exemplo
    private List<IngredientsRecipes> ingredients;

    @Schema(name = "Lista de comentários", example = "Teste") //Adicionar um exemplo
    private List<Coments> coments;

    @NotNull(message = "A lista de passos não deve ser nula")
    @Schema(name = "Passos de preparação", example = "Teste") //Adicionar um exemplo
    @Field(name = "preparation_methods")
    private List<String> preparationMethods;

    @NotNull(message = "A lista de restricoes não deve ser nula")
    @Schema(name = "Lista de alimentos restritos", example = "Teste") //Adicionar um exemplo
    @Field(name = "broken_restrictions")
    private List<ObjectId> brokenRestrictions;



    public Recipes() { }

    public Recipes(String name, String description, String urlPhoto, List<IngredientsRecipes> ingredients, List<Coments> coments, List<String> preparationMethods, List<ObjectId> brokenRestrictions, Date creationDate) {
        this.name = name;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.ingredients = ingredients;
        this.coments = coments;
        this.preparationMethods = preparationMethods;
        this.brokenRestrictions = brokenRestrictions;
        this.creationDate = creationDate;
    }

    public Recipes(String name, String description, String urlPhoto, List<IngredientsRecipes> ingredients, List<String> preparationMethods, List<ObjectId> brokenRestrictions) {
        this.name = name;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.ingredients = ingredients;
        this.preparationMethods = preparationMethods;
        this.brokenRestrictions = brokenRestrictions;
    }

    public Recipes(String name, String description, String urlPhoto, Date creationDate){
        this.name = name;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.creationDate = creationDate;
    }

    public Recipes(ObjectId id, String description, String urlPhoto, Date creationDate){
        this.name = name;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id.toHexString();
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
    public void setId(String id) {
        this.id = new ObjectId(id);
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

    public @NotNull(message = "A lista de restricoes não deve ser nula") List<ObjectId> getBrokenRestrictions() {
        return brokenRestrictions;
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
                '}';
    }
}
