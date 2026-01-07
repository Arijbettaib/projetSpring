-- Create databases for the microservices
CREATE DATABASE IF NOT EXISTS auth2;
CREATE DATABASE IF NOT EXISTS product_db;
CREATE DATABASE IF NOT EXISTS stock_db;

-- Grant privileges (adjust if using a different MySQL user)
GRANT ALL PRIVILEGES ON auth2.* TO 'root'@'localhost';
GRANT ALL PRIVILEGES ON product_db.* TO 'root'@'localhost';
GRANT ALL PRIVILEGES ON stock_db.* TO 'root'@'localhost';

FLUSH PRIVILEGES;

-- Show created databases
SHOW DATABASES;
