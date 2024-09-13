package org.example.spring_nosql.Service;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Restrictions;
import org.example.spring_nosql.Repository.RestrictionsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
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

    //Método para inserir um novo registro de restrição
    public Restrictions insertRestrictions(Restrictions restrictions){
        return restrictionsRepository.insert(restrictions);
    }

    //Método para atualizar um registro de restrição
    public Restrictions updateRestrictions(Restrictions updatedRestrictions){
        return restrictionsRepository.save(updatedRestrictions);
    }

    //Método para excluir um registro de restrição
    public boolean deleteRestrictions(ObjectId id) {
        Restrictions restrictions = findRestrictionsById(id);
        if(findRestrictionsById(id) != null) {
            restrictionsRepository.delete(restrictions);
            return true;
        }
        return false;
    }
}
