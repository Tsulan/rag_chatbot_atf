package com.example.rag_chatbot_atf.evaluation.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class Faithfulness {

    @JsonProperty
    private String question;

    @JsonProperty
    private String context;

    @JsonProperty
    private String answer;

    @JsonProperty
    private List<String> statements;

    @JsonProperty
    private List<Faithfulness.Output> output;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        appendIfNotNull(sb, "question", question);
        appendIfNotNull(sb, "context", context);
        appendIfNotNull(sb, "answer", answer);

        if (statements != null && !statements.isEmpty()) {
            sb.append("statements: ")
                    .append(statements.stream()
                            .map(s -> String.format("- %s", s))
                            .collect(Collectors.joining("\n")))
                    .append("\n");
        }

        if (output != null && !output.isEmpty()) {
            sb.append("output: ")
                    .append(output.stream()
                            .map(Output::toString)
                            .collect(Collectors.joining("\n")))
                    .append("\n");
        }

        return sb.toString();
    }

    private void appendIfNotNull(StringBuilder sb, String fieldName, Object value) {
        if (value != null) {
            sb.append(String.format("%s: %s\n", fieldName, value));
        }
    }

    @Getter
    public static class Output implements MetricOutput {

        @JsonProperty("statement")
        private String statement;

        @JsonProperty("reason")
        private String reason;

        @JsonProperty("verdict")
        private int verdict;

        @JsonCreator
        public Output(@JsonProperty("statement") String statement,
                      @JsonProperty("reason") String reason,
                      @JsonProperty("verdict") int verdict) {
            this.statement = statement;
            this.reason = reason;
            this.verdict = verdict;
        }

        @Override
        public String toString() {
            return String.format("\nstatement: %s\nreason: %s\nverdict: %d\n", statement, reason, verdict);
        }
    }
}
