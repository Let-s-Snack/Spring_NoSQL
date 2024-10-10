package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Persons;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface PersonRepository extends MongoRepository<Persons, ObjectId> {
    //Query para retornarmos o e-mail e a senha do usuário com base no e-mail passado como parâmetro
    Persons findPersonByEmailIgnoreCase(String email);

    //Query para retornarmos o username do usuário com base no parâmetro
    Persons findPersonsByNicknameIgnoreCase(String username);

}
