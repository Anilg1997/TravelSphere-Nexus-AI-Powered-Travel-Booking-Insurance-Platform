-- V1__init_payment_schema.sql
CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payment_ref VARCHAR(50) UNIQUE NOT NULL,
    user_id UUID,
    booking_ref VARCHAR(100),
    service_type VARCHAR(50),
    amount DECIMAL(12,2),
    currency VARCHAR(10),
    payment_method VARCHAR(50),
    status VARCHAR(20),
    transaction_id VARCHAR(100),
    promo_code VARCHAR(50),
    discount_amount DECIMAL(12,2) DEFAULT 0,
    final_amount DECIMAL(12,2),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL,
    balance DECIMAL(12,2) DEFAULT 0,
    currency VARCHAR(10) DEFAULT 'INR',
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS promo_codes (
    code VARCHAR(50) PRIMARY KEY,
    id UUID DEFAULT gen_random_uuid(),
    discount_percent INT,
    max_discount_amount DECIMAL(10,2),
    min_order_amount DECIMAL(10,2) DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    usage_count INT DEFAULT 0,
    max_usage INT DEFAULT 1000,
    valid_from TIMESTAMP,
    valid_until TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS refunds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    refund_ref VARCHAR(50) UNIQUE NOT NULL,
    payment_id UUID,
    amount DECIMAL(12,2),
    reason TEXT,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT NOW()
);
