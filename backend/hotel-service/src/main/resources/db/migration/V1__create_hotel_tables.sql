CREATE TABLE hotel_schema.hotels (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    star_rating INTEGER CHECK (star_rating >= 1 AND star_rating <= 5),
    address VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    amenities TEXT[],
    check_in_time TIME DEFAULT '14:00',
    check_out_time TIME DEFAULT '11:00',
    is_active BOOLEAN DEFAULT TRUE,
    image_s3_keys TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE hotel_schema.room_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    hotel_id UUID NOT NULL REFERENCES hotel_schema.hotels(id),
    type_name VARCHAR(100) NOT NULL,
    description TEXT,
    max_occupancy INTEGER NOT NULL,
    base_price_per_night DECIMAL(12,2) NOT NULL,
    total_rooms INTEGER NOT NULL,
    available_rooms INTEGER NOT NULL,
    amenities TEXT[]
);

CREATE TABLE hotel_schema.hotel_bookings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_ref VARCHAR(20) UNIQUE NOT NULL,
    user_id UUID,
    hotel_id UUID REFERENCES hotel_schema.hotels(id),
    room_type_id UUID REFERENCES hotel_schema.room_types(id),
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    guests INTEGER NOT NULL,
    total_price DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    special_requests TEXT
);

CREATE TABLE hotel_schema.hotel_reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    hotel_id UUID REFERENCES hotel_schema.hotels(id),
    user_id UUID,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    title VARCHAR(255),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_hotels_city ON hotel_schema.hotels(city);
CREATE INDEX idx_hotels_stars ON hotel_schema.hotels(star_rating);
CREATE INDEX idx_room_types_hotel ON hotel_schema.room_types(hotel_id);
CREATE INDEX idx_hotel_bookings_ref ON hotel_schema.hotel_bookings(booking_ref);
CREATE INDEX idx_hotel_bookings_user ON hotel_schema.hotel_bookings(user_id);
CREATE INDEX idx_hotel_reviews_hotel ON hotel_schema.hotel_reviews(hotel_id);
