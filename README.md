---
title: SAé S4.A02.1 - Rapport
author: Valentin THUILLIER <valentin.thuillier.etu@univ-lille.fr>
date: BUT2 (année 2023-2024)
---

# API Ingredient

---

## Tableau représentatif

---

| URI                     | Opération | MIME                          | Requête           | Réponse                                                                                                                                                                                    |
|-------------------------|-----------|-------------------------------|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| /ingredients            | GET       | <- application/json           |                   | La collection de tous les ingrédients.                                                                                                                                                     |
| /ingredients/{id}       | GET       | <- application/json           |                   | L'ingrédient ayant l'id associé. Erreur 400 si l'id donné n'est pas un nombre. Erreur 404 si l'ingrédient n'existe pas.                                                                    |
| /ingredients/{id}/name  | GET       | <- text/plain                 |                   | Le nom de l'ingrédient ayant l'id associé. Erreur 400 si l'id donné n'est pas un nombre. Erreur 404 si l'ingrédient n'existe pas.                                                          |
| /ingredients/{id}/price | GET       | <- text/plain                 |                   | Le prix de l'ingrédient ayant l'id associé. Erreur 400 si l'id donné n'est pas un nombre. Erreur 404 si l'ingrédient n'existe pas.                                                         |
| /ingredients            | POST      | -> application/json <- status | Ingrédient (ing1) | Status 201 (créer). Erreur 400 si le JSON donné est invalide. Erreur 409 si l'ingrédient existe déjà. Erreur 500 si il y a eu un problème de sauvegarde                                    |
| /ingredients/{id}       | DELETE    | <- status                     |                   | Status 204 (aucun contenu). Erreur 400 si l'url OU l'id est invalid. Erreur 404 si l'ingrédient ayant l'id associé n'a pas été trouvé. Erreur 500 si il y a eu un problème de suppression  |

## Base de données

Pour stocker les ingrédients dans la base de données, voici comment je l'ai implémenté.

```sql
CREATE TABLE ingredient
(
    id   SERIAL, -- Identifiant unique
    nom  TEXT, -- Nom de l'ingrédient
    prix DECIMAL(5, 2), -- Prix
    CONSTRAINT ingredient_pk PRIMARY KEY (id) -- Contrainte l'identifiant unique
);
```

## Corps des requêtes

---

### Représentation d'un ingrédient

---

Voici la représentation d'un ingrédient qui peut être retourné par l'API.  
```json
{
  "id": 1,
  "nom": "tomate",
  "prix": 1.5
}
```  
A noté que l'identifiant n'est pas obligatoire pour l'ajout d'un ingrédient dans la base de données.  
Si ce dernier n'est pas présent, la base de données va automatiquement lui en fournir un.  
Donc il est possible d'envoyer un ingrédient sosu cette forme:  
```json
{
  "nom": "carotte",
  "prix": 0.2
}
```

## Exemples

---

### Lister tous les ingrédients présents dans la base de données

> GET /ingredients

Requête vers le serveur.  

Réponse:  
```json
[
  {
    "id": 1,
    "nom": "tomate",
    "prix": 1.5
  },
  {
    "id": 2,
    "nom": "mozzarella",
    "prix": 2
  },
  {
    "id": 3,
    "nom": "jambon",
    "prix": 2.5
  },
  {
    "id": 4,
    "nom": "pate a pizza",
    "prix": 0.5
  }
]
```

Code du status de retour: **200 OK**.  

### Récupérer un ingrédient spécifique avec son identifiant

---

> GET /ingredients/{id}

Requête vers le serveur.  


Réponse:  
```json
{
  "id": 1,
  "nom": "tomate",
  "prix": 1.5
}
```  

Code du status de retour: **200 OK**.  
Sinon **404 Not Found** si l'ingrédient n'existe pas.  

### Récuperer le nom du premier ingrédient.

---

> GET /ingredients/{id}/name

Requête vers le serveur.  


Réponse:  
```text
tomate
```

Code du status de retour: **200 OK**.
Sinon **404 Not Found** si l'ingrédient n'existe pas.  

### Ajout d'un ingrédient avec un identifiant

---

> POST /ingredients?token={token}

Requête vers le serveur:  

```json
{
  "id": 1,
  "nom": "tomate",
  "prix": 1.5
}
```

Réponse du serveur.  

Code du status de retour: **201 Created**.  
Sinon **409 Conflict** si l'ingrédient avec cette identifiant existe déjà.  

### Ajout d'un ingrédient sans identifiant.  

---

> POST /ingredients?token={token}

Requête vers le serveur:  

```json
{
  "nom": "carotte",
  "prix": 0.2
}
```  

Réponse du serveur.  

Code du status de retour: **201 Created**.  

### Suppresion d'un ingrédient

> DELETE /ingredients/{id}?token={token}  

Requête vers le serveur.  

Réponse.  

Code du status de retour: **204 No Content**.  
Sinon, **404 Not Found** si l'ingrédient n'existe pas.  

# Api Pizza

