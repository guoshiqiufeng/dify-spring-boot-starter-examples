package io.github.guoshiqiufeng.dify.examples.all.chat.controller;//package com.ltzk.ai.magic.cube.api.v1.agent;


import io.github.guoshiqiufeng.dify.chat.dto.request.ChatMessageSendRequest;
import io.github.guoshiqiufeng.dify.chat.dto.request.TextToAudioRequest;
import io.github.guoshiqiufeng.dify.chat.dto.response.ChatMessageSendCompletionResponse;
import io.github.guoshiqiufeng.dify.chat.dto.response.ChatMessageSendResponse;
import io.github.guoshiqiufeng.dify.examples.all.chat.service.DifyChatService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


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

    @GetMapping("/textToAudio")
    public String textToAudio(HttpServletResponse response) {
        TextToAudioRequest request = new TextToAudioRequest();
        request.setApiKey("app-SiCHU6I8RsWBODLJ1IpL8lsh");
        request.setText("hello world");
        difyChatService.textToAudio(response, request);
        return "ok";
    }
}
