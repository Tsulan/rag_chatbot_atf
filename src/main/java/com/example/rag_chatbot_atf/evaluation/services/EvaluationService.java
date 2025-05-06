package com.example.rag_chatbot_atf.evaluation.services;

import com.azure.ai.openai.models.ChatRequestMessage;
import com.example.rag_chatbot_atf.azureai.client.AzureOpenAiClient;
import com.example.rag_chatbot_atf.evaluation.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.rag_chatbot_atf.azureai.utils.ChatUtility.*;
import static com.example.rag_chatbot_atf.evaluation.model.MetricType.*;

@Slf4j
@Service
public class EvaluationService {
    private final ObjectMapper objectMapper;
    private final EvaluationRepository evaluationRepository;
    private final AzureOpenAiClient azureOpenAiClient;
    private final MetricService metricService;

    public EvaluationService(
            final EvaluationRepository evaluationRepository,
            final AzureOpenAiClient azureOpenAiClient,
            final MetricService metricService,
            final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.evaluationRepository = evaluationRepository;
        this.azureOpenAiClient = azureOpenAiClient;
        this.metricService = metricService;
    }

    public EvaluationResult evaluate(EvaluationRequest request) {
        EvaluationResult evaluationResult = new EvaluationResult();
        evaluationResult.setQuestion(request.getQuestion());
        evaluationResult.setContextPrecision(evaluateMetric(request, CONTEXT_PRECISION));
        evaluationResult.setContextRecall(evaluateMetric(request, CONTEXT_RECALL));
        evaluationResult.setAnswerRelevance(evaluateMetric(request, ANSWER_RELEVANCE));
        evaluationResult.setFaithfulness(calculateFaithfulness(request, FAITHFULNESS));
        return evaluationRepository.save(evaluationResult);
    }

    public EvaluationResult evaluateContextPrecision(EvaluationRequest request) {
        return evaluateByMetric(request, CONTEXT_PRECISION);
    }

    public EvaluationResult evaluateContextRecall(EvaluationRequest request) {
        return evaluateByMetric(request, CONTEXT_RECALL);
    }

    public EvaluationResult evaluateAnswerRelevance(EvaluationRequest request) {
        return evaluateByMetric(request, ANSWER_RELEVANCE);
    }

    public EvaluationResult evaluateFaithfulness(EvaluationRequest request) {
        double faithfulness = calculateFaithfulness(request, FAITHFULNESS);
        EvaluationResult evaluationResult = new EvaluationResult();
        evaluationResult.setQuestion(request.getQuestion());
        evaluationResult.setFaithfulness(faithfulness);
        return evaluationRepository.save(evaluationResult);
    }

    public EvaluationResult evaluateByMetric(EvaluationRequest request, MetricType metricType) {
        double score = evaluateMetric(request, metricType);
        EvaluationResult evaluationResult = new EvaluationResult();
        evaluationResult.setQuestion(request.getQuestion());

        switch (metricType) {
            case CONTEXT_PRECISION:
                evaluationResult.setContextPrecision(score);
                break;
            case CONTEXT_RECALL:
                evaluationResult.setContextRecall(score);
                break;
            case ANSWER_RELEVANCE:
                evaluationResult.setAnswerRelevance(score);
                break;
            default:
                throw new IllegalArgumentException("Unsupported metric type: " + metricType);
        }

        return evaluationRepository.save(evaluationResult);
    }

    private double evaluateMetric(EvaluationRequest request, MetricType metricType) {
        List<ChatRequestMessage> messages = getInstructions(request, metricType);
        String jsonResponse = azureOpenAiClient.getResponse(messages);
        double score;

        switch (metricType) {
            case CONTEXT_PRECISION:
                List<ContextPrecision.Output> contextPrecisionList = parseResponse(jsonResponse, new TypeReference<>() {
                });
                score = metricService.calculateContextPrecision(contextPrecisionList);
                break;
            case CONTEXT_RECALL:
                List<ContextRecall.Output> contextRecallList = parseResponse(jsonResponse, new TypeReference<>() {
                });
                score = metricService.calculateContextRecall(contextRecallList);
                break;
            case ANSWER_RELEVANCE:
                List<AnswerRelevance.Output> answerRelevanceList = parseResponse(jsonResponse, new TypeReference<>() {
                });
                score = metricService.calculateAnswerRelevance(request.getQuestion(), answerRelevanceList);
                break;
            default:
                throw new IllegalArgumentException("Unsupported metric type: " + metricType);
        }

        logScore(metricType, score);
        return score;
    }

    private double calculateFaithfulness(EvaluationRequest request, MetricType metricType) {
        double score;
        List<ChatRequestMessage> longFormAnswerMessages = longFormAnswer(request.getQuestion(), request.getAnswer());
        String longFormAnswerResponse = azureOpenAiClient.getResponse(longFormAnswerMessages);

        List<LongFormAnswer.Output> longFormAnswerOutputs = parseResponse(longFormAnswerResponse, new TypeReference<>() {
        });
        List<String> simplerStatements = longFormAnswerOutputs.stream()
                .flatMap(output -> output.getSimplerStatements().stream())
                .collect(Collectors.toList());

        if (simplerStatements.isEmpty()) {
            log.error("Simpler statements are empty");
            return 0.0;
        }

        List<ChatRequestMessage> faithfulnessMessages = faithfulnessInstructions(request.getContexts(), simplerStatements);
        String faithfulnessResponse = azureOpenAiClient.getResponse(faithfulnessMessages);
        List<Faithfulness.Output> faithfulnessOutputList = parseResponse(faithfulnessResponse, new TypeReference<>() {
        });

        score = metricService.calculateFaithfulness(faithfulnessOutputList);
        logScore(metricType, score);
        return score;
    }

    private List<ChatRequestMessage> getInstructions(EvaluationRequest request, MetricType metricType) {
        return switch (metricType) {
            case CONTEXT_PRECISION ->
                    contextPrecisionInstructions(request.getQuestion(), request.getContexts(), request.getGroundTruth());
            case CONTEXT_RECALL -> contextRecallInstructions(request.getContexts(), request.getGroundTruth());
            case ANSWER_RELEVANCE -> answerRelevanceInstructions(request.getAnswer());
            default -> throw new IllegalArgumentException("Unsupported metric type: " + metricType);
        };
    }

    @SneakyThrows
    private <T extends MetricOutput> List<T> parseResponse(String jsonResponse, TypeReference<List<T>> typeReference) {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode outputNode = rootNode.has("output") ? rootNode.get("output") : rootNode;
        return objectMapper.convertValue(outputNode, typeReference);
    }

    private void logScore(MetricType metricType, double score) {
        log.info(metricType + " score: " + score);
    }

    public List<EvaluationResult> getAllEvaluations() {
        return evaluationRepository.findAll();
    }
}
