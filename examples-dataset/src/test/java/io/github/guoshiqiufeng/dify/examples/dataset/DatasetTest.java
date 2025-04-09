package io.github.guoshiqiufeng.dify.examples.dataset;

import com.alibaba.fastjson2.JSON;
import io.github.guoshiqiufeng.dify.core.pojo.DifyPageResult;
import io.github.guoshiqiufeng.dify.dataset.DifyDataset;
import io.github.guoshiqiufeng.dify.dataset.dto.RetrievalModel;
import io.github.guoshiqiufeng.dify.dataset.dto.request.*;
import io.github.guoshiqiufeng.dify.dataset.dto.request.document.ProcessRule;
import io.github.guoshiqiufeng.dify.dataset.dto.response.*;
import io.github.guoshiqiufeng.dify.dataset.enums.IndexingTechniqueEnum;
import io.github.guoshiqiufeng.dify.dataset.enums.MetaDataActionEnum;
import io.github.guoshiqiufeng.dify.dataset.enums.SearchMethodEnum;
import io.github.guoshiqiufeng.dify.dataset.enums.document.DocFormEnum;
import io.github.guoshiqiufeng.dify.dataset.enums.document.DocTypeEnum;
import io.github.guoshiqiufeng.dify.dataset.enums.document.ModeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/3/12 15:51
 */
@Slf4j
@ActiveProfiles("dev")
@SpringBootTest
public class DatasetTest {

    @Resource
    private DifyDataset dataset;


