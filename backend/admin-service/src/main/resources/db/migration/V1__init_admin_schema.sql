-- V1__init_admin_schema.sql
CREATE TABLE IF NOT EXISTS fraud_alerts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID,
    alert_type VARCHAR(100),
    description TEXT,
    severity VARCHAR(20),
    status VARCHAR(20) DEFAULT 'OPEN',
    reference_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    resolved_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    admin_user_id UUID,
    action VARCHAR(100),
    entity_type VARCHAR(100),
    entity_id VARCHAR(255),
    details TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);
