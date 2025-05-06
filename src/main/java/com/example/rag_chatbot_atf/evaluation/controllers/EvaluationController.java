package com.example.rag_chatbot_atf.evaluation.controllers;

import com.example.rag_chatbot_atf.evaluation.model.EvaluationRequest;
import com.example.rag_chatbot_atf.evaluation.model.EvaluationResult;
import com.example.rag_chatbot_atf.evaluation.services.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public ResponseEntity<EvaluationResult> evaluateAllMetrics(@RequestBody EvaluationRequest request) {
        EvaluationResult evaluationResult = evaluationService.evaluate(request);
        return ResponseEntity.ok(evaluationResult);
    }

    @PostMapping("/contextPrecision")
    public ResponseEntity<EvaluationResult> evaluateContextPrecision(@RequestBody EvaluationRequest request) {
        EvaluationResult evaluationResult = evaluationService.evaluateContextPrecision(request);
        return ResponseEntity.ok(evaluationResult);
    }

    @PostMapping("/contextRecall")
    public ResponseEntity<EvaluationResult> evaluateContextRecall(@RequestBody EvaluationRequest request) {
        EvaluationResult evaluationResult = evaluationService.evaluateContextRecall(request);
        return ResponseEntity.ok(evaluationResult);
    }

    @PostMapping("/answerRelevance")
    public ResponseEntity<EvaluationResult> evaluateAnswerRelevance(@RequestBody EvaluationRequest request) {
        EvaluationResult evaluationResult = evaluationService.evaluateAnswerRelevance(request);
        return ResponseEntity.ok(evaluationResult);
    }

    @PostMapping("/faithfulness")
    public ResponseEntity<EvaluationResult> evaluateFaithfulness(@RequestBody EvaluationRequest request) {
        EvaluationResult evaluationResult = evaluationService.evaluateFaithfulness(request);
        return ResponseEntity.ok(evaluationResult);
    }

    @GetMapping("/history")
    public ResponseEntity<List<EvaluationResult>> getAllEvaluations() {
        List<EvaluationResult> evaluationResults = evaluationService.getAllEvaluations();
        return ResponseEntity.ok(evaluationResults);
    }
}
