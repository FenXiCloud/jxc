package com.flyemu.share.dto;

import com.flyemu.share.entity.Merchant;
import com.flyemu.share.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrganizationTreeDto {

    private Merchant merchant;
    private List<Organization> organizationList;
}
