package io.github.guoshiqiufeng.dify.examples.all.chat.service;

import io.github.guoshiqiufeng.dify.chat.DifyChat;
import io.github.guoshiqiufeng.dify.chat.dto.request.ChatMessageSendRequest;
import io.github.guoshiqiufeng.dify.chat.dto.request.MessageConversationsRequest;
import io.github.guoshiqiufeng.dify.chat.dto.response.ChatMessageSendCompletionResponse;
import io.github.guoshiqiufeng.dify.chat.dto.response.ChatMessageSendResponse;
import io.github.guoshiqiufeng.dify.chat.dto.response.MessageConversationsResponse;
import io.github.guoshiqiufeng.dify.core.pojo.DifyPageResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/3/25 10:48
 */
@Slf4j
@Service
public class DifyChatService {

    @Resource
    private DifyChat difyChat;

    public Flux<ChatMessageSendCompletionResponse> sendChatMessageStream(ChatMessageSendRequest sendRequest) {
        // 可以进行自定义逻辑处理：参数转换、权限校验等
        return difyChat.sendChatMessageStream(sendRequest);
    }

    public void stopMessagesStream (String apiKey, String taskId, String userId) {
        difyChat.stopMessagesStream(apiKey, taskId, userId);
    }

    public DifyPageResult<MessageConversationsResponse> conversations(MessageConversationsRequest request) {
        return difyChat.conversations(request);
    }

    public void delete(String conversationId, String apiKey, String userId) {
        difyChat.deleteConversation(conversationId, apiKey, userId);
    }
}
