-- ============================================================
-- V7 — Pedidos + checkout con pasarela de pago (Culqi)
-- ============================================================

CREATE TABLE IF NOT EXISTS orders (
    id                     BIGSERIAL PRIMARY KEY,
    public_reference       UUID          NOT NULL UNIQUE DEFAULT gen_random_uuid(),

    status                 VARCHAR(20)   NOT NULL DEFAULT 'PENDING_PAYMENT',

    customer_name          VARCHAR(150)  NOT NULL,
    customer_phone         VARCHAR(30)   NOT NULL,
    customer_email         VARCHAR(150),
    customer_address       VARCHAR(500)  NOT NULL,

    subtotal               NUMERIC(10,2) NOT NULL,
    total                  NUMERIC(10,2) NOT NULL,
    currency               VARCHAR(3)    NOT NULL DEFAULT 'PEN',

    payment_provider       VARCHAR(20),
    payment_reference      VARCHAR(100),
    payment_failure_reason VARCHAR(500),
    paid_at                TIMESTAMP,

    -- AuditableEntity
    version                BIGINT        NOT NULL DEFAULT 0,
    state                  BOOLEAN       NOT NULL DEFAULT TRUE,
    is_deleted             BOOLEAN       NOT NULL DEFAULT FALSE,
    delete_at              TIMESTAMP,
    created_user           BIGINT,
    created_at             TIMESTAMP     NOT NULL DEFAULT NOW(),
    update_user            BIGINT,
    update_at              TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_orders_public_reference ON orders(public_reference);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_state_deleted ON orders(state, is_deleted);

CREATE TABLE IF NOT EXISTS order_items (
    id                BIGSERIAL PRIMARY KEY,
    id_order          BIGINT        NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    id_variant_product BIGINT       NOT NULL REFERENCES variant_products(id),

    product_name      VARCHAR(150)  NOT NULL,
    variant_label     VARCHAR(150),
    unit_price        NUMERIC(10,2) NOT NULL,
    quantity          INT           NOT NULL CHECK (quantity > 0),
    line_total        NUMERIC(10,2) NOT NULL,

    -- AuditableEntity
    version           BIGINT        NOT NULL DEFAULT 0,
    state             BOOLEAN       NOT NULL DEFAULT TRUE,
    is_deleted        BOOLEAN       NOT NULL DEFAULT FALSE,
    delete_at         TIMESTAMP,
    created_user      BIGINT,
    created_at        TIMESTAMP     NOT NULL DEFAULT NOW(),
    update_user       BIGINT,
    update_at         TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_order_items_order ON order_items(id_order);
CREATE INDEX IF NOT EXISTS idx_order_items_variant ON order_items(id_variant_product);
