-- ============================================================
-- V1 — Schema inicial ms-products
-- ============================================================

-- roles
CREATE TABLE IF NOT EXISTS roles (
                                     id           BIGSERIAL PRIMARY KEY,
                                     name         VARCHAR(100) NOT NULL UNIQUE,
    description  VARCHAR(255),
    state        BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE,
    delete_at  TIMESTAMP,
    created_user BIGINT,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_user  BIGINT,
    update_at    TIMESTAMP
    );

-- users
CREATE TABLE IF NOT EXISTS users (
                                     id          BIGSERIAL PRIMARY KEY,
                                     email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    name        VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100),
    phone       VARCHAR(20),
    address     VARCHAR(500),
    birthdate   DATE,
    gender      VARCHAR(10),
    state        BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE,
    delete_at  TIMESTAMP,
    created_user BIGINT,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_user  BIGINT,
    update_at    TIMESTAMP
    );

-- user_roles (join table)
CREATE TABLE IF NOT EXISTS user_role (
                                          user_id BIGINT NOT NULL REFERENCES users(id)  ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id)  ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
    );

-- token_recuperation (refresh tokens)
CREATE TABLE IF NOT EXISTS token_recuperation (
                                                  id              BIGSERIAL PRIMARY KEY,
                                                  id_user         BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token           VARCHAR(255) NOT NULL UNIQUE,
    expiration_date TIMESTAMP    NOT NULL,
    is_used         BOOLEAN      NOT NULL DEFAULT FALSE,
    used_date       TIMESTAMP,
    created_date    TIMESTAMP    NOT NULL DEFAULT NOW()
    );

-- companies
CREATE TABLE IF NOT EXISTS companies (
                                         id               BIGSERIAL PRIMARY KEY,
                                         legal_name       VARCHAR(200) NOT NULL UNIQUE,
    trade_name       VARCHAR(200) NOT NULL UNIQUE,
    ruc              VARCHAR(11)  NOT NULL UNIQUE,
    contributor_type VARCHAR(20)  NOT NULL,
    tax_regime       VARCHAR(30),
    fiscal_address   VARCHAR(500) NOT NULL,
    phone            VARCHAR(20),
    email            VARCHAR(150),
    website          VARCHAR(255),
    state            BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted       BOOLEAN      NOT NULL DEFAULT FALSE,
    delete_at      TIMESTAMP,
    created_user     BIGINT,
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_user      BIGINT,
    update_at        TIMESTAMP
    );

-- company_user
CREATE TABLE IF NOT EXISTS company_user (
                                            id         BIGSERIAL PRIMARY KEY,
                                            user_id    BIGINT      NOT NULL REFERENCES users(id)     ON DELETE CASCADE,
    company_id BIGINT      NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    role       VARCHAR(20) NOT NULL,
    state            BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted       BOOLEAN      NOT NULL DEFAULT FALSE,
    delete_at      TIMESTAMP,
    created_user     BIGINT,
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_user      BIGINT,
    update_at        TIMESTAMP
    );

-- attribute_types
CREATE TABLE IF NOT EXISTS attribute_types (
                                               id            BIGSERIAL PRIMARY KEY,
                                               name          VARCHAR(100) NOT NULL UNIQUE,
    display_order INT          NOT NULL DEFAULT 0,
    is_visual     BOOLEAN      NOT NULL DEFAULT FALSE,
    state         BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    delete_at   TIMESTAMP,
    created_user  BIGINT,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_user   BIGINT,
    update_at     TIMESTAMP
    );

-- attribute_values
CREATE TABLE IF NOT EXISTS attribute_values (
                                                id                BIGSERIAL PRIMARY KEY,
                                                id_attribute_type BIGINT       NOT NULL REFERENCES attribute_types(id) ON DELETE CASCADE,
    value             VARCHAR(100) NOT NULL,
    display_order     INT          NOT NULL DEFAULT 0,
    state             BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted        BOOLEAN      NOT NULL DEFAULT FALSE,
    delete_at       TIMESTAMP,
    created_user      BIGINT,
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_user       BIGINT,
    update_at         TIMESTAMP,
    CONSTRAINT uk_attr_type_value UNIQUE (id_attribute_type, value)
    );

