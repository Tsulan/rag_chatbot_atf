package com.example.rag_chatbot_atf.evaluation.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ContextRecall {

    @JsonProperty("question")
    private String question;

    @JsonProperty("context")
    private List<String> contexts;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("output")
    private List<Output> output;

    @Override
    public String toString() {
        return String.format("\ncontext: %s\nanswer: %s\noutput: %s\n",
                question, contexts, answer);
    }

    @Getter
    public static class Output implements MetricOutput {

        @JsonProperty("sentence")
        private String sentence;

        @JsonProperty("reason")
        private String reason;

        @JsonProperty("attributed")
        private int attributed;

        @JsonCreator
        public Output(@JsonProperty("sentence") String sentence,
                      @JsonProperty("reason") String reason,
                      @JsonProperty("attributed") int attributed) {
            this.sentence = sentence;
            this.reason = reason;
            this.attributed = attributed;
        }

        @Override
        public String toString() {
            return String.format("\nsentence: %s\nreason: %s\nattributed: %s\n", sentence, reason, attributed);
        }
    }
}
