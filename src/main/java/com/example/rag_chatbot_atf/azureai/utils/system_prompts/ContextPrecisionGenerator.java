package com.example.rag_chatbot_atf.azureai.utils.system_prompts;

import com.example.rag_chatbot_atf.evaluation.model.ContextPrecision;

import java.util.List;

public class ContextPrecisionGenerator extends BaseGenerator<ContextPrecision> {
    private static final String INSTRUCTION = """
            Given question, answer, and context verify if the context was useful in arriving at the given answer. Evaluate each context in the order provided. Give verdict as "1" if useful and "0" if not, with JSON output.
            """;

    private static final String OUTPUT_FORMAT_INSTRUCTIONS = """
            The output should be a JSON array with objects for each of the contexts, that conforms the following schema:
            [
                {
                    "reason": "<reason for the verdict>",
                    "verdict": <0 or 1>
                },
                ...
            ]
            Evaluate each context in the order they are provided. Ensure JSON is valid and well-structured. Provide only JSON without any additional text or symbols.
            """;

    private static final List<ContextPrecision> CONTEXT_PRECISION_EXAMPLES = List.of(
            new ContextPrecision(
                    "Who is Albert Einstein?",
                    List.of(
                            "Albert Einstein was born on March 14, 1879, in Germany.",
                            "He developed the theory of relativity, and received the Nobel Prize in Physics in 1921",
                            "Einstein published his famous equation E=mc^2."
                    ),
                    "Albert Einstein was a German-born physicist who developed the theory of relativity and received the Nobel Prize in Physics in 1921.",
                    List.of(
                            new ContextPrecision.Output(
                                    "The context provided helps in answering about Albert Einstein's birth.",
                                    1
                            ),
                            new ContextPrecision.Output(
                                    "The context provided states that Albert Einstein developed the theory of relativity and received the Nobel Prize in Physics in 1921, which helps in answering who Albert Einstein is.",
                                    1
                            ),
                            new ContextPrecision.Output(
                                    "The context provided mentions Einstein's famous equation E=mc^2, which does not include information about who Albert Einstein is or directly relate to the question about his personality.",
                                    0
                            )
                    )
            ),
            new ContextPrecision(
                    "What is the capital of France?",
                    List.of(
                            "France, officially the French Republic, is a country primarily located in Western Europe.",
                            "Its capital is Paris, one of the most important cities in the world.",
                            "Paris is known for its significant influence on art, fashion, and culture."
                    ),
                    "Paris",
                    List.of(
                            new ContextPrecision.Output(
                                    "The context introduces France and its official name, which does not directly answer the question about the capital of France.",
                                    0
                            ),
                            new ContextPrecision.Output(
                                    "The context provided clearly states that Paris is the capital of France.",
                                    1
                            ),
                            new ContextPrecision.Output(
                                    "The context highlights Paris's cultural significance, which is relevant, but not directly answering the question about the capital.",
                                    0
                            )
                    )
            ),
            new ContextPrecision(
                    "Who pointed the Mona Lisa?",
                    List.of(
                            "The Mona Lisa is a half-length portrait painting by Italian artist Leonardo da Vinci.",
                            "It is considered an archetypal masterpiece of the Italian Renaissance.",
                            "Leonardo da Vinci's work on the Mona Lisa has influenced countless artists."
                    ),
                    "Leonardo da Vinci",
                    List.of(
                            new ContextPrecision.Output(
                                    "The context explicitly mentions Leonardo da Vinci as the artist.",
                                    1
                            ),
                            new ContextPrecision.Output(
                                    "The context recognizes the Mona Lisa as a masterpiece of the Italian Renaissance, which is relevant but bot directly who painted it.",
                                    0
                            ),
                            new ContextPrecision.Output(
                                    "The context acknowledges Leonardo da Vinci's influence on artists, which is relevant but not directly answering who painted the Mona Lisa.",
                                    0
                            )
                    )
            )
    );

    @Override
    protected String getTaskName() {
        return "Context Precision";
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
    protected List<ContextPrecision> getExamples() {
        return CONTEXT_PRECISION_EXAMPLES;
    }
}
