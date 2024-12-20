--liquibase formatted sql
--changeset adam.zimny:1 labels:DEV

--------------------------------------------
---------------Tables-----------------------
--------------------------------------------

-- If you are using PostgreSQL, you may need to enable the UUID extension
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY , -- Auto-incremented primary key
    uuid UUID NOT NULL DEFAULT gen_random_uuid(), -- UUID column with default value
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    verification_code VARCHAR(255),
    verification_code_expired_at TIMESTAMP,
    reset_password_code VARCHAR(255),
    reset_password_code_expired_at TIMESTAMP,
    password_reset_completed BOOLEAN DEFAULT FALSE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    failed_attempt INT DEFAULT 0,
    lock_time TIMESTAMP
);

CREATE TABLE appuser (
    id BIGSERIAL PRIMARY KEY, -- Auto-incremented primary key
    uuid UUID NOT NULL DEFAULT gen_random_uuid(), -- UUID column with default value
    first_name VARCHAR(30) NOT NULL, -- First name, not null, with size constraints
    second_name VARCHAR(30), -- Second name, optional
    surname VARCHAR(30) NOT NULL, -- Surname, not null, with size constraints
    year_of_birth SMALLINT, -- Year of birth, using SMALLINT for short
    telephone VARCHAR(9), -- Telephone number, fixed length of 9 characters
    account_id BIGINT NOT NULL, -- Foreign key referencing the accounts table
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE -- Assuming you have an accounts table
);

CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY, -- Auto-incremented primary key
    name VARCHAR(255) NOT NULL UNIQUE, -- Role name, unique and not null
    description VARCHAR(255), -- Description of the role
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation timestamp
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL -- Update timestamp
);

CREATE TABLE permission (
    id BIGSERIAL PRIMARY KEY, -- Auto-incremented primary key
    name VARCHAR(255) NOT NULL UNIQUE, -- Permission name, unique and not null
    description VARCHAR(255), -- Description of the permission
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Creation timestamp
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL -- Update timestamp
);

-- Create the user_roles join table
CREATE TABLE user_roles (
    account_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (account_id, role_id),
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE -- Assuming you have a roles table
);


-- Create the role_permissions join table
CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE -- Assuming you have a permissions table
);

--rollback DROP TABLE account;
--rollback DROP TABLE appuser;
--rollback DROP TABLE role;
--rollback DROP TABLE permission;
--rollback DROP TABLE user_roles;
--rollback DROP TABLE role_permissions;