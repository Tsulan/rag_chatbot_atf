package com.example.rag_chatbot_atf.azureai.utils;

import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.example.rag_chatbot_atf.azureai.utils.system_prompts.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor()
public class ChatUtility {

    public static List<ChatRequestMessage> contextRecallInstructions(final List<String> contexts, final String groundTruth) {
        ContextRecallGenerator generator = new ContextRecallGenerator();
        return buildMessages(
                generator.generateSystemPrompt(),
                "Here is the context:\n" + contexts + "\n\n" +
                        "Here is the answer:\n" + groundTruth + "\n\n"
        );
    }

    public static List<ChatRequestMessage> contextPrecisionInstructions(final String question, final List<String> contexts, final String groundTruth) {
        ContextPrecisionGenerator generator = new ContextPrecisionGenerator();
        StringBuilder contextBuilder = new StringBuilder();
        for (int i = 0; i < contexts.size(); i++) {
            contextBuilder.append("Context ").append(i + 1).append(":\n")
                    .append(contexts.get(i)).append("\n\n");
        }
        return buildMessages(
                generator.generateSystemPrompt(),
                "Here is the question:\n" + question + "\n\n" +
                        "Here is the list of contexts:\n" + contextBuilder +
                        "Here is the answer:\n" + groundTruth + "\n\n"
        );
    }

    public static List<ChatRequestMessage> faithfulnessInstructions(final List<String> context, final List<String> statements) {
        FaithfulnessGenerator generator = new FaithfulnessGenerator();
        StringBuilder statementsBuilder = new StringBuilder();
        for (int i = 0; i < statements.size(); i++) {
            statementsBuilder.append("Statement: ").append(i + 1).append(":\n")
                    .append(statements.get(i)).append("\n\n");
        }

        return buildMessages(
                generator.generateSystemPrompt(),
                "Here is the context:\n" + context +
                        "Here is/are the statement/s:\n" + statementsBuilder
        );
    }

    public static List<ChatRequestMessage> longFormAnswer(final String question, final String answer) {
        LongFormAnswerFaithfulnessGenerator generator = new LongFormAnswerFaithfulnessGenerator();
        return buildMessages(
                generator.generateSystemPrompt(),
                "Here is the question:\n" + question + "\n\n" +
                        "Here is the answer:\n" + answer + "\n\n"
        );
    }

    public static List<ChatRequestMessage> answerRelevanceInstructions(final String answer) {
        AnswerRelevanceGenerator generator = new AnswerRelevanceGenerator();
        return buildMessages(
                generator.generateSystemPrompt(),
                "Here is the answer:\n" + answer + "\n\n"
        );
    }

    private static List<ChatRequestMessage> buildMessages(final String systemPrompt, final String dataSetContent) {
        List<ChatRequestMessage> messages = new ArrayList<>();
        messages.add(new ChatRequestSystemMessage(systemPrompt));
        messages.add(new ChatRequestUserMessage(dataSetContent));
        return messages;
    }
}
