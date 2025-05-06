package com.example.rag_chatbot_atf.evaluation.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ContextPrecision {

    @JsonProperty("question")
    private String question;

    @JsonProperty("context")
    private List<String> context;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("output")
    private List<ContextPrecision.Output> output;

    @Override
    public String toString() {
        return String.format("\ncontext: %s\nanswer: %s\noutput: %s\n",
                question, context, answer);
    }

    @Getter
    public static class Output implements MetricOutput {

        @JsonProperty("reason")
        private String reason;

        @JsonProperty("verdict")
        private int verdict;

        @JsonCreator
        public Output(@JsonProperty("reason") String reason,
                      @JsonProperty("verdict") int verdict) {
            this.reason = reason;
            this.verdict = verdict;
        }

        @Override
        public String toString() {
            return String.format("\nreason: %s\nverdict: %s\n", reason, verdict);
        }
    }
}
