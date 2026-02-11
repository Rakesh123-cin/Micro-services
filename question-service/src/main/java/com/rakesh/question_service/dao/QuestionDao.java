package com.rakesh.question_service.dao;

import com.rakesh.question_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question,Integer> {
    List<Question> findByCategoryIgnoreCase(String category);
    @Query(value="Select q.id from question q where q.category=:category ORDER BY Random() LIMIT :noOfQuestions", nativeQuery = true)
    List<Integer> findRandomQuestionsByCategory(String category, int noOfQuestions);
}