-- categories
CREATE TABLE IF NOT EXISTS categories (
                                          id            BIGSERIAL PRIMARY KEY,
                                          name          VARCHAR(100) NOT NULL UNIQUE,
    description   VARCHAR(1000),
    image_url     VARCHAR(255) NOT NULL,
    display_order INT,
    state         BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    delete_at   TIMESTAMP,
    created_user  BIGINT,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_user   BIGINT,
    update_at     TIMESTAMP
    );
CREATE INDEX IF NOT EXISTS idx_cat_state_deleted ON categories(state, is_deleted);
CREATE INDEX IF NOT EXISTS idx_cat_name          ON categories(name);

-- subcategories
CREATE TABLE IF NOT EXISTS subcategories (
                                             id            BIGSERIAL PRIMARY KEY,
                                             id_category   BIGINT       REFERENCES categories(id) ON DELETE SET NULL,
    name          VARCHAR(100) NOT NULL,
    description   VARCHAR(1000),
    image_url     VARCHAR(255) NOT NULL,
    display_order INT,
    state         BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    delete_at   TIMESTAMP,
    created_user  BIGINT,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_user   BIGINT,
    update_at     TIMESTAMP
    );
CREATE INDEX IF NOT EXISTS idx_sub_category      ON subcategories(id_category);
CREATE INDEX IF NOT EXISTS idx_sub_state_deleted ON subcategories(state, is_deleted);
CREATE INDEX IF NOT EXISTS idx_sub_name          ON subcategories(name);

-- brands
CREATE TABLE IF NOT EXISTS brands (
                                      id           BIGSERIAL PRIMARY KEY,
                                      name         VARCHAR(100) NOT NULL UNIQUE,
    state        BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE,
    delete_at  TIMESTAMP,
    created_user BIGINT,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_user  BIGINT,
    update_at    TIMESTAMP
    );
CREATE INDEX IF NOT EXISTS idx_brand_state_deleted ON brands(state, is_deleted);
CREATE INDEX IF NOT EXISTS idx_brand_name          ON brands(name);

-- products
CREATE TABLE IF NOT EXISTS products (
                                        id           BIGSERIAL PRIMARY KEY,
                                        id_brand     BIGINT        NOT NULL REFERENCES brands(id) ON DELETE RESTRICT,
    name         VARCHAR(100)  NOT NULL,
    description  VARCHAR(1000),
    score        NUMERIC(2, 1) DEFAULT 0.0,
    state        BOOLEAN       NOT NULL DEFAULT TRUE,
    is_deleted   BOOLEAN       NOT NULL DEFAULT FALSE,
    delete_at  TIMESTAMP,
    created_user BIGINT,
    created_at   TIMESTAMP     NOT NULL DEFAULT NOW(),
    update_user  BIGINT,
    update_at    TIMESTAMP,
    CONSTRAINT fk_product_brand FOREIGN KEY (id_brand) REFERENCES brands(id)
    );
CREATE INDEX IF NOT EXISTS idx_prod_brand         ON products(id_brand);
CREATE INDEX IF NOT EXISTS idx_prod_state_deleted ON products(state, is_deleted);
CREATE INDEX IF NOT EXISTS idx_prod_name          ON products(name);

-- product_subcategory (join table ManyToMany)
CREATE TABLE IF NOT EXISTS product_subcategory (
                                                   id_product     BIGINT NOT NULL REFERENCES products(id)     ON DELETE CASCADE,
    id_subcategory BIGINT NOT NULL REFERENCES subcategories(id) ON DELETE CASCADE,
    PRIMARY KEY (id_product, id_subcategory),
    CONSTRAINT fk_ps_product     FOREIGN KEY (id_product)     REFERENCES products(id),
    CONSTRAINT fk_ps_subcategory FOREIGN KEY (id_subcategory) REFERENCES subcategories(id)
    );

