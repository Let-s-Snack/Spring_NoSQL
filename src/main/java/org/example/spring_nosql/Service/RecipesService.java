package org.example.spring_nosql.Service;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Recipes;
import org.example.spring_nosql.Repository.RecipesRepository;

import java.util.List;

public class RecipesService {
    private final RecipesRepository recipesRepository;

    public RecipesService(RecipesRepository recipesRepository){
        this.recipesRepository = recipesRepository;
    }

    //método para retornar uma receita com base no id
    public Recipes findRecipesById(ObjectId id){
        return recipesRepository.findRecipesById(id);
    }

}
