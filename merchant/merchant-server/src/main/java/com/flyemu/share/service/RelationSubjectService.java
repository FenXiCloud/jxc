package com.flyemu.share.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flyemu.share.api.Fenxi;
import com.flyemu.share.entity.*;
import com.flyemu.share.repository.RelationSubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelationSubjectService extends AbsService{
    private final Fenxi fenxi;
    private final RelationSubjectRepository subjectRepository;
    private static final QRelationSubject qRelationSubject = QRelationSubject.relationSubject;


    public List<Dict> loadSubject(Long accountSetsId){
        List<Dict> dicts = new ArrayList<>();
        JSONArray array =  fenxi.loadSubject(accountSetsId);
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            Dict dict = new Dict().set("subjectId", object.getString("id"))
                    .set("subjectName", object.getString("fullName"))
                    .set("subjectCode", object.getString("code"))
                    .set("name", object.getString("name"));
            dicts.add(dict);
        }
       return dicts;
    }

    public List<RelationSubject> query(Long cwRelationId, Long merchantId, Long organizationId) {
        List<RelationSubject> subjects = new ArrayList<>();
        bqf.selectFrom(qRelationSubject)
                .where(qRelationSubject.cwRelationId.eq(cwRelationId).and(qRelationSubject.merchantId.eq(merchantId).and(qRelationSubject.organizationId.eq(organizationId))))
                .fetch().forEach(tuple->{
                    RelationSubject relationSubject = BeanUtil.toBean(tuple, RelationSubject.class);
                    subjects.add(relationSubject);
                });
        return subjects;
    }

    /**
     * 更新
     *
     * @param relationSubjects
     * @return
     */
    @Transactional
    public void save(List<RelationSubject> relationSubjects) {
        subjectRepository.saveAll(relationSubjects);
    }


}
