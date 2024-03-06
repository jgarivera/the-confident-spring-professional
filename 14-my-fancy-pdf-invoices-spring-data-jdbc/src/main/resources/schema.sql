CREATE TABLE IF NOT EXISTS "invoices" (
    id      UUID  DEFAULT RANDOM_UUID() PRIMARY KEY,
    pdf_url VARCHAR(255),
    user_id VARCHAR(255),
    amount  INT
);
