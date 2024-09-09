package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Recipes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RecipesRepository extends MongoRepository<Recipes, String> {
    //Query para retornarmos a receita com base no id passado como parâmetro
    @Query(value = "{'id': ?0}", fields = "{'id':1, 'password': 1}")
    Recipes findRecipesById(ObjectId id);

}
