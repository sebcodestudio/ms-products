-- Insertar brand
INSERT INTO brand (name)
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
    ('Generico')
    ;

-- Insertar product
INSERT INTO product (name, description, state, sold_count, score, id_brand, created_user, update_user)
VALUES
    ('Polo Basico', 'Camiseta de algodón, cómoda y versátil.', 1, 20, 2.5, 16, 1, 1),
    ('Men''s antora jacket', 'Calce estándar. -/- El tejido DryVent™ de dos capas, impermeable, transpirable y con las costuras selladas y acabado hidrófugo duradero (DWR) te mantendrá siempre seco. -/- Tejido totalmente cortaviento. -/- Diseño de estilo alpino con bolsillos con cierre para las manos. -/- Capucha de tres piezas integrada con cordón ajustable y tope de bloqueo. -/- Cierre oculto por solapa de protección con cierre de velcro en la parte central delantera. -/- Refuerzos elásticos en los puños. -/- Ajuste lateral de la pretina. -/- Logo The North Face de transferencia térmica en el lado izquierdo del pecho y en la parte trasera derecha del hombro.', 1, 14, 2.5, 11, 1, 1),
    ('Jeans Hombre Levi''s 512 Slim Taper', 'Encontrar los jeans perfectos no es tarea fácil, pero el 512 Slim Taper tiene todo lo que te gusta de nuestro Slim, aunque actualizado. Estos jeans logran un equilibrio perfecto entre el skinny y el taper: cuentan con la misma cintura que un 511, pero son más delgados en la pierna hacia el tobillo, lo que les da un look más moderno. Inspirados para que luzcas tu calzado. Un pantalón de corte delgado, pero con cinco bosillos. Cuentan con el stretch necesario para hacerlos cómodos todo el día.', 1, 18, 3.8, 12, 1, 1),
    ('Chompa All Over Jacquard', 'Sumérgete en la rica cultura peruana con nuestra Chompa All Over Jacquard, un ejemplar que destaca por su cuidado artesanal y la representación de la autenticidad peruana. Los diseños originales y detallados no solo rinden homenaje a la tradición, sino que también reflejan la calidad excepcional y la fineza de la prenda. Confeccionada con un 100% de Fine Alpaca, esta chompa ofrece una experiencia de uso lujosa y suave al tiempo que resalta la belleza natural de la fibra. Hecha con maestría en Perú, cada diseño representa el trabajo meticuloso de artesanos expertos, fusionando la moda contemporánea con la riqueza cultural de la artesanía peruana.', 1, 14, 2.1, 13, 1, 1),
    ('BIVIDI ESTAMPADO AZUL PV25', 'Los bividí estampado son de 100% algodón, son ideales para el clima de verano y su estampado es tipo geométrico.', 1, 23, 4.0, 14, 2, 2),
    ('Zapatillas Urbanas Hombre Adidas Originals Samba Og', 'Zapatillas Urbanas Hombre', 1, 11, 4.2, 3, 2, 2),

    ('Vestido Mujer Leonor Print Marron Chocolate', 'Topitop ha diseñado prendas para pasar esta temporada con moda y estilo de la marca Topitop Mujer. Renueva tu guardarropa con este Vestido Mujer en el color de tu preferencia y combínalos para armar tu look perfecto', 1, 14, 4.4, 15, 2, 2),
    ('Falda Short Mini Yesi Mujer', 'Descubre la elegancia en cada paso con nuestra hermosa falda mini con short interno. Este modelo ceñido en tela loma es la elección perfecta para ocasiones formales o eventos especiales, amoldándose a tu figura con gracia. Combínalo con tu outfit favorito y haz una declaración de estilo.Características:- Falda mini con short interno para mayor comodidad y confianza.', 1, 16, 4.2, 16, 2, 2);
    -- ('Sudadera', 'Sudadera', 1, 25, 4.6, 4, 2, 2),
    -- ('Sudadera', 'Sudadera', 1, 31, 4.8, 4, 2, 2);

    -- ('Sudadera con Capucha', 'Sudadera de felpa con capucha, ideal para el frío.', 1, 10, 4, 4, 2, 2),
    -- ('Zapatos Deportivos', 'Zapatos deportivos ligeros y cómodos para el día a día.', 1, 10, 5, 5, 2, 2),
    -- ('Zapatillas Running', 'zapatilla', 1, 10, 4.5, 2, 1, 1),
    -- ('Smartwatch Pro', 'smart', 1, 10, 4.2, 2, 1, 1),
    -- ('Proteína Whey', 'proteina', 1, 10, 4.8, 3, 1, 1),
    -- ('Polo Deportivo', 'polo', 1, 10, 4.0, 4, 1, 1),
    -- ('Laptop Gamer', 'Potente laptop con tarjeta gráfica dedicada.', 1, 10, 4.7, 2, 1, 1),
    -- ('Smartphone', 'Último modelo con cámara de alta resolución.', 1, 10, 4.5, 2, 1, 1),
    -- ('Auriculares Inalámbricos', 'Cancelación de ruido y batería de 30 horas.', 1, 10, 4.6, 3, 1, 1),
    -- ('Smartwatch', 'Monitorea tu salud y recibe notificaciones.', 1, 10, 4.4, 4, 1, 1),
    -- ('Teclado Mecánico', 'Retroiluminado y switches azules.', 1, 10, 4.8, 5, 1, 1);

