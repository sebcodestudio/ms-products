
ALTER TABLE users MODIFY COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Haz lo mismo para las demás tablas:
ALTER TABLE roles MODIFY COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE companies MODIFY COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE company_user MODIFY COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
-- etc...

-- =====================================================
-- ROLES
-- =====================================================
INSERT INTO roles (name, description, created_user) VALUES
                                                        ('ADMIN', 'Super administrador del sistema', 1),
                                                        ('USER', 'Usuario regular del sistema', 1);

-- =====================================================
-- USUARIOS
-- =====================================================
-- Password para todos: "admin123" (hasheado con BCrypt)
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

-- 1. Super Admin del sistema
INSERT INTO users (email, password, name, last_name, phone, address, birthdate, gender, active) VALUES
    ('admin@sistema.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'Sistema', '987654321', 'Av. Javier Prado 123, San Isidro, Lima', '1990-01-15', 1, TRUE);

-- 2. Usuario Owner de Casa Store
INSERT INTO users (email, password, name, last_name, phone, address, birthdate, gender, active) VALUES
    ('juan.perez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Juan', 'Pérez García', '999888777', 'Jr. Las Flores 456, Miraflores, Lima', '1988-05-20', 1, TRUE);

-- 3. Usuario Owner de Ropa Store
INSERT INTO users (email, password, name, last_name, phone, address, birthdate, gender, active) VALUES
    ('maria.lopez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'María', 'López Sánchez', '988777666', 'Av. Larco 789, Miraflores, Lima', '1992-08-10', 2, TRUE);

-- 4. Usuario Manager de Casa Store
INSERT INTO users (email, password, name, last_name, phone, address, birthdate, gender, active) VALUES
    ('carlos.ramirez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Carlos', 'Ramírez Torres', '977666555', 'Av. Benavides 321, Surco, Lima', '1995-03-15', 1, TRUE);

-- 5. Usuario Seller de ambas tiendas
INSERT INTO users (email, password, name, last_name, phone, address, birthdate, gender, active) VALUES
    ('ana.garcia@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ana', 'García Flores', '966555444', 'Jr. Manco Cápac 567, La Victoria, Lima', '1998-11-25', 2, TRUE);

-- 6. Cliente regular (sin compañía)
INSERT INTO users (email, password, name, last_name, phone, address, birthdate, gender, active) VALUES
    ('cliente@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Pedro', 'Martínez Vega', '955444333', 'Av. Universitaria 890, Los Olivos, Lima', '2000-07-08', 1, TRUE);

-- =====================================================
-- ASIGNAR ROLES A USUARIOS
-- =====================================================
-- Admin del sistema
INSERT INTO user_role (id_user, id_role) VALUES (1, 1); -- ADMIN

-- Owners y usuarios regulares
INSERT INTO user_role (id_user, id_role) VALUES
                                             (2, 2), -- Juan = USER
                                             (3, 2), -- María = USER
                                             (4, 2), -- Carlos = USER
                                             (5, 2), -- Ana = USER
                                             (6, 2); -- Cliente = USER

-- =====================================================
-- COMPAÑÍAS
-- =====================================================
INSERT INTO companies (legal_name, trade_name, ruc, contributor_type, fiscal_address, tax_regime, created_user) VALUES
-- 1. Casa Store
('Comercial Hogar y Decoración SAC', 'Casa Store', '20123456789', 'LEGAL_ENTITY', 'Av. Los Olivos 1234, San Juan de Lurigancho, Lima', 'GENERAL', 1),

-- 2. Ropa Store
('Textiles y Moda EIRL', 'Ropa Store', '20987654321', 'LEGAL_ENTITY', 'Jr. Gamarra 567, La Victoria, Lima', 'MYPE', 1),

-- 3. Tech Store (sin usuarios asignados aún)
('Tecnología Digital SAC', 'Tech Store', '20456789123', 'LEGAL_ENTITY', 'Av. Javier Prado 890, San Isidro, Lima', 'GENERAL', 1),

-- 4. Compañía inactiva (para pruebas de filtros)
('Empresa Desactivada SAC', 'Empresa Test', '20111222333', 'LEGAL_ENTITY', 'Av. Test 123, Lima', 'RER', 1);

-- =====================================================
-- RELACIÓN USUARIOS <-> COMPAÑÍAS (company_users)
-- =====================================================
-- Casa Store (ID: 1)
INSERT INTO company_user (user_id, company_id, role) VALUES
                                                          (2, 1, 'OWNER'),    -- Juan es OWNER de Casa Store
                                                          (4, 1, 'MANAGER'),  -- Carlos es MANAGER de Casa Store
                                                          (5, 1, 'SELLER');   -- Ana es SELLER de Casa Store

-- Ropa Store (ID: 2)
INSERT INTO company_user (user_id, company_id, role) VALUES
                                                          (3, 2, 'OWNER'),    -- María es OWNER de Ropa Store
                                                          (5, 2, 'SELLER');   -- Ana es SELLER de Ropa Store (trabaja en ambas)

-- Insertar brand
INSERT INTO brands (name)
VALUES
    ('Nike'),
    ('Puma'),
    ('Adidas'),
    ('Fila'),
    ('Billabon'),
    ('Optimum Nutrition'),
    ('Apple'),
    ('Samsung'),
    ('HP'),
    ('Lenovo'),
    ('THE NORTH FACE'),
    ('Levi''s'),
    ('Patapampa'),
    ('MBO'),
    ('Topitop'),
    ('Generico'),
    ('Badass')
;

-- Insertar product
INSERT INTO products (name, description, state, score, id_brand, created_user, update_user)
VALUES
    ('Polo Basico', 'Camiseta de algodón, cómoda y versátil.', 1, 2.5, 1, 1, 1),
    ('Men''s antora jacket', 'Calce estándar. -/- El tejido DryVent™ de dos capas, impermeable, transpirable y con las costuras selladas y acabado hidrófugo duradero (DWR) te mantendrá siempre seco. -/- Tejido totalmente cortaviento. -/- Diseño de estilo alpino con bolsillos con cierre para las manos. -/- Capucha de tres piezas integrada con cordón ajustable y tope de bloqueo. -/- Cierre oculto por solapa de protección con cierre de velcro en la parte central delantera. -/- Refuerzos elásticos en los puños. -/- Ajuste lateral de la pretina. -/- Logo The North Face de transferencia térmica en el lado izquierdo del pecho y en la parte trasera derecha del hombro.', 1, 2.5, 11, 1, 1),
    ('Jeans Hombre Levi''s 512 Slim Taper', 'Encontrar los jeans perfectos no es tarea fácil, pero el 512 Slim Taper tiene todo lo que te gusta de nuestro Slim, aunque actualizado. Estos jeans logran un equilibrio perfecto entre el skinny y el taper: cuentan con la misma cintura que un 511, pero son más delgados en la pierna hacia el tobillo, lo que les da un look más moderno. Inspirados para que luzcas tu calzado. Un pantalón de corte delgado, pero con cinco bosillos. Cuentan con el stretch necesario para hacerlos cómodos todo el día.', 1, 3.8, 12, 1, 1),
    ('Chompa All Over Jacquard', 'Sumérgete en la rica cultura peruana con nuestra Chompa All Over Jacquard, un ejemplar que destaca por su cuidado artesanal y la representación de la autenticidad peruana. Los diseños originales y detallados no solo rinden homenaje a la tradición, sino que también reflejan la calidad excepcional y la fineza de la prenda. Confeccionada con un 100% de Fine Alpaca, esta chompa ofrece una experiencia de uso lujosa y suave al tiempo que resalta la belleza natural de la fibra. Hecha con maestría en Perú, cada diseño representa el trabajo meticuloso de artesanos expertos, fusionando la moda contemporánea con la riqueza cultural de la artesanía peruana.', 1, 2.1, 13, 1, 1),
    ('BIVIDI ESTAMPADO AZUL PV25', 'Los bividí estampado son de 100% algodón, son ideales para el clima de verano y su estampado es tipo geométrico.', 1, 4.0, 14, 2, 2),
    ('Zapatillas Urbanas Hombre Adidas Originals Samba Og', 'Zapatillas Urbanas Hombre', 1, 4.2, 3, 2, 2),

    ('Vestido Mujer Leonor Print Marron Chocolate', 'Topitop ha diseñado prendas para pasar esta temporada con moda y estilo de la marca Topitop Mujer. Renueva tu guardarropa con este Vestido Mujer en el color de tu preferencia y combínalos para armar tu look perfecto', 1, 4.4, 15, 2, 2),
    ('Falda Short Mini Yesi Mujer', 'Descubre la elegancia en cada paso con nuestra hermosa falda mini con short interno. Este modelo ceñido en tela loma es la elección perfecta para ocasiones formales o eventos especiales, amoldándose a tu figura con gracia. Combínalo con tu outfit favorito y haz una declaración de estilo.Características:- Falda mini con short interno para mayor comodidad y confianza.', 1, 4.2, 14, 2, 2),

    ('Polo Basico Goku Kaioken X3', 'Polo de algodón con estampado de Goku en alta calidad.', 1, 4.5, 17, 1, 1),
    ('Polo Basico Goku', 'Polo de algodón con estampado de Goku en alta calidad.', 1, 4.5, 17, 1, 1),
    ('Polo Basico Esqueleto Que Me Miras Flaco', 'Polo de algodón con estampado de Esqueleto Gym con frase Que Me Miras Flaco en alta calidad.', 1, 4.5, 17, 1, 1),
    ('Polo Basico Simbolo Arana', 'Polo de algodón con estampado de Arana en alta calidad.', 1, 4.5, 17, 1, 1),
    ('Polo Basico Venom', 'Polo de algodón con estampado de Venom en alta calidad.', 1, 4.5, 17, 1, 1)

;

-- variant product
INSERT INTO variant_products (sku, sold_count, price, discount, original_price, discount_start_date, discount_end_date, stock, id_product, created_user, update_user)
VALUES
    ('POLO-ROJO-XL', 4, 25.99, NULL, NULL, NULL, NULL, 150, 1, 1, 1),
    ('POLO-ROJO-L', 4, 19.99, 10, 17.00, '2025-11-01', '2025-11-30', 150, 1, 1, 1),
    ('POLO-BLANCO-M', 4, 19.99, 10, 17.00, '2025-11-01', '2025-11-30', 150, 1, 1, 1),
    ('POLO-BLANCO-L', 4, 19.99, 10, 17.00, '2025-11-01', '2025-11-30', 150, 1, 1, 1),
    ('VAR-MEN-JKT-01', 0, 350.00, 10, 315.00, '2025-11-01', '2025-11-30', 5, 2, 1, 1),
    ('VAR-MEN-JKT-02', 0, 360.00, 5, 342.00, '2025-11-01', '2025-11-30', 3, 2, 1, 1),
    ('VAR-LEVIS-512-01', 0, 280.00, 15, 238.00, '2025-11-01', '2025-11-30', 6, 3, 1, 1),
    ('VAR-LEVIS-512-02', 0, 290.00, 10, 261.00, '2025-11-01', '2025-11-30', 8, 3, 1, 1),
    ('VAR-CHOMPA-01', 0, 210.00, 12, 184.80, '2025-11-01', '2025-11-30', 4, 4, 1, 1),
    ('VAR-CHOMPA-02', 0, 220.00, 8, 202.40, '2025-11-01', '2025-11-30', 5, 4, 1, 1),
    ('VAR-BIVIDI-01', 0, 70.00, 20, 56.00, '2025-11-01', '2025-11-30', 10, 5, 2, 2),
    ('VAR-BIVIDI-02', 0, 75.00, 15, 63.75, '2025-11-01', '2025-11-30', 8, 5, 2, 2),
    ('VAR-SAMBA-01', 0, 400.00, 10, 360.00, '2025-11-01', '2025-11-30', 6, 6, 2, 2),
    ('VAR-SAMBA-02', 0, 420.00, 5, 399.00, '2025-11-01', '2025-11-30', 7, 6, 2, 2),
    ('VAR-VESTIDO-01', 0, 190.00, 10, 171.00, '2025-11-01', '2025-11-30', 5, 7, 2, 2),
    ('VAR-VESTIDO-02', 0, 200.00, 8, 184.00, '2025-11-01', '2025-11-30', 4, 7, 2, 2),
    ('VAR-FALDA-01', 0, 120.00, 12, 105.60, '2025-11-01', '2025-11-30', 9, 8, 2, 2),
    ('VAR-FALDA-02', 0, 130.00, 10, 117.00, '2025-11-01', '2025-11-30', 8, 8, 2, 2),

    ('PB-GKX3-BL-S', 0, 19.90, NULL, NULL, NULL, NULL, 10, 9, 1, NULL),
    ('PB-GKX3-BL-M', 0, 19.90, NULL, NULL, NULL, NULL, 10, 9, 1, NULL),
    ('PB-GKX3-BL-L', 0, 19.90, NULL, NULL, NULL, NULL, 10, 9, 1, NULL),
    ('PB-GKX3-WH-S', 0, 19.90, NULL, NULL, NULL, NULL, 10, 9, 1, NULL),
    ('PB-GKX3-WH-M', 0, 19.90, NULL, NULL, NULL, NULL, 10, 9, 1, NULL),
    ('PB-GKX3-WH-L', 0, 19.90, NULL, NULL, NULL, NULL, 10, 9, 1, NULL),

    ('PB-GK-BL-S', 0, 19.90, NULL, NULL, NULL, NULL, 10, 10, 1, NULL),
    ('PB-GK-BL-M', 0, 19.90, NULL, NULL, NULL, NULL, 10, 10, 1, NULL),
    ('PB-GK-BL-L', 0, 19.90, NULL, NULL, NULL, NULL, 10, 10, 1, NULL),
    ('PB-GK-WH-S', 0, 19.90, NULL, NULL, NULL, NULL, 10, 10, 1, NULL),
    ('PB-GK-WH-M', 0, 19.90, NULL, NULL, NULL, NULL, 10, 10, 1, NULL),
    ('PB-GK-WH-L', 0, 19.90, NULL, NULL, NULL, NULL, 10, 10, 1, NULL),

    ('PB-EQQMF-BL-S', 0, 19.90, NULL, NULL, NULL, NULL, 10, 11, 1, NULL),
    ('PB-EQQMF-BL-M', 0, 19.90, NULL, NULL, NULL, NULL, 10, 11, 1, NULL),
    ('PB-EQQMF-BL-L', 0, 19.90, NULL, NULL, NULL, NULL, 10, 11, 1, NULL),
    ('PB-EQQMF-WH-S', 0, 19.90, NULL, NULL, NULL, NULL, 10, 11, 1, NULL),
    ('PB-EQQMF-WH-M', 0, 19.90, NULL, NULL, NULL, NULL, 10, 11, 1, NULL),
    ('PB-EQQMF-WH-L', 0, 19.90, NULL, NULL, NULL, NULL, 10, 11, 1, NULL),

    ('PB-ARTB-BL-S', 0, 19.90, NULL, NULL, NULL, NULL, 10, 12, 1, NULL),
    ('PB-ARTB-BL-M', 0, 19.90, NULL, NULL, NULL, NULL, 10, 12, 1, NULL),
    ('PB-ARTB-BL-L', 0, 19.90, NULL, NULL, NULL, NULL, 10, 12, 1, NULL),
    ('PB-ARTB-WH-S', 0, 19.90, NULL, NULL, NULL, NULL, 10, 12, 1, NULL),
    ('PB-ARTB-WH-M', 0, 19.90, NULL, NULL, NULL, NULL, 10, 12, 1, NULL),
    ('PB-ARTB-WH-L', 0, 19.90, NULL, NULL, NULL, NULL, 10, 12, 1, NULL),

    ('PB-VE-BL-S', 0, 19.90, NULL, NULL, NULL, NULL, 10, 13, 1, NULL),
    ('PB-VE-BL-M', 0, 19.90, NULL, NULL, NULL, NULL, 10, 13, 1, NULL),
    ('PB-VE-BL-L', 0, 19.90, NULL, NULL, NULL, NULL, 10, 13, 1, NULL),
    ('PB-VE-WH-S', 0, 19.90, NULL, NULL, NULL, NULL, 10, 13, 1, NULL),
    ('PB-VE-WH-M', 0, 19.90, NULL, NULL, NULL, NULL, 10, 13, 1, NULL),
    ('PB-VE-WH-L', 0, 19.90, NULL, NULL, NULL, NULL, 10, 13, 1, NULL)

;

INSERT INTO attribute_types (name, created_user, update_user)
VALUES
    ('TALLA-PRENDA', 1, 1),
    ('TALLA-CALZADO', 1, 1),
    ('COLOR', 1, 1),
    ('MEMORIA-GB', 1, 1),
    ('SABOR', 1, 1)

;

INSERT INTO attribute_values (value, id_attribute_type, created_user, update_user)
VALUES
    ('S', 1, 1, 1),
    ('M', 1, 1, 1),
    ('L', 1, 1, 1),
    ('XL', 1, 1, 1),
    ('39', 2, 1, 1),
    ('40', 2, 1, 1),
    ('41', 2, 1, 1),
    ('42', 2, 1, 1),
    ('43', 2, 1, 1),
    ('44', 2, 1, 1),
    ('45', 2, 1, 1),
    ('ROJO', 3, 1, 1),
    ('AZUL', 3, 1, 1),
    ('NEGRO', 3, 1, 1),
    ('BLANCO', 3, 1, 1),
    ('GRIS', 3, 1, 1),
    ('128', 4, 1, 1),
    ('256', 4, 1, 1),
    ('CHOCOLATE', 5, 1, 1),
    ('COOKIES', 5, 1, 1),
    ('FRESA', 5, 1, 1)

;

-- Insertar variant_attribute
INSERT INTO variant_attributes (id_variant_product, created_user, update_user)
VALUES
    (1, 1, 2),
    (2, 1, 2),
    (3, 1, 2),
    (4, 1, 2),
    (5, 1, 2),
    (6, 1, 2),
    (7, 1, 2),
    (8, 1, 2),
    (9, 1, 2),
    (10, 1, 2),
    (11, 1, 2),
    (12, 1, 2),
    (13, 1, 2),
    (14, 1, 2),
    (15, 1, 2),
    (16, 1, 2),
    (17, 1, 2),
    (18, 1, 2),

    (19, 1, NULL),
    (20, 1, NULL),
    (21, 1, NULL),
    (22, 1, NULL),
    (23, 1, NULL),
    (24, 1, NULL),

    (25, 1, NULL),
    (26, 1, NULL),
    (27, 1, NULL),
    (28, 1, NULL),
    (29, 1, NULL),
    (30, 1, NULL),

    (31, 1, NULL),
    (32, 1, NULL),
    (33, 1, NULL),
    (34, 1, NULL),
    (35, 1, NULL),
    (36, 1, NULL),

    (37, 1, NULL),
    (38, 1, NULL),
    (39, 1, NULL),
    (40, 1, NULL),
    (41, 1, NULL),
    (42, 1, NULL),

    (43, 1, NULL),
    (44, 1, NULL),
    (45, 1, NULL),
    (46, 1, NULL),
    (47, 1, NULL),
    (48, 1, NULL)

;

INSERT INTO variant_attribute_attribute_value (id_attribute_value, id_variant_attribute)
VALUES
    (4, 1),  -- XL
    (12, 1), -- ROJO
    (3, 2),  -- L
    (12, 2), -- ROJO
    (2, 3),  -- M
    (15, 3), -- BLANCO
    (3, 4),  -- L
    (15, 4), -- BLANCO
    (3, 5),  -- L
    (16, 5), -- GRIS
    (4, 6),  -- XL
    (14, 6), -- NEGRO
    (3, 7),
    (4, 8),
    (3, 9),
    (13, 9),
    (4, 10),
    (16, 10),
    (2, 11),
    (13, 11),
    (3, 12),
    (12, 12),
    (7, 13),
    (15, 13),
    (8, 14),
    (14, 14),
    (3, 15),
    (19, 15),
    (4, 16),
    (15, 16),
    (2, 17),
    (13, 17),
    (3, 18),
    (14, 18),

    (1, 19),
    (15, 19),
    (2, 20),
    (15, 20),
    (3, 21),
    (15, 21),
    (1, 22),
    (14, 22),
    (2, 23),
    (14, 23),
    (3, 24),
    (14, 24),

    (1, 25),
    (15, 25),
    (2, 26),
    (15, 26),
    (3, 27),
    (15, 27),
    (1, 28),
    (14, 28),
    (2, 29),
    (14, 29),
    (3, 30),
    (14, 30),

    (1, 31),
    (15, 31),
    (2, 32),
    (15, 32),
    (3, 33),
    (15, 33),
    (1, 34),
    (14, 34),
    (2, 35),
    (14, 35),
    (3, 36),
    (14, 36),

    (1, 37),
    (15, 37),
    (2, 38),
    (15, 38),
    (3, 39),
    (15, 39),
    (1, 40),
    (14, 40),
    (2, 41),
    (14, 41),
    (3, 42),
    (14, 42),

    (1, 43),
    (15, 43),
    (2, 44),
    (15, 44),
    (3, 45),
    (15, 45),
    (1, 46),
    (14, 46),
    (2, 47),
    (14, 47),
    (3, 48),
    (14, 48)
;

-- Insertar product_img
INSERT INTO product_imgs (name, image_url, id_variant_attribute, created_user, update_user)
VALUES
    ('Polo Básica', 'img/products/camiseta_negra.jpg', 1, 1, 2),
    ('Polo Básica', 'img/products/camiseta_negra.jpg', 2, 1, 2),
    ('Polo Básica', 'img/products/camiseta_negra.jpg', 3, 1, 2),
    ('Polo Básica', 'img/products/camiseta_negra.jpg', 4, 1, 2),

    ('Men''s antora jacket', 'img/products/jacket.webp', 5, 1, 2),
    ('Men''s antora jacket', 'img/products/jacket.webp', 6, 1, 2),

    ('Jeans Hombre Levi''s 512 Slim Taper', 'img/products/jeans.webp', 7, 1, 2),
    ('Jeans Hombre Levi''s 512 Slim Taper', 'img/products/jeans.webp', 8, 1, 2),

    ('Chompa All Over Jacquard', 'img/products/chompa.webp', 9, 1, 2),
    ('Chompa All Over Jacquard', 'img/products/chompa.webp', 10, 1, 2),

    ('BIVIDI ESTAMPADO AZUL PV25', 'img/products/bividi10.webp', 11, 1, 2),
    ('BIVIDI ESTAMPADO AZUL PV25', 'img/products/bividi11.webp', 11, 1, 2),
    ('BIVIDI ESTAMPADO AZUL PV25', 'img/products/bividi12.webp', 11, 1, 2),
    ('BIVIDI ESTAMPADO AZUL PV25', 'img/products/bividi10.webp', 12, 1, 2),

    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas10.webp', 13, 1, 2),
    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas11.webp', 13, 1, 2),
    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas12.webp', 13, 1, 2),
    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas13.webp', 13, 1, 2),
    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas14.webp', 13, 1, 2),
    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas10.webp', 14, 1, 2),

    ('Vestido Mujer Leonor Print Marron Chocolate', 'img/products/vestido1.webp', 15, 1, 2),
    ('Vestido Mujer Leonor Print Marron Chocolate', 'img/products/vestido2.webp', 15, 1, 2),
    ('Vestido Mujer Leonor Print Marron Chocolate', 'img/products/vestido3.webp', 15, 1, 2),
    ('Vestido Mujer Leonor Print Marron Chocolate', 'img/products/vestido4.webp', 15, 1, 2),
    ('Vestido Mujer Leonor Print Marron Chocolate', 'img/products/vestido4.webp', 16, 1, 2),

    ('Falda Short Mini Yesi Mujer', 'img/products/falda10.webp', 17, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda11.webp', 17, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda12.webp', 17, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda13.webp', 17, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda14.webp', 17, 1, 2),

    ('Falda Short Mini Yesi Mujer', 'img/products/falda20.webp', 18, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda21.webp', 18, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda22.webp', 18, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda23.webp', 18, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda24.webp', 18, 1, 2),

    ('polo-basico-gokukaiokenx3-black-600', 'img/products/polo-basico-gokukaiokenx3-black-600.webp', 19, 1, NULL),
    ('polo-basico-gokukaiokenx3-black-1200', 'img/products/polo-basico-gokukaiokenx3-black-1200.webp', 19, 1, NULL),
    ('polo-basico-gokukaiokenx3-black-600', 'img/products/polo-basico-gokukaiokenx3-black-600.webp', 20, 1, NULL),
    ('polo-basico-gokukaiokenx3-black-1200', 'img/products/polo-basico-gokukaiokenx3-black-1200.webp', 20, 1, NULL),
    ('polo-basico-gokukaiokenx3-black-600', 'img/products/polo-basico-gokukaiokenx3-black-600.webp', 21, 1, NULL),
    ('polo-basico-gokukaiokenx3-black-1200', 'img/products/polo-basico-gokukaiokenx3-black-1200.webp', 21, 1, NULL),
    ('polo-basico-gokukaiokenx3-white-600', 'img/products/polo-basico-gokukaiokenx3-white-600.webp', 22, 1, NULL),
    ('polo-basico-gokukaiokenx3-white-1200', 'img/products/polo-basico-gokukaiokenx3-white-1200.webp', 22, 1, NULL),
    ('polo-basico-gokukaiokenx3-white-600', 'img/products/polo-basico-gokukaiokenx3-white-600.webp', 23, 1, NULL),
    ('polo-basico-gokukaiokenx3-white-1200', 'img/products/polo-basico-gokukaiokenx3-white-1200.webp', 23, 1, NULL),
    ('polo-basico-gokukaiokenx3-white-600', 'img/products/polo-basico-gokukaiokenx3-white-600.webp', 24, 1, NULL),
    ('polo-basico-gokukaiokenx3-white-1200', 'img/products/polo-basico-gokukaiokenx3-white-1200.webp', 24, 1, NULL),

    ('polo-basico-gokusombra-black-600', 'img/products/polo-basico-gokusombra-black-600.webp', 25, 1, NULL),
    ('polo-basico-gokusombra-black-1200', 'img/products/polo-basico-gokusombra-black-1200.webp', 25, 1, NULL),
    ('polo-basico-gokusombra-black-600', 'img/products/polo-basico-gokusombra-black-600.webp', 26, 1, NULL),
    ('polo-basico-gokusombra-black-1200', 'img/products/polo-basico-gokusombra-black-1200.webp', 26, 1, NULL),
    ('polo-basico-gokusombra-black-600', 'img/products/polo-basico-gokusombra-black-600.webp', 27, 1, NULL),
    ('polo-basico-gokusombra-black-1200', 'img/products/polo-basico-gokusombra-black-1200.webp', 27, 1, NULL),
    ('polo-basico-gokusombra-white-600', 'img/products/polo-basico-gokusombra-white-600.webp', 28, 1, NULL),
    ('polo-basico-gokusombra-white-1200', 'img/products/polo-basico-gokusombra-white-1200.webp', 28, 1, NULL),
    ('polo-basico-gokusombra-white-600', 'img/products/polo-basico-gokusombra-white-600.webp', 29, 1, NULL),
    ('polo-basico-gokusombra-white-1200', 'img/products/polo-basico-gokusombra-white-1200.webp', 29, 1, NULL),
    ('polo-basico-gokusombra-white-600', 'img/products/polo-basico-gokusombra-white-600.webp', 30, 1, NULL),
    ('polo-basico-gokusombra-white-1200', 'img/products/polo-basico-gokusombra-white-1200.webp', 30, 1, NULL),

    ('polo-basico-que_me_miras_flaco-black-600', 'img/products/polo-basico-que_me_miras_flaco-black-600.webp', 31, 1, NULL),
    ('polo-basico-que_me_miras_flaco-black-1200', 'img/products/polo-basico-que_me_miras_flaco-black-1200.webp', 31, 1, NULL),
    ('polo-basico-que_me_miras_flaco-black-600', 'img/products/polo-basico-que_me_miras_flaco-black-600.webp', 32, 1, NULL),
    ('polo-basico-que_me_miras_flaco-black-1200', 'img/products/polo-basico-que_me_miras_flaco-black-1200.webp', 32, 1, NULL),
    ('polo-basico-que_me_miras_flaco-black-600', 'img/products/polo-basico-que_me_miras_flaco-black-600.webp', 33, 1, NULL),
    ('polo-basico-que_me_miras_flaco-black-1200', 'img/products/polo-basico-que_me_miras_flaco-black-1200.webp', 33, 1, NULL),
    ('polo-basico-que_me_miras_flaco-white-600', 'img/products/polo-basico-que_me_miras_flaco-white-600.webp', 34, 1, NULL),
    ('polo-basico-que_me_miras_flaco-white-1200', 'img/products/polo-basico-que_me_miras_flaco-white-1200.webp', 34, 1, NULL),
    ('polo-basico-que_me_miras_flaco-white-600', 'img/products/polo-basico-que_me_miras_flaco-white-600.webp', 35, 1, NULL),
    ('polo-basico-que_me_miras_flaco-white-1200', 'img/products/polo-basico-que_me_miras_flaco-white-1200.webp', 35, 1, NULL),
    ('polo-basico-que_me_miras_flaco-white-600', 'img/products/polo-basico-que_me_miras_flaco-white-600.webp', 36, 1, NULL),
    ('polo-basico-que_me_miras_flaco-white-1200', 'img/products/polo-basico-que_me_miras_flaco-white-1200.webp', 36, 1, NULL),

    ('polo-basico-spiderman-tobey-black-600', 'img/products/polo-basico-spiderman-tobey-black-600.webp', 37, 1, NULL),
    ('polo-basico-spiderman-tobey-black-1200', 'img/products/polo-basico-spiderman-tobey-black-1200.webp', 37, 1, NULL),
    ('polo-basico-spiderman-tobey-black-600', 'img/products/polo-basico-spiderman-tobey-black-600.webp', 38, 1, NULL),
    ('polo-basico-spiderman-tobey-black-1200', 'img/products/polo-basico-spiderman-tobey-black-1200.webp', 38, 1, NULL),
    ('polo-basico-spiderman-tobey-black-600', 'img/products/polo-basico-spiderman-tobey-black-600.webp', 39, 1, NULL),
    ('polo-basico-spiderman-tobey-black-1200', 'img/products/polo-basico-spiderman-tobey-black-1200.webp', 39, 1, NULL),
    ('polo-basico-spiderman-tobey-white-600', 'img/products/polo-basico-spiderman-tobey-white-600.webp', 40, 1, NULL),
    ('polo-basico-spiderman-tobey-white-1200', 'img/products/polo-basico-spiderman-tobey-white-1200.webp', 40, 1, NULL),
    ('polo-basico-spiderman-tobey-white-600', 'img/products/polo-basico-spiderman-tobey-white-600.webp', 41, 1, NULL),
    ('polo-basico-spiderman-tobey-white-1200', 'img/products/polo-basico-spiderman-tobey-white-1200.webp', 41, 1, NULL),
    ('polo-basico-spiderman-tobey-white-600', 'img/products/polo-basico-spiderman-tobey-white-600.webp', 42, 1, NULL),
    ('polo-basico-spiderman-tobey-white-1200', 'img/products/polo-basico-spiderman-tobey-white-1200.webp', 42, 1, NULL),

    ('polo-basico-venom-black-600', 'img/products/polo-basico-venom-black-600.webp', 43, 1, NULL),
    ('polo-basico-venom-black-1200', 'img/products/polo-basico-venom-black-1200.webp', 43, 1, NULL),
    ('polo-basico-venom-black-600', 'img/products/polo-basico-venom-black-600.webp', 44, 1, NULL),
    ('polo-basico-venom-black-1200', 'img/products/polo-basico-venom-black-1200.webp', 44, 1, NULL),
    ('polo-basico-venom-black-600', 'img/products/polo-basico-venom-black-600.webp', 45, 1, NULL),
    ('polo-basico-venom-black-1200', 'img/products/polo-basico-venom-black-1200.webp', 45, 1, NULL),
    ('polo-basico-venom-white-600', 'img/products/polo-basico-venom-white-600.webp', 46, 1, NULL),
    ('polo-basico-venom-white-1200', 'img/products/polo-basico-venom-white-1200.webp', 46, 1, NULL),
    ('polo-basico-venom-white-600', 'img/products/polo-basico-venom-white-600.webp', 47, 1, NULL),
    ('polo-basico-venom-white-1200', 'img/products/polo-basico-venom-white-1200.webp', 47, 1, NULL),
    ('polo-basico-venom-white-600', 'img/products/polo-basico-venom-white-600.webp', 48, 1, NULL),
    ('polo-basico-venom-white-1200', 'img/products/polo-basico-venom-white-1200.webp', 48, 1, NULL)
;

INSERT INTO categories (name, description, created_user, update_user, image_url)
VALUES
    ('Ropa Hombre', 'Categoría de ropa para hombres, incluye camisetas, jeans, chaquetas, etc.', 1, 1, 'img/categories/ropa_hombre.jpg'),
    ('Ropa Mujer', 'Ropa de mujer ideal para estaciones frías, como sudaderas y chaquetas.', 1, 1, 'img/categories/ropa_mujer.jpg')

;

INSERT INTO subcategories (id_category, name, description, created_user, update_user, image_url)
VALUES
    (1, 'Polos', 'polito', 1, 1,'img/subcategories/polo_modelo.webp'),
    (1, 'Casacas', 'chompita', 1, 1, 'img/subcategories/casaca_modelo.webp'),
    (1, 'Pantalones', 'vividi comodo', 1, 1, 'img/subcategories/pantalon_modelo.webp'),
    (1, 'Chompas', 'chompita', 1, 1, 'img/subcategories/chompa.jpg'),
    (1, 'Bividis', 'bividi comodo', 1, 1, 'img/subcategories/bividi.jpeg'),
    (1, 'Zapatillas', 'Zapatilla comoda', 2, 2, 'img/subcategories/zapatillas_modelo.webp'),
    (2, 'Vestidos', 'vestidito', 1, 1, 'img/subcategories/vestido.jpg'),
    (2, 'Faldas', 'faldas', 1, 1, 'img/subcategories/falda.jpg'),
    (2, 'Casacas', 'casacasa mujer', 1, 1, 'img/subcategories/casaca_mujer.jpg'),
    (2, 'Pantalones', 'pantalon mujer', 1, 1, 'img/subcategories/pantalon_mujer.jpeg')

;

INSERT INTO product_subcategory (id_product, id_subcategory)
VALUES
    (1,1),
    (2,2),
    (3,3),
    (4,4),
    (5,5),
    (6,6),
    (7,7),
    (8,8),

    (9,1),
    (10,1),
    (11,1),
    (12,1),
    (13,1)

;
