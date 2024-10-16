package org.example.spring_nosql.Service;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Restrictions;
import org.example.spring_nosql.Repository.RestrictionsRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RestrictionsService{
    private final RestrictionsRepository restrictionsRepository;

    public RestrictionsService(RestrictionsRepository restrictionsRepository){
        this.restrictionsRepository = restrictionsRepository;
    }

    //Método para retornar todos os registros de restrições
    public List<Restrictions> findAllRestrictions(){
        return restrictionsRepository.findAllByIsDeletedIsFalse();
    }

    //Método para retornar um registro de restrição pelo id
    public Restrictions findRestrictionsById(ObjectId id){
        return restrictionsRepository.findRestrictionsByIdAndIsDeletedIsFalse(id);
    }

    //Método para retornar um registro de restrição pelo nome
    public Restrictions findRestrictionsByName(String name){
        return restrictionsRepository.findRestrictionsByNameIgnoreCaseAndIsDeletedIsFalse(name);
    }
}