-- variant product
INSERT INTO variant_product (sku, sold_count, price, discount, price_discount, discount_start_date, discount_end_date, stock, id_product, created_user, update_user)
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
    ('VAR-FALDA-02', 0, 130.00, 10, 117.00, '2025-11-01', '2025-11-30', 8, 8, 2, 2);
    -- ('POLO-ROJO-XL', 4, 25.99, null, null, 150, 1, 1, 1),
    -- ('POLO-ROJO-L', 4, 19.99, 10, 17.00, 150, 1, 1, 1),
    -- ('POLO-BLANCO-M', 4, 19.99, 10, 17.00, 150, 1, 1, 1),
    -- ('POLO-BLANCO-L', 4, 19.99, 10, 17.00, 150, 1, 1, 1),
    -- ('VAR-MEN-JKT-01', 0, 350.00, 10, 315.00, 5, 2, 1, 1),
    -- ('VAR-MEN-JKT-02', 0, 360.00, 5, 342.00, 3, 2, 1, 1),
    -- ('VAR-LEVIS-512-01', 0, 280.00, 15, 238.00, 6, 3, 1, 1),
    -- ('VAR-LEVIS-512-02', 0, 290.00, 10, 261.00, 8, 3, 1, 1),
    -- ('VAR-CHOMPA-01', 0, 210.00, 12, 184.80, 4, 4, 1, 1),
    -- ('VAR-CHOMPA-02', 0, 220.00, 8, 202.40, 5, 4, 1, 1),
    -- ('VAR-BIVIDI-01', 0, 70.00, 20, 56.00, 10, 5, 2, 2),
    -- ('VAR-BIVIDI-02', 0, 75.00, 15, 63.75, 8, 5, 2, 2),
    -- ('VAR-SAMBA-01', 0, 400.00, 10, 360.00, 6, 6, 2, 2),
    -- ('VAR-SAMBA-02', 0, 420.00, 5, 399.00, 7, 6, 2, 2),
    -- ('VAR-VESTIDO-01', 0, 190.00, 10, 171.00, 5, 7, 2, 2),
    -- ('VAR-VESTIDO-02', 0, 200.00, 8, 184.00, 4, 7, 2, 2),
    -- ('VAR-FALDA-01', 0, 120.00, 12, 105.60, 9, 8, 2, 2),
    -- ('VAR-FALDA-02', 0, 130.00, 10, 117.00, 8, 8, 2, 2);

    -- ('BG02', 3, 35.00, 20, 30.00, 80, 2, 1, 1),
    -- ('BG03', 2, 120.00, 10, 110, 50, 3, 1, 1),
    -- ('BG04', 1, 45.00, 5, 40.00, 200, 4, 2, 2),
    -- ('BG05', 45, 65.00, 2, 63.00, 100, 5, 2, 2),
    -- ('VR01', 45, 220.00, 5, 209.00, 100, 6, 1, 1), -- Zapatillas Running
    -- ('TEC01', 25, 1200.00, 0, 1200.00, 50, 10, 1, 1), -- Laptop Gamer
    -- ('TEC02', 30, 800.00, 5, 760.00, 100, 11, 1, 1),  -- Smartphone
    -- ('TEC03', 40, 150.00, 10, 135.00, 200, 12, 1, 1), -- Auriculares
    -- ('TEC04', 35, 200.00, 8, 184.00, 150, 13, 1, 1),  -- Smartwatch
    -- ('TEC05', 20, 100.00, 0, 100.00, 120, 14, 1, 1);  -- Teclado Mecánico
