/*
 * Copyright (c) 2025-2026, fubluesky (fubluesky@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.guoshiqiufeng.java.examples;

import io.github.guoshiqiufeng.dify.chat.DifyChat;
import io.github.guoshiqiufeng.dify.chat.dto.request.*;
import io.github.guoshiqiufeng.dify.chat.dto.response.*;
import io.github.guoshiqiufeng.dify.chat.dto.response.parameter.Enabled;
import io.github.guoshiqiufeng.dify.chat.enums.AnnotationReplyActionEnum;
import io.github.guoshiqiufeng.dify.client.codec.jackson.JacksonJsonMapper;
import io.github.guoshiqiufeng.dify.client.core.http.HttpClientFactory;
import io.github.guoshiqiufeng.dify.client.integration.okhttp.http.JavaHttpClientFactory;
import io.github.guoshiqiufeng.dify.core.config.DifyProperties;
import io.github.guoshiqiufeng.dify.core.pojo.DifyPageResult;
import io.github.guoshiqiufeng.dify.core.pojo.response.MessagesResponseVO;
import io.github.guoshiqiufeng.dify.dataset.DifyDataset;
import io.github.guoshiqiufeng.dify.dataset.dto.response.TextEmbeddingListResponse;
import io.github.guoshiqiufeng.dify.dataset.dto.response.textembedding.TextEmbedding;
import io.github.guoshiqiufeng.dify.server.DifyServer;
import io.github.guoshiqiufeng.dify.server.dto.response.ApiKeyResponse;
import io.github.guoshiqiufeng.dify.server.dto.response.AppsResponse;
import io.github.guoshiqiufeng.dify.server.dto.response.DatasetApiKeyResponse;
import io.github.guoshiqiufeng.dify.support.impl.builder.DifyChatBuilder;
import io.github.guoshiqiufeng.dify.support.impl.builder.DifyDatasetBuilder;
import io.github.guoshiqiufeng.dify.support.impl.builder.DifyServerBuilder;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

/**
 * Chat API integration tests using builder pattern
 *
 * @author yanghq
 * @version 1.0
 * @since 2026/1/12 14:00
 */
public class ChatTest {

    private static DifyChat difyChat;
    private static DifyServer difyServer;
    private static DifyDataset difyDataset;
    private static String baseUrl = "http://localhost";
    private static String serverEmail = "admin@admin.com";
    private static String serverPassword = "admin123456";

    private static final String userId = "1504";
    private static String conversationId;
    private static String messageId;
    private static String taskId;
    private static String annotationId;
    private static String replyJobId;
    private static String apiKey;

    private static Boolean suggestedEnable;

    public static void setUp() {
        // Initialize HTTP client factory
        JavaHttpClientFactory httpClientFactory = new JavaHttpClientFactory(new JacksonJsonMapper());

        // Initialize DifyServer to get API key
        difyServer = DifyServerBuilder.create(DifyServerBuilder.builder()
                .baseUrl(baseUrl)
                .serverProperties(new DifyProperties.Server(serverEmail, serverPassword))
                .httpClientFactory(httpClientFactory)
                .build());

        // Get chat app API key
        List<AppsResponse> apps = difyServer.apps("chat", "");
        if (!apps.isEmpty()) {
            List<ApiKeyResponse> appApiKey = difyServer.getAppApiKey(apps.get(0).getId());
            if (appApiKey.isEmpty()) {
                appApiKey = difyServer.initAppApiKey(apps.get(0).getId());
            }
            apiKey = appApiKey.get(0).getToken();
        }

        // Initialize DifyChat
        difyChat = DifyChatBuilder.create(DifyChatBuilder.builder()
                .baseUrl(baseUrl)
                .httpClientFactory(httpClientFactory)
                .build());
    }

    public void testChatSend() {
        ChatMessageSendRequest chatMessageSendRequest = new ChatMessageSendRequest();
        chatMessageSendRequest.setContent("hi");
        chatMessageSendRequest.setApiKey(apiKey);
        chatMessageSendRequest.setUserId(userId);
        ChatMessageSendResponse sendResponse = difyChat.send(chatMessageSendRequest);
        assertNotNull(sendResponse);

        // Store conversation and message IDs for subsequent tests
        if (sendResponse.getConversationId() != null) {
            conversationId = sendResponse.getConversationId();
        }
        if (sendResponse.getId() != null) {
            messageId = sendResponse.getId();
        }
    }