    @Test
    public void test() throws InterruptedException, IOException {

        page();

        DatasetResponse datasetResponse = createDataSet();

        String datasetId = datasetResponse.getId();

        DocumentCreateResponse documentByText = createDocumentByText(datasetId);
        String documentId = documentByText.getDocument().getId();

        MultipartFile multipartFile = getMultipartFile();

        DocumentCreateResponse documentByFile = createDocumentByFile(datasetId, multipartFile);

        String oldDocumentId = documentByFile.getDocument().getId();

        pageDocument(datasetId);

        DocumentUpdateByTextRequest documentUpdateByTextRequest = new DocumentUpdateByTextRequest();

        documentUpdateByTextRequest.setDatasetId(datasetId);
        documentUpdateByTextRequest.setDocumentId(documentId);
        documentUpdateByTextRequest.setName("updateDocumentByText");

        ProcessRule processRule = new ProcessRule();
        processRule.setMode(ModeEnum.automatic);
        // processRule.setRule();

        documentUpdateByTextRequest.setProcessRule(processRule);
        Thread.sleep(500);
        dataset.updateDocumentByText(documentUpdateByTextRequest);

        updateDocumentByFile(datasetId, multipartFile, oldDocumentId);


        DocumentDeleteResponse documentDeleteResponse = dataset.deleteDocument(datasetId, oldDocumentId);
        log.info("doc del:{}", JSON.toJSONString(documentDeleteResponse));

        DocumentIndexingStatusRequest documentIndexingStatusRequest = new DocumentIndexingStatusRequest();
        documentIndexingStatusRequest.setDatasetId(datasetId);
        documentIndexingStatusRequest.setBatch(documentByText.getBatch());

        DocumentIndexingStatusResponse documentIndexingStatusResponse = dataset.indexingStatus(documentIndexingStatusRequest);
        log.info("indexing status:{}", JSON.toJSONString(documentIndexingStatusResponse));

        Thread.sleep(1500);

        pageSegment(datasetId, documentId);

        SegmentResponse segmentResponse = createSegment(datasetId, documentId);

        String segmentId = segmentResponse.getData().get(0).getId();

        updateSegment(datasetId, documentId, segmentId);

        SegmentDeleteResponse segmentDeleteResponse = dataset.deleteSegment(datasetId, documentId, segmentId);
        log.info("segment delete:{}", JSON.toJSONString(segmentDeleteResponse));

        UploadFileInfoResponse uploadFileInfoResponse = dataset.uploadFileInfo(datasetId, documentId);
        log.info("upload file:{}", JSON.toJSONString(uploadFileInfoResponse));

        RetrieveRequest retrieveRequest = new RetrieveRequest();
        retrieveRequest.setDatasetId(datasetId);
        retrieveRequest.setQuery("更新");
        //retrieveRequest.setRetrievalModel();

        RetrieveResponse retrieve = dataset.retrieve(retrieveRequest);
        log.info("retrieve:{}", JSON.toJSONString(retrieve));

        MetaDataCreateRequest metaDataCreateRequest = new MetaDataCreateRequest();
        metaDataCreateRequest.setDatasetId(datasetId);
        metaDataCreateRequest.setType("string");
        metaDataCreateRequest.setName("test");
        MetaDataResponse metaData = dataset.createMetaData(metaDataCreateRequest);
        log.info("metaData:{}", JSON.toJSONString(metaData));

        String metaDataId = metaData.getId();

        MetaDataUpdateRequest metaDataUpdateRequest = new MetaDataUpdateRequest();
        metaDataUpdateRequest.setDatasetId(datasetId);
        metaDataUpdateRequest.setMetaDataId(metaDataId);
        metaDataUpdateRequest.setName("test api");
        MetaDataResponse metaDataResponse = dataset.updateMetaData(metaDataUpdateRequest);
        log.info("metaDataResponse:{}", JSON.toJSONString(metaDataResponse));

        MetaDataActionRequest metaDataActionRequest = new MetaDataActionRequest();
        metaDataActionRequest.setDatasetId(datasetId);
        metaDataActionRequest.setAction(MetaDataActionEnum.disable);
        dataset.actionMetaData(metaDataActionRequest);

        DocumentMetaDataUpdateRequest documentMetaDataUpdateRequest = new DocumentMetaDataUpdateRequest();
        documentMetaDataUpdateRequest.setDatasetId(datasetId);
        List<DocumentMetaDataUpdateRequest.OperationData> operationDataList = new ArrayList<>();
        DocumentMetaDataUpdateRequest.OperationData operationData = new DocumentMetaDataUpdateRequest.OperationData();
        operationData.setDocumentId(documentId);
        List<MetaData> dataList = new ArrayList<>();
        MetaData metaData1 = new MetaData();
        metaData1.setId("1");
        metaData1.setType("string");
        metaData1.setName("api test");
        dataList.add(metaData1);
        operationData.setMetadataList(dataList);

        operationDataList.add(operationData);
        documentMetaDataUpdateRequest.setOperationData(operationDataList);

        dataset.updateDocumentMetaData(documentMetaDataUpdateRequest);

        MetaDataListResponse metaDataListResponse = dataset.listMetaData(datasetId);
        log.info("metaDataListResponse:{}", JSON.toJSONString(metaDataListResponse));

        dataset.deleteMetaData(datasetId, metaDataId);

        dataset.delete(datasetId);
    }

    private SegmentResponse createSegment(String datasetId, String documentId) {
        SegmentCreateRequest segmentCreateRequest = new SegmentCreateRequest();
        segmentCreateRequest.setDatasetId(datasetId);
        segmentCreateRequest.setDocumentId(documentId);
        List<SegmentParam> segments = new ArrayList<>();
        SegmentParam segmentParam = new SegmentParam();
        segmentParam.setContent("检测文本");
        segments.add(segmentParam);
        segmentCreateRequest.setSegments(segments);

        SegmentResponse segmentResponse = dataset.createSegment(segmentCreateRequest);
        log.info("segment create:{}", JSON.toJSONString(segmentResponse));
        return segmentResponse;
    }

