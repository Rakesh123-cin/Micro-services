package com.rakesh.QuizService.service;

import com.rakesh.QuizService.dao.QuizDao;
import com.rakesh.QuizService.feign.QuizInterface;
import com.rakesh.QuizService.model.QuestionWrapper;
import com.rakesh.QuizService.model.Quiz;
import com.rakesh.QuizService.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {
    @Autowired
    private QuizDao quizDao;
    @Autowired
    private QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int noOfQuestions, String title) {
        List<Integer> questionIds = quizInterface.generateQuestionsForQuiz(category,noOfQuestions).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questionIds);
        quizDao.save(quiz);
        return new ResponseEntity<>("Quiz Created...", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).orElse(null);
        if(quiz==null)
        {
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
        }
        List<Integer> questionIds = quiz.getQuestionIds();
        List<QuestionWrapper> questionsForUser = quizInterface.getQuestionsFromId(questionIds).getBody();

        return new ResponseEntity<>(questionsForUser,HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Quiz quiz = quizDao.findById(id).orElse(null);
        if(quiz==null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        int score = quizInterface.getScore(responses).getBody();
        return new ResponseEntity<>(score,HttpStatus.OK);
    }
}
