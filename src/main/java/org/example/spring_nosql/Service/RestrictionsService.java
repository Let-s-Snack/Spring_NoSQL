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
        return restrictionsRepository.findAll();
    }

    //Método para retornar um registro de restrição pelo id
    public Restrictions findRestrictionsById(ObjectId id){
        return restrictionsRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Restricão não encontrada"));
    }

    //Método para retornar um registro de restrição pelo nome
    public List<Restrictions> findRestrictionsByName(String name){
        return restrictionsRepository.findRestrictionsByNameIgnoreCase(name);
    }
}
