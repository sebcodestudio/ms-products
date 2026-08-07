-- ============================================================
-- V5 — Migra las URLs de imágenes de MinIO local a Cloudflare R2
-- (producción). Reemplaza el host, mantiene bucket/nombre de archivo.
-- ============================================================

UPDATE product_images
SET image_url = replace(image_url, 'http://localhost:9000', 'https://pub-8dc495aef7934c3fa7f17aca4fa698a5.r2.dev')
WHERE image_url LIKE 'http://localhost:9000%';

UPDATE categories
SET image_url = replace(image_url, 'http://localhost:9000', 'https://pub-8dc495aef7934c3fa7f17aca4fa698a5.r2.dev')
WHERE image_url LIKE 'http://localhost:9000%';

UPDATE subcategories
SET image_url = replace(image_url, 'http://localhost:9000', 'https://pub-8dc495aef7934c3fa7f17aca4fa698a5.r2.dev')
WHERE image_url LIKE 'http://localhost:9000%';
