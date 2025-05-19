package io.github.guoshiqiufeng.dify.examples.all.chat.controller;//package com.ltzk.ai.magic.cube.api.v1.agent;


import io.github.guoshiqiufeng.dify.chat.dto.request.ChatMessageSendRequest;
import io.github.guoshiqiufeng.dify.chat.dto.request.MessageConversationsRequest;
import io.github.guoshiqiufeng.dify.chat.dto.response.ChatMessageSendCompletionResponse;
import io.github.guoshiqiufeng.dify.chat.dto.response.MessageConversationsResponse;
import io.github.guoshiqiufeng.dify.core.pojo.DifyPageResult;
import io.github.guoshiqiufeng.dify.examples.all.chat.service.DifyChatService;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


/**
 * @author yanghq
 * @version 1.0
 * @since 2025/3/25 10:48
 */
@RestController
@RequestMapping("/v1/chat")
public class V1ChatController {

    @Resource
    private DifyChatService difyChatService;

    /**
     * 发送消息（流式）
     *
     * @param sendRequest 消息参数 （可以自定义参数，调用 difyChat 实例时重新组装即可），
     *                    用户 id可以改为从上下文（token）获取，
     *                    apikey 建议在数据库进行存储，前端调用时传智能体 id，从数据库查询
     */
    @PostMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatMessageSendCompletionResponse> sendChatMessageStream(@RequestBody ChatMessageSendRequest sendRequest) {
        return difyChatService.sendChatMessageStream(sendRequest);
    }

    @PostMapping("/stop/{taskId}")
    public String stopMessagesStream(@PathVariable String taskId) {
        difyChatService.stopMessagesStream("app-OTcT9pIoM9rpjbPIHmOn1dLP",taskId,"test-12475");
        return "ok";
    }


    @GetMapping("/conversations")
    public DifyPageResult<MessageConversationsResponse> conversations(MessageConversationsRequest request) {
        request.setApiKey("app-OTcT9pIoM9rpjbPIHmOn1dLP");
        request.setUserId("test-12475");
        return difyChatService.conversations(request);
    }

    @DeleteMapping("/{conversationId}")
    public String deleteConversation(@PathVariable String conversationId) {
        difyChatService.delete(conversationId, "app-OTcT9pIoM9rpjbPIHmOn1dLP", "test-12475");
        return "ok";
    }
}
