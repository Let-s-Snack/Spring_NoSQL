package org.example.spring_nosql.Service;

import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Criteria;
import org.example.spring_nosql.Repository.RecipesRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RecipesService {
    private final RecipesRepository recipesRepository;
    private final PersonsService personsService;
    private final RestrictionsService restrictionsService;
    private final MongoTemplate mongoTemplate;

    public RecipesService(RecipesRepository recipesRepository, MongoTemplate mongoTemplate, PersonsService personsService, RestrictionsService restrictionsService){
        this.recipesRepository = recipesRepository;
        this.mongoTemplate = mongoTemplate;
        this.personsService = personsService;
        this.restrictionsService = restrictionsService;
    }

    //Método para retornar todas as receitas
    public List<Recipes> findAllRecipes(){
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("is_deleted").is(false)),
                addFieldsOperation("ingredientId", "$ingredients.ingredient_id"),
                addFieldsOperation("personsId", "$coments.persons_id"),

                Aggregation.lookup("Ingredients", "ingredientId", "_id", "ingredientsInfo"),

                Aggregation.lookup("Persons", "personsId", "_id", "personsInfo"),

                addAverageRatingOperation(), // Adicionando média do campo rating

                addCommentsMappingOperation(),

                addIngredientsMappingOperation(),

                Aggregation.project()
                        .and("_id").as("_id")
                        .and("name").as("name")
                        .and("description").as("description")
                        .and("url_photo").as("url_photo")
                        .and("creation_date").as("creation_date")

                        .and("ingredients").as("ingredients")
                        .and("coments").as("coments")
                        .and("rating").as("rating")

                        .and("preparation_methods").as("preparation_methods")
                        .and("broken_restrictions").as("broken_restrictions")
        ), Recipes.class, Recipes.class).getMappedResults();
    }

    //Método para retornar uma receita com base no id
    public Recipes findRecipesById(ObjectId id, String personsEmail){
        AggregationOperation addFieldsPersonsFavorite = Aggregation.addFields().addField("personsEmailFavorite").withValue(personsEmail).build();

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("is_deleted").is(false)),
                Aggregation.match(Criteria.where("_id").is(id)),
                addFieldsPersonsFavorite,

                addFieldsOperation("ingredientId", "$ingredients.ingredient_id"),
                addFieldsOperation("personsId", "$coments.persons_id"),

                Aggregation.lookup("Ingredients", "ingredientId", "_id", "ingredientsInfo"),

                Aggregation.lookup("Persons", "personsId", "_id", "personsInfo"),

                Aggregation.lookup("Persons", "personsEmailFavorite", "email", "personsFavorite"),

                addAverageRatingOperation(), // Adicionando média do campo rating

                addCommentsMappingOperation(),

                addIngredientsMappingOperation(),

                addIsFavoriteFieldOperation(),

                Aggregation.project()
                        .and("_id").as("_id")
                        .and("name").as("name")
                        .and("description").as("description")
                        .and("url_photo").as("url_photo")
                        .and("creation_date").as("creation_date")
                        .and("ingredients").as("ingredients")
                        .and("coments").as("coments")
                        .and("rating").as("rating")
                        .and("isFavorite").as("isFavorite")
                        .and("preparation_methods").as("preparation_methods")
                        .and("restrictionsInfo").as("broken_restrictions")
        ), Recipes.class, Recipes.class).getMappedResults().get(0);
    }

    //Método para retornar uma receita com base no seu nome
    public List<Recipes> findRecipesByName(String recipeNameFilter, String personsEmail){
        List<PersonsRestrictions> listPersonsRestrictions = personsService.findPersonByEmail(personsEmail).getRestrictions();
        List<String> listObjectId = new ArrayList<>();

        for (PersonsRestrictions personsRestrictions : listPersonsRestrictions){
            listObjectId.add(personsRestrictions.getRestrictionId());
        }

        List<Recipes> recipes = findAllRecipes();
        List<Recipes> finalRecipesList = new ArrayList<>();

        recipeNameFilter = Normalizer.normalize(recipeNameFilter, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "").toLowerCase();

        for (Recipes recipe : recipes){
            String recipeName = Normalizer.normalize(recipe.getName(), Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "").toLowerCase();

            if(recipeName.contains(recipeNameFilter)){
                List<String> listRestrictionsObjectId = recipe.getBrokenRestrictions();
                List<Restrictions> listRestrictions = new ArrayList<>();

                for (String restrictionsId : listRestrictionsObjectId){
                    listRestrictions.add(restrictionsService.findRestrictionsById(new ObjectId(restrictionsId)));
                }

                if(!listRestrictions.isEmpty()){
                    for (Restrictions restrictions : listRestrictions) {
                        if (!listObjectId.contains(restrictions.getId())) {
                            finalRecipesList.add(recipe);
                        }
                    }
                }else{
                    finalRecipesList.add(recipe);
                }
            }

        }

        return finalRecipesList;
    }

    //Método para encontrar as receitas que se encaixam na restrição passada como parâmetro
    public List<Recipes> findRecipesByRestriction(ObjectId restrictionId, String personsEmail){
        AggregationOperation addFieldsPersonsFavorite = Aggregation.addFields().addField("personsEmailFavorite").withValue(personsEmail).build();
        AggregationOperation addFieldsRestrictionId = Aggregation.addFields().addField("restrictionId").withValue(restrictionId).build();

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("is_deleted").is(false)),
                addFieldsOperation("restrictionsId", "$broken_restrictions"),
                addAverageRatingOperation(),
                addFieldsPersonsFavorite,
                addFieldsRestrictionId,
                createRestrictionMatchOperation(restrictionId),

                Aggregation.lookup("Persons", "personsEmailFavorite", "_id", "personsFavorite"),

                addIsFavoriteFieldOperation(),

                    Aggregation.project()
                        .and("_id").as("_id")
                        .and("name").as("name")
                        .and("description").as("description")
                        .and("url_photo").as("url_photo")
                        .and("creation_date").as("creation_date")
                        .and("rating").as("rating")
                        .and("isFavorite").as("isFavorite")
        ),Recipes.class, Recipes.class).getMappedResults();
    }

    //Método para inserir um comentário na receita
    public UpdateResult insertComent(Query query, Update update){
        return mongoTemplate.updateFirst(query, update, Recipes.class);
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
                new Document("$avg", "$coments.rating") // Calculando a média do rating
        ));
    }

    public AggregationOperation addIsFavoriteFieldOperation() {
        return context -> new Document("$addFields", new Document("isFavorite",
                new Document("$cond",
                        new Document("if",
                                new Document("$gt",
                                        List.of(
                                                new Document("$size",
                                                        new Document("$filter",
                                                                new Document("input",
                                                                        new Document("$map",
                                                                                new Document("input",
                                                                                        new Document("$ifNull",
                                                                                                List.of(
                                                                                                        new Document("$reduce",
                                                                                                                new Document("input", "$personsFavorite.wishlist")
                                                                                                                        .append("initialValue", new ArrayList<>())
                                                                                                                        .append("in",
                                                                                                                                new Document("$concatArrays", List.of("$$value", "$$this.recipes_id"))
                                                                                                                        )
                                                                                                        ),
                                                                                                        List.of())
                                                                                        )
                                                                                )
                                                                                        .append("as", "recipeIdString")
                                                                                        .append("in", new Document("$toObjectId", "$$recipeIdString"))
                                                                        )
                                                                )
                                                                        .append("as", "convertedRecipeId")
                                                                        .append("cond", new Document("$eq", List.of("$$convertedRecipeId", "$_id")))
                                                        )
                                                ),
                                                0
                                        )
                                )
                        ).append("then", true)
                                .append("else", false)
                )
        ));
    }

    public AggregationOperation addIngredientsMappingOperation() {
        return context -> new Document("$addFields", new Document("ingredients",
                new Document("$map",
                        new Document("input", "$ingredients")
                                .append("as", "ingredient")
                                .append("in", new Document()
                                        .append("ingredient_id", "$$ingredient.ingredient_id")
                                        .append("medition_type", "$$ingredient.medition_type")
                                        .append("quantity", "$$ingredient.quantity")
                                        .append("name",
                                                new Document("$arrayElemAt",
                                                        Arrays.asList(
                                                                new Document("$map", new Document("input",
                                                                        new Document("$filter", new Document("input", "$ingredientsInfo")
                                                                                .append("as", "ingredientInfo")
                                                                                .append("cond", new Document("$eq", Arrays.asList("$$ingredientInfo._id",
                                                                                        new Document("$toObjectId", "$$ingredient.ingredient_id"))))
                                                                        ))
                                                                        .append("as", "filteredIngredient")
                                                                        .append("in", "$$filteredIngredient.name")
                                                                ),
                                                                0
                                                        )
                                                )
                                        )
                                        .append("description",
                                                new Document("$arrayElemAt",
                                                        Arrays.asList(
                                                                new Document("$map", new Document("input",
                                                                        new Document("$filter", new Document("input", "$ingredientsInfo")
                                                                                .append("as", "ingredientInfo")
                                                                                .append("cond", new Document("$eq", Arrays.asList("$$ingredientInfo._id",
                                                                                        new Document("$toObjectId", "$$ingredient.ingredient_id"))))
                                                                        ))
                                                                        .append("as", "filteredIngredient")
                                                                        .append("in", "$$filteredIngredient.description")
                                                                ),
                                                                0
                                                        )
                                                )
                                        )
                                )
                )
        ));
    }

    public AggregationOperation addCommentsMappingOperation() {
        return context -> new Document("$addFields", new Document("coments",
                new Document("$map",
                        new Document("input", "$coments")
                                .append("as", "coment")
                                .append("in", new Document()
                                        .append("coment_id", "$$coment.coment_id")
                                        .append("creation_date", "$$coment.creation_date")
                                        .append("message", "$$coment.message")
                                        .append("persons_id", "$$coment.persons_id")
                                        .append("rating", "$$coment.rating")
                                        .append("persons_name",
                                                new Document("$arrayElemAt",
                                                        Arrays.asList(
                                                                new Document("$map", new Document("input",
                                                                        new Document("$filter", new Document("input", "$personsInfo")
                                                                                .append("as", "personInfo")
                                                                                .append("cond", new Document("$eq", Arrays.asList("$$personInfo._id",
                                                                                        new Document("$toObjectId", "$$coment.persons_id"))))
                                                                        ))
                                                                        .append("as", "filteredPerson")
                                                                        .append("in", "$$filteredPerson.name")
                                                                ),
                                                                0
                                                        )
                                                )
                                        )
                                )
                )
        ));
    }

    public AggregationOperation createRestrictionMatchOperation(ObjectId restrictionId) {
        return context -> new Document("$match",
                new Document("$expr",
                        new Document("$not",
                                new Document("$in", Arrays.asList(
                                        restrictionId,
                                        new Document("$ifNull", Arrays.asList("$restrictionsId", new ArrayList<>()))
                                ))
                        )
                )
        );
    }
}
