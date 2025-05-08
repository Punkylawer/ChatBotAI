
package com.chatbot.ai;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.CreateCollectionOptions;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.mongodb.MongoDbEmbeddingStore;
import dev.langchain4j.store.embedding.mongodb.IndexMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

import static dev.langchain4j.model.openai.OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL;

@Configuration
public class ChatBotConfiguration {

    @Value("${app.mongodb.url}")
    private String mongodbUrl;

    @Value("${app.mongodb.db-name}")
    private String databaseName;

    @Value("${app.openai.apiKey}")
    private String apiKey;

    // 1️⃣ Cliente MongoDB
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongodbUrl);
    }

    // 2️⃣ Embedding Store
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(MongoClient mongoClient) {
        String collectionName = "embeddings";
        String indexName = "embedding";
        Long maxResultRatio = 10L;

        IndexMapping indexMapping = IndexMapping.builder()
                .dimension(TEXT_EMBEDDING_3_SMALL.dimension())
                .metadataFieldNames(new HashSet<>())
                .build();

        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions();

        return new MongoDbEmbeddingStore(
                mongoClient,
                databaseName,
                collectionName,
                indexName,
                maxResultRatio,
                createCollectionOptions,
                null,
                indexMapping,
                true
        );
    }

    // 3️⃣ Embedding Model
    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName(TEXT_EMBEDDING_3_SMALL)
                .build();
    }


    // 4️⃣ Chat Model
    @Bean
    public ChatModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o")
                .build();
    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(10)
                .minScore(0.8)
                .build();
    }

    /*@Bean
    public CustomRetriever customRetriever(MongoClient mongoClient,
                                           @Value("${app.mongodb.db-name}") String dbName,
                                           EmbeddingModel embeddingModel) {
        return new CustomContentRetriever(mongoClient, dbName, "embeddings", embeddingModel);
    }


    @Bean
    public ArticleBasedAssistant articleBasedAssistant(ChatModel chatModel,
                                                       CustomRetriever customRetriever) {
        return AiServices.builder(ArticleBasedAssistant.class)
                .chatModel(chatModel)
                .contentRetriever(query -> customRetriever.retrieve(query.text()))
                .build();
    }*/

    @Bean
    public ArticleBasedAssistant articleBasedAssistant(ChatModel chatModel, ContentRetriever contentRetriever) {
        return AiServices.builder(ArticleBasedAssistant.class)
                .chatModel(chatModel)
                .contentRetriever(contentRetriever)
                .build();
    }


}
