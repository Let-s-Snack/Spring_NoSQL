package org.example.spring_nosql.Service;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Persons;
import org.example.spring_nosql.Model.Recipes;
import org.example.spring_nosql.Repository.PersonRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import java.util.Date;
import java.util.List;

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
        return personRepository.findAll();
    }

    //Fazendo um método para retornar todos os usuários com base no id
    public Persons findPersonById(ObjectId id){
        return personRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Usuário não encontrado"));
    }

    //Fazendo um método para retornar o usuário caso ele esteja cadastrado ou não
    public Persons findPersonLoggedByEmail(String email, String password) {
        Persons person = personRepository.findPersonByEmail(email);

        if (person != null && checkPassword(password, person.getPassword())) {
            return person;
        } else {
            return null;
        }
    }

    //Fazendo um método para retornar as receitas favoritas do usuário
    public List<Recipes> findPersonFavoritesById(ObjectId id){
        AggregationOperation addFieldsOperation = addFieldsOperation("favoritesObjectId", "$favorites.recipes_id");

        return mongoTemplate.aggregate(newAggregation(
                Aggregation.match(Criteria.where("_id").is(id)),
                addFieldsOperation,
                Aggregation.lookup("Recipes","favoritesObjectId","_id","recipes"),
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

    //Fazendo um método para retornar a wishlist do usuário com base no seu id
    public List<Recipes> findWishlistPersonById(ObjectId id){
        AggregationOperation addFieldsOperation = addFieldsOperation("wishlistObjectId", "$wishlist.recipes_id");

        return mongoTemplate.aggregate(newAggregation(
                Aggregation.match(Criteria.where("_id").is(id)),
                addFieldsOperation,
                Aggregation.lookup("Recipes","wishlistObjectId","_id","recipes"),
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

    //Fazendo um método para retornar uma lista das receita da semana do usuário
    public List<Recipes> findDirectionWeekById(ObjectId id){
        AggregationOperation addFieldsOperation = addFieldsOperation("directionsWeekObjectId", "$directions_week.recipes_id");

        return mongoTemplate.aggregate(newAggregation(
                Aggregation.match(Criteria.where("_id").is(id)),
                addFieldsOperation,
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

    /*//Fazendo um método para retornar uma lista das receitas e dos ingredientes do usuário
    public List<Document> findShoppingListById(ObjectId id) {
        List<Document> results = mongoTemplate.aggregate(Aggregation.newAggregation(
                match(Criteria.where("_id").is(id)),
                unwind("shopping_list"),
                unwind("shopping_list.ingredients"),
                addFieldsOperation("recipes_id_shopping_list", "$shopping_list.recipes_id"),
                addFieldsOperation("ingredients_id_shopping_list", "$shopping_list.ingredients.ingredient_id"),
                lookup("Recipes", "recipes_id_shopping_list", "_id", "recipe_info"),
                lookup("Ingredients", "ingredients_id_shopping_list", "_id", "ingredient_info"),
                unwind("recipe_info"),
                unwind("ingredient_info"),
                addFields()
                        .addFieldWithValue("matching_recipe_ingredient", ArrayOperators.ArrayElemAt.arrayOf(
                                ArrayOperators.Filter.filter("recipe_info.ingredients")
                                        .as("recipe_ingredient")
                                        .by(ComparisonOperators.Eq.valueOf("$$recipe_ingredient.ingredient_id")
                                                .equalTo("$ingredients_id_shopping_list"))
                        ).elementAt(0)) // Pegar o primeiro elemento do array retornado
                        .build(),
                Aggregation.group("recipes_id_shopping_list")
                        .first("recipe_info.name").as("recipe_name")
                        .first("shopping_list.creation_date").as("creation_date") // Adiciona o creation_date
                        .push(
                                Aggregation.project()
                                        .and(ConvertOperators.ToString.toString("$ingredients_id_shopping_list")).as("ingredient_id")
                                        .and("ingredient_info.name").as("ingredient_name")
                                        .and("shopping_list.ingredients.is_checked").as("is_checked")
                                        .and("matching_recipe_ingredient.medition_type").as("medition_type")
                                        .and("matching_recipe_ingredient.quantity").as("quantity")
                        ).as("ingredients"),
                addFields().addFieldWithValue("recipes_id", ConvertOperators.ToString.toString("$_id")).build()
                ,project()
                        .andExclude("_id")
                        .and("recipes_id").as("recipes_id")
                        .and("recipe_name").as("recipe_name")
                        .and("creation_date").as("creation_date")
                        .and("ingredients").as("ingredients")
        ), "Persons", Document.class).getMappedResults();

        return results;
    }*/

    //Fazendo um método de criação do usuário
    public Persons insertPerson(Persons person){
        return personRepository.insert(person);
    }

    //Finalizar
    /*public Object updateIngredientCheck(ObjectId personId, List<ObjectId> checkedIngredientsId){
        Persons person = findPersonById(personId);
        List<ShoppingList> shoppingList = person.getShoppingList();
        List<IngredientsShoppingList> ingredientsShoppingList = new ArrayList<>();

        for (ShoppingList objectShoppingList : shoppingList){
            ingredientsShoppingList = objectShoppingList.getIngredients();
        }

        for (IngredientsShoppingList ingredient : ingredientsShoppingList){
            if(checkedIngredientsId.contains(ingredient.getIngredientId())){
                ingredient.setIsChecked(true);
            }
        }

        return personRepository.save(person);
    }*/

    //Fazendo um método para fazer a atualização do usuário{
    public Persons updatePerson(Persons person){
        return mongoTemplate.save(person);
    }

    //Fazendo um método para fazer a exclusão do usuário

    public Persons deletePerson(Persons excludePerson){
        excludePerson.setDeactivationDate(new Date());
        return mongoTemplate.save(excludePerson);
    }

    // Verifica se a senha corresponde ao hash
    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    public AggregationOperation addFieldsOperation(String nomeNovoCampo, String nomeColuna){
        return context -> new Document("$addFields", new Document(nomeNovoCampo,
                new Document("$map", new Document("input", nomeColuna)
                        .append("as", "recipeId")
                        .append("in", new Document("$toObjectId", "$$recipeId"))
                )));
    }
}
