DELETE FROM cars WHERE make IN ('Honda', 'Toyota') AND condition IS NOT NULL;

INSERT INTO cars (make, model, year, color, seller_ask_price, condition)
VALUES
  ('Honda',  'Civic',      2020, 'Blue',  18000.00, 'GOOD'),
  ('Honda',  'Accord',     2021, 'Black', 22000.00, 'EXCELLENT'),
  ('Honda',  'Prelude',    1999, 'Red',   9000.00,  'FAIR'),
  ('Toyota', 'Camry',      2022, 'White', 25000.00, 'EXCELLENT'),
  ('Toyota', 'Corolla',    2019, 'Gray',  16000.00, 'GOOD'),
  ('Toyota', 'Highlander', 2023, 'Black', 38000.00, 'EXCELLENT');

