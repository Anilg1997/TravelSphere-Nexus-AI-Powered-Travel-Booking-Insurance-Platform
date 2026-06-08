CREATE TABLE flight_schema.airports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    iata_code VARCHAR(3) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    timezone VARCHAR(50)
);

CREATE TABLE flight_schema.flights (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    flight_number VARCHAR(20) NOT NULL,
    airline VARCHAR(100) NOT NULL,
    origin_airport_id UUID REFERENCES flight_schema.airports(id),
    destination_airport_id UUID REFERENCES flight_schema.airports(id),
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL,
    aircraft_type VARCHAR(50),
    base_price DECIMAL(12,2) NOT NULL,
    available_seats INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE flight_schema.flight_bookings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_ref VARCHAR(20) UNIQUE NOT NULL,
    user_id UUID,
    flight_id UUID REFERENCES flight_schema.flights(id),
    passenger_name VARCHAR(255) NOT NULL,
    passenger_email VARCHAR(255),
    seat_number VARCHAR(10),
    cabin_class VARCHAR(20),
    price_paid DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pnr VARCHAR(10) UNIQUE
);

CREATE TABLE flight_schema.seats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    flight_id UUID REFERENCES flight_schema.flights(id),
    seat_number VARCHAR(10) NOT NULL,
    cabin_class VARCHAR(20),
    is_available BOOLEAN DEFAULT TRUE,
    is_window BOOLEAN DEFAULT FALSE,
    is_aisle BOOLEAN DEFAULT FALSE,
    UNIQUE(flight_id, seat_number)
);

CREATE INDEX idx_flights_route ON flight_schema.flights(origin_airport_id, destination_airport_id, departure_time);
CREATE INDEX idx_flight_bookings_ref ON flight_schema.flight_bookings(booking_ref);
CREATE INDEX idx_flight_bookings_user ON flight_schema.flight_bookings(user_id);
CREATE INDEX idx_seats_flight ON flight_schema.seats(flight_id);