    public void testChatMessageStream() {
        ChatMessageSendRequest chatMessageSendRequest = new ChatMessageSendRequest();
        chatMessageSendRequest.setContent("tell me more");
        chatMessageSendRequest.setApiKey(apiKey);
        chatMessageSendRequest.setUserId(userId);
        chatMessageSendRequest.setConversationId(conversationId);

        Flux<ChatMessageSendCompletionResponse> responseFlux = difyChat.sendChatMessageStream(chatMessageSendRequest);
    }

    public void testStopMessagesStream() {
        // This test might not actually verify the behavior due to the nature of streaming
        difyChat.stopMessagesStream(apiKey, taskId, userId);
    }

    public void testMessageFeedback() {
        MessageFeedbackRequest request = new MessageFeedbackRequest();
        request.setApiKey(apiKey);
        request.setUserId(userId);
        request.setMessageId(messageId);
        request.setRating(MessageFeedbackRequest.Rating.LIKE);

        MessageFeedbackResponse response = difyChat.messageFeedback(request);
        assertNotNull(response);
    }

    public void testFeedbacks() {
        AppFeedbackPageRequest request = new AppFeedbackPageRequest();
        request.setApiKey(apiKey);

        DifyPageResult<AppFeedbackResponse> response = difyChat.feedbacks(request);
        assertNotNull(response);
        assertNotNull(response.getData());
        assertNotNull(response.getData().get(0));
    }

    public void testGetConversations() {
        MessageConversationsRequest request = new MessageConversationsRequest();
        request.setApiKey(apiKey);
        request.setUserId(userId);
        request.setLastId(null);
        request.setLimit(10);

        DifyPageResult<MessageConversationsResponse> result = difyChat.conversations(request);
        assertNotNull(result);
        assertFalse(result.getData().isEmpty(), "Should have at least one conversation");
    }

    public void testGetMessages() {
        MessagesRequest request = new MessagesRequest();
        request.setApiKey(apiKey);
        request.setUserId(userId);
        request.setConversationId(conversationId);
        request.setFirstId(null);
        request.setLimit(10);

        DifyPageResult<MessagesResponseVO> result = difyChat.messages(request);
        assertNotNull(result);
        assertFalse(result.getData().isEmpty(), "Should have at least one message");
    }

    public void testRenameConversation() {
        RenameConversationRequest request = new RenameConversationRequest();
        request.setApiKey(apiKey);
        request.setUserId(userId);
        request.setConversationId(conversationId);
        request.setName("Renamed Test Conversation");

        MessageConversationsResponse response = difyChat.renameConversation(request);
        assertNotNull(response);
    }

    public void testGetParameters() {
        AppParametersResponseVO response = difyChat.parameters(apiKey);
        assertNotNull(response);
        suggestedEnable = Optional.ofNullable(response.getSuggestedQuestionsAfterAnswer())
                .orElse(new Enabled(false))
                .getEnabled();
    }

    public void testGetSuggestedMessages() {
        if (!suggestedEnable) {
            return;
        }
        List<String> suggestions = difyChat.messagesSuggested(messageId, apiKey, userId);
        assertNotNull(suggestions);
    }

    public void testGetSite() {
        AppSiteResponse site = difyChat.site(apiKey);
        assertNotNull(site);
    }

    public void testConversationVariables() {
        ConversationVariableRequest conversationVariableRequest = new ConversationVariableRequest();
        conversationVariableRequest.setConversationId(conversationId);
        conversationVariableRequest.setApiKey(apiKey);
        conversationVariableRequest.setUserId(userId);
        DifyPageResult<ConversationVariableResponse> response =
                difyChat.conversationVariables(conversationVariableRequest);
        assertNotNull(response, "Conversation variables response should not be null");
    }

    public void testDeleteConversation() {
        difyChat.deleteConversation(conversationId, apiKey, userId);
    }

    public void testInfo() {
        AppInfoResponse info = difyChat.info(apiKey);
        assertNotNull(info);
    }

    public void testMeta() {
        AppMetaResponse meta = difyChat.meta(apiKey);
        assertNotNull(meta, "Meta response should not be null");
    }

    public void testCreateAppAnnotation() {
        AppAnnotationCreateRequest request = new AppAnnotationCreateRequest();
        request.setApiKey(apiKey);
        request.setUserId(userId);
        request.setQuestion("Test annotation question");
        request.setAnswer("Test annotation answer");

        AppAnnotationResponse response = difyChat.createAppAnnotation(request);
        assertNotNull(response);
        assertNotNull(response.getId());
        annotationId = response.getId();
    }

