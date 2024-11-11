package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Categories;
import org.example.spring_nosql.Model.Restrictions;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoriesRepository extends MongoRepository<Categories, ObjectId> {
    //Método para buscar todas as categorias que não sofreram soft delete
    List<Categories> findAllByIsDeletedIsFalse();

    //Método para buscar a categoria a partir do ID e ele só ira retornar os registros que não sofreram soft delete
    Categories findCategoriesByIdAndIsDeletedIsFalse(ObjectId id);

    //Método para buscar a categoria a partir do nome e ele só ira retornar os registros que não sofreram soft delete
    Categories findCategoriesByNameIgnoreCaseAndIsDeletedIsFalse(String name);
}
