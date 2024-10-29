package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Persons;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface PersonRepository extends MongoRepository<Persons, ObjectId> {
    //Query para retornarmos todos os usuários que não estejam excluidos
    List<Persons> findPersonsByDeactivationDateIsNull();

    //Query para buscar o usuário pelo id
    Persons findPersonsByIdAndDeactivationDateIsNull(ObjectId id);

    //Query para retornarmos o e-mail e a senha do usuário com base no e-mail passado como parâmetro
    Persons findPersonByEmailIgnoreCaseAndDeactivationDateIsNull(String email);

    //Query para retornarmos o username do usuário com base no parâmetro
    Persons findPersonsByNicknameIgnoreCaseAndDeactivationDateIsNull(String username);

}
