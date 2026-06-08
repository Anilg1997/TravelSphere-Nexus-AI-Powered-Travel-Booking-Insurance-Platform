-- =============================================================================
-- TravelSphere Platform — Database Initialization
-- Creates all schemas for schema-per-service isolation
-- =============================================================================

-- Create schemas for each microservice
CREATE SCHEMA IF NOT EXISTS auth_schema;
CREATE SCHEMA IF NOT EXISTS user_schema;
CREATE SCHEMA IF NOT EXISTS flight_schema;
CREATE SCHEMA IF NOT EXISTS hotel_schema;
CREATE SCHEMA IF NOT EXISTS transport_schema;
CREATE SCHEMA IF NOT EXISTS car_schema;
CREATE SCHEMA IF NOT EXISTS insurance_schema;
CREATE SCHEMA IF NOT EXISTS package_schema;
CREATE SCHEMA IF NOT EXISTS payment_schema;
CREATE SCHEMA IF NOT EXISTS document_schema;
CREATE SCHEMA IF NOT EXISTS search_schema;
CREATE SCHEMA IF NOT EXISTS admin_schema;

-- Set search paths
ALTER DATABASE travelsphere SET search_path TO public, auth_schema, user_schema, flight_schema,
    hotel_schema, transport_schema, car_schema, insurance_schema, package_schema,
    payment_schema, document_schema, search_schema, admin_schema;

-- Grant permissions
GRANT ALL ON SCHEMA auth_schema TO travelsphere;
GRANT ALL ON SCHEMA user_schema TO travelsphere;
GRANT ALL ON SCHEMA flight_schema TO travelsphere;
GRANT ALL ON SCHEMA hotel_schema TO travelsphere;
GRANT ALL ON SCHEMA transport_schema TO travelsphere;
GRANT ALL ON SCHEMA car_schema TO travelsphere;
GRANT ALL ON SCHEMA insurance_schema TO travelsphere;
GRANT ALL ON SCHEMA package_schema TO travelsphere;
GRANT ALL ON SCHEMA payment_schema TO travelsphere;
GRANT ALL ON SCHEMA document_schema TO travelsphere;
GRANT ALL ON SCHEMA search_schema TO travelsphere;
GRANT ALL ON SCHEMA admin_schema TO travelsphere;

-- Create pgcrypto extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
