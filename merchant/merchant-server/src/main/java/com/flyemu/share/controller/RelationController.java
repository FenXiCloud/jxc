package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relation")
@RequiredArgsConstructor
@SaCheckLogin
public class RelationController {
    private final RelationService relationService;


    @GetMapping("/init")
    public JsonResult list() {
        return JsonResult.successful(relationService.test());
    }
}
