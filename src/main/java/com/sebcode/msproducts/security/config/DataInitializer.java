package com.sebcode.msproducts.security.config;

//import com.soa.onlinestorebackend.category.entity.Subcategory;
//import com.soa.onlinestorebackend.category.repository.CategoryRepository;
//import com.soa.onlinestorebackend.category.repository.SubcategoryRepository;

import com.sebcode.msproducts.product.entity.AttributeType;
import com.sebcode.msproducts.product.entity.AttributeValue;
import com.sebcode.msproducts.product.repository.AttributeTypeRepository;
import com.sebcode.msproducts.product.repository.AttributeValueRepository;
import com.sebcode.msproducts.security.entity.Role;
import com.sebcode.msproducts.security.repository.RoleRepository;
import com.sebcode.msproducts.user.entity.User;
import com.sebcode.msproducts.user.enums.Gender;
import com.sebcode.msproducts.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
//@Profile("dev") // ⚠️ Solo en desarrollo
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository          roleRepository;
    private final UserRepository          userRepository;
    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final PasswordEncoder         passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            log.info("DataInitializer: datos ya existentes, se omite la carga.");
            return;
        }
        log.info("DataInitializer: iniciando carga de datos semilla...");

        List<Role> roles = insertRoles();
        List<User> users = insertUsers(roles);
        List<AttributeType> attrTypes = insertAttributeTypes();
        insertAttributeValues(attrTypes, users);

        log.info("DataInitializer: carga completada.");
    }

    // =========================================================================
    // ROLES
    // =========================================================================
    private List<Role> insertRoles() {
        Role admin = Role.builder()
                .name("ADMIN")
                .description("Super administrador del sistema")
                .build();

        Role user = Role.builder()
                .name("USER")
                .description("Usuario regular del sistema")
                .build();

        return roleRepository.saveAll(List.of(admin, user));
    }

    // =========================================================================
    // USUARIOS — password "admin123"
    // =========================================================================
    private List<User> insertUsers(List<Role> roles) {
        Role roleAdmin = roles.get(0); // ADMIN
        Role roleUser  = roles.get(1); // USER

        String hash = passwordEncoder.encode("admin123");

        User admin = User.builder()
                .email("admin@sistema.com")
                .password(hash)
                .name("Admin")
                .lastName("Sistema")
                .phone("987654321")
                .address("Av. Javier Prado 123, San Isidro, Lima")
                .birthdate(LocalDate.of(1990, 1, 15))
                .gender(Gender.MALE)
                .state(true)
                .roles(Set.of(roleAdmin, roleUser))
                .build();

        User juan = User.builder()
                .email("juan.perez@gmail.com")
                .password(hash)
                .name("Juan")
                .lastName("Pérez García")
                .phone("999888777")
                .address("Jr. Las Flores 456, Miraflores, Lima")
                .birthdate(LocalDate.of(1988, 5, 20))
                .gender(Gender.MALE)
                .state(true)
                .roles(Set.of(roleUser))
                .build();

        return userRepository.saveAll(List.of(admin, juan));
    }

    // =========================================================================
    // ATTRIBUTE TYPES
    // =========================================================================
    private List<AttributeType> insertAttributeTypes() {
        List<AttributeType> types = List.of(
                AttributeType.builder().name("Talla")    .displayOrder(1).isVisual(false).build(),
                AttributeType.builder().name("Número")   .displayOrder(2).isVisual(false).build(),
                AttributeType.builder().name("Color")    .displayOrder(3).isVisual(true) .build(),
                AttributeType.builder().name("Capacidad").displayOrder(4).isVisual(false).build(),
                AttributeType.builder().name("Sabor")    .displayOrder(5).isVisual(true) .build()
        );
        return attributeTypeRepository.saveAll(types);
    }

    private void insertAttributeValues(List<AttributeType> types, List<User> users) {
        Long uid = users.get(0).getId();

        AttributeType talla    = types.get(0); // id: 1
        AttributeType numero   = types.get(1); // id: 2
        AttributeType color    = types.get(2); // id: 3
        AttributeType capacidad = types.get(3); // id: 4
        AttributeType sabor    = types.get(4); // id: 5

        attributeValueRepository.saveAll(List.of(
                // Tallas
                AttributeValue.builder().value("S") .attributeType(talla)    .displayOrder(1).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("M") .attributeType(talla)    .displayOrder(2).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("L") .attributeType(talla)    .displayOrder(3).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("XL").attributeType(talla)    .displayOrder(4).createdUser(uid).updateUser(uid).build(),
                // Números
                AttributeValue.builder().value("39").attributeType(numero)   .displayOrder(1).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("40").attributeType(numero)   .displayOrder(2).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("41").attributeType(numero)   .displayOrder(3).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("42").attributeType(numero)   .displayOrder(4).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("43").attributeType(numero)   .displayOrder(5).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("44").attributeType(numero)   .displayOrder(6).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("45").attributeType(numero)   .displayOrder(7).createdUser(uid).updateUser(uid).build(),
                // Colores
                AttributeValue.builder().value("ROJO")  .attributeType(color).displayOrder(1).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("AZUL")  .attributeType(color).displayOrder(2).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("NEGRO") .attributeType(color).displayOrder(3).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("BLANCO").attributeType(color).displayOrder(4).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("GRIS")  .attributeType(color).displayOrder(5).createdUser(uid).updateUser(uid).build(),
                // Capacidad
                AttributeValue.builder().value("128").attributeType(capacidad).displayOrder(1).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("256").attributeType(capacidad).displayOrder(2).createdUser(uid).updateUser(uid).build(),
                // Sabores
                AttributeValue.builder().value("CHOCOLATE").attributeType(sabor).displayOrder(1).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("COOKIES")  .attributeType(sabor).displayOrder(2).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("FRESA")    .attributeType(sabor).displayOrder(3).createdUser(uid).updateUser(uid).build()
        ));
    }
}
