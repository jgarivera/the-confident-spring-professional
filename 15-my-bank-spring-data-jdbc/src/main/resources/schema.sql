CREATE TABLE IF NOT EXISTS "transactions" (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    amount DECIMAL(20, 2),
    reference VARCHAR(255),
    receiving_user_id VARCHAR(255),
    timestamp TIMESTAMP,
    slogan VARCHAR(255)
);
