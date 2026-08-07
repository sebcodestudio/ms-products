-- ============================================================
-- V4 — Libro de Reclamaciones (Perú, D.S. N° 011-2011-PCM)
-- ============================================================

CREATE TABLE IF NOT EXISTS complaints (
    id                   BIGSERIAL PRIMARY KEY,

    -- Tipo: RECLAMO | QUEJA
    type                 VARCHAR(20)   NOT NULL,

    -- Datos del consumidor reclamante
    consumer_name        VARCHAR(150)  NOT NULL,
    consumer_last_name   VARCHAR(150)  NOT NULL,
    document_type        VARCHAR(20)   NOT NULL,
    document_number      VARCHAR(30)   NOT NULL,
    address              VARCHAR(255)  NOT NULL,
    email                VARCHAR(150)  NOT NULL,
    phone                VARCHAR(30)   NOT NULL,
    is_minor             BOOLEAN       NOT NULL DEFAULT FALSE,
    guardian_name        VARCHAR(255),

    -- Datos del bien contratado: PRODUCTO | SERVICIO
    product_type         VARCHAR(20)   NOT NULL,
    amount_claimed       NUMERIC(10,2),
    product_description  VARCHAR(1000) NOT NULL,

    -- Detalle de la reclamación/queja y pedido concreto del consumidor
    detail               VARCHAR(2000) NOT NULL,
    consumer_request     VARCHAR(1000) NOT NULL,

    -- Seguimiento interno (no forma parte del formulario INDECOPI)
    status               VARCHAR(20)   NOT NULL DEFAULT 'PENDIENTE',
    notified_by_email    BOOLEAN       NOT NULL DEFAULT FALSE,

    -- AuditableEntity
    state                BOOLEAN       NOT NULL DEFAULT TRUE,
    is_deleted           BOOLEAN       NOT NULL DEFAULT FALSE,
    delete_at            TIMESTAMP,
    created_user         BIGINT,
    created_at           TIMESTAMP     NOT NULL DEFAULT NOW(),
    update_user          BIGINT,
    update_at            TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_complaints_state_deleted ON complaints(state, is_deleted);
CREATE INDEX IF NOT EXISTS idx_complaints_created_at ON complaints(created_at);
CREATE INDEX IF NOT EXISTS idx_complaints_status ON complaints(status);
