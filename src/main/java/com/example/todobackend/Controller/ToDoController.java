package com.example.todobackend.Controller;

import com.example.todobackend.Model.ToDo;
import com.example.todobackend.Repository.ToDoRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class ToDoController {

    private final ToDoRepository repository;

    public ToDoController(ToDoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public Iterable<ToDo> getAll(@RequestParam(required = false) String priority){
        if (priority != null) {
            return this.repository.findAllByPriority(priority);
        }
        else return this.repository.findAll();
    }

    @PostMapping("")
    public ToDo create(@RequestBody ToDo toDo){
        return this.repository.save(toDo);
    }

    @GetMapping("/{id}")
    public ToDo getById(@PathVariable Long id){
        if (this.repository.findById(id).isPresent()){
            return this.repository.findById(id).get();
        }
        else return null;
    }

    @PatchMapping("/{id}")
    public ToDo update(@PathVariable Long id, @RequestBody ToDo toDo){
        if (toDo.getDescription() != null) this.repository.findById(id).get().setDescription(toDo.getDescription());
        if (toDo.getPriority() != null) this.repository.findById(id).get().setPriority(toDo.getPriority());
        if (toDo.getDueDate() != null) this.repository.findById(id).get().setDueDate(toDo.getDueDate());
        return this.repository.save(this.repository.findById(id).get());
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        if (this.repository.findById(id).isPresent()){
            this.repository.deleteById(id);
            return String.format("ToDo with id %s deleted", id);
        }
        else return null;
    }
}
