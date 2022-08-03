CREATE TABLE IF NOT EXISTS member(
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS balance(
    id SERIAL PRIMARY KEY,
    member_id TEXT NOT NULL,
    balance numeric(13,2) NOT NULL,
    created_by TEXT NOT NULL,
    created_date bigint NOT NULL,
    last_modified_date bigint NOT NULL,
    last_modified_by TEXT NOT NULL,
    updated_by TEXT
);