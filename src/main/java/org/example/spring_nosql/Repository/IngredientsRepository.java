package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Ingredients;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IngredientsRepository extends MongoRepository<Ingredients, ObjectId> {

    //Query para buscar ingredientes por ID
    Ingredients findIngredientsByIdAndIsDeletedIsFalse(ObjectId id);

    //Query para buscar ingredientes por nome
    //findIngredientsByNameIgnoreCaseAndIsDeletedIsFalse
    List<Ingredients> findIngredientsByNameContainingIgnoreCaseAndIsDeletedIsFalse(String name);

    //Query para buscar todos os ingredientes que não foram excluidos
    List<Ingredients> findAllByIsDeletedIsFalse();
}
