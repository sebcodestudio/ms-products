package com.sebcode.msproducts.security.config.security;

import com.sebcode.msproducts.company.enums.CompanyRole;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Reglas de autorización para el catálogo, usadas desde @PreAuthorize.
 * Un ROLE_ADMIN global siempre pasa (superusuario). Sin eso, se evalúa el
 * CompanyRole del usuario en su empresa activa: OWNER/ADMIN > MANAGER > SELLER,
 * de mayor a menor alcance. No hay scoping por empresa (un solo catálogo
 * compartido) — esto solo decide QUÉ puede tocar cada rol, no de quién es.
 */
@Component("catalogAccess")
public class CatalogAccess {

    /** Taxonomía estructural: categorías, subcategorías, marcas, tipos/valores de atributo. */
    public boolean canManageTaxonomy(Authentication authentication) {
        CustomUserPrincipal principal = principalOf(authentication);
        if (principal == null) return false;
        return principal.isAdmin() || principal.isCompanyOwnerOrAdmin();
    }

    /** Productos, sus imágenes y su asignación a subcategorías. */
    public boolean canManageProducts(Authentication authentication) {
        CustomUserPrincipal principal = principalOf(authentication);
        if (principal == null) return false;
        return canManageTaxonomy(authentication) || principal.hasCompanyRole(CompanyRole.MANAGER);
    }

    /** Variantes vendibles: SKU, precio, stock, descuento y sus atributos (color/talla). */
    public boolean canManageVariants(Authentication authentication) {
        CustomUserPrincipal principal = principalOf(authentication);
        if (principal == null) return false;
        return canManageProducts(authentication) || principal.hasCompanyRole(CompanyRole.SELLER);
    }

    private CustomUserPrincipal principalOf(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {
            return null;
        }
        return principal;
    }
}
