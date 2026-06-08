-- Seed demo hotels for development/testing
INSERT INTO hotel_schema.hotels (id, name, description, star_rating, address, city, country, latitude, longitude, amenities, is_active)
VALUES
    (gen_random_uuid(), 'Grand Palace Hotel', 'Luxury 5-star hotel in the heart of the city with panoramic views', 5,
     '1 MG Road', 'Mumbai', 'India', 18.9219841, 72.8346547,
     ARRAY['WiFi', 'Pool', 'Spa', 'Gym', 'Restaurant', 'Bar', 'Room Service', 'Parking'], TRUE),
    (gen_random_uuid(), 'Seaside Resort & Spa', 'Beautiful beachfront resort with private beach access', 4,
     '42 Beach Road', 'Goa', 'India', 15.4909301, 73.8278491,
     ARRAY['WiFi', 'Pool', 'Beach Access', 'Spa', 'Restaurant', 'Bar', 'Water Sports'], TRUE),
    (gen_random_uuid(), 'Himalayan View Retreat', 'Mountain resort with breathtaking Himalayan views', 4,
     'Mall Road', 'Manali', 'India', 32.2431870, 77.1891760,
     ARRAY['WiFi', 'Fireplace', 'Trekking', 'Restaurant', 'Parking', 'Bonfire'], TRUE),
    (gen_random_uuid(), 'Budget Inn Express', 'Clean, comfortable budget accommodation near the station', 2,
     'Station Road', 'Delhi', 'India', 28.6143124, 77.2025708,
     ARRAY['WiFi', 'Breakfast', 'Parking', 'Laundry'], TRUE),
    (gen_random_uuid(), 'Royal Heritage Haveli', 'Experience royal Rajasthani hospitality in a restored haveli', 4,
     'Pink City', 'Jaipur', 'India', 26.9124336, 75.7872709,
     ARRAY['WiFi', 'Pool', 'Traditional Dining', 'Courtyard', 'Cultural Shows', 'Parking'], TRUE),
    (gen_random_uuid(), 'Tech Park Business Hotel', 'Modern business hotel near the tech corridor', 3,
     'Outer Ring Road', 'Bangalore', 'India', 12.9551559, 77.6416177,
     ARRAY['WiFi', 'Conference Room', 'Gym', 'Restaurant', 'Business Center', 'Parking'], TRUE);

-- Add room types for each hotel (using subqueries to get hotel IDs)
INSERT INTO hotel_schema.room_types (hotel_id, type_name, description, max_occupancy, base_price_per_night, total_rooms, available_rooms, amenities)
SELECT id, 'Deluxe Room', 'Spacious room with city view', 2, 15000.00, 50, 50, ARRAY['King Bed', 'AC', 'TV', 'Mini Bar', 'Safe']
FROM hotel_schema.hotels WHERE name = 'Grand Palace Hotel';

INSERT INTO hotel_schema.room_types (hotel_id, type_name, description, max_occupancy, base_price_per_night, total_rooms, available_rooms, amenities)
SELECT id, 'Suite', 'Premium suite with living room and balcony', 4, 35000.00, 20, 20, ARRAY['King Bed', 'AC', 'TV', 'Living Room', 'Jacuzzi', 'Butler Service']
FROM hotel_schema.hotels WHERE name = 'Grand Palace Hotel';

INSERT INTO hotel_schema.room_types (hotel_id, type_name, description, max_occupancy, base_price_per_night, total_rooms, available_rooms, amenities)
SELECT id, 'Standard Room', 'Comfortable room with basic amenities', 2, 3000.00, 100, 100, ARRAY['Double Bed', 'AC', 'TV', 'WiFi']
FROM hotel_schema.hotels WHERE name = 'Budget Inn Express';

INSERT INTO hotel_schema.room_types (hotel_id, type_name, description, max_occupancy, base_price_per_night, total_rooms, available_rooms, amenities)
SELECT id, 'Dormitory', 'Shared dormitory for budget travelers', 1, 800.00, 20, 20, ARRAY['Bunk Bed', 'Shared Bathroom', 'Locker', 'WiFi']
FROM hotel_schema.hotels WHERE name = 'Budget Inn Express';
