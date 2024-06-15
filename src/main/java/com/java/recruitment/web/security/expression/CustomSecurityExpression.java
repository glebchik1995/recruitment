package com.java.recruitment.web.security.expression;

import com.java.recruitment.repositoty.exception.DataAccessException;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.IChatMessageService;
import com.java.recruitment.service.IVacancyService;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.web.security.JwtEntity;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("cse")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final IVacancyService vacancyService;

    private final IJobRequestService jobRequestService;

    private final IChatMessageService mailService;


    public boolean canAccessUser(@NotNull final Long id) {
        JwtEntity user = this.getPrincipal();
        Long userId = user.getId();
        return userId.equals(id) || hasAnyRole(Role.ADMIN);
    }

    public Long getIdFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtEntity) {
            return ((JwtEntity) authentication.getPrincipal()).getId();
        }
        throw new DataAccessException("Пользователь не аутентифицирован или неверные данные аутентификации");
    }

    public boolean canAccessJobRequest(@NotNull final Long jobRequestId) {
        JwtEntity user = this.getPrincipal();
        Long id = user.getId();

        return jobRequestService.isJobRequestOwner(id, jobRequestId) || hasAnyRole(Role.ADMIN);
    }

    public boolean canAccessJobRequestForRecruiter(@NotNull final Long jobRequestId) {
        JwtEntity user = this.getPrincipal();
        Long id = user.getId();

        return jobRequestService.isRecruiterForJobRequest(id, jobRequestId) || hasAnyRole(Role.ADMIN);
    }

    public boolean canAccessMessageForRecruiter(@NotNull final Long messageId) {
        JwtEntity user = this.getPrincipal();
        Long id = user.getId();

        return mailService.isRecruiterForMessage(id, messageId) || hasAnyRole(Role.ADMIN);
    }

    public boolean canAccessMessageForHr(@NotNull final Long messageId) {
        JwtEntity user = this.getPrincipal();
        Long id = user.getId();

        return mailService.isHrForMessage(id, messageId) || hasAnyRole(Role.ADMIN);
    }

    public boolean canAccessVacancy(@NotNull final Long vacancyId) {
        JwtEntity user = this.getPrincipal();
        Long id = user.getId();

        return vacancyService.isVacancyOwner(id, vacancyId) || hasAnyRole(Role.ADMIN);
    }

    private boolean hasAnyRole(final Role... roles) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    private JwtEntity getPrincipal() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        return (JwtEntity) authentication.getPrincipal();
    }
}
