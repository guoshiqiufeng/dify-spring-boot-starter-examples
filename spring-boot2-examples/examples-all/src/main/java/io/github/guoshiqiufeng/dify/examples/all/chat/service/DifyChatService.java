package io.github.guoshiqiufeng.dify.examples.all.chat.service;

import io.github.guoshiqiufeng.dify.chat.DifyChat;
import io.github.guoshiqiufeng.dify.chat.dto.request.AudioToTextRequest;
import io.github.guoshiqiufeng.dify.chat.dto.request.ChatMessageSendRequest;
import io.github.guoshiqiufeng.dify.chat.dto.request.TextToAudioRequest;
import io.github.guoshiqiufeng.dify.chat.dto.response.ChatMessageSendCompletionResponse;
import io.github.guoshiqiufeng.dify.chat.dto.response.ChatMessageSendResponse;

import io.github.guoshiqiufeng.dify.chat.dto.response.DifyTextVO;
import io.github.guoshiqiufeng.dify.client.core.response.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
            log.error("textToAudio error: {}", e.getMessage());
            throw new RuntimeException("textToAudio error");
        }
    }

    public DifyTextVO audioToText(AudioToTextRequest request) {
        return difyChat.audioToText(request);
    }
}
