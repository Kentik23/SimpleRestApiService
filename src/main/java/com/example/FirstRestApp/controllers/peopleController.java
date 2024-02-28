package com.example.FirstRestApp.controllers;

import com.example.FirstRestApp.models.Person;
import com.example.FirstRestApp.service.PersonService;
import com.example.FirstRestApp.util.PersonErrorResponse;
import com.example.FirstRestApp.util.PersonNotCreatedException;
import com.example.FirstRestApp.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("people")
public class peopleController {
    private PersonService personService;

    @Autowired
    public peopleController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("{id}")
    public Person findById(@PathVariable("id") int id) {
        return personService.findById(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append("; ");
            }

            throw new PersonNotCreatedException(errorMsg.toString());
        }

        personService.save(person);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Человек с таким id не найден", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
