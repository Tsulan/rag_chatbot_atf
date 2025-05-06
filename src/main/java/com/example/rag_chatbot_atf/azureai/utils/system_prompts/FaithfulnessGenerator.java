package com.example.rag_chatbot_atf.azureai.utils.system_prompts;

import com.example.rag_chatbot_atf.evaluation.model.Faithfulness;

import java.util.List;

public class FaithfulnessGenerator extends BaseGenerator<Faithfulness> {

    private static final String INSTRUCTION = """
            Your task is to judge the faithfulness of a series of simple statements based on a given context. For each statement you must return verdict as 1 if the statement can be directly inferred based on the context or 0 if the statement can not be directly inferred based on the context.
            """;

    private static final String OUTPUT_FORMAT_INSTRUCTIONS = """
            The output should be a JSON array with objects for each of the simpler_statements, that conforms the following schema:
            [
                {
                    "statement": "<simpler_statement>",
                    "reason": "<reason for the classification>",
                    "verdict": <0 or 1>
                },
                ...
            ]
            Ensure JSON is valid and well-structured. Provide only JSON without any additional text or symbols.
            """;

    private static final List<Faithfulness> FAITHFULNESS_EXAMPLES = List.of(
            new Faithfulness(
                    null,
                    "John is a student at XYZ University. He is pursuing a degree in Computer Science. He is enrolled in several courses this semester, including Data Structures, Algorithms, and Database Management. John is a diligent student and spends a significant amount of time studying and completing assignments. He often stays late in the library to work on his projects.",
                    null,
                    List.of("John is majoring in Biology.",
                            "John is taking a course on Artificial Intelligence.",
                            "John is a dedicated student.",
                            "John has a part-time job."),
                    List.of(
                            new Faithfulness.Output(
                                    "John is majoring in Biology.",
                                    "John's major is explicitly mentioned as Computer Science. There is no information suggesting he is majoring in Biology.",
                                    0
                            ),
                            new Faithfulness.Output(
                                    "John is taking a course on Artificial Intelligence.",
                                    "The context mentions the courses John is currently enrolled in, and Artificial Intelligence is not mentioned. Therefore, it cannot be deduced that John is taking a course on AI.",
                                    0
                            ),
                            new Faithfulness.Output(
                                    "John is a dedicated student.",
                                    "The context states that he spends a significant amount of time studying and completing assignments. Additionally, it mentions that he often stays late in the library to work on his projects, which implies dedication.",
                                    1
                            ),
                            new Faithfulness.Output(
                                    "John has a part-time job.",
                                    "There is no information given in the context about John having a part-time job.",
                                    0
                            )
                    )
            ),
            new Faithfulness(
                    null,
                    "Photosynthesis is a process used by plants, algae, and certain bacteria to convert light energy into chemical energy.",
                    null,
                    List.of("Albert Einstein was a genius."),
                    List.of(
                            new Faithfulness.Output(
                                    "Albert Einstein was a genius.",
                                    "The context and statement are unrelated",
                                    0
                            )
                    )
            )
    );

    @Override
    protected String getTaskName() {
        return "Faithfulness";
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
    protected List<Faithfulness> getExamples() {
        return FAITHFULNESS_EXAMPLES;
    }
}
