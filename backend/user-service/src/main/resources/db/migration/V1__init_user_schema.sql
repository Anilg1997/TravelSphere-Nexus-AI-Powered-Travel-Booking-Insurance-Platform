-- V1__init_user_schema.sql
CREATE TABLE IF NOT EXISTS user_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL,
    full_name VARCHAR(255),
    phone VARCHAR(50),
    date_of_birth TIMESTAMP,
    address TEXT,
    city VARCHAR(100),
    country VARCHAR(100),
    profile_image_s3_key VARCHAR(500),
    loyalty_points INT DEFAULT 0,
    loyalty_tier VARCHAR(20) DEFAULT 'SILVER',
    total_trips INT DEFAULT 0,
    total_spent DOUBLE PRECISION DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS referrals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    referrer_user_id UUID NOT NULL,
    referred_email VARCHAR(255) NOT NULL,
    referral_code VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    referred_user_id UUID,
    bonus_points_awarded INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    completed_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS loyalty_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    points INT NOT NULL,
    type VARCHAR(20),
    description TEXT,
    reference_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);
