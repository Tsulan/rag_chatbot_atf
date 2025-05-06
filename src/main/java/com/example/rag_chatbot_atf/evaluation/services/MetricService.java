package com.example.rag_chatbot_atf.evaluation.services;

import com.example.rag_chatbot_atf.azureai.client.AzureOpenAiClient;
import com.example.rag_chatbot_atf.evaluation.model.AnswerRelevance;
import com.example.rag_chatbot_atf.evaluation.model.ContextPrecision;
import com.example.rag_chatbot_atf.evaluation.model.ContextRecall;
import com.example.rag_chatbot_atf.evaluation.model.Faithfulness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MetricService {

    private final AzureOpenAiClient azureOpenAiClient;

    public MetricService(final AzureOpenAiClient azureOpenAiClient) {
        this.azureOpenAiClient = azureOpenAiClient;
    }

    public double calculateContextRecall(List<ContextRecall.Output> statements) {
        if (statements == null || statements.isEmpty()) {
            throw new IllegalArgumentException("Statements list cannot be null or empty");
        }

        long relevantStatements = statements.stream()
                .filter(answer -> answer.getAttributed() == 1)
                .count();

        return (double) relevantStatements / statements.size();
    }

    public double calculateContextPrecision(List<ContextPrecision.Output> verdicts) {
        if (verdicts == null || verdicts.isEmpty()) {
            throw new IllegalArgumentException("Verdicts list cannot be null or empty");
        }

        List<Integer> verdictList = verdicts.stream()
                .map(ver -> ver.getVerdict() == 1 ? 1 : 0)
                .toList();

        double denominator = verdictList.stream()
                .mapToInt(Integer::intValue)
                .sum();

        if (denominator == 0) {
            return 0.0;
        }

        double numerator = 0.0;
        double cumulativeSum = 0.0;

        for (int i = 0; i < verdictList.size(); i++) {
            cumulativeSum += verdictList.get(i);
            numerator += (cumulativeSum / (i + 1)) * verdictList.get(i);
        }

        return numerator / denominator;
    }

    public double calculateFaithfulness(List<Faithfulness.Output> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException("Answers list cannot be null or empty");
        }

        long faithfulVerdicts = answers.stream()
                .filter(answer -> answer.getVerdict() == 1)
                .count();

        return (double) faithfulVerdicts / answers.size();
    }

    public double calculateAnswerRelevance(String originalQuestion, List<AnswerRelevance.Output> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("Questions list cannot be null or empty");
        }

        List<Float> questionEmbedding = Optional.ofNullable(azureOpenAiClient.getEmbeddings(originalQuestion))
                .orElseThrow(() -> new RuntimeException("Failed to get embeddings for the original question"));

        double totalScore = 0.0;
        int validQuestionsCount = 0;

        for (AnswerRelevance.Output value : questions) {
            List<Float> generateQuestionEmbedding = Optional.ofNullable(azureOpenAiClient.getEmbeddings(value.getQuestion()))
                    .orElseThrow(() -> new RuntimeException("Failed to get embeddings for the generated question: " + value.getQuestion()));

            boolean isNoncommittal = value.getNoncommittal() == 1;
            double cosineSimilarity = calculateCosineSimilarity(questionEmbedding, generateQuestionEmbedding);

            log.debug("Cosine similarity for question '{}': {}", value.getQuestion(), cosineSimilarity);

            if (!isNoncommittal) {
                totalScore += cosineSimilarity;
                validQuestionsCount++;
            }
        }

        if (validQuestionsCount == 0) {
            throw new IllegalStateException("No valid questions found to calculate answer relevance score");
        }

        return totalScore / validQuestionsCount;
    }

    public double calculateCosineSimilarity(List<Float> vectorA, List<Float> vectorB) {
        if (vectorA == null || vectorB == null || vectorA.size() != vectorB.size()) {
            throw new IllegalArgumentException("Vectors must be non-null and of the same size");
        }

        double dotProduct = 0.0;
        double magnitudeA = 0.0;
        double magnitudeB = 0.0;

        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            magnitudeA += Math.pow(vectorA.get(i), 2);
            magnitudeB += Math.pow(vectorB.get(i), 2);
        }

        double denominator = Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB);

        if (denominator == 0) {
            throw new ArithmeticException("Cosine similarity denominator is zero, cannot divide by zero");
        }

        return dotProduct / denominator;
    }
}
