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