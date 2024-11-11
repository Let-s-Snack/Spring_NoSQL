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
import com.google.gson.Gson;


import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        AggregationOperation addEmailField = Aggregation.addFields().addField("personsEmail").withValue("$coments.email").build();

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("is_deleted").is(false)),
                addFieldsOperation("ingredientId", "$ingredients.ingredient_id"),
                addEmailField,

                Aggregation.lookup("Ingredients", "ingredientId", "_id", "ingredientsInfo"),

                Aggregation.lookup("Persons", "personsEmail", "email", "personsInfo"),

                addAverageRatingOperation(), // Adicionando média do campo rating

                addCommentsMappingOperation(),

                addIngredientsMappingOperation(),

                addIsSwift(),

                Aggregation.lookup("Categories", "categories", "_id", "categoriesInfo"),

                addPartners(),

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

                        .and("categories").as("categories")
                        .and("is_swift").as("is_swift")
                        .and("partner").as("partner")
        ), Recipes.class, Recipes.class).getMappedResults();
    }

    //Método para retornar uma receita com base no id
    public Recipes findRecipesById(ObjectId id, String personsEmail){
        AggregationOperation addFieldsPersonsFavorite = Aggregation.addFields().addField("personsEmailFavorite").withValue(personsEmail).build();
        AggregationOperation addFieldsPersonsEmail = Aggregation.addFields().addField("personsEmail").withValue("$coments.email").build();

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("is_deleted").is(false)),
                Aggregation.match(Criteria.where("_id").is(id)),
                addFieldsPersonsFavorite,

                addFieldsOperation("ingredientId", "$ingredients.ingredient_id"),
                addFieldsPersonsEmail,

                Aggregation.lookup("Ingredients", "ingredientId", "_id", "ingredientsInfo"),

                Aggregation.lookup("Persons", "personsEmail", "email", "personsInfo"),

                Aggregation.lookup("Persons", "personsEmailFavorite", "email", "personsFavorite"),

                addAverageRatingOperation(), // Adicionando média do campo rating

                addCommentsMappingOperation(),

                addIngredientsMappingOperation(),

                addIsFavoriteFieldOperation(),

                addIsSwift(),

                Aggregation.lookup("Categories", "categories", "_id", "categoriesInfo"),

                addPartners(),

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
                        .and("categories").as("categories")
                        .and("is_swift").as("is_swift")
                        .and("partner").as("partner")
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

                Aggregation.lookup("Persons", "personsEmailFavorite", "email", "personsFavorite"),

                addIsFavoriteFieldOperation(),

                addIsSwift(),

                Aggregation.lookup("Categories", "categories", "_id", "categoriesInfo"),

                addPartners(),

                Aggregation.project()
                    .and("_id").as("_id")
                    .and("name").as("name")
                    .and("description").as("description")
                    .and("url_photo").as("url_photo")
                    .and("creation_date").as("creation_date")
                    .and("rating").as("rating")
                    .and("isFavorite").as("isFavorite")
                    .and("categories").as("categories")
                    .and("is_swift").as("is_swift")
                    .and("partner").as("partner")
        ),Recipes.class, Recipes.class).getMappedResults();
    }

    //Método para encontrar as receitas que se encaixam na restrição passada como parâmetro
    public List<Recipes> findRecipesByAllRestriction(List<ObjectId> restrictionsId, String personsEmail) {
            AggregationOperation addFieldsPersonsFavorite = Aggregation.addFields()
                    .addField("personsEmailFavorite").withValue(personsEmail).build();
            AggregationOperation addFieldsRestrictionsId = Aggregation.addFields()
                    .addField("restrictionsId").withValue(restrictionsId).build();

            return mongoTemplate.aggregate(Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("is_deleted").is(false)),
                    addFieldsOperation("restrictionsId", "$broken_restrictions"),
                    addAverageRatingOperation(),
                    addFieldsPersonsFavorite,
                    addFieldsRestrictionsId,
                    createRestrictionsMatchOperation(restrictionsId),

                    Aggregation.lookup("Persons", "personsEmailFavorite", "email", "personsFavorite"),

                    addIsFavoriteFieldOperation(),

                    addIsSwift(),

                    Aggregation.lookup("Categories", "categories", "_id", "categoriesInfo"),

                    addPartners(),

                    Aggregation.project()
                            .and("_id").as("_id")
                            .and("name").as("name")
                            .and("description").as("description")
                            .and("url_photo").as("url_photo")
                            .and("creation_date").as("creation_date")
                            .and("rating").as("rating")
                            .and("isFavorite").as("isFavorite")
                            .and("coments").as("coments")
                            .and("categories").as("categories")
                            .and("is_swift").as("is_swift")
                            .and("partner").as("partner")
            ), Recipes.class, Recipes.class).getMappedResults();
    }

    //Método para encontrar as receitas que se encaixam na restrição passada como parâmetro
    public List<Recipes> findRecipesByCategory(ObjectId categoryId, String personsEmail) {
        AggregationOperation addFieldsPersonsFavorite = Aggregation.addFields()
                .addField("personsEmailFavorite").withValue(personsEmail).build();
        AggregationOperation addFieldsRestrictionId = Aggregation.addFields()
                .addField("categoryId").withValue(categoryId).build();

        return mongoTemplate.aggregate(Aggregation.newAggregation(
                Aggregation.match(Criteria.where("is_deleted").is(false)),
                addFieldsOperation("categoriesId", "$categories"),
                addAverageRatingOperation(),
                addFieldsPersonsFavorite,
                addFieldsRestrictionId,
                createCategoryMatchOperation(categoryId),  // Usando o match atualizado

                Aggregation.lookup("Persons", "personsEmailFavorite", "email", "personsFavorite"),

                addIsFavoriteFieldOperation(),

                addIsSwift(),

                Aggregation.lookup("Categories", "categories", "_id", "categoriesInfo"),

                addPartners(),

                Aggregation.project()
                        .and("_id").as("_id")
                        .and("name").as("name")
                        .and("description").as("description")
                        .and("url_photo").as("url_photo")
                        .and("creation_date").as("creation_date")
                        .and("rating").as("rating")
                        .and("isFavorite").as("isFavorite")
                        .and("categories").as("categories")
                        .and("is_swift").as("is_swift")
                        .and("partner").as("partner")
        ), Recipes.class, Recipes.class).getMappedResults();
    }

    //Método para retornar as receitas recomendada
    public List<Recipes> findTrendingRecipes(String email) {
        List<PersonsRestrictions> listPersonsRestrictions = personsService.findPersonByEmail(email).getRestrictions();
        List<ObjectId> listObjectId = new ArrayList<>();
        List<Recipes> listRecipes = new ArrayList<>();

        for(PersonsRestrictions objectPersonsRestrictions : listPersonsRestrictions){
            listObjectId.add(new ObjectId(objectPersonsRestrictions.getRestrictionId()));
        }

        List<Recipes> recipes = findRecipesByAllRestriction(listObjectId ,email);

        for(Recipes objectRecipes : recipes){
            if(objectRecipes.getRating() != null){
                listRecipes.add(objectRecipes);

            }
        }
        listRecipes.sort(Comparator.comparingDouble(Recipes::getRating).reversed());

        return listRecipes.size() > 10 ? listRecipes.subList(0, 10) : listRecipes;
    }

    //Método para retornar as receitas em alta
    public List<Recipes> findRecommendedRecipes(String email) {
        List<PersonsRestrictions> listPersonsRestrictions = personsService.findPersonByEmail(email).getRestrictions();
        List<ObjectId> listObjectId = new ArrayList<>();
        List<Recipes> listRecipes = new ArrayList<>();

        for(PersonsRestrictions objectPersonsRestrictions : listPersonsRestrictions){
            listObjectId.add(new ObjectId(objectPersonsRestrictions.getRestrictionId()));
        }

        List<Recipes> recipes = findRecipesByAllRestriction(listObjectId ,email);

        for(Recipes objectRecipes : recipes){
            if(objectRecipes.getId() != null){
                listRecipes.add(objectRecipes);
            }
        }
        listRecipes.sort(Comparator.comparing(Recipes::getId).reversed());

        return listRecipes.size() > 10 ? listRecipes.subList(0, 10) : listRecipes;
    }

    //Método para retornar as receitas mais comentadas
    public List<Recipes> findMostCommentedRecipes(String email) {
        List<PersonsRestrictions> listPersonsRestrictions = personsService.findPersonByEmail(email).getRestrictions();
        List<ObjectId> listObjectId = new ArrayList<>();
        List<Recipes> listRecipes = new ArrayList<>();

        for(PersonsRestrictions objectPersonsRestrictions : listPersonsRestrictions){
            listObjectId.add(new ObjectId(objectPersonsRestrictions.getRestrictionId()));
        }

        List<Recipes> recipes = findRecipesByAllRestriction(listObjectId ,email);

        for(Recipes objectRecipes : recipes){
            if(objectRecipes.getComents() != null){
                listRecipes.add(objectRecipes);
            }
        }

        listRecipes.sort(Comparator.comparingInt(Recipes::getCommentCount).reversed());

        return listRecipes.size() > 10 ? listRecipes.subList(0, 10) : listRecipes;
    }

    //Método para retornar as receitas em alta
    public List<Recipes> findRecipesByBrokenRestrictions(String email) {
        List<PersonsRestrictions> listPersonsRestrictions = personsService.findPersonByEmail(email).getRestrictions();
        List<ObjectId> listObjectId = new ArrayList<>();
        List<Recipes> listRecipes = new ArrayList<>();

        for(PersonsRestrictions objectPersonsRestrictions : listPersonsRestrictions){
            listObjectId.add(new ObjectId(objectPersonsRestrictions.getRestrictionId()));
        }

        for(Recipes objectRecipes : findRecipesByAllRestriction(listObjectId ,email)){
            if(objectRecipes.getId() != null){
                listRecipes.add(objectRecipes);
            }
        }
        listRecipes.sort(Comparator.comparing(Recipes::getId).reversed());

        return listRecipes.size() > 10 ? listRecipes.subList(0, 10) : listRecipes;
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
                                        .append("is_swift", "$$ingredient.is_swift")
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
                                        .append("email", "$$coment.email")
                                        .append("rating", "$$coment.rating")
                                        .append("persons_name",
                                                new Document("$arrayElemAt",
                                                        Arrays.asList(
                                                                new Document("$map", new Document("input",
                                                                        new Document("$filter", new Document("input", "$personsInfo")
                                                                                .append("as", "personInfo")
                                                                                .append("cond", new Document("$eq", Arrays.asList("$$personInfo.email",
                                                                                        "$$coment.email")))
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

    public AggregationOperation createCategoryMatchOperation(ObjectId categoryId) {
        // Verificação específica para o ID 672d94bedb6a04681a5844c6
        if (categoryId.toHexString().equals("672d94bedb6a04681a5844c6")) {
            // Retorna receitas onde 'categories' contém somente este ID e o tamanho é 1
            return context -> new Document("$match",
                    new Document("categories", new Document("$eq", Arrays.asList(categoryId)))
            );
        } else {
            // Condição para outros IDs: verifica apenas se o ID existe em 'categories'
            return context -> new Document("$match",
                    new Document("categories", categoryId)
            );
        }
    }


    public AggregationOperation addIsSwift() {
        return context -> new Document("$addFields",
                new Document("is_swift",
                        new Document("$anyElementTrue", Arrays.asList(
                                new Document("$map", new Document("input", "$ingredients")
                                        .append("as", "ingredient")
                                        .append("in", "$$ingredient.is_swift")
                                )
                        ))
                )
        );
    }

    public AggregationOperation addPartners() {
        return context -> new Document("$addFields",
                new Document("partner",
                        new Document("$cond", new Document("if",
                                new Document("$anyElementTrue", Arrays.asList(
                                        new Document("$map", new Document("input", "$categoriesInfo")
                                                .append("as", "category")
                                                .append("in", new Document("$eq", Arrays.asList(
                                                        new Document("$toLower", "$$category.name"),
                                                        "germinachef"))))
                                )))
                                .append("then", 2)
                                .append("else", new Document("$cond", new Document("if",
                                        new Document("$anyElementTrue", Arrays.asList(
                                                new Document("$map", new Document("input", "$categoriesInfo")
                                                        .append("as", "category")
                                                        .append("in", new Document("$eq", Arrays.asList(
                                                                new Document("$toLower", "$$category.name"),
                                                                "swift"))))
                                        )))
                                        .append("then", 1)
                                        .append("else", new Document("$cond", new Document("if",
                                                new Document("$anyElementTrue", Arrays.asList(
                                                        new Document("$map", new Document("input", "$categoriesInfo")
                                                                .append("as", "category")
                                                                .append("in", new Document("$ne", Arrays.asList(
                                                                        new Document("$toLower", "$$category.name"),
                                                                        "germinachef"))))
                                                )))
                                                .append("then", 3)
                                                .append("else", -1)
                                        ))
                                ))
                        )
                ));
    }

    public AggregationOperation createRestrictionsMatchOperation(List<ObjectId> restrictionIds) {
        return context -> new Document("$match",
                new Document("$expr",
                        new Document("$anyElementTrue", Arrays.asList(
                                new Document("$map", new Document()
                                        .append("input", restrictionIds)
                                        .append("as", "id")
                                        .append("in", new Document("$in", Arrays.asList("$$id", "$restrictionsId")))
                                )
                        ))
                )
        );
    }
}
