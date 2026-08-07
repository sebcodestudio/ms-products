-- ============================================================
-- V3 — Productos demo para Calzado y Suplementos
--
-- Objetivo: las categorías "Calzado" y "Suplementos" (ids 2 y 3)
-- ya existían junto con sus subcategorías ("Zapatillas" id 3,
-- "Proteínas" id 4) y los attribute_types correspondientes
-- (Número id 3, Sabor id 5), pero sin productos — por lo que sus
-- secciones del Home (destacados/más vendidos por categoría)
-- quedaban vacías y ocultas. Se agregan productos reales con
-- variantes, sold_count variado (para probar el ordenamiento por
-- más vendidos) e imágenes ya subidas a MinIO (buckets "calzado"
-- y "suplementos").
-- ============================================================

-- Productos
INSERT INTO products (id, id_brand, name, description, score, state, is_deleted, created_at) VALUES
    (6, 1, 'Zapatillas Running Pro', 'Zapatillas deportivas para running, amortiguación premium.', 4.6, true, false, NOW()),
    (7, 1, 'Zapatillas Urbanas Street', 'Zapatillas urbanas casuales, estilo streetwear.', 4.3, true, false, NOW()),
    (8, 1, 'Proteína Whey Premium', 'Proteína de suero premium, 24g de proteína por porción.', 4.7, true, false, NOW()),
    (9, 1, 'Creatina Monohidratada 300g', 'Creatina monohidratada pura, sin sabor.', 4.5, true, false, NOW());
SELECT setval('products_id_seq', 9, true);

-- product_subcategory
INSERT INTO product_subcategory (id_product, id_subcategory) VALUES
    (6, 3),
    (7, 3),
    (8, 4),
    (9, 4);

-- variant_products (sold_count variado para probar orden "más vendidos")
INSERT INTO variant_products (id, id_product, sku, price, stock, sold_count, state, is_deleted, created_at) VALUES
    (41, 6, 'zapatillas-running-pro-40', 189.90, 20, 45, true, false, NOW()),
    (42, 6, 'zapatillas-running-pro-41', 189.90, 20, 30, true, false, NOW()),
    (43, 6, 'zapatillas-running-pro-42', 189.90, 15, 15, true, false, NOW()),
    (44, 7, 'zapatillas-urbanas-street-39', 159.90, 25, 22, true, false, NOW()),
    (45, 7, 'zapatillas-urbanas-street-41', 159.90, 20, 10, true, false, NOW()),
    (46, 8, 'proteina-whey-premium-chocolate', 129.90, 30, 60, true, false, NOW()),
    (47, 8, 'proteina-whey-premium-cookies', 129.90, 25, 40, true, false, NOW()),
    (48, 8, 'proteina-whey-premium-fresa', 129.90, 20, 25, true, false, NOW()),
    (49, 9, 'creatina-monohidratada-300g', 79.90, 40, 35, true, false, NOW());
SELECT setval('variant_products_id_seq', 49, true);

-- variant_attributes (Número=3 para Calzado, Sabor=5 para Suplementos; Creatina sin atributo)
INSERT INTO variant_attributes (id_variant_product, id_attribute_type, id_attribute_value, state, is_deleted, created_at) VALUES
    (41, 3, 8,  true, false, NOW()), -- Zapatillas Running Pro - Número 40
    (42, 3, 9,  true, false, NOW()), -- Zapatillas Running Pro - Número 41
    (43, 3, 10, true, false, NOW()), -- Zapatillas Running Pro - Número 42
    (44, 3, 7,  true, false, NOW()), -- Zapatillas Urbanas Street - Número 39
    (45, 3, 9,  true, false, NOW()), -- Zapatillas Urbanas Street - Número 41
    (46, 5, 13, true, false, NOW()), -- Proteína Whey Premium - Chocolate
    (47, 5, 14, true, false, NOW()), -- Proteína Whey Premium - Cookies
    (48, 5, 15, true, false, NOW()); -- Proteína Whey Premium - Fresa

-- product_images (una imagen MAIN por producto, sin atributo visual ya que
-- Número/Sabor no son atributos visuales como Color)
INSERT INTO product_images (id_product, image_url, image_order, is_main, alt_text, image_type, state, is_deleted, created_at) VALUES
    (6, 'http://localhost:9000/calzado/zapatillas-running-pro.webp', 1, true, 'Zapatillas Running Pro', 'MAIN', true, false, NOW()),
    (7, 'http://localhost:9000/calzado/zapatillas-urbanas-street.webp', 1, true, 'Zapatillas Urbanas Street', 'MAIN', true, false, NOW()),
    (8, 'http://localhost:9000/suplementos/proteina-whey-premium.webp', 1, true, 'Proteína Whey Premium', 'MAIN', true, false, NOW()),
    (9, 'http://localhost:9000/suplementos/creatina-monohidratada-300g.webp', 1, true, 'Creatina Monohidratada 300g', 'MAIN', true, false, NOW());

-- Variar sold_count de los polos existentes (todos en 0) para que "Más
-- vendidos" y el orden dentro de "Ropa" no dependan solo del ID.
UPDATE variant_products SET sold_count = 50 WHERE id = 1;  -- Goku Kaioken x3 (black-s)
UPDATE variant_products SET sold_count = 38 WHERE id = 9;  -- Goku Sombra (black-s)
UPDATE variant_products SET sold_count = 28 WHERE id = 17; -- Que Me Miras Flaco (black-s)
UPDATE variant_products SET sold_count = 20 WHERE id = 25; -- Spiderman Tobey (black-s)
UPDATE variant_products SET sold_count = 12 WHERE id = 33; -- Venom (black-s)
