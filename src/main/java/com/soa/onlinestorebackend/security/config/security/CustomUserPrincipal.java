package com.soa.onlinestorebackend.security.config.security;

import com.soa.onlinestorebackend.company.enums.CompanyRole;
import com.soa.onlinestorebackend.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CustomUserPrincipal implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private boolean active;
    private Collection<? extends GrantedAuthority> authorities;
    private Long activeCompanyId;
    private CompanyRole companyRole;
    private Set<Long> companyIds; // Empresas disponibles

    // ========== FACTORY METHODS ==========

    /**
     * Crea un CustomUserPrincipal para LOGIN inicial (sin compañía activa)
     */
    public static CustomUserPrincipal create(User user) {
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        Set<Long> companyIds = user.getCompanyUsers().stream()
                .map(cu -> cu.getCompany().getId())
                .collect(Collectors.toSet());

        // Seleccionar primera compañía por defecto (si existe)
        Long defaultCompanyId = companyIds.isEmpty() ? null : companyIds.iterator().next();
        CompanyRole defaultRole = null;

        if (defaultCompanyId != null) {
            defaultRole = user.getCompanyUsers().stream()
                    .filter(cu -> cu.getCompany().getId().equals(defaultCompanyId))
                    .findFirst()
                    .map(cu -> cu.getRole())
                    .orElse(null);
        }

        return new CustomUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getActive(),
                authorities,
                defaultCompanyId,
                defaultRole,
                companyIds
        );
    }

    /**
     * Crea un CustomUserPrincipal con una compañía específica activa
     */
    public static CustomUserPrincipal create(User user, Long activeCompanyId, CompanyRole companyRole) {
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        Set<Long> companyIds = user.getCompanyUsers().stream()
                .map(cu -> cu.getCompany().getId())
                .collect(Collectors.toSet());

        return new CustomUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getActive(),
                authorities,
                activeCompanyId,
                companyRole,
                companyIds
        );
    }

    // ========== HELPER METHODS ==========

    /**
     * Verifica si el usuario tiene rol ADMIN global
     */
    public boolean isAdmin() {
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Verifica si el usuario tiene un rol específico en la compañía activa
     */
    public boolean hasCompanyRole(CompanyRole role) {
        return this.companyRole == role;
    }

    /**
     * Verifica si el usuario tiene alguno de los roles especificados en la compañía activa
     */
    public boolean hasAnyCompanyRole(CompanyRole... roles) {
        if (this.companyRole == null) {
            return false;
        }
        for (CompanyRole role : roles) {
            if (this.companyRole == role) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si el usuario pertenece a una compañía específica
     */
    public boolean belongsToCompany(Long companyId) {
        return companyIds != null && companyIds.contains(companyId);
    }

    /**
     * Verifica si el usuario tiene una compañía activa seleccionada
     */
    public boolean hasActiveCompany() {
        return activeCompanyId != null;
    }

    /**
     * Verifica si el usuario es OWNER o ADMIN de la compañía activa
     */
    public boolean isCompanyOwnerOrAdmin() {
        return hasAnyCompanyRole(CompanyRole.OWNER, CompanyRole.ADMIN);
    }

    /**
     * Verifica si el usuario puede gestionar la compañía (ADMIN global o OWNER/ADMIN de la compañía)
     */
    public boolean canManageCompany(Long companyId) {
        // ADMIN global puede gestionar cualquier compañía
        if (isAdmin()) {
            return true;
        }

        // Debe pertenecer a la compañía y ser OWNER o ADMIN
        return belongsToCompany(companyId) &&
                activeCompanyId != null &&
                activeCompanyId.equals(companyId) &&
                isCompanyOwnerOrAdmin();
    }

    /**
     * Obtiene el ID de la compañía activa (puede ser null)
     */
    public Long getActiveCompanyId() {
        return activeCompanyId;
    }

    /**
     * Obtiene el rol en la compañía activa (puede ser null)
     */
    public CompanyRole getCompanyRole() {
        return companyRole;
    }

    /**
     * Obtiene todas las compañías a las que pertenece el usuario
     */
    public Set<Long> getCompanyIds() {
        return companyIds;
    }

    // ========== USERDETAILS METHODS ==========

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}