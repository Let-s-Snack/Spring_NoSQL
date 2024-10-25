package org.example.spring_nosql.Service;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Adm;
import org.example.spring_nosql.Repository.AdmRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdmService {
    private final AdmRepository admRepository;

    public AdmService(AdmRepository admRepository){
        this.admRepository = admRepository;
    }

    //Fazendo um método para listar todos os adms
    public List<Adm> findAllAdm(){
        return admRepository.findAll();
    }

    //Método para retornar o adm pelo id
    public Adm findAdmById(ObjectId id){
        return admRepository.findAdmByIdAndIsDeletedIsFalse(id);
    }

    //Método para retornar o adm pelo e-mail
    public Adm findAdmByEmail(String email){
        return admRepository.findAdmByEmailAndIsDeletedIsFalse(email);
    }

    //Método para retornar o adm pelo nome
    public Adm findAdmByName(String name){
        return admRepository.findAdmByNameIgnoreCaseAndIsDeletedIsFalse(name);
    }
}
