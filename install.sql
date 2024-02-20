DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS pizza CASCADE;
DROP TABLE IF EXISTS compose CASCADE;

CREATE TABLE ingredient
(
    id   SERIAL,
    nom  TEXT,
    prix DECIMAL(5, 2),
    CONSTRAINT ingredient_pk PRIMARY KEY (id)
);

INSERT INTO ingredient (nom, prix)
VALUES ('tomate', 1.5);
INSERT INTO ingredient (nom, prix)
VALUES ('mozzarella', 2);
INSERT INTO ingredient (nom, prix)
VALUES ('jambon', 2.5);
INSERT INTO ingredient (nom, prix)
VALUES ('pate a pizza', 0.5);

CREATE TABLE pizza
(
    id       SERIAL,
    nom      TEXT,
    pate     INT,
    prixBase DECIMAL(5, 2),
    CONSTRAINT pizza_pk PRIMARY KEY (id),
    CONSTRAINT pizza_pate_fk FOREIGN KEY (pate) REFERENCES ingredient (id) ON DELETE CASCADE
);

INSERT INTO pizza(nom, pate, prixBase)
VALUES ('Margherita', 4, 6.00);

CREATE TABLE compose
(
    pizza      INT,
    ingredient INT,
    CONSTRAINT compose_pk PRIMARY KEY (pizza, ingredient),
    CONSTRAINT compose_pizza_fk FOREIGN KEY (pizza) REFERENCES pizza (id) ON DELETE CASCADE,
    CONSTRAINT compose_ingredient_fk FOREIGN KEY (ingredient) REFERENCES ingredient (id) ON DELETE CASCADE
);

INSERT INTO compose(pizza, ingredient)
VALUES (1, 1);
INSERT INTO compose(pizza, ingredient)
VALUES (1, 2);