-- INSERT INTO variant_product (sku, sold_count, price, discount, price_discount, stock, id_product, created_user, update_user, discount_start_date, discount_end_date, discount_update_user)
-- VALUES
--     ('VR02', 42, 350.00, 10, 315.00, 80, 7, 1, 1, '2025-10-01', '2025-12-31', 1),
--     ('VR03', 48, 120.00, 5, 114.00, 200, 8, 1, 1, '2025-10-01', '2025-12-31', 1),
--     ('VR04', 40, 70.00, 15, 59.50, 160, 9, 1, 1, '2025-10-01', '2025-12-31', 1);



INSERT INTO attribute_type (name, created_user, update_user)
VALUES
    ('TALLA-PRENDA', 1, 1),
    ('TALLA-CALZADO', 1, 1),
    ('COLOR', 1, 1),
    ('MEMORIA-GB', 1, 1),
    ('SABOR', 1, 1);

INSERT INTO attribute_value (value, id_attribute_type, created_user, update_user)
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
    ('FRESA', 5, 1, 1);

-- Insertar variant_attribute
INSERT INTO variant_attribute (id_variant_product, created_user, update_user)
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
    (18, 1, 2);

INSERT INTO variant_attribute_attribute_value (id_attribute_value, id_variant)
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
    (14, 18);
    -- (4, 1),
    -- (12, 1),
    -- (12, 2),
    -- (13, 2),
    -- (14, 2),
    -- (15, 2),
    -- (16, 2),
    -- (14, 3),
    -- (2, 3),
    -- (3, 3),
    -- (14, 4),
    -- (15, 4),
    -- (2, 4),
    -- (3, 4),
    -- (14, 5),
    -- (15, 5),
    -- (5, 5),
    -- (6, 5),
    -- (7, 5),
    -- (8, 5),
    -- (9, 5),
    -- (14, 6),
    -- (15, 6),
    -- (5, 6),
    -- (6, 6),
    -- (7, 6),
    -- (8, 6),
    -- (9, 6),
    -- (17, 7),
    -- (18, 7),
    -- (19, 8),
    -- (20, 8),
    -- (21, 8),
    -- (1, 9),
    -- (2, 9),
    -- (3, 9),
    -- (4, 9),
    -- (12, 9),
    -- (13, 9),
    -- (14, 10),
    -- (15, 10),
    -- (17, 10),
    -- (18, 10),
    -- (14, 11),
    -- (15, 11),
    -- (17, 11),
    -- (18, 11),
    -- (14, 12),
    -- (15, 12),
    -- (14, 13),
    -- (15, 13),
    -- (14, 14);

