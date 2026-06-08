-- V1__init_search_schema.sql
CREATE TABLE IF NOT EXISTS search_index (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_type VARCHAR(50),
    entity_id UUID,
    title VARCHAR(500),
    description TEXT,
    city VARCHAR(100),
    country VARCHAR(100),
    category VARCHAR(100),
    tags TEXT[],
    rating DOUBLE PRECISION DEFAULT 0.0,
    price DOUBLE PRECISION DEFAULT 0.0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_search_title ON search_index USING gin(to_tsvector('english', title));
CREATE INDEX IF NOT EXISTS idx_search_description ON search_index USING gin(to_tsvector('english', description));
CREATE INDEX IF NOT EXISTS idx_search_city ON search_index(city);
CREATE INDEX IF NOT EXISTS idx_search_category ON search_index(category);