    private void updateSegment(String datasetId, String documentId, String segmentId) {
        SegmentUpdateRequest segmentUpdateRequest = new SegmentUpdateRequest();
        segmentUpdateRequest.setDatasetId(datasetId);
        segmentUpdateRequest.setDocumentId(documentId);
        segmentUpdateRequest.setSegmentId(segmentId);

        segmentUpdateRequest.setSegment(new SegmentParam().setContent("更新检测文本"));

        SegmentUpdateResponse segmentResponse = dataset.updateSegment(segmentUpdateRequest);
        log.info("segment update:{}", JSON.toJSONString(segmentResponse));
    }

    private void pageSegment(String datasetId, String documentId) {
        SegmentPageRequest segmentPageRequest = new SegmentPageRequest();

        segmentPageRequest.setDatasetId(datasetId);
        segmentPageRequest.setDocumentId(documentId);
        SegmentResponse segmentResponse = dataset.pageSegment(segmentPageRequest);
        log.info("segment:{}", JSON.toJSONString(segmentResponse));
    }

    private void updateDocumentByFile(String datasetId, MultipartFile multipartFile, String documentId) {
        DocumentUpdateByFileRequest documentUpdateByFileRequest = new DocumentUpdateByFileRequest();

        documentUpdateByFileRequest.setDatasetId(datasetId);
        documentUpdateByFileRequest.setDocumentId(documentId);

        documentUpdateByFileRequest.setFile(multipartFile);
        DocumentCreateResponse documentCreateResponse = dataset.updateDocumentByFile(documentUpdateByFileRequest);
        log.info("update doc:{}", JSON.toJSONString(documentCreateResponse));
    }

    private void pageDocument(String datasetId) {
        DatasetPageDocumentRequest datasetPageDocumentRequest = new DatasetPageDocumentRequest();
        datasetPageDocumentRequest.setDatasetId(datasetId);

        DifyPageResult<DocumentInfo> documentInfoDifyPageResult = dataset.pageDocument(datasetPageDocumentRequest);
        log.info("doc page:{}", JSON.toJSONString(documentInfoDifyPageResult));
    }

    private MultipartFile getMultipartFile() throws IOException {
        String fileName = "test.txt";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        // 2. 将文件内容读取为字节数组
        return convertInputStreamToMultipartFile(inputStream, fileName, MediaType.TEXT_PLAIN_VALUE);
    }

    public static MultipartFile convertInputStreamToMultipartFile(
            InputStream inputStream,
            String filename,
            String contentType) throws IOException {

        byte[] bytes = IOUtils.toByteArray(inputStream); // 需要 Apache Commons IO
        return new MockMultipartFile("file", filename, contentType, bytes);
    }

    private DocumentCreateResponse createDocumentByFile(String datasetId, MultipartFile file) {
        DocumentCreateByFileRequest documentCreateByFileRequest = new DocumentCreateByFileRequest();

        documentCreateByFileRequest.setDatasetId(datasetId);
        // documentCreateByFileRequest.setName("文件测试");

        documentCreateByFileRequest.setFile(file);
        documentCreateByFileRequest.setDocType(DocTypeEnum.others);
        //documentCreateByFileRequest.setDocMetadata(Map.of("key", "file"));
        documentCreateByFileRequest.setIndexingTechnique(IndexingTechniqueEnum.HIGH_QUALITY);
        documentCreateByFileRequest.setDocForm(DocFormEnum.hierarchical_model);
        documentCreateByFileRequest.setDocLanguage("Chinese");

        ProcessRule processRule = new ProcessRule();
        processRule.setMode(ModeEnum.automatic);
        // processRule.setRule();
        documentCreateByFileRequest.setProcessRule(processRule);
        /**
         * 首次调用
         */
        RetrievalModel retrievalModel = new RetrievalModel();
        retrievalModel.setSearchMethod(SearchMethodEnum.hybrid_search);
        retrievalModel.setRerankingEnable(false);
        // retrievalModel.setRerankingModel();
        // retrievalModel.setWeights(Float.valueOf("0.3"));
        retrievalModel.setTopK(2);
        retrievalModel.setScoreThresholdEnabled(true);
        retrievalModel.setScoreThreshold(Float.valueOf("0.3"));

        documentCreateByFileRequest.setRetrievalModel(retrievalModel);
        documentCreateByFileRequest.setEmbeddingModel("bge-m3:latest");
        documentCreateByFileRequest.setEmbeddingModelProvider("langgenius/ollama/ollama");


        DocumentCreateResponse documentByFile = dataset.createDocumentByFile(documentCreateByFileRequest);
        log.info("doc:{}", documentByFile);
        return documentByFile;
    }

