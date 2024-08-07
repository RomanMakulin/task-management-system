package com.wayz.service.integration;

import com.wayz.model.User;

public interface SecurityAuthClient {
    User getCurrentAuthUser(String token);
}
