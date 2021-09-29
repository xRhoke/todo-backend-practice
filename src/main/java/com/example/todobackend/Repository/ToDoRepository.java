package com.example.todobackend.Repository;

import com.example.todobackend.Model.ToDo;
import org.springframework.data.repository.CrudRepository;

public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    public Iterable<ToDo> findAllByPriority(String priority);
}
