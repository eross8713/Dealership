CREATE TABLE IF NOT EXISTS cars (
  id BIGSERIAL PRIMARY KEY,

  -- identity (simple for now)
  make TEXT,
  model TEXT NOT NULL,
  year INT,
  color TEXT NOT NULL,
  price NUMERIC(12,2) NOT NULL,
  -- seller/business values
  seller_ask_price NUMERIC(12,2),
  condition TEXT,

  -- refresh fields (placeholders for later)
  kbb_value NUMERIC(12,2),
  kbb_last_updated TIMESTAMPTZ,
  edmunds_payload JSONB,
  edmunds_last_updated TIMESTAMPTZ,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_cars_model ON cars(model);
CREATE INDEX IF NOT EXISTS idx_cars_make ON cars(make);