    public void testPageAppAnnotation() {
        AppAnnotationPageRequest request = new AppAnnotationPageRequest();
        request.setApiKey(apiKey);
        request.setUserId(userId);
        request.setPage(1);
        request.setLimit(10);

        DifyPageResult<AppAnnotationResponse> result = difyChat.pageAppAnnotation(request);
        assertNotNull(result);
        assertFalse(result.getData().isEmpty(), "Should have at least one annotation");
    }

    public void testUpdateAppAnnotation() {
        AppAnnotationUpdateRequest request = new AppAnnotationUpdateRequest();
        request.setApiKey(apiKey);
        request.setUserId(userId);
        request.setAnnotationId(annotationId);
        request.setQuestion("Updated annotation question");
        request.setAnswer("Updated annotation answer");

        AppAnnotationResponse response = difyChat.updateAppAnnotation(request);
        assertNotNull(response);
    }

    public void testDeleteAppAnnotation() {
        difyChat.deleteAppAnnotation(annotationId, apiKey);
    }

    public void testAnnotationReply() {
        List<DatasetApiKeyResponse> datasetApiKeys = difyServer.getDatasetApiKey();
        if (datasetApiKeys == null || datasetApiKeys.isEmpty()) {
            datasetApiKeys = difyServer.initDatasetApiKey();
        }
        String token = datasetApiKeys.get(0).getToken();

        HttpClientFactory httpClientFactory = new JavaHttpClientFactory(new JacksonJsonMapper());

        difyDataset = DifyDatasetBuilder.create(DifyDatasetBuilder.builder()
                .baseUrl(baseUrl)
                .apiKey(token)
                .httpClientFactory(httpClientFactory)
                .build());

        TextEmbeddingListResponse textEmbeddingListResponse = difyDataset.listTextEmbedding();
        assertNotNull(textEmbeddingListResponse);
        TextEmbedding textEmbedding = textEmbeddingListResponse.getData().get(0);

        // Test the annotation reply
        AppAnnotationReplyRequest request = new AppAnnotationReplyRequest();
        request.setApiKey(apiKey);
        request.setUserId(userId);
        request.setAction(AnnotationReplyActionEnum.enable);
        request.setEmbeddingProviderName(textEmbedding.getProvider());
        request.setEmbeddingModelName(textEmbedding.getModels().get(0).getModel());
        request.setScoreThreshold(0.8f);

        AppAnnotationReplyResponse response = difyChat.annotationReply(request);
        assertNotNull(response);
        assertNotNull(response.getJobId());
        replyJobId = response.getJobId();
    }

    public void testQueryAnnotationReply() {
        if (replyJobId == null) {
            return;
        }

        AppAnnotationReplyQueryRequest request = new AppAnnotationReplyQueryRequest();
        request.setApiKey(apiKey);
        request.setUserId(userId);
        request.setAction(AnnotationReplyActionEnum.enable);
        request.setJobId(replyJobId);

        AppAnnotationReplyResponse response = difyChat.queryAnnotationReply(request);
        assertNotNull(response);
        assertNotNull(response.getJobStatus());
    }

    public static void main(String[] args) {
        ChatTest chatTest = new ChatTest();
        setUp();
        chatTest.testChatSend();
        chatTest.testChatMessageStream();
        chatTest.testStopMessagesStream();
        chatTest.testMessageFeedback();
        chatTest.testFeedbacks();
        chatTest.testGetConversations();
        chatTest.testGetMessages();
        chatTest.testRenameConversation();
        chatTest.testGetParameters();
        chatTest.testGetSuggestedMessages();
        chatTest.testGetSite();
        chatTest.testConversationVariables();
        chatTest.testDeleteConversation();
        chatTest.testInfo();
        chatTest.testMeta();
        chatTest.testCreateAppAnnotation();
        chatTest.testPageAppAnnotation();
        chatTest.testUpdateAppAnnotation();
        chatTest.testDeleteAppAnnotation();
        chatTest.testAnnotationReply();
        chatTest.testQueryAnnotationReply();
    }

    private static void assertNotNull(Object value) {
        assertNotNull(value, "Expected value to be non-null");
    }

    private static void assertNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalStateException(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new IllegalStateException(message);
        }
    }

}
