# RAG Chatbot Automated Evaluation Framework

A Spring Boot-based framework for evaluating Retrieval-Augmented Generation (RAG) chatbots using Azure OpenAI services.

## Project Overview

This project implements an automated evaluation framework for RAG chatbots, leveraging Azure OpenAI's GPT models and embedding capabilities. It provides a structured way to assess chatbot performance through various metrics and evaluation scenarios.

## Technical Stack

- **Java Version**: 17
- **Spring Boot**: 3.4.5
- **Key Dependencies**:
  - Spring Boot Starter Web
  - Azure AI OpenAI (1.0.0-beta.16)
  - Lombok

## Key Project Structure

```
src/main/java/com/example/rag_chatbot_atf/
├── RagChatbotAtfApplication.java
├── azureai/
│   ├── client/
│   │   └── AzureOpenAIClient.java
│   ├── config/
│   │   └── AzureOpenAIConfig.java
│   └── utils/
│       └── AzureOpenAIUtils.java
└── evaluation/
    ├── controllers/
    │   └── EvaluationController.java
    ├── model/
    │   ├── EvaluationRequest.java
    │   └── EvaluationResponse.java
    └── services/
        └── EvaluationService.java

src/main/resources/
├── application.properties
└── datasets/
```

## Architecture

### Core Modules

1. **Azure AI Integration (`azureai/`)**
   - Client: Handles communication with Azure OpenAI API
   - Config: Manages Azure OpenAI configuration
   - Utils: Provides utility functions for Azure OpenAI operations

2. **Evaluation Framework (`evaluation/`)**
   - Controllers: REST endpoints for evaluation requests
   - Model: Data transfer objects for evaluation
   - Services: Business logic for evaluation metrics

### Data Flow

1. HTTP Request → EvaluationController
2. Controller → EvaluationService
3. Service → AzureOpenAIClient
4. Azure OpenAI API Processing
5. Response Flow Back Through Layers

## Architecture Patterns

1. **Layered Architecture**
   - Presentation Layer (Controllers)
   - Service Layer (Business Logic)
   - Data Access Layer (Azure Client)

2. **DTO Pattern**
   - EvaluationRequest
   - EvaluationResponse

3. **Configuration Management**
   - Externalized configuration via application.properties
   - Environment variable for sensitive data

## API Endpoints

The framework exposes REST endpoints for:
- Chatbot evaluation
- Performance metrics separately

## Getting Started

1. Set up environment variables:
   ```bash
   export AZURE_AI_API_KEY=your_api_key
   ```

2. Configure application.properties with your Azure OpenAI endpoint

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Evaluation Metrics

The framework implements several key metrics to evaluate RAG chatbot performance:

### 1. Context Precision
Measures how precisely the chatbot uses the provided context to generate answers. This metric evaluates:
- The relevance of selected context segments
- The accuracy of context utilization
- The proportion of relevant information used from the context

### 2. Context Recall
Assesses how well the chatbot retrieves and incorporates all relevant information from the available context. This metric evaluates:
- The completeness of context coverage
- The ability to identify and use all relevant context segments
- The comprehensiveness of the answer in relation to available information

### 3. Answer Relevance
Evaluates how well the chatbot's response addresses the user's question. This metric considers:
- Direct relevance to the question asked
- Completeness of the answer
- Appropriateness of the response format

### 4. Faithfulness
Measures the accuracy and truthfulness of the chatbot's responses in relation to the provided context. This metric evaluates:
- Factual consistency with the context
- Absence of hallucinations or fabricated information
- Accuracy of information presented

Each metric produces:
- A numerical verdict (score)
- Detailed reasoning for the evaluation
- Specific feedback on areas of strength and improvement

### Metric Output Format
All metrics follow a consistent input structure:
```json
{
    "question": "User's question",
    "context": "Provided context",
    "answer": "Chatbot's response",
    "groundTruth": "Expected answer"
}
```

And output structure:
```json
{
  "userQuestion": "User's question",
  "score": 0.85
}
```