package com.java.recruitment.web.security.expression;

import com.java.recruitment.service.impl.UserService;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.web.security.JwtEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Setter
@Getter
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;
    private HttpServletRequest request;

    private UserService userService;

    public CustomMethodSecurityExpressionRoot(final Authentication authentication) {
        super(authentication);
    }

    public boolean canAccessInterviewer(final Long id) {
        JwtEntity user = (JwtEntity) this.getPrincipal();
        Long userId = user.getId();

        return userId.equals(id) || hasAnyRole(Role.INTERVIEW_SPECIALIST);
    }

    public boolean canAccessHr(final Long id) {
        JwtEntity user = (JwtEntity) this.getPrincipal();
        Long userId = user.getId();

        return userId.equals(id) || hasAnyRole(Role.HR);
    }

    private boolean hasAnyRole(final Role... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

//    public boolean canAccessTask(final Long taskId) {
//        JwtEntity user = (JwtEntity) this.getPrincipal();
//        Long id = user.getId();
//
//        return userService.isTaskOwner(id, taskId);
//    }

    @Override
    public Object getThis() {
        return target;
    }

}
