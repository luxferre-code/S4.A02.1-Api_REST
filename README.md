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

Voici la représentation d'un ingrédient qui peut être retourné par l'API.  
```json
{
  "id": 1,
  "nom": "tomate",
  "prix": 1.5
}
```  

