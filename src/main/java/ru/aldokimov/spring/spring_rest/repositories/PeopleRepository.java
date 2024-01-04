package ru.aldokimov.spring.spring_rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.aldokimov.spring.spring_rest.model.Person;


@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
}
