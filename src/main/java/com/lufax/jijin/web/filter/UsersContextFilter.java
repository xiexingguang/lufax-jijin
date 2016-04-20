package com.lufax.jijin.web.filter;

import com.lufax.jersey.usercontext.UserContext;
import com.lufax.jersey.usercontext.filter.UserContextFilter;
import com.lufax.jijin.user.dto.UserDTO;
import com.lufax.jijin.base.utils.UtilConstants;
import com.lufax.jijin.user.repository.UserRepository;
import com.lufax.jijin.base.utils.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class UsersContextFilter extends UserContextFilter {
    @Autowired
    protected UserRepository userRepository;

    @Override
    protected void setConfig(ServletRequest request, ServletResponse response, UserContext userContext) throws IOException, ServletException {

        if (userContext != null) {
            String userId = userContext.getUserId();
            if (StringUtils.isNotEmpty(userId)) {
                UserDTO user = null;
                try {
                    user = userRepository.getUserById(Long.parseLong(userId));
                } catch (Exception e) {
                    Logger.warn(this, "Could not find User: " + userId, e);
                }
                if (user != null) {
                    request.setAttribute(UtilConstants.USER, user);
                }
            }
        }
        super.setConfig(request, response, userContext);
    }
}