    private DocumentCreateResponse createDocumentByText(String datasetId) {
        DocumentCreateByTextRequest documentCreateByTextRequest = new DocumentCreateByTextRequest();

        documentCreateByTextRequest.setDatasetId(datasetId);

        documentCreateByTextRequest.setName("检测文本");
        documentCreateByTextRequest.setText("检测文本");
        documentCreateByTextRequest.setDocType(DocTypeEnum.others);
        //documentCreateByTextRequest.setDocMetadata(Map.of("key", "test"));
        documentCreateByTextRequest.setIndexingTechnique(IndexingTechniqueEnum.HIGH_QUALITY);
        documentCreateByTextRequest.setDocForm(DocFormEnum.hierarchical_model);
        documentCreateByTextRequest.setDocLanguage("Chinese");


        ProcessRule processRule = new ProcessRule();
        processRule.setMode(ModeEnum.automatic);
        // processRule.setRule();

        documentCreateByTextRequest.setProcessRule(processRule);

        /**
         * 首次调用
         */
        RetrievalModel retrievalModel = new RetrievalModel();
        retrievalModel.setSearchMethod(SearchMethodEnum.hybrid_search);
        retrievalModel.setRerankingEnable(false);
        // retrievalModel.setRerankingModel();
        RetrievalModel.RerankingModelWeight weights = new RetrievalModel.RerankingModelWeight();
        // weights.setWeightType();
        RetrievalModel.VectorSetting vectorSetting = new RetrievalModel.VectorSetting();
        vectorSetting.setVectorWeight(0.7f);
        vectorSetting.setEmbeddingModelName("bge-m3:latest");
        vectorSetting.setEmbeddingProviderName("langgenius/ollama/ollama");
        weights.setVectorSetting(vectorSetting);
        RetrievalModel.KeywordSetting keywordSetting = new RetrievalModel.KeywordSetting();
        keywordSetting.setKeywordWeight(0.3f);
        weights.setKeywordSetting(keywordSetting);
        retrievalModel.setWeights(weights);
        retrievalModel.setTopK(2);
        retrievalModel.setScoreThresholdEnabled(true);
        retrievalModel.setScoreThreshold(0.3f);

        documentCreateByTextRequest.setRetrievalModel(retrievalModel);
        documentCreateByTextRequest.setEmbeddingModel("bge-m3:latest");
        documentCreateByTextRequest.setEmbeddingModelProvider("langgenius/ollama/ollama");

        DocumentCreateResponse documentByText = dataset.createDocumentByText(documentCreateByTextRequest);
        log.info("doc:{}", JSON.toJSONString(documentByText));
        return documentByText;
    }

    private DatasetResponse createDataSet() {
        DatasetCreateRequest datasetCreateRequest = new DatasetCreateRequest();
        datasetCreateRequest.setName("api");
        datasetCreateRequest.setDescription("api des");

        DatasetResponse datasetResponse = dataset.create(datasetCreateRequest);
        log.info("create :{}", JSON.toJSONString(datasetResponse));
        return datasetResponse;
    }

    private void page() {
        DatasetPageRequest datasetPageRequest = new DatasetPageRequest();
        DifyPageResult<DatasetResponse> page = dataset.page(datasetPageRequest);

        log.info("page:{}", JSON.toJSONString(page));
    }
}
