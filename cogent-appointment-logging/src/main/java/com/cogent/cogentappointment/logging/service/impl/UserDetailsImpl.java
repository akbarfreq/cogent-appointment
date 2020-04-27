package com.cogent.cogentappointment.logging.service.impl;

import com.cogent.cogentappointment.logging.dto.response.LoggedInAdminDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Sauravi Thapa २०/१/१६
 */
public class UserDetailsImpl implements UserDetails {
    private Long id;

    private String email;

    private Character isCompany;

    @JsonIgnore
    private String password;

    private String hospitalCode;

    private Long hospitalId;

    private String apiKey;

    private String apiSecret;

    public UserDetailsImpl(Long id,
                           String email,
                           Character isCompany,
                           String password,
                           String hospitalCode,
                           Long hospitalId,
                           String apiKey,
                           String apiSecret) {
        this.id = id;
        this.email = email;
        this.isCompany = isCompany;
        this.password = password;
        this.hospitalCode = hospitalCode;
        this.hospitalId = hospitalId;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public static UserDetailsImpl build(LoggedInAdminDTO admin) {
        return new UserDetailsImpl(
                admin.getId(),
                admin.getEmail(),
                admin.getIsCompany(),
                admin.getPassword(),
                admin.getHospitalCode(),
                admin.getHospitalId(),
                admin.getApiKey(),
                admin.getApiSecret());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