-- Insertar product_img
INSERT INTO product_img (name, image_url, id_variant_attribute, created_user, update_user)
VALUES
    ('Polo Básica', 'img/products/camiseta_negra.jpg', 1, 1, 2),

    ('Men''s antora jacket', 'img/products/jacket.webp', 5, 1, 2),

    ('Jeans Hombre Levi''s 512 Slim Taper', 'img/products/jeans.webp', 7, 1, 2),

    ('Chompa All Over Jacquard', 'img/products/chompa.webp', 9, 1, 2),

    ('BIVIDI ESTAMPADO AZUL PV25', 'img/products/bividi10.webp', 11, 1, 2),
    ('BIVIDI ESTAMPADO AZUL PV25', 'img/products/bividi11.webp', 11, 1, 2),
    ('BIVIDI ESTAMPADO AZUL PV25', 'img/products/bividi12.webp', 11, 1, 2),

    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas10.webp', 13, 1, 2),
    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas11.webp', 13, 1, 2),
    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas12.webp', 13, 1, 2),
    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas13.webp', 13, 1, 2),
    ('Zapatillas Urbanas Mujer Adidas Originals Samba Og', 'img/products/zapatillas14.webp', 13, 1, 2),

    ('Vestido Mujer Leonor Print Marron Chocolate', 'img/products/vestido1.webp', 15, 1, 2),
    ('Vestido Mujer Leonor Print Marron Chocolate', 'img/products/vestido2.webp', 15, 1, 2),
    ('Vestido Mujer Leonor Print Marron Chocolate', 'img/products/vestido3.webp', 15, 1, 2),
    ('Vestido Mujer Leonor Print Marron Chocolate', 'img/products/vestido4.webp', 15, 1, 2),

    ('Falda Short Mini Yesi Mujer', 'img/products/falda10.webp', 17, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda11.webp', 17, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda12.webp', 17, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda13.webp', 17, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda14.webp', 17, 1, 2),

    ('Falda Short Mini Yesi Mujer', 'img/products/falda20.webp', 18, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda21.webp', 18, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda22.webp', 18, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda23.webp', 18, 1, 2),
    ('Falda Short Mini Yesi Mujer', 'img/products/falda24.webp', 18, 1, 2);

    -- ('Jeans Ajustados', 'img/products/jeans_azul.jpg', 2, 1, 2),
    -- ('Chaqueta de Cuero', 'img/products/chaqueta_cuero.jpg', 3, 1, 2),
    -- ('Sudadera con Capucha', 'img/products/gorra.jpg', 4, 1, 2),
    -- ('Zapatos Deportivos', 'img/products/zapatillas.jpg', 5, 1, 2),
    -- ('Zapatillas Running', 'img/products/zapatilla-deportiva.webp', 6, 1, 1),
    -- ('Smartwatch Pro', 'img/products/smart-watch.avif', 7, 1, 1),
    -- ('Proteína Whey', 'img/products/proteina-whey.webp', 8, 1, 1),
    -- ('Polo Deportivo', 'img/products/polo-deportivo.png', 9, 1, 1),
    -- ('Laptop Gamer', 'img/products/laptopgamer.webp', 10, 1, 1),
    -- ('Smartphone', 'img/products/smart-phone.avif', 11, 1, 1),
    -- ('Auriculares Inalámbricos', 'img/products/auriculares_inalambricos.webp', 12, 1, 1),
    -- ('Smartwatch', 'img/products/smart-watch.avif', 13, 1, 1),
    -- ('Teclado Mecánico', 'img/products/teclado_mecanico.webp', 14, 1, 1);

INSERT INTO category (name, description, created_user, update_user, image_url)
VALUES
    ('Ropa Hombre', 'Categoría de ropa para hombres, incluye camisetas, jeans, chaquetas, etc.', 1, 1, 'img/categories/ropa_hombre.jpg'),
    ('Ropa Mujer', 'Ropa de mujer ideal para estaciones frías, como sudaderas y chaquetas.', 1, 1, 'img/categories/ropa_mujer.jpg');
    -- ('Tecnología', 'Categoría de tecnología y gadgets.', 1, 1, 'img/categories/tecnologia.jpg'),
    -- ('Nutrición', 'Suplementos y productos de nutrición.', 1, 1, 'img/categories/suplementos.jpg'),
    -- ('Deporte', 'Artículos y ropa deportiva.', 1, 1, 'img/categories/deporte.jpg');

INSERT INTO subcategory (id_category, name, description, created_user, update_user, image_url)
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
    (2, 'Pantalones', 'pantalon mujer', 1, 1, 'img/subcategories/pantalon_mujer.jpeg');
    -- (2, 'Sombreros', 'sombreros mujer', 1, 1, 'img/subcategories/sombrero_mujer.jpg'),
    -- (2, 'Chompas', 'chompas mujer', 1, 1, 'img/subcategories/chompa_mujer.jpg'),
    -- (3, 'Gadgets', 'Dispositivos electrónicos modernos.', 1, 1, 'img/subcategories/gadgets.webp'),
    -- (3, 'Tablets', 'tablets', 1, 1, 'img/subcategories/tablets.avif'),
    -- (3, 'Celulares', 'celulares', 1, 1, 'img/subcategories/celulares.jpeg'),
    -- (3, 'TVs', 'tv', 1, 1, 'img/subcategories/tv.avif'),
    -- (3, 'laptops', 'laptops', 1, 1, 'img/subcategories/laptops.jpeg'),
    -- (3, 'Parlantes', 'parlantes', 1, 1, 'img/subcategories/parlantes.jpg'),
    -- (4, 'Proteinas', 'proteinas', 1, 1, 'img/subcategories/proteinas.webp'),
    -- (4, 'Creatinas', 'creatinas', 1, 1, 'img/subcategories/creatina.webp'),
    -- (4, 'Colagenos', 'colagenos', 1, 1, 'img/subcategories/colageno.webp'),
    -- (4, 'Vitaminas', 'vitaminas', 1, 1, 'img/subcategories/vitaminas.jpg'),
    -- (4, 'Preentrenos', 'preentrenos', 1, 1, 'img/subcategories/preentreno.webp'),
    -- (4, 'Carbohidratos', 'carbohidratos', 1, 1, 'img/subcategories/carbos.jpg'),
    -- (5, 'Pesas', 'pesas', 1, 1, 'img/subcategories/pesas.webp'),
    -- (5, 'Maquinas', 'maquinas', 1, 1, 'img/subcategories/maquinas.webp'),
    -- (5, 'Ropa Hombre Deportiva', 'ropa hombre deportiva', 1, 1, 'img/subcategories/ropa_hombre_deportiva.webp'),
    -- (5, 'Ropa Mujer Deportiva', 'carbohidratos', 1, 1, 'img/subcategories/ropa_mujer_deportiva.jpeg'),
    -- (5, 'Balones', 'balones', 1, 1, 'img/subcategories/balones.webp'),
    -- (5, 'Gadgets', 'gadgets', 1, 1, 'img/subcategories/gadgets.jpg');

INSERT INTO product_subcategory (id_product, id_subcategory)
VALUES
    (1,1),
    (2,2),
    (3,3),
    (4,4),
    (5,5),
    (6,6),
    (7,7);
    -- (6,5), -- Zapatillas Running -> Zapatilla
    -- (7,2), -- Smartwatch Pro -> Chompa (provisional, ya que no tienes categoría tech)
    -- (8,1), -- Proteína Whey -> Polo (provisional)
    -- (9,1), -- Polo Deportivo -> Polo
    -- (10, 6), -- Laptop Gamer -> Gadgets
    -- (11, 6), -- Smartphone -> Gadgets
    -- (12, 6), -- Auriculares -> Gadgets
    -- (13, 6), -- Smartwatch -> Gadgets
    -- (14, 6); -- Teclado Mecánico -> Gadgets










-- INSERT INTO category (name, description, created_user, update_user, image_url)
-- VALUES
--     ('Ropa Hombre', 'Ropa para hombres: polos, jeans, chaquetas, etc.', 1, 1, 'img/categories/ropa_hombre.jpg'),
--     ('Ropa Mujer', 'Ropa moderna y cómoda para mujeres.', 1, 1, 'img/categories/ropa_mujer.jpg'),
--     ('Tecnología', 'Dispositivos, accesorios y gadgets tecnológicos.', 1, 1, 'img/categories/tecnologia.jpg'),
--     ('Nutrición', 'Suplementos deportivos y alimentos saludables.', 1, 1, 'img/categories/nutricion.jpg'),
--     ('Deporte', 'Ropa y accesorios deportivos.', 1, 1, 'img/categories/deporte.jpg');

-- INSERT INTO subcategory (name, description, created_user, update_user, id_category, image_url)
-- VALUES
--     ('Polos', 'Polos básicos, deportivos y de moda.', 1, 1, 1, 'img/subcategories/polo_modelo.webp'),
--     ('Jeans', 'Pantalones denim ajustados o clásicos.', 1, 1, 1, 'img/subcategories/jeans_modelo.webp'),
--     ('Chaquetas', 'Chaquetas de cuero, jean o abrigo.', 1, 1, 1, 'img/subcategories/chaqueta_modelo.webp'),
--     ('Zapatillas', 'Zapatillas deportivas y casuales.', 1, 1, 5, 'img/subcategories/zapatillas_modelo.webp'),
--     ('Gadgets', 'Smartphones, smartwatches y otros dispositivos.', 1, 1, 3, 'img/subcategories/gadgets.webp'),
--     ('Suplementos', 'Proteínas, aminoácidos y vitaminas.', 1, 1, 4, 'img/subcategories/suplementos.webp');


-- INSERT INTO product (name, description, state, sold_count, score, id_brand, created_user, update_user)
-- VALUES
--     ('Polo Clásico', 'Polo de algodón 100% cómodo y duradero.', 1, 85, 4.3, 1, 1, 1),
--     ('Jeans Slim Fit', 'Jeans ajustados con mezclilla elástica.', 1, 60, 4.1, 2, 1, 1),
--     ('Chaqueta de Cuero', 'Chaqueta auténtica con cierre metálico.', 1, 40, 4.7, 3, 1, 1),
--     ('Zapatillas Running', 'Zapatillas ligeras con amortiguación avanzada.', 1, 120, 4.6, 2, 1, 1),
--     ('Smartwatch Pro', 'Reloj inteligente con monitor de ritmo cardíaco.', 1, 200, 4.8, 7, 1, 1),
--     ('Proteína Whey', 'Proteína de suero con alto valor biológico.', 1, 300, 4.9, 6, 1, 1);


-- INSERT INTO product_subcategory (id_product, id_subcategory)
-- VALUES
--     (1, 1), -- Polo Clásico -> Polos
--     (2, 2), -- Jeans -> Jeans
--     (3, 3), -- Chaqueta -> Chaquetas
--     (4, 4), -- Zapatillas -> Zapatillas
--     (5, 5), -- Smartwatch -> Gadgets
--     (6, 6); -- Proteína Whey -> Suplementos

-- INSERT INTO variant_product (sku, sold_count, price, discount, price_discount, stock, id_product, created_user, update_user)
-- VALUES
--     -- Polo Clásico (por color y talla)
--     ('POLO-ROJO-S', 30, 59.90, 0, 59.90, 100, 1, 1, 1),
--     ('POLO-ROJO-M', 25, 59.90, 0, 59.90, 80, 1, 1, 1),
--     ('POLO-NEGRO-S', 15, 59.90, 5, 56.90, 90, 1, 1, 1),

--     -- Jeans Slim Fit (por talla)
--     ('JEAN-30', 15, 120.00, 0, 120.00, 60, 2, 1, 1),
--     ('JEAN-32', 25, 120.00, 10, 108.00, 50, 2, 1, 1),

--     -- Zapatillas Running (por color y talla)
--     ('ZAPA-BLANCA-41', 40, 250.00, 5, 237.50, 100, 4, 1, 1),
--     ('ZAPA-NEGRA-42', 35, 250.00, 10, 225.00, 120, 4, 1, 1),

--     -- Smartwatch Pro (por memoria)
--     ('SMART-128GB', 80, 800.00, 10, 720.00, 90, 5, 1, 1),
--     ('SMART-256GB', 70, 900.00, 15, 765.00, 80, 5, 1, 1),

--     -- Proteína Whey (por sabor)
--     ('WHEY-CHOCOLATE', 120, 250.00, 5, 237.50, 200, 6, 1, 1),
--     ('WHEY-FRESA', 180, 250.00, 0, 250.00, 300, 6, 1, 1);

-- INSERT INTO attribute_type (name, created_user, update_user)
-- VALUES
--     ('TALLA-PRENDA', 1, 1),
--     ('TALLA-CALZADO', 1, 1),
--     ('COLOR', 1, 1),
--     ('MEMORIA-GB', 1, 1),
--     ('SABOR', 1, 1);

-- INSERT INTO attribute_value (value, id_attribute_type, created_user, update_user)
-- VALUES
--     ('S', 1, 1, 1),
--     ('M', 1, 1, 1),
--     ('30', 1, 1, 1),
--     ('32', 1, 1, 1),
--     ('41', 2, 1, 1),
--     ('42', 2, 1, 1),
--     ('ROJO', 3, 1, 1),
--     ('NEGRO', 3, 1, 1),
--     ('BLANCO', 3, 1, 1),
--     ('128GB', 4, 1, 1),
--     ('256GB', 4, 1, 1),
--     ('CHOCOLATE', 5, 1, 1),
--     ('FRESA', 5, 1, 1);

-- INSERT INTO variant_attribute (id_variant_product, created_user, update_user)
-- VALUES
--     (1, 1, 1), (2, 1, 1), (3, 1, 1),
--     (4, 1, 1), (5, 1, 1),
--     (6, 1, 1), (7, 1, 1),
--     (8, 1, 1), (9, 1, 1),
--     (10, 1, 1), (11, 1, 1);

-- INSERT INTO variant_attribute_attribute_value (id_attribute_value, id_variant)
-- VALUES
--     (7, 1), (1, 1),     -- Polo rojo S
--     (7, 2), (2, 2),     -- Polo rojo M
--     (8, 3), (1, 3),     -- Polo negro S
--     (3, 4),              -- Jeans talla 30
--     (4, 5),              -- Jeans talla 32
--     (9, 6), (5, 6),     -- Zapatilla blanca 41
--     (8, 7), (6, 7),     -- Zapatilla negra 42
--     (10, 8),             -- Smartwatch 128GB
--     (11, 9),             -- Smartwatch 256GB
--     (12, 10),            -- Whey sabor chocolate
--     (13, 11);            -- Whey sabor fresa

-- INSERT INTO product_img (name, image_url, id_variant_attribute, created_user, update_user)
-- VALUES
--     ('Polo Clásico Rojo', 'img/products/polo_rojo.jpg', 1, 1, 1),
--     ('Polo Clásico Negro', 'img/products/polo_negro.jpg', 3, 1, 1),
--     ('Jeans Slim Fit Azul', 'img/products/jeans_azul.jpg', 4, 1, 1),
--     ('Chaqueta Cuero', 'img/products/chaqueta_cuero.jpg', 5, 1, 1),
--     ('Zapatilla Blanca', 'img/products/zapatilla_blanca.jpg', 6, 1, 1),
--     ('Zapatilla Negra', 'img/products/zapatilla_negra.jpg', 7, 1, 1),
--     ('Smartwatch 128GB', 'img/products/smartwatch_128.jpg', 8, 1, 1),
--     ('Smartwatch 256GB', 'img/products/smartwatch_256.jpg', 9, 1, 1),
--     ('Proteína Chocolate', 'img/products/whey_chocolate.jpg', 10, 1, 1),
--     ('Proteína Fresa', 'img/products/whey_fresa.jpg', 11, 1, 1);
