package io.github.guoshiqiufeng.dify.examples.all.server.controller;

import io.github.guoshiqiufeng.dify.server.DifyServer;
import io.github.guoshiqiufeng.dify.server.dto.response.AppsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/10/28 11:09
 */
@RestController
@RequestMapping("/server")
public class ServerController {

    @Resource
    private DifyServer difyServer;

    @GetMapping("/apps")
    public List<AppsResponse> apps() {
        return difyServer.apps("", "");
    }

}
