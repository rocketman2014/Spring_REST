package ru.aldokimov.spring.spring_rest.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.aldokimov.spring.spring_rest.dto.PersonDTO;
import ru.aldokimov.spring.spring_rest.model.Person;
import ru.aldokimov.spring.spring_rest.services.PeopleService;
import ru.aldokimov.spring.spring_rest.util.PersonErrorResponse;
import ru.aldokimov.spring.spring_rest.util.PersonNotCreatedException;
import ru.aldokimov.spring.spring_rest.util.PersonNotFoundException;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService,
                            ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<PersonDTO> getPeople() {
        return peopleService.findAll().stream().map(this::conventToPersonDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(params = "id")
    public PersonDTO getPerson(@RequestParam("id") int id) {
        //статус - 200
        return conventToPersonDTO(peopleService.findOne(id)); // Jackson конвертируеться в JSON
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Person with this id wasn't found!", System.currentTimeMillis()
        );

        // В Http ответе тело ответа (Response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND - 404 статус
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO,
                                             BindingResult bindingResult) { // Можно вместо ResponseEntity написать Person
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(" – ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new PersonNotCreatedException(errorMessage.toString());

        }
        peopleService.save(convertToPerson(personDTO));

        // отправляем Http Отвте с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );

        // В Http ответе тело ответа (Response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // NOT_FOUND - 404 статус
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO conventToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}


