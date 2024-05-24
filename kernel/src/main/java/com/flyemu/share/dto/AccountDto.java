package com.flyemu.share.dto;

import com.flyemu.share.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
/**
 * @功能描述: AccountDto
 * @创建时间: 2023年08月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountDto implements Serializable {

    private Admin admin;

    private Merchant merchant;

    private Organization organization;

    private Role role;

    private LocalDate checkDate;

    private String costMethod;

    /**
     * 授权功能
     */
    private List<String> granted;

    public AccountDto(Admin admin, Merchant merchant, Role role,Organization organization,String costMethod) {
        this.admin = admin;
        this.merchant = merchant;
        this.role = role;
        this.organization = organization;
        this.checkDate = organization.getCheckoutDate();
        this.costMethod = costMethod;
    }

    public AccountDto(Admin admin, Merchant merchant, Role role) {
        this.admin = admin;
        this.merchant = merchant;
        this.role = role;
    }

    public Long getAdminId() {
        return admin.getId();
    }

    public Long getMerchantId() {
        return merchant.getId();
    }

    //TODO
    public Long getOrganizationId() {
        if (organization != null){
            return organization.getId();
        }else {
            return null;
        }
    }

}
