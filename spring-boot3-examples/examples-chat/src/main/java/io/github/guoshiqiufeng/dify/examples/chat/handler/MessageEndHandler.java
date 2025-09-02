package io.github.guoshiqiufeng.dify.examples.chat.handler;

import io.github.guoshiqiufeng.dify.chat.pipeline.ChatMessagePipelineModel;
import io.github.guoshiqiufeng.dify.core.pipeline.PipelineContext;
import io.github.guoshiqiufeng.dify.core.pipeline.PipelineProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/8/26 13:28
 */
@Slf4j
@Component
public class MessageEndHandler implements PipelineProcess<ChatMessagePipelineModel> {

    @Override
    public boolean support(PipelineContext<ChatMessagePipelineModel> context) {
        return "message_end".equals(context.getModel().getEvent());
    }

    @Override
    public void process(PipelineContext<ChatMessagePipelineModel> context) {
        log.info("MessageEndHandler");
    }
}
