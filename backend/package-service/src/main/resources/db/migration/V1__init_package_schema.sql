-- V1__init_package_schema.sql
CREATE TABLE IF NOT EXISTS holiday_packages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    package_name VARCHAR(255),
    description TEXT,
    destination VARCHAR(255),
    duration_days INT,
    duration_nights INT,
    price_per_person DECIMAL(10,2),
    max_group_size INT,
    included_services TEXT[],
    image_s3_key VARCHAR(500),
    is_active BOOLEAN DEFAULT true,
    rating DOUBLE PRECISION DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS package_itineraries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    package_id UUID,
    day_number INT,
    day_title VARCHAR(255),
    description TEXT,
    activities TEXT[]
);

CREATE TABLE IF NOT EXISTS package_bookings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_ref VARCHAR(50) UNIQUE NOT NULL,
    user_id UUID,
    package_id UUID,
    travel_date DATE,
    group_size INT,
    total_price DECIMAL(10,2),
    status VARCHAR(20),
    booked_at TIMESTAMP,
    special_requests TEXT
);
