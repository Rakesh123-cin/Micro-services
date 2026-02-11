package com.rakesh.question_service.controller;

import com.rakesh.question_service.model.Question;
import com.rakesh.question_service.model.QuestionWrapper;
import com.rakesh.question_service.model.Response;
import com.rakesh.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return questionService.getAllQuestions();
    }
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category)
    {
        return questionService.getQuestionsByCategory(category);
    }
    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question)
    {
        return questionService.addQuestion(question);
    }

    // update question
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateQuestion(@RequestBody Question question, @PathVariable Integer id)
    {
        return questionService.updateQuestion(question,id);
    }

    // delete question
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Integer id)
    {
        return questionService.deleteQuestion(id);
    }

    // generateQuestions
    @GetMapping("/generate")
    public ResponseEntity<List<Integer>> generateQuestionsForQuiz(@RequestParam String category, @RequestParam int noOfQuestions)
    {
        return questionService.generateQuestionsForQuiz(category,noOfQuestions);
    }

    // getQuestions (questionId)
    @PostMapping("/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds)
    {
        return questionService.getQuestionsFromId(questionIds);
    }

    // getScore
    @PostMapping("/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses)
    {
        return questionService.getScore(responses);
    }
}
