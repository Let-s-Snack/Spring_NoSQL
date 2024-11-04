package org.example.spring_nosql.Service;

import org.example.spring_nosql.Model.Adm;
import org.example.spring_nosql.Repository.AdmRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdmCustomDetailsService implements UserDetailsService {
    private final AdmRepository admRepository;

    public AdmCustomDetailsService(AdmRepository admRepository){
        this.admRepository = admRepository;
    }

    @Override//Método para retornar verificar se o adm está cadastrado ou não (username = email)
    public UserDetails loadUserByUsername(String email){
        Adm adm = admRepository.findAdmByEmailAndIsDeletedIsFalse(email);

        // Retornar uma implementação de UserDetails
        return new org.springframework.security.core.userdetails.User(
                adm.getEmail(),
                adm.getPassword(), // Senha criptografada
                true,
                true,
                true,
                true,
                Collections.emptyList()
        );
    }
}