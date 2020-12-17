package com.example.webquiz.controllers;

import com.example.webquiz.repositories.QuizRepository;
import com.example.webquiz.entities.Answer;
import com.example.webquiz.entities.ResponseQuiz;
import com.example.webquiz.entities.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    public QuizController() {
    }

    @PostMapping(path = "/{id}/solve")
    public ResponseQuiz getAnswer(@PathVariable Long id, @RequestBody Answer answer) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found for this id"));
        if (answer.getAnswer().equals(quiz.getAnswer()))
            return new ResponseQuiz(true);
        return new ResponseQuiz(false);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found for this id"));
        return ResponseEntity.ok().body(quiz);
    }

    @PostMapping()
    public Object createQuiz(@Valid @RequestBody Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @GetMapping()
    public List<Quiz> getQuizzes() {
        return quizRepository.findAll();
    }

    @DeleteMapping()
    public Map<String, Boolean> deleteQuizzes() {
        quizRepository.deleteAll();
        return createResponse();
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteQuiz(@PathVariable Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found for this id"));

        quizRepository.delete(quiz);
        return createResponse();
    }

    private Map<String, Boolean> createResponse() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}