package org.example.spring_nosql.Repository;

import org.example.spring_nosql.Model.Persons;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PersonRepository extends MongoRepository<Persons, Integer> {
    //Query para retornarmos o e-mail e a senha do usuário com base no e-mail passado como parâmetro
    @Query(value = "{'email': ?0}", fields = "{'email':1, 'password': 1}")
    Persons findPersonByEmail(String email);

    //Query para retornarmos as restrições do usuário
    @Query(value = "{'id': ?0}", fields = "{'id':  1, 'favorites':  1}") //Fazer uma query para trazer as informações das receitas favoritas com o lookup
    List<Object> findPersonRestrictionById(int id);

    //Query para retornarmos a wishlist do usuário com base em seu id
    @Query(value = "{'id':  ?0}", fields = "{'id': 1, 'wishlist':  1}") //Fazer uma query para trazer as informações da wishlist com o lookup
    List<Object> findPersonWishlistById(int id);

    //Query para retornarmos as receitas da semana do usuário com base em seu id
    @Query(value = "{'id':  ?0}", fields = "{'id':  1, 'directionsWeeks':  1}") //Fazer uma query para trazer as informações das receitas da semana com o lookup
    List<Object> findDirectionWeekById(int id);

}
