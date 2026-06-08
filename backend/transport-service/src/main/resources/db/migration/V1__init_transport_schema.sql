-- V1__init_transport_schema.sql
CREATE TABLE IF NOT EXISTS transport_routes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    route_name VARCHAR(255),
    operator VARCHAR(255),
    transport_type VARCHAR(20),
    origin_city VARCHAR(100),
    destination_city VARCHAR(100),
    origin_station VARCHAR(255),
    destination_station VARCHAR(255),
    departure_time TIMESTAMP,
    arrival_time TIMESTAMP,
    duration_minutes INT,
    base_price DECIMAL(10,2),
    available_seats INT,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS transport_bookings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_ref VARCHAR(50) UNIQUE NOT NULL,
    user_id UUID,
    route_id UUID,
    passenger_name VARCHAR(255),
    passenger_email VARCHAR(255),
    seat_number VARCHAR(20),
    price_paid DECIMAL(10,2),
    status VARCHAR(20),
    booked_at TIMESTAMP,
    pnr VARCHAR(10)
);
