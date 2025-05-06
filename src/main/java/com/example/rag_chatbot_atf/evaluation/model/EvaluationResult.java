package com.example.rag_chatbot_atf.evaluation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class EvaluationResult {
    private Long id;
    private String question;
    private Double faithfulness;
    private Double answerRelevance;
    private Double contextPrecision;
    private Double contextRecall;
}
