package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Ingredients;
import org.example.spring_nosql.Model.Restrictions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IngredientsRepository extends MongoRepository<Ingredients, String> {

    //Query para buscar ingredientes por ID
    Ingredients findIngredientsById(ObjectId id);

    //Query para buscar ingredientes por nome
    List<Ingredients> findIngredientsByNameIgnoreCase(String name);
}
