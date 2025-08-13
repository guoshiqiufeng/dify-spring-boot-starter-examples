package io.github.guoshiqiufeng.dify.examples.dataset.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yanghq
 * @version 1.0
 * @since 2025/7/24 15:42
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/")
    public TmpVo get() {
        TmpVo res = new TmpVo();
        res.setNow(LocalDateTime.now());
        return res;
    }

    @Data
    public static class TmpVo implements Serializable {
        private static final long serialVersionUID = 3856738742010341181L;

        private LocalDateTime now;
    }
}
