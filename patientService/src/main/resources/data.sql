CREATE TABLE IF NOT EXISTS patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    phone_number VARCHAR(20),
    address VARCHAR(255)
);

INSERT INTO patients (first_name, last_name, date_of_birth, gender, phone_number, address)
VALUES ('Test', 'TestNone', '1966-12-31', 'F', '100-222-3333', '1 Brookside St');

INSERT INTO patients (first_name, last_name, date_of_birth, gender, phone_number, address)
VALUES ('Test', 'TestBorderline', '1945-06-24', 'M', '200-333-4444', '2 High St');

INSERT INTO patients (first_name, last_name, date_of_birth, gender, phone_number, address)
VALUES ('Test', 'TestInDanger', '2004-06-18', 'M', '300-444-5555', '3 Club Road');

INSERT INTO patients (first_name, last_name, date_of_birth, gender, phone_number, address)
VALUES ('Test', 'TestEarlyOnset', '2002-06-28', 'F', '400-555-6666', '4 Valley Dr');

