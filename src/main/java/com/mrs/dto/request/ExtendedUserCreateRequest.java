package com.mrs.dto.request;

import com.mrs.annotation.ValidEnum;
import com.mrs.enumeration.AccountType;
import jakarta.validation.constraints.NotNull;

public class ExtendedUserCreateRequest extends UserCreateRequest {

    @NotNull(message = "Account type should not be null")
    @ValidEnum(enumClass = AccountType.class, message = "Account type is not valid")
    private String accountType;

    @NotNull
    private Integer roleId;

    public ExtendedUserCreateRequest() {}

    public ExtendedUserCreateRequest(UserCreateRequest userCreateRequest, String accountType, Integer roleId) {
        super(userCreateRequest.getName(), userCreateRequest.getPhone(), userCreateRequest.getEmail(), userCreateRequest.getGender());
        this.accountType = accountType;
        this.roleId = roleId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
