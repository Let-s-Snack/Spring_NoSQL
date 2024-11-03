package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Restrictions;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RestrictionsRepository extends MongoRepository<Restrictions, ObjectId> {

    //Método para buscar todas as restrições que não sofreram soft delete
    List<Restrictions> findAllByIsDeletedIsFalse();

    //Método para buscar a restrição a partir do ID e ele só ira retornar os registros que não sofreram soft delete
    Restrictions findRestrictionsByIdAndIsDeletedIsFalse(ObjectId id);

    //Método para buscar a restrição a partir do nome e ele só ira retornar os registros que não sofreram soft delete
    Restrictions findRestrictionsByNameIgnoreCaseAndIsDeletedIsFalse(String name);
}
