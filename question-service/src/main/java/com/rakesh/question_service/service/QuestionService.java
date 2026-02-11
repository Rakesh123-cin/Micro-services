package com.rakesh.question_service.service;

import com.rakesh.question_service.dao.QuestionDao;
import com.rakesh.question_service.model.Question;
import com.rakesh.question_service.model.QuestionWrapper;
import com.rakesh.question_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionDao questionDao;
    public ResponseEntity<List<Question>> getAllQuestions() {
        try{
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try{
            return new ResponseEntity<>(questionDao.findByCategoryIgnoreCase(category),HttpStatus.OK);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<String> addQuestion(Question question) {
        questionDao.save(question);
        return new ResponseEntity<>("success",HttpStatus.CREATED);
    }

    public ResponseEntity<String> updateQuestion(Question question, Integer id) {
        Question oldQuestion = questionDao.findById(id).orElse(null);
        if(oldQuestion!=null)
        {
            oldQuestion.setQuestionTitle(question.getQuestionTitle());
            oldQuestion.setCategory(question.getCategory());
            oldQuestion.setDifficultylevel(question.getDifficultylevel());
            oldQuestion.setOption1(question.getOption1());
            oldQuestion.setOption2(question.getOption2());
            oldQuestion.setOption3(question.getOption3());
            oldQuestion.setOption4(question.getOption4());
            oldQuestion.setRightAnswer(question.getRightAnswer());

            questionDao.save(oldQuestion);
        }
        return new ResponseEntity<>("Updated successfully",HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteQuestion(Integer id) {
        try{
            questionDao.deleteById(id);
            return new ResponseEntity<>("Deleted successfully",HttpStatus.OK);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong!!!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<Integer>> generateQuestionsForQuiz(String category, int noOfQuestions) {
        return new ResponseEntity<>(questionDao.findRandomQuestionsByCategory(category,noOfQuestions),HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        List<Question> questions = questionDao.findAllById(questionIds);

        for(Question question : questions)
        {
            QuestionWrapper questionWrapper = new QuestionWrapper();
            questionWrapper.setId(question.getId());
            questionWrapper.setQuestionTitle(question.getQuestionTitle());
            questionWrapper.setOption1(question.getOption1());
            questionWrapper.setOption2(question.getOption2());
            questionWrapper.setOption3(question.getOption3());
            questionWrapper.setOption4(question.getOption4());
            questionsForUser.add(questionWrapper);
        }

        return new ResponseEntity<>(questionsForUser,HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int score = 0;
        for(Response response : responses)
        {
            Question question = questionDao.findById(response.getId()).orElse(null);
            if(question!=null && response.getAnswer().equalsIgnoreCase(question.getRightAnswer()))
                score++;
        }
        return new ResponseEntity<>(score,HttpStatus.OK);
    }
}
