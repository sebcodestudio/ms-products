-- Locking optimista: agrega la columna @Version a toda entidad que extiende
-- AuditableEntity, para que dos usuarios editando el mismo registro a la vez
-- no se pisen en silencio (ver AuditableEntity.java).
ALTER TABLE users              ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE roles              ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE companies          ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE company_user       ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE categories         ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE subcategories      ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE brands             ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE products           ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE product_images     ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE attribute_types    ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE attribute_values   ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE variant_attributes ADD COLUMN version bigint NOT NULL DEFAULT 0;
ALTER TABLE complaints         ADD COLUMN version bigint NOT NULL DEFAULT 0;

-- variant_products ya tenía su propia columna "version" (V1), pero permitía NULL.
-- La alineamos con el resto: sin nulos, default 0.
UPDATE variant_products SET version = 0 WHERE version IS NULL;
ALTER TABLE variant_products ALTER COLUMN version SET NOT NULL;
ALTER TABLE variant_products ALTER COLUMN version SET DEFAULT 0;
