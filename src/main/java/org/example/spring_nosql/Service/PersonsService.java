package org.example.spring_nosql.Service;

import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.IngredientsShoppingList;
import org.example.spring_nosql.Model.Persons;
import org.example.spring_nosql.Model.Recipes;
import org.example.spring_nosql.Model.ShoppingList;
import org.example.spring_nosql.Repository.PersonRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class PersonsService{
    private final PersonRepository personRepository;
    private final MongoTemplate mongoTemplate;

    public PersonsService(PersonRepository personRepository, MongoTemplate mongoTemplate) {
        this.personRepository = personRepository;
        this.mongoTemplate = mongoTemplate;
    }

    //Fazendo um método para retornar todos os usuários
    public List<Persons> findAllPersons(){
        return personRepository.findPersonsByDeactivationDateIsNull();
    }

    //Fazendo um método para retornar todos os usuários com base no id
    public Persons findPersonById(ObjectId id){
        return personRepository.findPersonsByIdAndDeactivationDateIsNull(id);
    }

    //Fazendo um método para retornar o usuário caso ele esteja cadastrado ou não
    public Persons findPersonByEmail(String email) {
        return personRepository.findPersonByEmailIgnoreCaseAndDeactivationDateIsNull(email);
    }

    //Fazendo um método para retornar o usuário que contenha o username passado como parâmetro
    public Persons findPersonsByUsername(String username){
        return personRepository.findPersonsByNicknameIgnoreCaseAndDeactivationDateIsNull(username);
    }

    //Fazendo um método para retornar a wishlist do usuário com base no seu id
    public List<Recipes> findWishlistPersonByEmail(String email){
        AddFieldsOperation addIsFavorite = Aggregation.addFields().addField("is_favorite").withValue(true).build();

        return mongoTemplate.aggregate(newAggregation(
                Aggregation.match(Criteria.where("email").is(email)),
                addFieldsOperation("wishlistObjectId", "$wishlist.recipes_id"),
                Aggregation.lookup("Recipes","wishlistObjectId","_id","recipes"),
                unwind("recipes"),
                addAverageRatingOperation(),
                addIsFavorite,
                Aggregation.project()
                        .and("recipes._id").as("_id")
                        .and("recipes.name").as("name")
                        .and("recipes.description").as("description")
                        .and("recipes.url_photo").as("url_photo")
                        .and("recipes.creation_date").as("creation_date")
                        .and("recipes.ingredients").as("ingredients")
                        .and("recipes.preparation_methods").as("preparation_methods")
                        .and("recipes.broken_restrictions").as("broken_restrictions")
                        .and("is_favorite").as("isFavorite")
                        .and("rating").as("rating")
        ), Persons.class, Recipes.class).getMappedResults();
    }

    //Fazendo um método para retornar uma lista das receita da semana do usuário
    public List<Recipes> findDirectionWeekByEmail(String email){
        return mongoTemplate.aggregate(newAggregation(
                Aggregation.match(Criteria.where("deactivation_date").is(null)),
                Aggregation.match(Criteria.where("email").is(email)),
                addFieldsOperation("directionsWeekObjectId", "$directions_week.recipes_id"),
                Aggregation.lookup("Recipes","directionsWeekObjectId","_id","recipes"),
                unwind("recipes"),
                Aggregation.project()
                        .and("recipes._id").as("_id")
                        .and("recipes.name").as("name")
                        .and("recipes.description").as("description")
                        .and("recipes.url_photo").as("url_photo")
                        .and("recipes.creation_date").as("creation_date")
                        .and("recipes.ingredients").as("ingredients")
                        .and("recipes.preparation_methods").as("preparation_methods")
                        .and("recipes.broken_restrictions").as("broken_restrictions")
        ), Persons.class, Recipes.class).getMappedResults();
    }

    public List<ShoppingList> findShoppingListByEmail(String email){
        return mongoTemplate.aggregate(newAggregation(
                // Match
                Aggregation.match(Criteria.where("deactivation_date").is(null).and("email").is(email)),

                // Unwind shopping_list
                Aggregation.unwind("shopping_list"),

                // Add fields for recipes_id
                addFieldsOperationShoppingList("shopping_list.recipes_id", "shopping_list.recipes_id"),

                // Lookup for Recipes
                Aggregation.lookup("Recipes", "shopping_list.recipes_id", "_id", "recipeDetails"),

                // Unwind recipeDetails
                Aggregation.unwind("recipeDetails"),

                // Unwind ingredients
                Aggregation.unwind("shopping_list.ingredients"),

                // Add fields for ingredient_id
                addFieldsOperationShoppingList("shopping_list.ingredients.ingredient_id", "shopping_list.ingredients.ingredient_id"),

                // Lookup for Ingredients
                Aggregation.lookup("Ingredients", "shopping_list.ingredients.ingredient_id", "_id", "ingredientDetails"),

                // Unwind ingredientDetails
                Aggregation.unwind("ingredientDetails"),

                // Add fields for meditionType and quantity based on filtering
                addMeditionTypeAndQuantity("shopping_list.ingredients.ingredient_id", "recipeDetails.ingredients"),

                // First project after adding meditionType and quantity
                Aggregation.project()
                        .and("shopping_list.recipes_id").as("recipes_id")
                        .and("recipeDetails.name").as("recipe_name")
                        .and("shopping_list.creation_date").as("creation_date")
                        .and("shopping_list.ingredients.ingredient_id").as("ingredients.ingredient_id")
                        .and("shopping_list.ingredients.is_checked").as("ingredients.is_checked")
                        .and("ingredientDetails.name").as("ingredients.ingredient_name")
                        .and("meditionTypeFiltered.medition_type").as("ingredients.medition_type")
                        .and("quantityFiltered.quantity").as("ingredients.quantity"),

                // Group by recipesId, recipeName, creationDate
                Aggregation.group("recipes_id", "recipe_name", "creation_date")
                        .push(new Document("ingredient_id", "$ingredients.ingredient_id")
                                .append("is_checked", "$ingredients.is_checked")
                                .append("ingredient_name", "$ingredients.ingredient_name")
                                .append("medition_type", "$ingredients.medition_type")
                                .append("quantity", "$ingredients.quantity"))
                        .as("ingredients"),

                // Conversão final de ObjectId para String para receitasId e ingredientes
                Aggregation.addFields()
                        .addFieldWithValue("_id.recipes_id", new Document("$toString", "$_id.recipes_id"))
                        .addFieldWithValue("ingredients", new Document("$map",
                                new Document("input", "$ingredients")
                                        .append("as", "ingredient")
                                        .append("in", new Document("ingredient_id", new Document("$toString", "$$ingredient.ingredient_id"))
                                                .append("is_checked", "$$ingredient.is_checked")
                                                .append("ingredient_name", "$$ingredient.ingredient_name")
                                                .append("medition_type", "$$ingredient.medition_type")
                                                .append("quantity", "$$ingredient.quantity")))).build(),

                // Final project
                Aggregation.project()
                        .and("_id.recipes_id").as("recipes_id")
                        .and("_id.recipe_name").as("recipe_name")
                        .and("_id.creation_date").as("creation_date")
                        .and("ingredients").as("ingredients")
                        .andExclude("_id")
        ), Persons.class, ShoppingList.class).getMappedResults();
    }

    //Fazendo um método de criação do usuário
    public Persons insertPerson(Persons person){
        return personRepository.insert(person);
    }

    //Fazendo um método para fazer a atualização do usuário
    public UpdateResult updatePerson(Query query, Update update){
        return mongoTemplate.updateFirst(query, update, Persons.class);
    }

    //Fazendo um método para fazer a exclusão do usuário
    public Persons deletePerson(Persons excludePerson){
        excludePerson.setDeactivationDate(new Date());
        return mongoTemplate.save(excludePerson);
    }

    public AggregationOperation addFieldsOperation(String nomeNovoCampo, String nomeColuna){
        return context -> new Document("$addFields", new Document(nomeNovoCampo,
                new Document("$map", new Document("input", nomeColuna)
                        .append("as", "recipeId")
                        .append("in", new Document("$toObjectId", "$$recipeId"))
                )));
    }

    public AggregationOperation addAverageRatingOperation() {
        return context -> new Document("$addFields", new Document("rating",
                new Document("$avg", "$recipes.coments.rating") // Calculando a média do rating
        ));
    }

    private AggregationOperation addFieldsOperationShoppingList(String field, String value) {
        return context -> new Document("$set", new Document(field, new Document("$toObjectId", "$" + value)));
    }

    private AggregationOperation addMeditionTypeAndQuantity(String ingredientIdField, String recipeIngredientsField) {
        return context -> new Document("$addFields", new Document()
                .append("meditionTypeFiltered", new Document("$arrayElemAt", List.of(
                        new Document("$filter", new Document("input", "$" + recipeIngredientsField)
                                .append("as", "item")
                                .append("cond", new Document("$eq", List.of("$$item.ingredient_id", "$" + ingredientIdField)))),
                        0
                )))
                .append("quantityFiltered", new Document("$arrayElemAt", List.of(
                        new Document("$filter", new Document("input", "$" + recipeIngredientsField)
                                .append("as", "item")
                                .append("cond", new Document("$eq", List.of("$$item.ingredient_id", "$" + ingredientIdField)))),
                        0
                )))
        );
    }
}
