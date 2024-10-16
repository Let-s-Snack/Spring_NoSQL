package org.example.spring_nosql.Service;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Ingredients;
import org.example.spring_nosql.Repository.IngredientsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientsService {
    private final IngredientsRepository ingredientsRepository;

    public IngredientsService(IngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    //Metodo para retornar todos os ingredientes
    public List<Ingredients> findAllIngredients(){
        return ingredientsRepository.findAllByIsDeletedIsFalse();
    }

    //Metodo para retornar um ingrediente com base no seu id
    public Ingredients findIngredientsById(ObjectId id){
        return ingredientsRepository.findIngredientsByIdAndIsDeletedIsFalse(id);
    }

    //Metodo para retornar um ingrediente com base no seu nome
    public List<Ingredients> findIngredientsByName(String name){
        return ingredientsRepository.findIngredientsByNameContainingIgnoreCaseAndIsDeletedIsFalse(name);
    }
}
