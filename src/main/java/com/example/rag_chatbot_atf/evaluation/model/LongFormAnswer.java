package com.example.rag_chatbot_atf.evaluation.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class LongFormAnswer {

    @JsonProperty("question")
    private String question;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("sentences")
    private String sentences;

    @JsonProperty("output")
    private List<Output> output;

    @Override
    public String toString() {
        return String.format("question: %s\nanswer: %s\nsentences: %s\noutput: %s\n",
                question, answer, sentences, output);
    }

    @Getter
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    public static class Output implements MetricOutput {
        @JsonProperty("sentence_index")
        private int sentenceIndex;

        @JsonProperty("simpler_statements")
        private List<String> simplerStatements;

        @Override
        public String toString() {
            return String.format("sentence_index: %d\nsimpler_statements: %s\n",
                    sentenceIndex, simplerStatements);
        }
    }
}