## Tableau représentatif

| URI                         | Opération | MIME                             | Requête                        | Réponse                                                                                                                                                                                                                                                           |
|-----------------------------|-----------|----------------------------------|--------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| /pizzas                     | GET       | <- application/json              |                                | La collection de toutes les pizzas.                                                                                                                                                                                                                               |
| /pizzas/{id}                | GET       | <- application/json              |                                | La pizza ayant l'id associé.<br>Erreur 400 si l'id donné n'est pas un nombre.<br>Erreur 404 si la pizza n'existe pas.                                                                                                                                             |
| /pizzas/{id}/prixfinal      | GET       | <- application/json              |                                | Le prix final (additionnant le prix + le prix de la pâte + le prix des ingrédients).<br>Erreur 400 si l'id donné n'est pas un nombre ou l'attribut demandé n'existe pas.<br>Erreur 404 si la pizza n'existe pas.                                                  |
| /pizzas                     | POST      | -> application/json<br><- status | Pizza sous le format JSON      | Status 201 (créer).<br>Erreur 400 si le JSON n'est pas correct.<br>Erreur 401 si le token n'est pas valide.<br>Erreur 409 si la pizza existe déjà (même id).                                                                                                      |
| /pizzas/{id}                | POST      | -> application/json<br><- status | Ingrédient sous le format JSON | Status 201 si bien ajouter.<br>Erreur 400 si le JSON n'est pas correct.<br>Erreur 401 si le token n'est pas valide.<br>Erreur 404 si la pizza / l'ingrédient n'a pas été trouvé.<br>Erreur 409 si l'ingrédient est déjà dans la pizza.                            |
| /pizzas/{id}                | DELETE    | <- status                        |                                | Status 204 si modification bien effective (aucun contenu).<br>Erreur 400 si l'id n'est pas valide. Erreur 401 si le token n'est pas valide.<br>Erreur 404 si la pizza n'a pas été trouvée.<br>Erreur 500 si une erreur du système.                                |
| /pizzas/{id}/{idIngredient} | DELETE    | <- status                        |                                | Status 204 si modification bien effective (aucun contenu).<br>Erreur 400 si l'id de la pizza / l'ingrédient n'est pas valide.<br>Erreur 401 si le token n'est pas valide.<br>Erreur 404 si la pizza / l'ingrédient / l'ingrédient dans la pizza n'est pas trouvé. |
| /pizzas/{id}                | PATCH     | -> application/json<br><- status |                                | Status 204 si modification bien effective (aucun contenu).<br>Erreur 400 si le path / l'id / l'objet json est invalide.<br>Erreur 401 si le token n'est pas valide.<br>Erreur 404 si la pizza n'a pas été trouvée.<br>Erreur 500 si une erreur système            |

## Corps des requêtes  

---

## Représentation d'une pizza  

---

Voici la représentation d'une pizza qui peut être retourné par l'API sous le format JSON.  

```json
{
	"id":1,
	"nom":"Margherita",
	"pate":{
		"id":4,
		"nom":"pate a pizza",
		"prix":0.5
	},
	"prix":6.0,
	"ingredients":{
		"id_pizza":1,
		"ingredients":[
			{
				"id":1,
				"nom":"tomate",
				"prix":1.5
			},
			{
				"id":2,
				"nom":"mozzarella",
				"prix":2.0
			}
		]
	}
}
```  

Elle est composé de:  
- Son identifiant unique.  
- Son nom.  
- Sa pate qui est lui même une ingrédient.  
- Son prix de "base".  
- La liste de tous ces ingrédients (hormis la pate).  

## Base de données

Pour stocker une pizza, nous utilisons 2 tables.   
La première table qui stockent les informations principal d'une pizza.  
```sql
CREATE TABLE pizza
(
    id       SERIAL, -- Identifiant unique
    nom      TEXT, -- Nom de la pizza
    pate     INT, -- Identifiant unique de la pate (un ingrédient)
    prixBase DECIMAL(5, 2), -- Prix de base
    CONSTRAINT pizza_pk PRIMARY KEY (id), -- Contrainte identifiant unique
    CONSTRAINT pizza_pate_fk FOREIGN KEY (pate) REFERENCES ingredient (id) ON DELETE CASCADE -- Contrainte vérifi si l'id de la pate est valide dans la table ingredient
);
```  

La deuxième table stocke ce qui compose la pizza.  

```sql
CREATE TABLE compose
(
    pizza      INT, -- Identifiant unique de la pizza
    ingredient INT, -- Identifiant unique de l'ingrédient
    CONSTRAINT compose_pk PRIMARY KEY (pizza, ingredient), -- Contrainte identifiant unique
    CONSTRAINT compose_pizza_fk FOREIGN KEY (pizza) REFERENCES pizza (id) ON DELETE CASCADE, -- Contrainte vérifi si l'id de la pizza est valide dans la table pizza
    CONSTRAINT compose_ingredient_fk FOREIGN KEY (ingredient) REFERENCES ingredient (id) ON DELETE CASCADE  -- Contrainte vérifi si l'id de l'ingrédient est valide dans la table ingredient
);
```  

