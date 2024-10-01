package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Persons;
import org.example.spring_nosql.Model.PersonsRestrictions;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.Date;
import java.util.List;

public interface PersonRepository extends MongoRepository<Persons, ObjectId> {
    //Query para retornarmos o e-mail e a senha do usuário com base no e-mail passado como parâmetro
    @Query(value = "{'email': ?0}", fields = "{'_id': 1, 'email':1, 'password': 1}")
    Persons findPersonByEmail(String email);

    @Modifying
    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'deactivationDate': ?1 } }")
    void updateDeactivationDate(String id, Date deactivationDate);

}
