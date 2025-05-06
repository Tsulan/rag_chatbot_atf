package com.example.rag_chatbot_atf.azureai.utils.system_prompts;

import com.example.rag_chatbot_atf.evaluation.model.AnswerRelevance;

import java.util.List;

public class AnswerRelevanceGenerator extends BaseGenerator<AnswerRelevance> {

    private static final String INSTRUCTION = """
            Generate questions for the given answer and Identify if answer is noncommittal. Give noncommittal as 1 if the answer is noncommittal and 0 if the answer is committal. A noncommittal answer is one that is evasive, vague, or ambiguous. For example, "I don't know" or "I'm not sure" are noncommittal answers""";

    private static final String OUTPUT_FORMAT_INSTRUCTIONS = """
            The output should be a JSON array with objects for each of the generated question, that conforms the following schema:
            [
                {
                    "question": "<question generated from the answer>",
                    "noncommittal": <0 or 1>
                },
                ...
            ]
            Ensure JSON is valid and well-structured. Provide only JSON without any additional text or symbols.
            """;

    private static final List<AnswerRelevance> ANSWER_RELEVANCE_EXAMPLES = List.of(
            new AnswerRelevance(
                    "Albert Einstein was born in Germany.",
                    List.of(
                            new AnswerRelevance.Output(
                                    "\"Where was Albert Einstein born?\"", 0))),
            new AnswerRelevance(
                    "Everest",
                    List.of(
                            new AnswerRelevance.Output(
                                    "\"What is the tallest mountain on Eath?\"", 0))),
            new AnswerRelevance(
                    "I don't know.",
                    List.of(
                            new AnswerRelevance.Output(
                                    "\"What is the capital of France?\"", 1)))
    );

    @Override
    protected String getTaskName() {
        return "Answer Relevance";
    }

    @Override
    protected String getInstruction() {
        return INSTRUCTION;
    }

    @Override
    protected String getOutputFormatInstruction() {
        return OUTPUT_FORMAT_INSTRUCTIONS;
    }

    @Override
    protected List<AnswerRelevance> getExamples() {
        return ANSWER_RELEVANCE_EXAMPLES;
    }
}
