-- ============================================================
-- V8 — Datos de documento (DNI/RUC) para boleta/factura + estado
-- de envio al sistema de facturacion electronica (aun en desarrollo).
-- ============================================================

ALTER TABLE orders ADD COLUMN customer_document_type   VARCHAR(10) NOT NULL DEFAULT 'DNI';
ALTER TABLE orders ADD COLUMN customer_document_number VARCHAR(20) NOT NULL DEFAULT '';
ALTER TABLE orders ALTER COLUMN customer_document_type   DROP DEFAULT;
ALTER TABLE orders ALTER COLUMN customer_document_number DROP DEFAULT;

ALTER TABLE orders ADD COLUMN invoice_status    VARCHAR(20) NOT NULL DEFAULT 'NOT_SENT';
ALTER TABLE orders ADD COLUMN invoice_reference VARCHAR(100);
