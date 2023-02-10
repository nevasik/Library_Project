package ru.poplaukhin.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.poplaukhin.spring.dao.PersonDAO;
import ru.poplaukhin.spring.models.Person;
import ru.poplaukhin.spring.util.PersonValidator;
import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("fullPeople", personDAO.fullPeople());

        return "people/fullPeople";
    }

    @GetMapping("/{id}")
    public String personId(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        model.addAttribute("books",  personDAO.getBookByPersonId(id));

        return "people/show";
    }

    @GetMapping("/new")
    public String show(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        personDAO.save(person);

        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("peopleEdit", personDAO.show(id));

        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }

        personDAO.update(id, person);

        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);

        return "redirect:/people";
    }
}
