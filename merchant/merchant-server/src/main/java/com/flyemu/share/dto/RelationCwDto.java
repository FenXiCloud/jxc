package com.flyemu.share.dto;

import com.flyemu.share.entity.RelationCw;
import lombok.Data;

@Data
public class RelationCwDto extends RelationCw {
    private String organizationCode;
    private String organizationName;
    private String adminName;
}
