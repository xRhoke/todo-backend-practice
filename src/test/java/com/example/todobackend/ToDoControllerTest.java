package com.example.todobackend;

import com.example.todobackend.Model.ToDo;
import com.example.todobackend.Repository.ToDoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ToDoControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ToDoRepository repository;

    @Test
    @Transactional
    @Rollback
    public void canGetToDoListTest() throws Exception {
        ToDo testToDo = new ToDo();
        testToDo.setDescription("cut the grass");
        testToDo.setPriority("high");
        testToDo.setDueDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-30"));

        this.repository.save(testToDo);

        this.mvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].priority", is("high")));
    }

    @Test
    @Transactional
    @Rollback
    public void canCreateToDoItemTest() throws Exception {
        ToDo testToDo = new ToDo();
        testToDo.setDescription("cut the grass");
        testToDo.setPriority("high");
        testToDo.setDueDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-30"));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testToDo);

        this.mvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.priority", is("high")));
    }

    @Test
    @Transactional
    @Rollback
    public void canGetToDoByIdTest() throws Exception {
        ToDo testToDo = new ToDo();
        testToDo.setDescription("cut the grass");
        testToDo.setPriority("high");
        testToDo.setDueDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-30"));

        String path = String.format("/todo/%s", this.repository.save(testToDo).getId());

        this.mvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.priority", is("high")))
                .andExpect(jsonPath("$.description", is("cut the grass")));
    }

    @Test
    @Transactional
    @Rollback
    public void canUpdateToDoByIdTest() throws Exception {
        ToDo testToDo = new ToDo();
        testToDo.setDescription("cut the grass");
        testToDo.setPriority("high");
        testToDo.setDueDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-30"));
        String path = String.format("/todo/%s", this.repository.save(testToDo).getId());

        ToDo updatedToDo = new ToDo();
        updatedToDo.setPriority("low");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(updatedToDo);


        this.mvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.priority", is("low")))
                .andExpect(jsonPath("$.description", is("cut the grass")));
    }

    @Test
    @Transactional
    @Rollback
    public void canDeleteToDoByIdTest() throws Exception {
        ToDo testToDo = new ToDo();
        testToDo.setDescription("cut the grass");
        testToDo.setPriority("high");
        testToDo.setDueDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-30"));
        String path = String.format("/todo/%s", this.repository.save(testToDo).getId());

        this.mvc.perform(delete(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

}
