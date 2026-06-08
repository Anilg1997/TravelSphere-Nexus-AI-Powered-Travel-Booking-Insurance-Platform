CREATE TABLE insurance_schema.policy_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    coverage_type TEXT[],
    base_premium DECIMAL(12,2) NOT NULL,
    max_coverage DECIMAL(12,2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE insurance_schema.insurance_policies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    policy_number VARCHAR(50) UNIQUE NOT NULL,
    user_id UUID,
    policy_type_id UUID REFERENCES insurance_schema.policy_types(id),
    booking_ref VARCHAR(50),
    trip_destination VARCHAR(255),
    trip_start DATE,
    trip_end DATE,
    insured_amount DECIMAL(12,2),
    premium_paid DECIMAL(12,2),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    traveller_age INTEGER,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pdf_s3_key VARCHAR(500)
);

CREATE TABLE insurance_schema.claims (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_number VARCHAR(50) UNIQUE NOT NULL,
    policy_id UUID REFERENCES insurance_schema.insurance_policies(id),
    user_id UUID,
    claim_type VARCHAR(100) NOT NULL,
    incident_date DATE NOT NULL,
    description TEXT,
    claim_amount DECIMAL(12,2) NOT NULL,
    approved_amount DECIMAL(12,2),
    status VARCHAR(30) DEFAULT 'PENDING',
    filed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    resolution_notes TEXT,
    assigned_agent_id UUID
);

CREATE TABLE insurance_schema.claim_documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    claim_id UUID REFERENCES insurance_schema.claims(id),
    document_type VARCHAR(100),
    s3_key VARCHAR(500),
    file_name VARCHAR(255),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_policies_user ON insurance_schema.insurance_policies(user_id);
CREATE INDEX idx_policies_number ON insurance_schema.insurance_policies(policy_number);
CREATE INDEX idx_claims_policy ON insurance_schema.claims(policy_id);
CREATE INDEX idx_claims_user ON insurance_schema.claims(user_id);
CREATE INDEX idx_claims_status ON insurance_schema.claims(status);
CREATE INDEX idx_claim_docs_claim ON insurance_schema.claim_documents(claim_id);
