package com.example.rag_chatbot_atf.azureai.utils.system_prompts;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class SystemPrompt {
    private final String name;
    private final String instruction;
    private final String outputFormatInstruction;
    private final List<?> examples;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(instruction);
        sb.append("\n").append(outputFormatInstruction);
        if (examples != null && !examples.isEmpty()) {
            sb.append("\nExamples:\n");
            examples.forEach(example -> sb.append(example).append("\n"));
        }
        return sb.toString();
    }
}
