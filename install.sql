DROP TABLE IF EXISTS ingredient;

CREATE TABLE ingredient (
    id SERIAL,
    nom TEXT,
    prix DECIMAL(5, 2),
    CONSTRAINT ingredient_pk PRIMARY KEY (id)
);