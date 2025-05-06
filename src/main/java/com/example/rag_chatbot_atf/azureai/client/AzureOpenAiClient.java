package com.example.rag_chatbot_atf.azureai.client;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.*;
import com.azure.core.util.IterableStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AzureOpenAiClient {

    @Value("${azure.openai.max.output.tokens}")
    private Integer maxOutputTokens;

    @Value("${azure.openai.llm.model}")
    private String llmModel;

    @Value("${azure.openai.embedding.model}")
    private String embeddingModel;

    @Value("${azure.openai.llm.temperature}")
    private Double temperature;

    @Value("${azure.openai.embedding.dimensions}")
    private Integer vectorSearchDimension;

    private final OpenAIClient openAIClient;

    public IterableStream<ChatCompletions> createChatCompletions(final List<ChatRequestMessage> messages) {
        ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
                .setMaxTokens(maxOutputTokens)
                .setTemperature(temperature)
                .setSeed(42L);

        return openAIClient.getChatCompletionsStream(llmModel, options);
    }

    public String getResponse(final List<ChatRequestMessage> messagesArray) {
        log.info("\nRequest to LLM:\n" + formatMessages(messagesArray));
        StringBuilder response = new StringBuilder();
        IterableStream<ChatCompletions> completions = createChatCompletions(messagesArray);
        for (ChatCompletions chatCompletions : completions) {
            List<ChatChoice> choices = chatCompletions.getChoices();
            if (choices != null && !choices.isEmpty()) {
                String completionResult = choices.get(0).getDelta().getContent();
                if (Strings.isNotEmpty(completionResult)) {
                    response.append(completionResult);
                }
            } else {
                log.info("Waiting for response...");
            }
        }
        log.info("\nResponse from LLM:\n" + response);
        return response.toString();
    }

    public List<Float> getEmbeddings(String inputText) {
        List<String> inputList = List.of(inputText);
        EmbeddingsOptions embeddingsOptions = new EmbeddingsOptions(inputList);
        embeddingsOptions.setUser("rag-chatbot");
        embeddingsOptions.setModel(embeddingModel);
        embeddingsOptions.setInputType("text");
        embeddingsOptions.setDimensions(vectorSearchDimension);

        try {
            Embeddings embeddingResult = openAIClient.getEmbeddings(embeddingModel, embeddingsOptions);
            List<Float> embeddings = embeddingResult.getData().get(0).getEmbedding();
            log.info("Generated embeddings for input: {}", inputText);
            return embeddings;
        } catch (Exception e) {
            log.error("Error generating embeddings for input: {}", inputText, e);
            throw new RuntimeException("Failed to generate embeddings", e);
        }
    }

    public String formatMessages(List<ChatRequestMessage> messagesArray) {
        StringBuilder formattedMessages = new StringBuilder();

        for (ChatRequestMessage message : messagesArray) {
            String role;
            String content;

            if (message instanceof ChatRequestSystemMessage systemMessage) {
                role = "System";
                content = systemMessage.getContent().toString();
            } else if (message instanceof ChatRequestUserMessage userMessage) {
                role = "User";
                content = userMessage.getContent().toString();
            } else {
                continue;
            }
            formattedMessages.append("ChatMessage ")
                    .append("Role: ")
                    .append(role)
                    .append("\nContent:\n")
                    .append(content)
                    .append("\n");
        }
        return formattedMessages.toString();
    }
}
