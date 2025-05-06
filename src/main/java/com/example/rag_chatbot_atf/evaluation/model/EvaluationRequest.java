package com.example.rag_chatbot_atf.evaluation.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluationRequest {
    private String question;
    private List<String> contexts;
    private String answer;
    private String groundTruth;
}
