package com.flyemu.share.service;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONObject;
import com.flyemu.share.api.Fenxi;
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
public class RelationService extends AbsService{
    private final Fenxi fenxi;

    public List<Dict> test(){
        JSONObject object = fenxi.attendanceColumns();
        List<Dict> dicts = new ArrayList<>();
        if(object != null){
            List<JSONObject> list = (List<JSONObject>) object.get("accountSetsList");
            list.forEach(item->{
              Dict dict = new Dict().set("companyName",item.get("companyName"))
                      .set("accountSetsId",item.get("id"));
                dicts.add(dict);
            });
        }
        return dicts;
    }

}
