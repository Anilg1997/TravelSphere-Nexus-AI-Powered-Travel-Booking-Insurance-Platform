-- V1__init_car_schema.sql
CREATE TABLE IF NOT EXISTS vehicles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vehicle_name VARCHAR(255),
    brand VARCHAR(100),
    model VARCHAR(100),
    year INT,
    vehicle_type VARCHAR(20),
    color VARCHAR(50),
    fuel_type VARCHAR(50),
    transmission VARCHAR(50),
    seating_capacity INT,
    daily_rate DECIMAL(10,2),
    city VARCHAR(100),
    is_available BOOLEAN DEFAULT true,
    image_s3_key VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS vehicle_bookings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_ref VARCHAR(50) UNIQUE NOT NULL,
    user_id UUID,
    vehicle_id UUID,
    pickup_date DATE,
    return_date DATE,
    pickup_city VARCHAR(100),
    total_days INT,
    daily_rate DECIMAL(10,2),
    total_price DECIMAL(10,2),
    addons TEXT[],
    status VARCHAR(20),
    booked_at TIMESTAMP
);