-- variant_products
CREATE TABLE IF NOT EXISTS variant_products (
                                                id                   BIGSERIAL PRIMARY KEY,
                                                id_product           BIGINT        NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    version              BIGINT        DEFAULT 0,
    sku                  VARCHAR(100)  NOT NULL UNIQUE,
    price                NUMERIC(12,2) NOT NULL,
    original_price       NUMERIC(12,2),
    discount             INT           DEFAULT 0 CHECK (discount BETWEEN 0 AND 100),
    discount_start_date  TIMESTAMP,
    discount_end_date    TIMESTAMP,
    discount_update_user BIGINT,
    discount_update_date TIMESTAMP,
    stock                INT           NOT NULL DEFAULT 0 CHECK (stock >= 0),
    sold_count           INT           NOT NULL DEFAULT 0 CHECK (sold_count >= 0),
    state                BOOLEAN       NOT NULL DEFAULT TRUE,
    is_deleted           BOOLEAN       NOT NULL DEFAULT FALSE,
    delete_at          TIMESTAMP,
    created_user         BIGINT,
    created_at           TIMESTAMP     NOT NULL DEFAULT NOW(),
    update_user          BIGINT,
    update_at            TIMESTAMP,
    CONSTRAINT fk_variant_product FOREIGN KEY (id_product) REFERENCES products(id)
    );
CREATE INDEX IF NOT EXISTS idx_variant_product_id    ON variant_products(id_product);
CREATE INDEX IF NOT EXISTS idx_variant_stock         ON variant_products(stock);
CREATE INDEX IF NOT EXISTS idx_variant_available     ON variant_products(state, is_deleted, stock);
CREATE INDEX IF NOT EXISTS idx_variant_discount_dates ON variant_products(discount_start_date, discount_end_date);

-- variant_attributes
CREATE TABLE IF NOT EXISTS variant_attributes (
                                                  id                 BIGSERIAL PRIMARY KEY,
                                                  id_variant_product BIGINT  NOT NULL REFERENCES variant_products(id) ON DELETE CASCADE,
    id_attribute_type  BIGINT  NOT NULL REFERENCES attribute_types(id)  ON DELETE RESTRICT,
    id_attribute_value BIGINT  NOT NULL REFERENCES attribute_values(id) ON DELETE RESTRICT,
    state              BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted         BOOLEAN NOT NULL DEFAULT FALSE,
    delete_at        TIMESTAMP,
    created_user       BIGINT,
    created_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    update_user        BIGINT,
    update_at          TIMESTAMP,
    CONSTRAINT uk_variant_attr_type UNIQUE (id_variant_product, id_attribute_type),
    CONSTRAINT fk_var_attr_variant  FOREIGN KEY (id_variant_product) REFERENCES variant_products(id),
    CONSTRAINT fk_var_attr_type     FOREIGN KEY (id_attribute_type)  REFERENCES attribute_types(id),
    CONSTRAINT fk_var_attr_value    FOREIGN KEY (id_attribute_value) REFERENCES attribute_values(id)
    );
CREATE INDEX IF NOT EXISTS idx_variant_attr_product ON variant_attributes(id_variant_product);
CREATE INDEX IF NOT EXISTS idx_variant_attr_value   ON variant_attributes(id_attribute_value);

-- product_images
CREATE TABLE IF NOT EXISTS product_images (
                                              id                      BIGSERIAL PRIMARY KEY,
                                              id_variant_product      BIGINT       NOT NULL REFERENCES variant_products(id) ON DELETE CASCADE,
    id_visual_attribute_value BIGINT     REFERENCES attribute_values(id)          ON DELETE SET NULL,
    image_url               VARCHAR(500) NOT NULL UNIQUE,
    image_order             INT          NOT NULL DEFAULT 0,
    is_main                 BOOLEAN      NOT NULL DEFAULT FALSE,
    alt_text                VARCHAR(200),
    image_type              VARCHAR(20)  DEFAULT 'GALLERY',
    state                   BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted              BOOLEAN      NOT NULL DEFAULT FALSE,
    delete_at             TIMESTAMP,
    created_user            BIGINT,
    created_at              TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_user             BIGINT,
    update_at               TIMESTAMP,
    CONSTRAINT fk_img_variant    FOREIGN KEY (id_variant_product)       REFERENCES variant_products(id),
    CONSTRAINT fk_img_visual_attr FOREIGN KEY (id_visual_attribute_value) REFERENCES attribute_values(id)
    );
CREATE INDEX IF NOT EXISTS idx_img_variant     ON product_images(id_variant_product);
CREATE INDEX IF NOT EXISTS idx_img_main        ON product_images(id_variant_product, is_main);
CREATE INDEX IF NOT EXISTS idx_img_visual_attr ON product_images(id_visual_attribute_value);