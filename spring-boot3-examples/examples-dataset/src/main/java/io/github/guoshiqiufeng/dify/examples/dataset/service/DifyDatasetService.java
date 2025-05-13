package io.github.guoshiqiufeng.dify.examples.dataset.service;

import io.github.guoshiqiufeng.dify.core.pojo.DifyPageResult;
import io.github.guoshiqiufeng.dify.dataset.DifyDataset;
import io.github.guoshiqiufeng.dify.dataset.dto.request.*;
import io.github.guoshiqiufeng.dify.dataset.dto.response.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/3/25 13:50
 */
@Slf4j
@Service
public class DifyDatasetService {

    @Resource
    private DifyDataset difyDataset;

    public DifyPageResult<DatasetResponse> page(DatasetPageRequest datasetPageRequest) {
        return difyDataset.page(datasetPageRequest);
    }

    public DatasetResponse create(DatasetCreateRequest datasetCreateRequest) {
        return difyDataset.create(datasetCreateRequest);
    }

    public void delete(String id) {
        difyDataset.delete(id);
    }

    /************ document ************/
    public DocumentCreateResponse createDocumentByText(DocumentCreateByTextRequest documentCreateByTextRequest) {
        return difyDataset.createDocumentByText(documentCreateByTextRequest);
    }

    public DocumentCreateResponse createDocumentByFile(DocumentCreateByFileRequest documentCreateByFileRequest) {
        return difyDataset.createDocumentByFile(documentCreateByFileRequest);
    }

    public DocumentCreateResponse updateDocumentByText(DocumentUpdateByTextRequest documentUpdateByTextRequest) {
        return difyDataset.updateDocumentByText(documentUpdateByTextRequest);
    }

    public DocumentCreateResponse updateDocumentByFile(DocumentUpdateByFileRequest documentCreateByFileRequest) {
        return difyDataset.updateDocumentByFile(documentCreateByFileRequest);
    }

    public DifyPageResult<DocumentInfo> pageDocument(DatasetPageDocumentRequest datasetPageDocumentRequest) {
        return difyDataset.pageDocument(datasetPageDocumentRequest);
    }

    public DocumentIndexingStatusResponse indexingStatus(DocumentIndexingStatusRequest documentIndexingStatusRequest) {
        return difyDataset.indexingStatus(documentIndexingStatusRequest);
    }

    public void deleteDocument(String datasetId, String documentId) {
        difyDataset.deleteDocument(datasetId, documentId);
    }

    public SegmentResponse pageSegment(SegmentPageRequest request) {
        return difyDataset.pageSegment(request);
    }

    public DifyPageResult<SegmentChildChunkResponse> pageSegmentChildChunk(SegmentChildChunkPageRequest request) {
        return difyDataset.pageSegmentChildChunk(request);
    }

}
