package com.flyemu.share.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.flyemu.share.annotation.SaAccountSetsId;
import com.flyemu.share.annotation.SaAdminId;
import com.flyemu.share.annotation.SaMerchantId;
import com.flyemu.share.annotation.SaOrganizationId;
import com.flyemu.share.entity.RelationCw;
import com.flyemu.share.entity.RelationSubject;
import com.flyemu.share.service.RelationCwService;
import com.flyemu.share.service.RelationSubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relationSubject")
@RequiredArgsConstructor
@SaCheckLogin
public class RelationSubjectController {
    private final RelationSubjectService relationSubjectService;

    /**
     * 加载对应的帐套信息
     * @return
     */
    @GetMapping("/loadSubject")
    public JsonResult loadAccountSets(@SaAccountSetsId Long accountSetsId) {
        return JsonResult.successful(relationSubjectService.loadSubject(accountSetsId));
    }

    /**
     * 加载组织关联财务系统信息
     * @param merchantId
     * @param organizationId
     * @return
     */
    @GetMapping("load/{cwRelationId}")
    public JsonResult list(@PathVariable Long cwRelationId, @SaMerchantId Long merchantId, @SaOrganizationId Long organizationId){
        return JsonResult.successful(relationSubjectService.query(cwRelationId,merchantId,organizationId));
    }

    /**
     * 保存或更新关联信息
     *
     * @param subjects
     * @return
     */
    @PostMapping
    public JsonResult save(@RequestBody @Valid List<RelationSubject> subjects) {
        relationSubjectService.save(subjects);
        return JsonResult.successful();
    }

}
