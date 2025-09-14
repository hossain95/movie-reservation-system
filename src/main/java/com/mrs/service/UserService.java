package com.mrs.service;

import com.mrs.dto.request.ExtendedUserCreateRequest;
import com.mrs.dto.response.SuccessResponse;

public interface UserService {
    SuccessResponse addNewAdminUser(ExtendedUserCreateRequest requestDto);

    void createDefaultAdminUser();
}
