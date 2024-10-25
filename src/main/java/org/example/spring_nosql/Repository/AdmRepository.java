package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Adm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdmRepository extends MongoRepository<Adm, ObjectId>{
    //Método para retornar os Adm pelo ID
    Adm findAdmByIdAndIsDeletedIsFalse(ObjectId id);

    //Método para retornar o Adm pelo e-mail
    Adm findAdmByEmailAndIsDeletedIsFalse(String email);

    //Método para retornar o Adm pelo nome
    Adm findAdmByNameIgnoreCaseAndIsDeletedIsFalse(String name);
}
