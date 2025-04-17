package io.github.guoshiqiufeng.dify.examples.all.dataset.controller;

import com.alibaba.fastjson2.JSON;
import io.github.guoshiqiufeng.dify.core.pojo.DifyPageResult;
import io.github.guoshiqiufeng.dify.dataset.dto.request.*;
import io.github.guoshiqiufeng.dify.dataset.dto.response.DocumentCreateResponse;
import io.github.guoshiqiufeng.dify.dataset.dto.response.DocumentInfo;
import io.github.guoshiqiufeng.dify.examples.all.dataset.service.DifyDatasetService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/3/25 14:16
 */
@RestController
@RequestMapping("/v1/dataset/document")
public class V1DatasetDocumentController {

    @Resource
    private DifyDatasetService difyDatasetService;

    @PostMapping("/text")
    public DocumentCreateResponse createDocumentByText(@RequestBody DocumentCreateByTextRequest documentCreateByTextRequest) {
        return difyDatasetService.createDocumentByText(documentCreateByTextRequest);
    }

    @PostMapping(path = "/file", consumes = "multipart/form-data")
    public DocumentCreateResponse createDocumentByFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") String data
    ) {
        DocumentCreateByFileRequest documentCreateByFileRequest = JSON.parseObject(data, DocumentCreateByFileRequest.class);
        documentCreateByFileRequest.setFile(file);
        return difyDatasetService.createDocumentByFile(documentCreateByFileRequest);
    }

    @PutMapping("/text")
    public DocumentCreateResponse updateDocumentByText(@RequestBody DocumentUpdateByTextRequest documentCreateByTextRequest) {
        return difyDatasetService.updateDocumentByText(documentCreateByTextRequest);
    }

    @PutMapping(path = "/file", consumes = "multipart/form-data")
    public DocumentCreateResponse updateDocumentByFile(@RequestPart("file") MultipartFile file,
                                                       @RequestPart("data") String data) {
        DocumentUpdateByFileRequest documentUpdateByFileRequest = JSON.parseObject(data, DocumentUpdateByFileRequest.class);
        documentUpdateByFileRequest.setFile(file);
        return difyDatasetService.updateDocumentByFile(documentUpdateByFileRequest);
    }

    @GetMapping("/page")
    public DifyPageResult<DocumentInfo> page(DatasetPageDocumentRequest datasetPageDocumentRequest) {
        return difyDatasetService.pageDocument(datasetPageDocumentRequest);
    }

    @DeleteMapping("/{datasetId}/{documentId}")
    public void delete(@PathVariable("datasetId") String datasetId,
                       @PathVariable("documentId") String documentId) {
        difyDatasetService.deleteDocument(datasetId, documentId);
    }
}
