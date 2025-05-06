package com.example.rag_chatbot_atf.azureai.utils.system_prompts;

import com.example.rag_chatbot_atf.evaluation.model.ContextRecall;

import java.util.List;

public class ContextRecallGenerator extends BaseGenerator<ContextRecall> {

    private static final String INSTRUCTION = """
            Given a context, and an answer, analyze each sentence in the answer and classify if the sentence can be attributed to the given context or not. Use only 'Yes' (1) or 'No' (0) as a binary classification. Output json with reason.
            """;

    private static final String OUTPUT_FORMAT_INSTRUCTIONS = """
            The output should be a JSON array with objects for each of the sentences in the answer, that conforms the following schema:
            [
                {
                    "sentence": "<sentence from the answer>",
                    "reason": "<reason for the classification>",
                    "attributed": <0 or 1>
                },
                ...
            ]
            Ensure JSON is valid and well-structured. Provide only JSON without any additional text or symbols.
            """;

    private static final List<ContextRecall> CONTEXT_RECALL_EXAMPLES = List.of(
            new ContextRecall(
                    "What can you tell me about albert Albert Einstein?",
                    List.of("Albert Einstein (14 March 1879 - 18 April 1955) was a German-born theoretical physicist, widely held to be one of the greatest and most influential scientists of all time. Best known for developing the theory of relativity, he also made important contributions to quantum mechanics, and was thus a central figure in the revolutionary reshaping of the scientific understanding of nature that modern physics accomplished in the first decades of the twentieth century. His mass-energy equivalence formula E = mc2, which arises from relativity theory, has been called 'the world's most famous equation'. He received the 1921 Nobel Prize in Physics 'for his services to theoretical physics, and especially for his discovery of the law of the photoelectric effect', a pivotal step in the development of quantum theory. His work is also known for its influence on the philosophy of science. In a 1999 poll of 130 leading physicists worldwide by the British journal Physics World, Einstein was ranked the greatest physicist of all time. His intellectual achievements and originality have made Einstein synonymous with genius."),
                    "Albert Einstein born in 14 March 1879 was  German-born theoretical physicist, widely held to be one of the greatest and most influential scientists of all time. He received the 1921 Nobel Prize in Physics for his services to theoretical physics. He published 4 papers in 1905.  Einstein moved to Switzerland in 1895",
                    List.of(
                            new ContextRecall.Output(
                                    "Albert Einstein, born on 14 March 1879, was a German-born theoretical physicist, widely held to be one of the greatest and most influential scientists of all time.",
                                    "The date of birth of Einstein is mentioned clearly in the context.",
                                    1
                            ),
                            new ContextRecall.Output(
                                    "He received the 1921 Nobel Prize in Physics for his services to theoretical physics.",
                                    "The exact sentence is present in the given context.",
                                    1
                            ),
                            new ContextRecall.Output(
                                    "He published 4 papers in 1905.",
                                    "There is no mention about papers he wrote in the given context.",
                                    0
                            ),
                            new ContextRecall.Output(
                                    "Einstein moved to Switzerland in 1895.",
                                    "There is no supporting evidence for this in the given context.",
                                    0
                            )
                    )
            ),
            new ContextRecall(
                    "What is the primary fuel for the Sun?",
                    List.of("NULL"),
                    "HYDROGEN",
                    List.of(
                            new ContextRecall.Output(
                                    "The Sun's primary fuel is hydrogen.",
                                    "The context contains no information",
                                    0
                            )
                    )
            )
    );

    String highContextRecall = "France, in Western Europe, encompasses medieval cities, alpine villages and Mediterranean beaches. Its capital city is Paris, is famed for its fashion houses, classical art museums including the Louvre and monuments like the Eiffel Tower.";

    String lowContextRecall = "France, in Western Europe, encompasses medieval cities, alpine villages and Mediterranean beaches. The country is also renowned for its wines and sophisticated cuisine. Lascaux's ancient cave drawings, Lyon's Roman theater, and the Palace of Versailles attest to its rich history.";

    @Override
    public String getTaskName() {
        return "Context Recall";
    }

    @Override
    public String getInstruction() {
        return INSTRUCTION;
    }

    @Override
    public String getOutputFormatInstruction() {
        return OUTPUT_FORMAT_INSTRUCTIONS;
    }

    @Override
    public List<ContextRecall> getExamples() {
        return CONTEXT_RECALL_EXAMPLES;
    }
}
