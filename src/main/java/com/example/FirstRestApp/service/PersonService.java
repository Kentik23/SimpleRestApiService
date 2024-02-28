package com.example.FirstRestApp.service;

import com.example.FirstRestApp.models.Person;
import com.example.FirstRestApp.repositories.PersonRepository;
import com.example.FirstRestApp.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(int id) {
        Optional<Person> personOptional = personRepository.findById(id);
        return personOptional.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public void save(Person person) {
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }

    public Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }
}
