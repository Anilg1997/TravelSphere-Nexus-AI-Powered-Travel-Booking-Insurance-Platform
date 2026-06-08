INSERT INTO insurance_schema.policy_types (id, name, description, coverage_type, base_premium, max_coverage, is_active) VALUES
    (gen_random_uuid(), 'Trip Cancellation', 'Coverage for non-refundable trip costs if you need to cancel', ARRAY['Trip Cancellation', 'Trip Interruption', 'Missed Connection'], 1500.00, 100000.00, TRUE),
    (gen_random_uuid(), 'Medical Emergency', 'Emergency medical coverage while traveling abroad', ARRAY['Medical Expenses', 'Emergency Evacuation', 'Repatriation'], 2500.00, 500000.00, TRUE),
    (gen_random_uuid(), 'Baggage Loss', 'Coverage for lost, stolen, or delayed baggage', ARRAY['Baggage Loss', 'Baggage Delay', 'Personal Belongings'], 800.00, 50000.00, TRUE),
    (gen_random_uuid(), 'Flight Delay', 'Compensation for flight delays and cancellations', ARRAY['Flight Delay', 'Missed Connection', 'Overnight Stay'], 500.00, 20000.00, TRUE),
    (gen_random_uuid(), 'Adventure Sports', 'Coverage for adventure and extreme sports activities', ARRAY['Adventure Sports', 'Injury While Sports', 'Equipment Damage'], 3500.00, 300000.00, TRUE),
    (gen_random_uuid(), 'Comprehensive Travel', 'Complete coverage including medical, baggage, cancellation, and more', ARRAY['Medical', 'Baggage', 'Cancellation', 'Flight Delay', 'Personal Liability'], 5000.00, 1000000.00, TRUE);
