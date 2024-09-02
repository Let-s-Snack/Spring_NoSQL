package org.example.spring_nosql.Service;

import org.example.spring_nosql.Model.Gender;
import org.example.spring_nosql.Repository.GenderRepository;
import org.example.spring_nosql.Repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GenderService {
    private final GenderRepository genderRepository;

    public GenderService(GenderRepository genderRepository){
        this.genderRepository = genderRepository;
    }


    //Fazendo um método para retornar todos os registros de gêneros
    public List<Gender> findAllGenders(){
        return genderRepository.findAll();
    }

    //Fazendo um método para retornar um gênero com base no seu id
    public Gender findGenderById(int pkId){
        return genderRepository.findById(pkId).orElseThrow(() ->
                new RuntimeException("Gênero não encontrado"));
    }

    //Fazendo um método para retornar um gênero com base no seu nome
    public List<Gender> findByGenderName(String name){
        return genderRepository.findGendersByNameIgnoreCase(name);
    }

    //Fazendo um método para inserir um novo gênero
    public Gender insertGender(Gender gender){
        gender.setId(lastId() + 1);
        gender.setCreationDate(new Date());
        return genderRepository.insert(gender);
    }

    //Fazendo um método para atualizar um gênero
    public Gender updateGender(Gender updatedGender){
        return genderRepository.save(updatedGender);
    }

    //Fazendo um método para excluir um gênero
    public Gender deleteGender(int id){
        //if(personRepository.findPersonByGenderId(id) == null){
            Gender gender = findGenderById(id);
            genderRepository.delete(gender);
            return gender;
        //}else{
       //     return null;
        //}
    }

    //Método para pegar o maior id
    public int lastId(){
        List<Gender> allGenders = findAllGenders();
        int maxId = 0;

        for (Gender gender : allGenders){
            if(gender.getId() > maxId){
                maxId = gender.getId();
            }
        }
        return maxId;
    }
}
