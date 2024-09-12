package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Ingredients;
import org.example.spring_nosql.Model.Restrictions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IngredientsRepository extends MongoRepository<Ingredients, String> {

    //Query para buscar ingredientes por ID
    @Query(value = "{'id': ?0}", fields = "{'id': 1, 'name': 1, 'broken_restrictions': 1}")
    Ingredients findIngredientsById(ObjectId id);

    //Query para buscar ingredientes por nome
    @Query(value = "{'name': ?0}", fields = "{'id': 1, 'name': 1, 'broken_restrictions': 1,}")
    List<Object> findIngredientsByNameIgnoreCase(String name);
}
