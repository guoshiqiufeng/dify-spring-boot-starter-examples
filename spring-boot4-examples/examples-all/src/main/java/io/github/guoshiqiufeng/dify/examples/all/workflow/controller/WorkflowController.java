package io.github.guoshiqiufeng.dify.examples.all.workflow.controller;

import io.github.guoshiqiufeng.dify.workflow.DifyWorkflow;
import io.github.guoshiqiufeng.dify.workflow.dto.request.WorkflowRunRequest;
import io.github.guoshiqiufeng.dify.workflow.dto.response.WorkflowRunStreamResponse;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/5/21 16:41
 */
@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    @Resource
    private DifyWorkflow difyWorkflow;

    @PostMapping(value = "/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<WorkflowRunStreamResponse> runWorkflowStream(@RequestBody WorkflowRunRequest req) {
        return difyWorkflow.runWorkflowStream(req);
    }
}
