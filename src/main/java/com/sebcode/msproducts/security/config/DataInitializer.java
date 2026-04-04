package com.sebcode.msproducts.security.config;

import com.sebcode.msproducts.category.entity.Category;
import com.sebcode.msproducts.category.entity.Subcategory;
import com.sebcode.msproducts.category.repository.CategoryRepository;
import com.sebcode.msproducts.category.repository.SubcategoryRepository;
import com.sebcode.msproducts.company.entity.Company;
import com.sebcode.msproducts.company.entity.CompanyUser;
import com.sebcode.msproducts.company.enums.CompanyRole;
import com.sebcode.msproducts.company.enums.ContributorType;
import com.sebcode.msproducts.company.enums.TaxRegime;
import com.sebcode.msproducts.company.repository.CompanyRepository;
import com.sebcode.msproducts.product.entity.*;
import com.sebcode.msproducts.product.enums.ImageType;
import com.sebcode.msproducts.product.repository.*;
import com.sebcode.msproducts.security.entity.Role;
import com.sebcode.msproducts.security.repository.RoleRepository;
import com.sebcode.msproducts.user.entity.User;
import com.sebcode.msproducts.user.enums.Gender;
import com.sebcode.msproducts.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Profile("dev")
@DependsOn("flyway")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final Flyway flyway;
    private final RoleRepository           roleRepository;
    private final UserRepository           userRepository;
    private final CategoryRepository       categoryRepository;
    private final SubcategoryRepository    subcategoryRepository;
    private final BrandRepository          brandRepository;
    private final ProductRepository        productRepository;
    private final VariantProductRepository variantProductRepository;
    private final AttributeTypeRepository  attributeTypeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final VariantAttributeRepository variantAttributeRepository;
    private final ProductImageRepository   productImageRepository;
    private final CompanyRepository        companyRepository;
    private final PasswordEncoder          passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            log.info("DataInitializer: datos ya existentes, se omite la carga.");
            return;
        }
        log.info("DataInitializer: iniciando carga de datos semilla...");

        List<Role>          roles      = insertRoles();
        List<User>          users      = insertUsers(roles);
        List<Company>       companies  = insertCompanies(users);
        List<AttributeType> attrTypes  = insertAttributeTypes();
        List<AttributeValue> attrVals  = insertAttributeValues(attrTypes, users.get(0));
        List<Category>      categories = insertCategories(users.get(0));
        List<Subcategory>   subs       = insertSubcategories(categories, users.get(0));
        insertPolos(subs, attrVals, users.get(0));

        log.info("DataInitializer: carga completada.");
    }

    // =========================================================================
    // ROLES
    // =========================================================================
    private List<Role> insertRoles() {
        return roleRepository.saveAll(List.of(
                Role.builder().name("ADMIN") .description("Super administrador del sistema").build(),
                Role.builder().name("SELLER").description("Vendedor de empresa").build(),
                Role.builder().name("USER")  .description("Cliente regular").build()
        ));
    }

    // =========================================================================
    // USUARIOS
    // =========================================================================
    private List<User> insertUsers(List<Role> roles) {
        Role roleAdmin  = roles.get(0);
        Role roleSeller = roles.get(1);
        Role roleUser   = roles.get(2);
        String hash = passwordEncoder.encode("admin123");

        User admin = User.builder()
                .email("admin@gmail.com").password(hash)
                .name("Sebastian").lastName("Ortega")
                .phone("987654321").address("Av. Javier Prado 123, San Isidro, Lima")
                .birthdate(LocalDate.of(1995, 3, 10))
                .gender(Gender.MALE).state(true)
                .roles(Set.of(roleAdmin, roleUser))
                .build();

        User seller = User.builder()
                .email("seller@gmail.com").password(hash)
                .name("Ana").lastName("Torres")
                .phone("999888777").address("Jr. Las Flores 456, Miraflores, Lima")
                .birthdate(LocalDate.of(1992, 7, 15))
                .gender(Gender.FEMALE).state(true)
                .roles(Set.of(roleSeller, roleUser))
                .build();

        User cliente = User.builder()
                .email("cliente@gmail.com").password(hash)
                .name("Carlos").lastName("Quispe")
                .phone("976543210").address("Av. Brasil 789, Jesús María, Lima")
                .birthdate(LocalDate.of(1998, 11, 25))
                .gender(Gender.MALE).state(true)
                .roles(Set.of(roleUser))
                .build();

        return userRepository.saveAll(List.of(admin, seller, cliente));
    }

    // =========================================================================
    // EMPRESAS
    // =========================================================================
    private List<Company> insertCompanies(List<User> users) {
        User admin  = users.get(0);
        User seller = users.get(1);

        CompanyUser ownerAdmin = CompanyUser.builder()
                .user(admin).role(CompanyRole.OWNER)
                .state(true).isDeleted(false)
                .build();

        CompanyUser employeeSeller = CompanyUser.builder()
                .user(seller).role(CompanyRole.MANAGER)
                .state(true).isDeleted(false)
                .build();

        Company exampleCompany = Company.builder()
                .legalName("COMEX S.A.C.")
                .tradeName("COMEX")
                .ruc("20601234567")
                .taxRegime(TaxRegime.GENERAL)
                .contributorType(ContributorType.LEGAL_ENTITY)
                .fiscalAddress("Av. La Marina 1234, San Miguel, Lima")
//                .phone("01-2345678")
//                .email("contacto@gmail.com")
//                .website("https://comex.com")
                .state(true).isDeleted(false)
                .companyUsers(List.of(ownerAdmin, employeeSeller))
                .build();

        ownerAdmin.setCompany(exampleCompany);
        employeeSeller.setCompany(exampleCompany);

        return companyRepository.saveAll(List.of(exampleCompany));
    }

    // =========================================================================
    // ATTRIBUTE TYPES
    // =========================================================================
    private List<AttributeType> insertAttributeTypes() {
        return attributeTypeRepository.saveAll(List.of(
                AttributeType.builder().name("Talla")    .displayOrder(1).isVisual(false).state(true).isDeleted(false).build(),
                AttributeType.builder().name("Color")    .displayOrder(2).isVisual(true) .state(true).isDeleted(false).build(),
                AttributeType.builder().name("Número")   .displayOrder(3).isVisual(false).state(true).isDeleted(false).build(),
                AttributeType.builder().name("Capacidad").displayOrder(4).isVisual(false).state(true).isDeleted(false).build(),
                AttributeType.builder().name("Sabor")    .displayOrder(5).isVisual(true) .state(true).isDeleted(false).build()
        ));
    }

    // =========================================================================
    // ATTRIBUTE VALUES
    // =========================================================================
    private List<AttributeValue> insertAttributeValues(List<AttributeType> types, User admin) {
        Long uid = admin.getId();
        AttributeType talla    = types.get(0);
        AttributeType color    = types.get(1);
        AttributeType numero   = types.get(2);
        AttributeType capacidad = types.get(3);
        AttributeType sabor    = types.get(4);

        return attributeValueRepository.saveAll(List.of(
                // Tallas
                AttributeValue.builder().value("S") .attributeType(talla).displayOrder(1).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("M") .attributeType(talla).displayOrder(2).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("L") .attributeType(talla).displayOrder(3).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("XL").attributeType(talla).displayOrder(4).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                // Colores
                AttributeValue.builder().value("BLACK").attributeType(color).displayOrder(1).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("WHITE").attributeType(color).displayOrder(2).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                // Números
                AttributeValue.builder().value("39").attributeType(numero).displayOrder(1).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("40").attributeType(numero).displayOrder(2).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("41").attributeType(numero).displayOrder(3).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("42").attributeType(numero).displayOrder(4).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                // Capacidad
                AttributeValue.builder().value("128GB").attributeType(capacidad).displayOrder(1).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("256GB").attributeType(capacidad).displayOrder(2).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                // Sabores
                AttributeValue.builder().value("CHOCOLATE").attributeType(sabor).displayOrder(1).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("COOKIES")  .attributeType(sabor).displayOrder(2).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build(),
                AttributeValue.builder().value("FRESA")    .attributeType(sabor).displayOrder(3).state(true).isDeleted(false).createdUser(uid).updateUser(uid).build()
        ));
    }

    // =========================================================================
    // CATEGORIES
    // =========================================================================
    private List<Category> insertCategories(User admin) {
        Long uid = admin.getId();
        return categoryRepository.saveAll(List.of(
                Category.builder()
                        .name("Ropa").description("Prendas de vestir y accesorios")
                        .imageUrl("https://cdn.comex.com/categories/ropa.webp")
                        .displayOrder(1).state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid).build(),
                Category.builder()
                        .name("Calzado").description("Zapatillas, zapatos y sandalias")
                        .imageUrl("https://cdn.comex.com/categories/calzado.webp")
                        .displayOrder(2).state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid).build(),
                Category.builder()
                        .name("Suplementos").description("Proteínas, vitaminas y suplementos deportivos")
                        .imageUrl("https://cdn.comex.com/categories/suplementos.webp")
                        .displayOrder(3).state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid).build()
        ));
    }

    // =========================================================================
    // SUBCATEGORIES
    // =========================================================================
    private List<Subcategory> insertSubcategories(List<Category> categories, User admin) {
        Long uid = admin.getId();
        Category ropa       = categories.get(0);
        Category calzado    = categories.get(1);
        Category suplementos = categories.get(2);

        return subcategoryRepository.saveAll(List.of(
                Subcategory.builder()
                        .name("Polos").description("Polos básicos y estampados")
                        .imageUrl("https://cdn.comex.com/subcategories/polos.webp")
                        .displayOrder(1).category(ropa).state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid).build(),
                Subcategory.builder()
                        .name("Hoodies").description("Hoodies y sudaderas")
                        .imageUrl("https://cdn.comex.com/subcategories/hoodies.webp")
                        .displayOrder(2).category(ropa).state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid).build(),
                Subcategory.builder()
                        .name("Zapatillas").description("Zapatillas deportivas y casuales")
                        .imageUrl("https://cdn.comex.com/subcategories/zapatillas.webp")
                        .displayOrder(1).category(calzado).state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid).build(),
                Subcategory.builder()
                        .name("Proteínas").description("Whey protein y proteínas vegetales")
                        .imageUrl("https://cdn.comex.com/subcategories/proteinas.webp")
                        .displayOrder(1).category(suplementos).state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid).build()
        ));
    }

    // =========================================================================
    // POLOS — 5 diseños x 2 colores x 4 tallas = 40 variantes
    // =========================================================================
    private void insertPolos(List<Subcategory> subcategories, List<AttributeValue> attrVals, User admin) {
        Long uid = admin.getId();
        Subcategory subPolos = subcategories.get(0); // Polos

        // Attribute values por índice según el orden de inserción
        AttributeValue tallaS  = attrVals.get(0);
        AttributeValue tallaM  = attrVals.get(1);
        AttributeValue tallaL  = attrVals.get(2);
        AttributeValue tallaXL = attrVals.get(3);
        AttributeValue black   = attrVals.get(4);
        AttributeValue white   = attrVals.get(5);

        // Brand SEB-WOLF
        Brand brand = brandRepository.save(
                Brand.builder().name("COMEX").state(true).isDeleted(false)
                        .createdUser(uid).updateUser(uid).build()
        );

        // ── 5 diseños de polos ──
        String[] designs = {
                "gokukaiokenx3",
                "gokusombra",
                "que_me_miras_flaco",
                "spiderman-tobey",
                "venom"
        };
        String[] designNames = {
                "Goku Kaioken x3",
                "Goku Sombra",
                "Que Me Miras Flaco",
                "Spiderman Tobey",
                "Venom"
        };

        for (int d = 0; d < designs.length; d++) {
            String design     = designs[d];
            String designName = designNames[d];

            // Crear el Product
            Product product = Product.builder()
                    .name("Polo Básico " + designName)
                    .description("Polo de algodón 100% con estampado de " + designName +
                            ". Disponible en colores negro y blanco.")
                    .score(new BigDecimal("4.5"))
                    .brand(brand)
                    .state(true).isDeleted(false)
                    .createdUser(uid).updateUser(uid)
                    .build();
            product.addSubcategory(subPolos);
            product = productRepository.save(product);

            // Crear variantes: 2 colores x 4 tallas = 8 variantes por diseño
            AttributeValue[] colors  = { black, white };
            String[]         colorNames = { "black", "white" };
            AttributeValue[] tallas  = { tallaS, tallaM, tallaL, tallaXL };
            String[]         tallaNames = { "S", "M", "L", "XL" };

            for (int c = 0; c < colors.length; c++) {
                AttributeValue colorVal  = colors[c];
                String         colorName = colorNames[c];

                // Imagen 600px y 1200px para esta combinación diseño+color
                String img600  = String.format("https://cdn.comex.com/polos/polo-basico-%s-%s-600.webp",  design, colorName);
                String img1200 = String.format("https://cdn.comex.com/polos/polo-basico-%s-%s-1200.webp", design, colorName);

                for (int t = 0; t < tallas.length; t++) {
                    AttributeValue tallaVal  = tallas[t];
                    String         tallaName = tallaNames[t];

                    String sku = String.format("polo-%s-%s-%s", design, colorName, tallaName).toLowerCase();

                    VariantProduct variant = VariantProduct.builder()
                            .sku(sku)
                            .price(new BigDecimal("49.90"))
                            .originalPrice(new BigDecimal("49.90"))
                            .stock(50)
                            .soldCount(0)
                            .product(product)
                            .state(true).isDeleted(false)
                            .createdUser(uid).updateUser(uid)
                            .build();
                    variant = variantProductRepository.save(variant);

                    // Atributo Talla
                    variantAttributeRepository.save(VariantAttribute.builder()
                            .variantProduct(variant)
                            .attributeType(tallaVal.getAttributeType())
                            .attributeValue(tallaVal)
                            .state(true).isDeleted(false)
                            .createdUser(uid).updateUser(uid)
                            .build());

                    // Atributo Color
                    variantAttributeRepository.save(VariantAttribute.builder()
                            .variantProduct(variant)
                            .attributeType(colorVal.getAttributeType())
                            .attributeValue(colorVal)
                            .state(true).isDeleted(false)
                            .createdUser(uid).updateUser(uid)
                            .build());

                    // Imágenes (solo para la primera talla de cada color, evitar duplicados)
                    if (t == 0) {
                        productImageRepository.save(ProductImage.builder()
                                .imageUrl(img600)
                                .imageOrder(1).isMain(true)
                                .altText("Polo " + designName + " " + colorName + " - 600px")
                                .imageType(ImageType.MAIN)
                                .attributeValue(colorVal)
                                .variantProduct(variant)
                                .state(true).isDeleted(false)
                                .createdUser(uid).updateUser(uid)
                                .build());

                        productImageRepository.save(ProductImage.builder()
                                .imageUrl(img1200)
                                .imageOrder(2).isMain(false)
                                .altText("Polo " + designName + " " + colorName + " - 1200px")
                                .imageType(ImageType.DETAIL)
                                .attributeValue(colorVal)
                                .variantProduct(variant)
                                .state(true).isDeleted(false)
                                .createdUser(uid).updateUser(uid)
                                .build());
                    }
                }
            }
            log.info("Polo '{}' creado con {} variantes", designName, colors.length * tallas.length);
        }
    }
}