ALTER TABLE orders
  ADD COLUMN quoted_price numeric(10, 2);
ALTER TABLE orders
  ADD COLUMN projected_close_date timestamptz;
ALTER TABLE orders
  ADD COLUMN pay_on_close boolean NOT NULL DEFAULT FALSE;
ALTER TABLE orders
  ADD COLUMN charged_at_beginning boolean NOT NULL DEFAULT FALSE;
ALTER TABLE orders
  ADD COLUMN created_date timestamptz;
  
ALTER TABLE properties
  ADD COLUMN county VARCHAR(255);