package com.example.rag_chatbot_atf.azureai.utils.system_prompts;

import java.util.List;

public abstract class BaseGenerator<T> {

    protected abstract String getTaskName();

    protected abstract String getInstruction();

    protected abstract String getOutputFormatInstruction();

    protected abstract List<T> getExamples();

    public String generateSystemPrompt() {
        return new SystemPrompt(
                getTaskName(),
                getInstruction(),
                getOutputFormatInstruction(),
                getExamples()
        ).toString();
    }
}
