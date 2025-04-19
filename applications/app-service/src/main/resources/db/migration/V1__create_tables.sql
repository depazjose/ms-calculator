CREATE TABLE IF NOT EXISTS "tb_user" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE
);


CREATE TABLE IF NOT EXISTS "tb_operation" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    operation VARCHAR(50) NOT NULL,
    operand_a NUMERIC,
    operand_b NUMERIC,
    result NUMERIC,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
