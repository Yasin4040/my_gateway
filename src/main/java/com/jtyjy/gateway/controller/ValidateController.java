package com.jtyjy.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author wyq
 * @date 2022/4/9
 */

@RestController
public class ValidateController {

    @Value("${wechat.validate-text}")
    private String validateText;


    @GetMapping("/WW_verify_Opcld7AocIGq70AI.txt")
    public Mono<String> validateTextTest() {
        return Mono.just(validateText);
    }


    @GetMapping("/WW_verify_t3kRri2gJL8aLmVd.txt")
    public Mono<String> validateTextProd() {
        return Mono.just(validateText);
    }


}
