-- V1__init_document_schema.sql
CREATE TABLE IF NOT EXISTS documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID,
    document_type VARCHAR(50),
    file_name VARCHAR(500),
    s3_key VARCHAR(500),
    content_type VARCHAR(100),
    file_size BIGINT,
    booking_ref VARCHAR(100),
    created_at TIMESTAMP DEFAULT NOW()
);
