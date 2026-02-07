package com.soa.onlinestorebackend.security.config;

import com.soa.onlinestorebackend.category.entity.Subcategory;
import com.soa.onlinestorebackend.category.repository.CategoryRepository;
import com.soa.onlinestorebackend.category.repository.SubcategoryRepository;
import com.soa.onlinestorebackend.company.repository.CompanyRepository;
import com.soa.onlinestorebackend.security.entity.Role;
import com.soa.onlinestorebackend.security.repository.RoleRepository;
import com.soa.onlinestorebackend.user.entity.User;
import com.soa.onlinestorebackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("dev") // ⚠️ Solo en desarrollo
public class DataInitializer {
//        implements CommandLineRunner {

    // Repositories
//    private final RoleRepository roleRepository;
//    private final UserRepository userRepository;
//    private final CompanyRepository companyRepository;
//    private final CompanyUserRepository companyUserRepository;
//    private final CartRepository cartRepository;
//    private final BrandRepository brandRepository;
//    private final ProductRepository productRepository;
//    private final VariantProductRepository variantProductRepository;
//    private final AttributeTypeRepository attributeTypeRepository;
//    private final AttributeValueRepository attributeValueRepository;
//    private final VariantAttributeRepository variantAttributeRepository;
//    private final ProductImgRepository productImgRepository;
//    private final CategoryRepository categoryRepository;
//    private final SubcategoryRepository subcategoryRepository;
//
//    private final PasswordEncoder passwordEncoder;
//
//    // Maps para referencias
//    private final Map<String, User> userMap = new HashMap<>();
//    private final Map<String, Brand> brandMap = new HashMap<>();
//    private final Map<String, Product> productMap = new HashMap<>();
//    private final Map<String, VariantProduct> variantProductMap = new HashMap<>();
//    private final Map<String, AttributeType> attributeTypeMap = new HashMap<>();
//    private final Map<String, AttributeValue> attributeValueMap = new HashMap<>();
//    private final Map<Integer, VariantAttribute> variantAttributeMap = new HashMap<>();
//    private final Map<String, Category> categoryMap = new HashMap<>();
//    private final Map<String, Subcategory> subcategoryMap = new HashMap<>();
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//        if (userRepository.count() > 0) {
//            log.info("📦 Database already initialized, skipping...");
//            return;
//        }
//
//        log.info("🚀 Initializing database with test data...");
//
//        initializeRoles();
//        initializeUsers();
//        initializeCompanies();
//        initializeCarts();
//        initializeBrands();
//        initializeProducts();
//        initializeVariantProducts();
//        initializeAttributeTypes();
//        initializeAttributeValues();
//        initializeVariantAttributes();
//        initializeProductImages();
//        initializeCategories();
//        initializeSubcategories();
//        linkProductsToSubcategories();
//
//        log.info("✅ Database initialized successfully!");
//        printLoginCredentials();
//    }
//
//    private void initializeRoles() {
//        log.info("Creating roles...");
//
//        Role admin = Role.builder()
//                .name("ADMIN")
//                .description("Super administrador del sistema")
//                .build();
//
//        Role user = Role.builder()
//                .name("USER")
//                .description("Usuario regular del sistema")
//                .build();
//
//        roleRepository.saveAll(List.of(admin, user));
//        log.info("✅ Roles created");
//    }
//
//    private void initializeUsers() {
//        log.info("Creating users...");
//
//        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
//        Role userRole = roleRepository.findByName("USER").orElseThrow();
//
//        List<User> users = List.of(
//                createUser("admin@sistema.com", "Admin", "Sistema", "987654321",
//                        "Av. Javier Prado 123, San Isidro, Lima", "1990-01-15", true, adminRole),
//                createUser("juan.perez@gmail.com", "Juan", "Pérez García", "999888777",
//                        "Jr. Las Flores 456, Miraflores, Lima", "1988-05-20", true, userRole),
//                createUser("maria.lopez@gmail.com", "María", "López Sánchez", "988777666",
//                        "Av. Larco 789, Miraflores, Lima", "1992-08-10", false, userRole),
//                createUser("carlos.ramirez@gmail.com", "Carlos", "Ramírez Torres", "977666555",
//                        "Av. Benavides 321, Surco, Lima", "1995-03-15", true, userRole),
//                createUser("ana.garcia@gmail.com", "Ana", "García Flores", "966555444",
//                        "Jr. Manco Cápac 567, La Victoria, Lima", "1998-11-25", false, userRole),
//                createUser("cliente@gmail.com", "Pedro", "Martínez Vega", "955444333",
//                        "Av. Universitaria 890, Los Olivos, Lima", "2000-07-08", true, userRole)
//        );
//
//        userRepository.saveAll(users);
//        users.forEach(u -> userMap.put(u.getEmail(), u));
//
//        log.info("✅ Users created: {} users", users.size());
//    }
//
//    private User createUser(String email, String name, String lastName, String phone,
//                            String address, String birthdate, boolean gender, Role role) {
//        return User.builder()
//                .email(email)
//                .password(passwordEncoder.encode("admin123"))
//                .name(name)
//                .lastName(lastName)
//                .phone(phone)
//                .address(address)
//                .birthdate(LocalDate.parse(birthdate))
//                .gender(gender)
//                .active(true)
//                .roles(Set.of(role))
//                .build();
//    }
//
//    private void initializeCompanies() {
//        log.info("Creating companies...");
//
//        User juan = userMap.get("juan.perez@gmail.com");
//        User maria = userMap.get("maria.lopez@gmail.com");
//        User carlos = userMap.get("carlos.ramirez@gmail.com");
//        User ana = userMap.get("ana.garcia@gmail.com");
//
//        Company casaStore = createCompany(
//                "Comercial Hogar y Decoración SAC", "Casa Store", "20123456789",
//                "Av. Los Olivos 1234, San Juan de Lurigancho, Lima", TaxRegime.GENERAL, true
//        );
//
//        Company ropaStore = createCompany(
//                "Textiles y Moda EIRL", "Ropa Store", "20987654321",
//                "Jr. Gamarra 567, La Victoria, Lima", TaxRegime.MYPE, true
//        );
//
//        Company techStore = createCompany(
//                "Tecnología Digital SAC", "Tech Store", "20456789123",
//                "Av. Javier Prado 890, San Isidro, Lima", TaxRegime.GENERAL, true
//        );
//
//        Company testStore = createCompany(
//                "Empresa Desactivada SAC", "Empresa Test", "20111222333",
//                "Av. Test 123, Lima", TaxRegime.RER, false
//        );
//
//        companyRepository.saveAll(List.of(casaStore, ropaStore, techStore, testStore));
//
//        // Company relationships
//        companyUserRepository.saveAll(List.of(
//                createCompanyUser(juan, casaStore, CompanyRole.OWNER),
//                createCompanyUser(carlos, casaStore, CompanyRole.MANAGER),
//                createCompanyUser(ana, casaStore, CompanyRole.SELLER),
//                createCompanyUser(maria, ropaStore, CompanyRole.OWNER),
//                createCompanyUser(ana, ropaStore, CompanyRole.SELLER)
//        ));
//
//        log.info("✅ Companies and relationships created");
//    }
//
//    private Company createCompany(String legalName, String tradeName, String ruc,
//                                  String address, TaxRegime taxRegime, boolean active) {
//        return Company.builder()
//                .legalName(legalName)
//                .tradeName(tradeName)
//                .ruc(ruc)
//                .fiscalAddress(address)
//                .taxRegime(taxRegime)
//                .active(active)
//                .isDeleted(false)
//                .build();
//    }
//
//    private CompanyUser createCompanyUser(User user, Company company, CompanyRole role) {
//        return CompanyUser.builder()
//                .user(user)
//                .company(company)
//                .role(role)
//                .active(true)
//                .build();
//    }
//
//    private void initializeCarts() {
//        log.info("Creating carts...");
//
//        List<Cart> carts = userMap.values().stream()
//                .map(user -> Cart.builder().user(user).build())
//                .toList();
//
//        cartRepository.saveAll(carts);
//        log.info("✅ Carts created: {}", carts.size());
//    }
//
//    private void initializeBrands() {
//        log.info("Creating brands...");
//
//        String[] brandNames = {"Nike", "Puma", "Adidas", "Fila", "Billabon",
//                "Optimum Nutrition", "Apple", "Samsung", "HP", "Lenovo", "THE NORTH FACE",
//                "Levi's", "Patapampa", "MBO", "Topitop", "Generico", "Badass"};
//
//        List<Brand> brands = Arrays.stream(brandNames)
//                .map(name -> Brand.builder().name(name).build())
//                .toList();
//
//        brandRepository.saveAll(brands);
//        brands.forEach(b -> brandMap.put(b.getName(), b));
//
//        log.info("✅ Brands created: {}", brands.size());
//    }
//
//    private void initializeProducts() {
//        log.info("Creating products...");
//
//        List<Product> products = List.of(
//                createProduct("Polo Basico", "Camiseta de algodón, cómoda y versátil.", 2.5, "Nike"),
//                createProduct("Men's antora jacket", "Calce estándar. Tejido DryVent™...", 2.5, "THE NORTH FACE"),
//                createProduct("Jeans Hombre Levi's 512 Slim Taper", "Encontrar los jeans perfectos...", 3.8, "Levi's"),
//                createProduct("Chompa All Over Jacquard", "Sumérgete en la rica cultura peruana...", 2.1, "Patapampa"),
//                createProduct("BIVIDI ESTAMPADO AZUL PV25", "Los bividí estampado son de 100% algodón...", 4.0, "MBO"),
//                createProduct("Zapatillas Urbanas Hombre Adidas Originals Samba Og", "Zapatillas Urbanas Hombre", 4.2, "Adidas"),
//                createProduct("Vestido Mujer Leonor Print Marron Chocolate", "Topitop ha diseñado prendas...", 4.4, "Topitop"),
//                createProduct("Falda Short Mini Yesi Mujer", "Descubre la elegancia en cada paso...", 4.2, "MBO"),
//                createProduct("Polo Basico Goku Kaioken X3", "Polo de algodón con estampado de Goku...", 4.5, "Badass"),
//                createProduct("Polo Basico Goku", "Polo de algodón con estampado de Goku...", 4.5, "Badass"),
//                createProduct("Polo Basico Esqueleto Que Me Miras Flaco", "Polo de algodón con estampado...", 4.5, "Badass"),
//                createProduct("Polo Basico Simbolo Arana", "Polo de algodón con estampado de Araña...", 4.5, "Badass"),
//                createProduct("Polo Basico Venom", "Polo de algodón con estampado de Venom...", 4.5, "Badass")
//        );
//
//        productRepository.saveAll(products);
//        for (int i = 0; i < products.size(); i++) {
//            productMap.put("P" + (i + 1), products.get(i));
//        }
//
//        log.info("✅ Products created: {}", products.size());
//    }
//
//    private Product createProduct(String name, String description, double score, String brandName) {
//        return Product.builder()
//                .name(name)
//                .description(description)
//                .state(true)
//                .score(BigDecimal.valueOf(score))
//                .brand(brandMap.get(brandName))
//                .build();
//    }
//
//    private void initializeVariantProducts() {
//        log.info("Creating variant products...");
//
//        // Aquí solo muestro algunos ejemplos, replica el patrón para todos
//        List<VariantProduct> variants = new ArrayList<>();
//
//        // Polo Basico
//        variants.add(createVariant("POLO-ROJO-XL", 4, 25.99, null, null, null, null, 150, "P1"));
//        variants.add(createVariant("POLO-ROJO-L", 4, 19.99, 10.0, 17.00, "2025-11-01", "2025-11-30", 150, "P1"));
//        variants.add(createVariant("POLO-BLANCO-M", 4, 19.99, 10.0, 17.00, "2025-11-01", "2025-11-30", 150, "P1"));
//        variants.add(createVariant("POLO-BLANCO-L", 4, 19.99, 10.0, 17.00, "2025-11-01", "2025-11-30", 150, "P1"));
//
//        // Men's antora jacket
//        variants.add(createVariant("VAR-MEN-JKT-01", 0, 350.00, 10.0, 315.00, "2025-11-01", "2025-11-30", 5, "P2"));
//        variants.add(createVariant("VAR-MEN-JKT-02", 0, 360.00, 5.0, 342.00, "2025-11-01", "2025-11-30", 3, "P2"));
//
//        // ... continúa con todos los demás productos siguiendo el patrón del data.sql
//
//        variantProductRepository.saveAll(variants);
//        for (int i = 0; i < variants.size(); i++) {
//            variantProductMap.put("VP" + (i + 1), variants.get(i));
//        }
//
//        log.info("✅ Variant products created: {}", variants.size());
//    }
//
//    private VariantProduct createVariant(String sku, int soldCount, double price,
//                                         Double discount, Double originalPrice,
//                                         String discountStart, String discountEnd,
//                                         int stock, String productKey) {
//        VariantProduct.VariantProductBuilder builder = VariantProduct.builder()
//                .sku(sku)
//                .soldCount(soldCount)
//                .price(BigDecimal.valueOf(price))
//                .stock(stock)
//                .product(productMap.get(productKey));
//
//        if (discount != null) {
//            builder.discount(BigDecimal.valueOf(discount))
//                    .originalPrice(BigDecimal.valueOf(originalPrice))
//                    .discountStartDate(LocalDate.parse(discountStart))
//                    .discountEndDate(LocalDate.parse(discountEnd));
//        }
//
//        return builder.build();
//    }
//
//    private void initializeAttributeTypes() {
//        log.info("Creating attribute types...");
//
//        List<AttributeType> types = List.of(
//                AttributeType.builder().name("TALLA-PRENDA").build(),
//                AttributeType.builder().name("TALLA-CALZADO").build(),
//                AttributeType.builder().name("COLOR").build(),
//                AttributeType.builder().name("MEMORIA-GB").build(),
//                AttributeType.builder().name("SABOR").build()
//        );
//
//        attributeTypeRepository.saveAll(types);
//        types.forEach(t -> attributeTypeMap.put(t.getName(), t));
//
//        log.info("✅ Attribute types created: {}", types.size());
//    }
//
//    private void initializeAttributeValues() {
//        log.info("Creating attribute values...");
//
//        List<AttributeValue> values = new ArrayList<>();
//
//        // TALLA-PRENDA
//        values.addAll(createAttributeValues("TALLA-PRENDA", "S", "M", "L", "XL"));
//
//        // TALLA-CALZADO
//        values.addAll(createAttributeValues("TALLA-CALZADO", "39", "40", "41", "42", "43", "44", "45"));
//
//        // COLOR
//        values.addAll(createAttributeValues("COLOR", "ROJO", "AZUL", "NEGRO", "BLANCO", "GRIS"));
//
//        // MEMORIA-GB
//        values.addAll(createAttributeValues("MEMORIA-GB", "128", "256"));
//
//        // SABOR
//        values.addAll(createAttributeValues("SABOR", "CHOCOLATE", "COOKIES", "FRESA"));
//
//        attributeValueRepository.saveAll(values);
//        values.forEach(v -> attributeValueMap.put(v.getValue(), v));
//
//        log.info("✅ Attribute values created: {}", values.size());
//    }
//
//    private List<AttributeValue> createAttributeValues(String typeName, String... values) {
//        AttributeType type = attributeTypeMap.get(typeName);
//        return Arrays.stream(values)
//                .map(v -> AttributeValue.builder()
//                        .value(v)
//                        .attributeType(type)
//                        .build())
//                .toList();
//    }
//
//    private void initializeVariantAttributes() {
//        log.info("Creating variant attributes...");
//
//        // Crea los VariantAttribute basándote en el data.sql
//        // Ejemplo simplificado:
//        List<VariantAttribute> variantAttributes = new ArrayList<>();
//
//        for (int i = 1; i <= 48; i++) {
//            VariantAttribute va = VariantAttribute.builder()
//                    .variantProduct(variantProductMap.get("VP" + i))
//                    .build();
//            variantAttributes.add(va);
//        }
//
//        variantAttributeRepository.saveAll(variantAttributes);
//        for (int i = 0; i < variantAttributes.size(); i++) {
//            variantAttributeMap.put(i + 1, variantAttributes.get(i));
//        }
//
//        log.info("✅ Variant attributes created: {}", variantAttributes.size());
//    }
//
//    private void initializeProductImages() {
//        log.info("Creating product images...");
//
//        // Simplificado - replica el patrón del data.sql
//        List<ProductImg> images = new ArrayList<>();
//
//        // Ejemplo:
//        images.add(createProductImg("Polo Básica", "img/products/camiseta_negra.jpg", 1));
//        // ... continúa con todas las imágenes
//
//        productImgRepository.saveAll(images);
//        log.info("✅ Product images created: {}", images.size());
//    }
//
//    private ProductImg createProductImg(String name, String url, int variantAttrIndex) {
//        return ProductImg.builder()
//                .name(name)
//                .imageUrl(url)
//                .variantAttribute(variantAttributeMap.get(variantAttrIndex))
//                .build();
//    }
//
//    private void initializeCategories() {
//        log.info("Creating categories...");
//
//        List<Category> categories = List.of(
//                Category.builder()
//                        .name("Ropa Hombre")
//                        .description("Categoría de ropa para hombres...")
//                        .imageUrl("img/categories/ropa_hombre.jpg")
//                        .build(),
//                Category.builder()
//                        .name("Ropa Mujer")
//                        .description("Ropa de mujer ideal para estaciones frías...")
//                        .imageUrl("img/categories/ropa_mujer.jpg")
//                        .build()
//        );
//
//        categoryRepository.saveAll(categories);
//        categories.forEach(c -> categoryMap.put(c.getName(), c));
//
//        log.info("✅ Categories created: {}", categories.size());
//    }
//
//    private void initializeSubcategories() {
//        log.info("Creating subcategories...");
//
//        // Implementa según tu data.sql
//
//        log.info("✅ Subcategories created");
//    }
//
//    private void linkProductsToSubcategories() {
//        log.info("Linking products to subcategories...");
//
//        // Implementa las relaciones product_subcategory
//
//        log.info("✅ Products linked to subcategories");
//    }
//
//    private void printLoginCredentials() {
//        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//        log.info("📧 LOGIN CREDENTIALS (password: admin123)");
//        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//        log.info("   🔑 Admin: admin@sistema.com");
//        log.info("   👤 Owner Casa: juan.perez@gmail.com");
//        log.info("   👤 Owner Ropa: maria.lopez@gmail.com");
//        log.info("   👤 Manager: carlos.ramirez@gmail.com");
//        log.info("   👤 Seller: ana.garcia@gmail.com");
//        log.info("   🛒 Cliente: cliente@gmail.com");
//        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//    }
}