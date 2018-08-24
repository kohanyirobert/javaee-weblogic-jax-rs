CREATE SEQUENCE card_type_sequence START WITH 1
CREATE SEQUENCE contact_type_sequence START WITH 1
CREATE SEQUENCE bankcard_sequence START WITH 1
CREATE SEQUENCE contact_person_sequence START WITH 1
CREATE TABLE card_type (id INTEGER PRIMARY KEY, type_name VARCHAR(255) NOT NULL UNIQUE)
CREATE TABLE bankcard (id INTEGER PRIMARY KEY, card_number VARCHAR(19) NOT NULL UNIQUE, valid_year NUMBER(4, 0) NOT NULL, valid_month NUMBER(2, 0) NOT NULL, card_hash RAW(32) NOT NULL UNIQUE, disabled NUMBER(1, 0) NOT NULL, owner_name VARCHAR(255) NOT NULL, card_type_id INTEGER REFERENCES card_type(id))
CREATE TABLE contact_type (id INTEGER PRIMARY KEY, type_name VARCHAR(32) NOT NULL UNIQUE)
CREATE TABLE contact_person (id INTEGER PRIMARY KEY, contact VARCHAR(255) NOT NULL, contact_type_id INTEGER REFERENCES contact_type(id), bankcard_id INTEGER NOT NULL REFERENCES bankcard(id))
