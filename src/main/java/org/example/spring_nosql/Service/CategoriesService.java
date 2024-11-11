package org.example.spring_nosql.Service;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Categories;
import org.example.spring_nosql.Model.Restrictions;
import org.example.spring_nosql.Repository.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    //Método para retornar todos os registros de restrições
    public List<Categories> findAllCategories(){
        return categoriesRepository.findAllByIsDeletedIsFalse();
    }

    //Método para retornar um registro de restrição pelo id
    public Categories findCategoriesById(ObjectId id){
        return categoriesRepository.findCategoriesByIdAndIsDeletedIsFalse(id);
    }

    //Método para retornar um registro de restrição pelo nome
    public Categories findCategoriesByName(String name){
        return categoriesRepository.findCategoriesByNameIgnoreCaseAndIsDeletedIsFalse(name);
    }
}


