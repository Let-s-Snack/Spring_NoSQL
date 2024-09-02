package org.example.spring_nosql.Repository;

import org.example.spring_nosql.Model.Gender;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GenderRepository extends MongoRepository<Gender, Integer> {

    List<Gender> findGendersByNameIgnoreCase(String name);
}
