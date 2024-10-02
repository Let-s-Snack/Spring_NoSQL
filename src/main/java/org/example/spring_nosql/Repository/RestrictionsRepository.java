package org.example.spring_nosql.Repository;

import org.bson.types.ObjectId;
import org.example.spring_nosql.Model.Restrictions;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RestrictionsRepository extends MongoRepository<Restrictions, ObjectId> {
    List<Restrictions> findRestrictionsByNameIgnoreCase(String name);
}
