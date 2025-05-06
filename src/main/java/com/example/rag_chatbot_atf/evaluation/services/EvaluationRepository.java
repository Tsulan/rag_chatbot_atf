package com.example.rag_chatbot_atf.evaluation.services;

import com.example.rag_chatbot_atf.evaluation.model.EvaluationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EvaluationRepository {
    private final List<EvaluationResult> evaluationResults = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    public EvaluationResult save(EvaluationResult evaluationResult) {
        if (evaluationResult.getId() == null) {
            evaluationResult.setId(idCounter.incrementAndGet());
        }
        evaluationResults.add(evaluationResult);
        return evaluationResult;
    }

    public List<EvaluationResult> findAll() {
        return new ArrayList<>(evaluationResults);
    }
}
