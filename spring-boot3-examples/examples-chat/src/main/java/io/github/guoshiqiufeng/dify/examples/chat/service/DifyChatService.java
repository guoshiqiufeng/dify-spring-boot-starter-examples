package io.github.guoshiqiufeng.dify.examples.chat.service;

import io.github.guoshiqiufeng.dify.chat.DifyChat;
import io.github.guoshiqiufeng.dify.chat.dto.request.ChatMessageSendRequest;
import io.github.guoshiqiufeng.dify.chat.dto.request.FilePreviewRequest;
import io.github.guoshiqiufeng.dify.chat.dto.request.FileUploadRequest;
import io.github.guoshiqiufeng.dify.chat.dto.response.ChatMessageSendCompletionResponse;
import io.github.guoshiqiufeng.dify.chat.dto.response.ChatMessageSendResponse;
import io.github.guoshiqiufeng.dify.chat.dto.response.FileUploadResponse;
import io.github.guoshiqiufeng.dify.chat.dto.response.message.CompletionData;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    public ChatMessageSendResponse send(ChatMessageSendRequest sendRequest) {
        // 可以进行自定义逻辑处理：参数转换、权限校验等
        return difyChat.send(sendRequest);
    }

    public FileUploadResponse fileUpload(FileUploadRequest fileUploadRequest) {
        // 可以进行自定义逻辑处理：参数转换、权限校验等
        return difyChat.fileUpload(fileUploadRequest);
    }

    public void filePreview(FilePreviewRequest request, HttpServletResponse response) {
        try {
            ResponseEntity<byte[]> responseEntity = difyChat.filePreview(request);

            // 设置响应头
            String contentType = responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
            response.setContentType(contentType != null ? contentType : "application/octet-stream");

            String contentLength = responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_LENGTH);
            if (contentLength != null) {
                response.setContentLength(Integer.parseInt(contentLength));
            }

            // 复制缓存控制头
            String cacheControl = responseEntity.getHeaders().getFirst(HttpHeaders.CACHE_CONTROL);
            if (cacheControl != null) {
                response.setHeader(HttpHeaders.CACHE_CONTROL, cacheControl);
            }

            // 写入文件内容
            if (responseEntity.getBody() != null) {
                response.getOutputStream().write(responseEntity.getBody());
                response.getOutputStream().flush();
            }

        } catch (Exception e) {
            log.error("File preview error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    public void fileDownload(FilePreviewRequest request, HttpServletResponse response) {
        try {
            ResponseEntity<byte[]> responseEntity = difyChat.filePreview(request);

            // 设置响应头
            String contentType = responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
            response.setContentType(contentType != null ? contentType : "application/octet-stream");

            String contentDisposition = responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
            if (contentDisposition != null) {
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
            } else {
                // 设置自定义文件名
                String filename = "";
                String safeFilename = filename != null ? filename : "download";
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + safeFilename + "\"");
            }

            // 写入文件内容
            if (responseEntity.getBody() != null) {
                response.getOutputStream().write(responseEntity.getBody());
                response.getOutputStream().flush();
            }

        } catch (Exception e) {
            log.error("File preview error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
