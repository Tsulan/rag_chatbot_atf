package com.example.rag_chatbot_atf.evaluation.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class AnswerRelevance {

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("output")
    private List<Output> output;

    @Override
    public String toString() {
        return String.format("\nanswer: %s\noutput: %s\n", answer, output);
    }

    @Getter
    public static class Output implements MetricOutput {

        @JsonProperty("question")
        private String question;

        @JsonProperty("noncommittal")
        private int noncommittal;

        @JsonCreator
        public Output(@JsonProperty("question") String question,
                      @JsonProperty("noncommittal") int noncommittal) {
            this.question = question;
            this.noncommittal = noncommittal;
        }

        @Override
        public String toString() {
            return String.format("\nquestion: %s\nnoncommittal: %d\n", question, noncommittal);
        }
    }
}
