package com.flyemu.share.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.flyemu.share.api.Fenxi;
import com.flyemu.share.common.Constants;
import com.flyemu.share.dto.RelationCwDto;
import com.flyemu.share.entity.*;
import com.flyemu.share.repository.RelationCwRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelationCwService extends AbsService{
    private final Fenxi fenxi;
    private final RelationCwRepository relationCwRepository;
    private static final QRelationCw qRelationCw = QRelationCw.relationCw;
    private static final QRelationSubject qRelationSubject = QRelationSubject.relationSubject;
    private static final QOrganization qOrganization = QOrganization.organization;
    private static final QAdmin qAdmin = QAdmin.admin;

    public List<Dict> loadAccountSets(){
        JSONObject object = fenxi.loadAccountSets();
        List<Dict> dicts = new ArrayList<>();
        if(object != null){
            List<JSONObject> list = (List<JSONObject>) object.get("accountSetsList");
            list.forEach(item->{
              Dict dict = new Dict().set("companyName",item.getString("companyName"))
                      .set("accountSetsId",item.getString("id"));
                dicts.add(dict);
            });
        }
        return dicts;
    }

    public List<RelationCwDto> load(Long merchantId, Long organizationId) {
        List<RelationCwDto> dtos = new ArrayList<>();
        bqf.selectFrom(qOrganization)
                .select(qRelationCw,qOrganization.name,qOrganization.code,qAdmin.name)
                .leftJoin(qRelationCw).on(qOrganization.id.eq(qRelationCw.organizationId).and(qRelationCw.merchantId.eq(merchantId)))
                .leftJoin(qAdmin).on(qRelationCw.adminId.eq(qAdmin.id))
                .where(qOrganization.id.eq(organizationId).and(qOrganization.merchantId.eq(merchantId))).fetch().forEach(tuple -> {
                    RelationCwDto dto = new RelationCwDto();
                    dto.setIsRelation(false);
                    if(tuple.get(qRelationCw) != null){
                        dto = BeanUtil.toBean(tuple.get(qRelationCw), RelationCwDto.class);
                        dto.setAdminName(tuple.get(qAdmin.name));
                    }
                    dto.setOrganizationName(tuple.get(qOrganization.name));
                    dto.setOrganizationCode(tuple.get(qOrganization.code));
                    dtos.add(dto);
                });
        return dtos;
    }

    /**
     * 保存/更新
     *
     * @param relationCw
     * @return
     */
    @Transactional
    public Long save(RelationCw relationCw) {
        relationCw.setUpdateDate(LocalDateTime.now());
        if (relationCw.getIsRelation()){
                Assert.isTrue(relationCw.getAccountSetsId() != null && StrUtil.isNotEmpty(relationCw.getCompanyName()),"财务帐套数据为空");
        }else {
            relationCw.setAccountSetsId(null);
            relationCw.setCompanyName(null);
        }
        if (relationCw.getId() == null ){
           relationCw = relationCwRepository.save(relationCw);
           lazyDao.batchUpdate("initSubjectRelation",toRelations(relationCw.getMerchantId(), relationCw.getOrganizationId(), relationCw.getId(),relationCw.getAccountSetsId()));
        }else {
            RelationCw orgRelationCw = relationCwRepository.getById(relationCw.getId());
            if(orgRelationCw.getAccountSetsId() != null && !orgRelationCw.getAccountSetsId().equals( relationCw.getAccountSetsId())){
                jqf.update(qRelationSubject)
                        .set(qRelationSubject.accountSetsId,relationCw.getAccountSetsId())
                        .setNull(qRelationSubject.subjectId)
                        .setNull(qRelationSubject.title)
                        .setNull(qRelationSubject.type)
                        .setNull(qRelationSubject.code)
                        .where(qRelationSubject.cwRelationId.eq(relationCw.getId()).and(qRelationSubject.merchantId.eq(relationCw.getMerchantId())).and(qRelationSubject.organizationId.eq(relationCw.getOrganizationId()))).execute();
            }
            relationCwRepository.save(relationCw);
        }
        return relationCw.getAccountSetsId();
    }

    private List<RelationSubject> toRelations(Long merchantId, Long organizationId, Long cwRelationId,Long accountSetsId) {
        List<String> strings =new ArrayList<>(Arrays.asList("应收账款","应付账款","库存盘亏","库存盘赢","库存商品","订单收入","订单成本","其他收入","其他成本","客户","供应商"));
        RelationSubject relationSubject = new RelationSubject();
        relationSubject.setMerchantId(merchantId);
        relationSubject.setOrganizationId(organizationId);
        relationSubject.setCwRelationId(cwRelationId);
        relationSubject.setAccountSetsId(accountSetsId);
        List<RelationSubject> relations = new ArrayList<>();
        strings.forEach(str->{
            RelationSubject sRelation = new RelationSubject();
            BeanUtil.copyProperties(relationSubject,sRelation);
            sRelation.setName(str);
            relations.add(sRelation);
        });
        return relations;
    }

}
