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
    type    varchar(50) check (type in ('CAR', 'MOTORCYCLE'))
);

CREATE INDEX idx_user_id ON tb_vehicle (user_id);
CREATE INDEX idx_id ON tb_vehicle (id);
CREATE INDEX idx_plate ON tb_vehicle (plate);


CREATE TABLE tb_park
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    plate         VARCHAR(255) NOT NULL,
    version       INT          NOT NULL,
    time          varchar(50) check (time in ('FIFTEEN', 'THIRTY', 'SIXTY', 'INDETERMINATE')),
    status        varchar(50) check (status in ('START', 'END')),
    creation_date TIMESTAMP,
    last_update   TIMESTAMP
);

CREATE INDEX idx_id ON tb_park (id);
CREATE INDEX idx_plate ON tb_park (plate);