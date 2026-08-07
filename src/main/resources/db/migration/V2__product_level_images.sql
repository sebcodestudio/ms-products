-- ============================================================
-- V2 — Imágenes a nivel de Producto (antes eran por VariantProduct)
--
-- Objetivo: una imagen ahora pertenece al Product y, opcionalmente,
-- a un AttributeValue visual (ej. Color=Rojo). Esto permite que:
--   - El catálogo muestre una única foto "general" por producto.
--   - El detalle cambie de foto solo cuando cambia el atributo visual
--     (Color), no cuando cambia Talla u otro atributo no visual.
-- ============================================================

ALTER TABLE product_images ADD COLUMN id_product BIGINT;

UPDATE product_images pi
SET id_product = vp.id_product
FROM variant_products vp
WHERE pi.id_variant_product = vp.id;

ALTER TABLE product_images ALTER COLUMN id_product SET NOT NULL;
ALTER TABLE product_images ADD CONSTRAINT fk_img_product FOREIGN KEY (id_product) REFERENCES products(id) ON DELETE CASCADE;

ALTER TABLE product_images DROP CONSTRAINT fk_img_variant;
DROP INDEX IF EXISTS idx_img_variant;
DROP INDEX IF EXISTS idx_img_main;
ALTER TABLE product_images DROP COLUMN id_variant_product;

CREATE INDEX IF NOT EXISTS idx_img_product      ON product_images(id_product);
CREATE INDEX IF NOT EXISTS idx_img_product_main ON product_images(id_product, is_main);
