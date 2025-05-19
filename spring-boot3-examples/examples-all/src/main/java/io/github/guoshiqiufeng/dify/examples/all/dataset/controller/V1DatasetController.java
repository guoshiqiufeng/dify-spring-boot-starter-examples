package io.github.guoshiqiufeng.dify.examples.all.dataset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.guoshiqiufeng.dify.core.pojo.DifyPageResult;
import io.github.guoshiqiufeng.dify.dataset.dto.request.DatasetCreateRequest;
import io.github.guoshiqiufeng.dify.dataset.dto.request.DatasetPageRequest;
import io.github.guoshiqiufeng.dify.dataset.dto.response.DatasetResponse;
import io.github.guoshiqiufeng.dify.examples.all.dataset.service.DifyDatasetService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/3/25 13:50
 */
@RestController
@RequestMapping("/v1/dataset")
public class V1DatasetController {

    @Resource
    private DifyDatasetService difyDatasetService;

    @Resource
    private ObjectMapper objectMapper;

    @GetMapping("/page")
    public DifyPageResult<DatasetResponse> page(DatasetPageRequest datasetPageRequest) {
        return difyDatasetService.page(datasetPageRequest);
    }

    @PostMapping("")
    public DatasetResponse create(@RequestBody DatasetCreateRequest datasetCreateRequest) {
        return difyDatasetService.create(datasetCreateRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        difyDatasetService.delete(id);
    }

    @GetMapping("/test")
    public String test() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>(3);
        map.put("version", "1.0.0");
        map.put("tar", null);
        return objectMapper.writeValueAsString(map);
    }
}
