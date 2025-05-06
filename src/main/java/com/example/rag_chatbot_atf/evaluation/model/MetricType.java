package com.example.rag_chatbot_atf.evaluation.model;

import lombok.Getter;

@Getter
public enum MetricType {
    CONTEXT_PRECISION("Context Precision"),

    CONTEXT_RECALL("Context Recall"),

    ANSWER_RELEVANCE("Answer Relevance"),

    FAITHFULNESS("Faithfulness");

    private final String displayName;

    MetricType(String displayName) {
        this.displayName = displayName;
    }
}
