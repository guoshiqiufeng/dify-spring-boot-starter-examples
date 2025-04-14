package io.github.guoshiqiufeng.dify.examples.workflow;

import com.alibaba.fastjson2.JSON;
import io.github.guoshiqiufeng.dify.core.pojo.DifyPageResult;
import io.github.guoshiqiufeng.dify.workflow.DifyWorkflow;
import io.github.guoshiqiufeng.dify.workflow.dto.request.WorkflowLogsRequest;
import io.github.guoshiqiufeng.dify.workflow.dto.request.WorkflowRunRequest;
import io.github.guoshiqiufeng.dify.workflow.dto.response.WorkflowInfoResponse;
import io.github.guoshiqiufeng.dify.workflow.dto.response.WorkflowLogs;
import io.github.guoshiqiufeng.dify.workflow.dto.response.WorkflowRunResponse;
import io.github.guoshiqiufeng.dify.workflow.dto.response.WorkflowRunStreamResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/3/12 15:51
 */
@Slf4j
@ActiveProfiles("dev")
@SpringBootTest
public class WorkflowTest {

    public static final String apiKey = "app-0M83umUpl8HN1mHjOBYPSa64";

    @Resource
    private DifyWorkflow difyWorkflow;


    @Test
    public void test() throws InterruptedException {
        WorkflowRunRequest request = new WorkflowRunRequest();
        //request.setFiles();
        request.setInputs(Map.of());
        request.setApiKey(apiKey);
        request.setUserId("6");

        WorkflowRunResponse workflowRunResponse = difyWorkflow.runWorkflow(request);
        log.info("res:{}", JSON.toJSONString(workflowRunResponse));

        Flux<WorkflowRunStreamResponse> workflowRunResponseFlux = difyWorkflow.runWorkflowStream(request);
        workflowRunResponseFlux.subscribe(t -> {
            log.info("stream res:{}", JSON.toJSONString(t));
        });


        WorkflowInfoResponse info = difyWorkflow.info(workflowRunResponse.getWorkflowRunId(), apiKey);
        log.info("info:{}", JSON.toJSONString(info));

        WorkflowLogsRequest logsRequest = new WorkflowLogsRequest();
        logsRequest.setApiKey(apiKey);
        logsRequest.setUserId("6");

        DifyPageResult<WorkflowLogs> logs = difyWorkflow.logs(logsRequest);
        log.info("logs: {}", JSON.toJSONString(logs));

        Thread.sleep(20000);
    }
}
