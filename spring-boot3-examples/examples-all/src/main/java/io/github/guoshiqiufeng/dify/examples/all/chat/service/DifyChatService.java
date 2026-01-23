package io.github.guoshiqiufeng.dify.examples.all.chat.service;

import io.github.guoshiqiufeng.dify.chat.DifyChat;
import io.github.guoshiqiufeng.dify.chat.dto.request.*;
import io.github.guoshiqiufeng.dify.chat.dto.response.ChatMessageSendCompletionResponse;
import io.github.guoshiqiufeng.dify.chat.dto.response.DifyTextVO;
import io.github.guoshiqiufeng.dify.chat.dto.response.FileUploadResponse;
import io.github.guoshiqiufeng.dify.chat.dto.response.MessageConversationsResponse;
import io.github.guoshiqiufeng.dify.client.core.response.ResponseEntity;
import io.github.guoshiqiufeng.dify.core.pojo.DifyPageResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.net.http.HttpResponse;

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

//    public Flux<ChatMessageSendCompletionResponse> sendChatMessageStream2(ChatMessageSendRequest sendRequest) {
//        DifyChat difyChatTmp = DifyChatBuilder.create(
//                DifyChatBuilder.DifyChatClientBuilder
//                        .builder()
//                        .baseUrl("http://localhost:80")
//                        .clientConfig(new DifyProperties.ClientConfig())
//                        .build());
//        // 可以进行自定义逻辑处理：参数转换、权限校验等
//        return difyChatTmp.sendChatMessageStream(sendRequest);
//    }

    public void stopMessagesStream(String apiKey, String taskId, String userId) {
        difyChat.stopMessagesStream(apiKey, taskId, userId);
    }

    public DifyPageResult<MessageConversationsResponse> conversations(MessageConversationsRequest request) {
        return difyChat.conversations(request);
    }

    public void delete(String conversationId, String apiKey, String userId) {
        difyChat.deleteConversation(conversationId, apiKey, userId);
    }

    public FileUploadResponse fileUpload(FileUploadRequest request) {
        return difyChat.fileUpload(request);
    }

    public void textToAudio(HttpServletResponse response, TextToAudioRequest request) {
        try {
            ResponseEntity<byte[]> responseEntity = difyChat.textToAudio(request);

            String type = responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
            response.setContentType(type != null ? type : "audio/mpeg");

            String contentDisposition = responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
            if (contentDisposition != null) {
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
            } else {
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=audio.mp3");
            }

            if (responseEntity.getBody() != null) {
                response.getOutputStream().write(responseEntity.getBody());
                response.getOutputStream().flush();
            }

        } catch (Exception e) {
            log.error("textToAudio error: {}", e.getMessage(), e);
            throw new RuntimeException("textToAudio error");
        }
    }

    public DifyTextVO audioToText(AudioToTextRequest request) {
        return difyChat.audioToText(request);
    }
}
