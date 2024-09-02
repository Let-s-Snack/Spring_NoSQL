package org.example.spring_nosql.Service;

import org.example.spring_nosql.Model.Gender;
import org.example.spring_nosql.Model.Persons;
import org.example.spring_nosql.Repository.GenderRepository;
import org.example.spring_nosql.Repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonsService{
    private final PersonRepository personRepository;

    public PersonsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    //Fazendo um método para retornar todos os usuários
    public List<Persons> listAllPersons(){
        return personRepository.findAll();
    }

    //Fazendo um método para retornar todos os usuários com base no id
    public Object listPersonById(int id){
        return personRepository.findById(id);
    }

    //Fazendo um método para retornar os usuário e a senha com base no e-mail
    public Persons listPersonByEmail(String email){
        return personRepository.findPersonByEmail(email);
    }

    //Fazendo um método para retornar as restrições do usuário com base no seu id
    public List<Object> listPersonRestrictionById(int id){
        return personRepository.findPersonRestrictionById(id);
    }

    //Fazendo um método para retornar a wishlist do usuário com base no seu id
    public List<Object> listWishlistPersonById(int id){
        return personRepository.findPersonWishlistById(id);
    }

    //Fazendo um método para retornar uma lista das receita da semana do usuário
    public List<Object> listDirectionWeekById(int id){
        return personRepository.findDirectionWeekById(id);
    }

    //Fazendo um método de criação do usuário
    public Persons insertPerson(Persons person){
        return personRepository.insert(person);
    }

    //Fazendo um método para fazer a atualização do usuário{





}
