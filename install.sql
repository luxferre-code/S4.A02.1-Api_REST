DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS pizza CASCADE;
DROP TABLE IF EXISTS compose CASCADE;
DROP TABLE IF EXISTS commande CASCADE;
DROP TABLE IF EXISTS commande_pizza CASCADE;
DROP TABLE IF EXISTS users;

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

CREATE TABLE commande
(
    id       SERIAL,
    nom      TEXT,
    date    DATE DEFAULT CURRENT_DATE,
    CONSTRAINT commande_pk PRIMARY KEY (id)
);

INSERT INTO commande(nom) VALUES ('Commande 1');

CREATE TABLE commande_pizza
(
    commande INT,
    pizza    INT,
    CONSTRAINT commande_pizza_commande_fk FOREIGN KEY (commande) REFERENCES commande (id) ON DELETE CASCADE,
    CONSTRAINT commande_pizza_pizza_fk FOREIGN KEY (pizza) REFERENCES pizza (id) ON DELETE CASCADE
);

INSERT INTO commande_pizza(commande, pizza)
VALUES (1, 1);
INSERT INTO commande_pizza(commande, pizza)
VALUES (1, 1);
INSERT INTO commande_pizza(commande, pizza)
VALUES (1, 1);

CREATE TABLE users
(
    login TEXT,
    pwd TEXT,
    token TEXT DEFAULT NULL,
    CONSTRAINT users_pk PRIMARY KEY (login)
);

INSERT INTO users(login, pwd) VALUES ('jean', 'jean');