## Exemples

### Lister toutes les pizzas présentes dans la base de données.  

> GET /pizzas  

Requête vers le serveur.  

Réponse:  

```json
[
	{
		"id":1,
		"nom":"Margherita",
		"pate":{
			"id":4,
			"nom":"pate a pizza",
			"prix":0.5
		},
		"prix":6.0,
		"ingredients":{
			"id_pizza":1,
			"ingredients":[
				{
					"id":1,
					"nom":"tomate",
					"prix":1.5
				},
				{
					"id":2,
					"nom":"mozzarella",
					"prix":2.0
				}
			]
		}
	}
]
```  

Code de status de retour: **200 OK**.  

### Récupérer une pizza spécifique grâce à son indentifiant.  

> GET /pizzas/{id}  

Requête vers le serveur. **{id} -> 1**.  

Réponse:  

```json
{
	"id":1,
	"nom":"Margherita",
	"pate":{
		"id":4,
		"nom":"pate a pizza",
		"prix":0.5
	},
	"prix":6.0,
	"ingredients":{
		"id_pizza":1,
		"ingredients":[
			{
				"id":1,
				"nom":"tomate",
				"prix":1.5
			},
			{
				"id":2,
				"nom":"mozzarella",
				"prix":2.0
			}
		]
	}
}
```

Code du status de retour: **200 OK**.  
Sinon **400 Bad Request** si l'id n'est pas un nombre.  
Sinon **404 Not Found** si la pizza n'existe pas.  

### Récuperer le prix final d'une pizza  

> GET /pizzas/{id}/prixFinal  

Requête vers le serveur. **{id} -> 1**  

Réponse:  

```json
10
```

Code du status de retour: **200 OK**.  
Sinon **400 Bad Request** si l’id donné n’est pas un nombre ou l’attribut demandé n’existe pas.  
Sinon **404 Not Found** si la pizza n’existe pas.  

### Ajout d'une pizza avec / sans son idenfiant

> POST /pizzas?token={token}  

Requête vers le serveur:  

```json
{
    "id": 2,
    "nom": "Pizza à la tomate :)",
    "pate": {
      "id": 4,
      "nom": "pate a pizza",
      "prix": 0.5
    },
    "prix": 1,
    "ingredients": {
      "id_pizza": 2,
      "ingredients": [
        {
          "id": 1,
          "nom": "tomate",
          "prix": 1.5
        }
      ]
    }
  }
```

Réponse du serveur.  

Code du status de retour: **201 Created**.  
Sinon **400 Bad Request** si le JSON n’est pas correct.  
Sinon **401 Unauthorized** si le token n’est pas valide.  
Sinon **409 Conflict** si la pizza existe déjà (même id).  

### Ajout d'un ingrédient à une pizza  

> POST /pizzas/{id}?token={token}  

Requête vers le serveur:  
**{id} => 1**  
**{token} => APIToken**  
```json
{
    "id": 1
}
```

Réponse du serveur:  

Code du status de retour: **201 Created**.  
Sinon **400 Bad Request** si le JSON n’est pas correct.  
Sinon **401 Unauthorized** si le token n’est pas valide.  
Sinon **404 Not Found** si la pizza / l'ingrédient n'a pas été trouvé.  
Sinon **409 Conflict** si la pizza existe déjà (même id).  

### Suppression d'une pizza

> DELETE /pizzas/{id}?token={token}  

Requête vers le serveur:  
**{id} => 1**  
**{token} => APIToken**   

Réponse du serveur:  

Code du status de retour: **204 No Content**.  
Sinon **400 Bad Request** si l'id n'est pas valide.  
Sinon **401 Unauthorized** si le token n’est pas valide.  
Sinon **404 Not Found** si la pizza n'a pas été trouvée.  

### Suppresion d'un ingrédient d'une pizza  

> DELETE /pizzas/{idPizza}/{idIngredient}?token={token}  

Requête vers le serveur:  
**{idPizza} => 1**  
**{idIngredient} => 1**  
**{token} => APIToken**   

Réponse du serveur:  

Code du status de retour: **204 No Content**.  
Sinon **400 Bad Request** si l'id de la pizza / l'ingrédient n'est pas valide.  
Sinon **401 Unauthorized** si le token n’est pas valide.  
Sinon **404 Not Found** si la pizza / l'ingrédient / l'ingrédient dans la pizza n'est pas trouvé.  

### Modification d'une pizza  

> PATCH /pizzas/{id}?token={token}  

Requête vers le serveur:  
**{id} => 1**  
**{token} => APIToken**   

```json
{ "prix": 40 }
```

Réponse du serveur:  
Code du status de retour: **204 No Content**.  
Sinon **400 Bad Request** si le path / l'id / l'objet json est invalide.  
Sinon **401 Unauthorized** si le token n’est pas valide.  
Sinon **404 Not Found** si la pizza n'a pas été trouvée.  