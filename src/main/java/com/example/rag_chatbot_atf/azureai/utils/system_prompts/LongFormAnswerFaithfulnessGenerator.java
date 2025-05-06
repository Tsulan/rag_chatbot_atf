package com.example.rag_chatbot_atf.azureai.utils.system_prompts;

import com.example.rag_chatbot_atf.evaluation.model.LongFormAnswer;

import java.util.List;

public class LongFormAnswerFaithfulnessGenerator extends BaseGenerator<LongFormAnswer> {

    private static final String INSTRUCTION = """
            Given a question and an answer, analyze the complexity of each sentence in the answer. Break down each sentence into one or more fully understandable statements. Ensure that no pronouns are used in any statement. Format the outputs in JSON.
            """;

    private static final String OUTPUT_FORMAT_INSTRUCTION = """
            The output should be a JSON array of objects, that conforms the following schema:
            [
                {
                    "sentence_index": <index of the sentence in the answer>,
                    "simpler_statements": [<list of simpler statements>]
                },
                ...
            ]
            Ensure JSON is valid and well-structured. Provide only JSON without any additional text or symbols.
            """;

    private static final List<LongFormAnswer> LONG_FORM_ANSWER_EXAMPLES = List.of(
            new LongFormAnswer(
                    "Who was Albert Einstein and what is he best known for?",
                    "He was a German-born theoretical physicist, widely acknowledged to be one of the greatest and most influential physicists of all time. He was best known for developing the theory of relativity, he also made important contributions to the development of the theory of quantum mechanics.",
                    """
                            0:He was a German-born theoretical physicist, widely acknowledged to be one of the greatest and most influential physicists of all time.
                            1:He was best known for developing the theory of relativity, he also made important contributions to the development of the theory of quantum mechanics.
                            """,
                    List.of(
                            new LongFormAnswer.Output(
                                    0,
                                    List.of(
                                            "Albert Einstein was a German-born theoretical physicist.",
                                            "Albert Einstein is recognized as one of the greatest and most influential physicists of all time."
                                    )
                            ),
                            new LongFormAnswer.Output(
                                    1,
                                    List.of(
                                            "Albert Einstein was best known for developing the theory of relativity.",
                                            "Albert Einstein also made important contributions to the development of the theory of quantum mechanics."
                                    )
                            )
                    )
            )
    );

    @Override
    public String getTaskName() {
        return "LongFormAnswerFaithfulness";
    }

    @Override
    public String getInstruction() {
        return INSTRUCTION;
    }

    @Override
    public String getOutputFormatInstruction() {
        return OUTPUT_FORMAT_INSTRUCTION;
    }

    @Override
    public List<LongFormAnswer> getExamples() {
        return LONG_FORM_ANSWER_EXAMPLES;
    }
}
