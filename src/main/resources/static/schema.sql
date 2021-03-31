
CREATE TABLE transaction_type (
                id INT NOT NULL,
                name VARCHAR(25) NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE user_account (
                id INT NOT NULL,
                email VARCHAR(254) NOT NULL,
                password VARCHAR(15) NOT NULL,
                first_name VARCHAR(25) NOT NULL,
                last_name VARCHAR(25) NOT NULL,
                address VARCHAR(150) NOT NULL,
                account_balance DECIMAL(2) NOT NULL,
                PRIMARY KEY (id)
);


CREATE UNIQUE INDEX user_account_idx
 ON user_account
 ( email );

CREATE TABLE bank_account (
                id INT NOT NULL,
                iban VARCHAR(34) NOT NULL,
                caption VARCHAR(50) NOT NULL,
                holder_name VARCHAR(120) NOT NULL,
                user_id INT NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE transaction (
                id INT NOT NULL,
                date DATETIME NOT NULL,
                description VARCHAR(150) NOT NULL,
                transaction_type_id INT NOT NULL,
                fee_rate DECIMAL(2) NOT NULL,
                amount DECIMAL(2) NOT NULL,
                sender_user_id INT NOT NULL,
                beneficiary_user_id INT NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE contact (
                id INT NOT NULL,
                contact_alias VARCHAR(50) NOT NULL,
                user_id INT NOT NULL,
                contact_user_id INT NOT NULL,
                PRIMARY KEY (id)
);


ALTER TABLE transaction ADD CONSTRAINT transaction_type_transaction_fk
FOREIGN KEY (transaction_type_id)
REFERENCES transaction_type (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE contact ADD CONSTRAINT user_account_contact_friend_fk
FOREIGN KEY (user_id)
REFERENCES user_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE contact ADD CONSTRAINT user_account_contact_owner_fk
FOREIGN KEY (contact_user_id)
REFERENCES user_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE bank_account ADD CONSTRAINT user_account_bank_account_fk
FOREIGN KEY (user_id)
REFERENCES user_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT user_account_transaction_sender_fk
FOREIGN KEY (sender_user_id)
REFERENCES user_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT user_account_transaction_beneficiary_fk1
FOREIGN KEY (beneficiary_user_id)
REFERENCES user_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE transaction ADD CONSTRAINT bank_account_transaction_fk
FOREIGN KEY (id)
REFERENCES bank_account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;