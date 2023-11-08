CREATE TABLE IF NOT EXISTS tb_user
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255),
    address        VARCHAR(255),
    address_number VARCHAR(50),
    zip_code       VARCHAR(255),
    phone          VARCHAR(20),
    cpf            VARCHAR(14) UNIQUE,
    email          VARCHAR(255) UNIQUE,
    password       VARCHAR(255)
);

CREATE INDEX idx_cpf ON tb_user (cpf);
CREATE INDEX idx_email ON tb_user (email);
CREATE INDEX idx_password ON tb_user (password);


CREATE TABLE tb_vehicle
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    plate   VARCHAR(255),
    branch  VARCHAR(255),
    model   VARCHAR(255),
    year    INT,
    type    VARCHAR(50) CHECK (type IN ('CAR', 'MOTORCYCLE'))
);

CREATE INDEX idx_user_id ON tb_vehicle (user_id);
CREATE INDEX idx_id ON tb_vehicle (id);
CREATE INDEX idx_plate ON tb_vehicle (plate);


CREATE TABLE tb_park
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    plate         VARCHAR(255) NOT NULL,
    user_id       BIGINT,
    version       INT          NOT NULL,
    time          VARCHAR(50) CHECK (time IN ('FIFTY_SECOND', 'FIFTEEN', 'THIRTY', 'SIXTY', 'INDETERMINATE')),
    status        VARCHAR(50) CHECK (status IN ('START', 'END')),
    creation_date TIMESTAMP,
    last_update   TIMESTAMP
);

CREATE INDEX idx_id ON tb_park (id);
CREATE INDEX idx_plate ON tb_park (plate);
CREATE INDEX idx_user_id ON tb_park (user_id);


CREATE TABLE tb_payment_method
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT,
    payment_method_type VARCHAR(50) CHECK (payment_method_type IN ('PIX', 'CREDIT_CARD', 'DEBIT_CARD')),
    pix_key             VARCHAR(255),
    card_number         VARCHAR(255),
    card_flag           VARCHAR(255)
);

CREATE INDEX idx_id ON tb_payment_method (id);

CREATE TABLE tb_receipt
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT,
    plate         VARCHAR(255),
    time          VARCHAR(50) CHECK (time IN ('FIFTY_SECOND', 'FIFTEEN', 'THIRTY', 'SIXTY', 'INDETERMINATE')),
    creation_date TIMESTAMP,
    last_update   TIMESTAMP,
    duration      INT,
    minute_rate   DECIMAL(10, 2),
    total         DECIMAL(10, 2)
);

CREATE INDEX idx_id ON tb_receipt (id);
CREATE INDEX idx_user_id ON tb_receipt (user_id);
CREATE INDEX idx_plate ON tb_receipt (plate);
CREATE INDEX idx_creation_date ON tb_receipt (creation_date);
CREATE INDEX idx_last_update ON tb_receipt (last_update);
