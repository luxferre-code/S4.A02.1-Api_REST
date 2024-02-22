import com.fasterxml.jackson.databind.ObjectMapper;
import fr.valentinthuillier.sae.dao.ComposeDaoSQL;
import fr.valentinthuillier.sae.dao.IDao;
import fr.valentinthuillier.sae.dao.IngredientDaoSQL;
import fr.valentinthuillier.sae.dao.PizzaDaoSQL;
import fr.valentinthuillier.sae.dto.Compose;
import fr.valentinthuillier.sae.dto.Ingredient;
import fr.valentinthuillier.sae.dto.Pizza;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/pizzas/*")
public class PizzaREST extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();
        if(info == null) {
            info = "";
        }
        String[] parts = info.split("/");

        if(parts[0].isEmpty()) {
            String[] newParts = new String[parts.length - 1];
            System.arraycopy(parts, 1, newParts, 0, parts.length - 1);
            parts = newParts;
        }

        resp.setContentType("application/json;charset=UTF-8");
        PizzaDaoSQL pizzaDao = new PizzaDaoSQL();
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = resp.getWriter();
        Pizza pizza = null;
        int id = -1;

        switch(parts.length) {
            case 0:
                Pizza[] pizzas = pizzaDao.findAll();
                if(pizzaDao == null) {
                    pizzas = new Pizza[0];
                }
                out.println(mapper.writeValueAsString(pizzas));
                break;
            case 1:
                id = IngredientREST.parseInt(parts[0]);
                if(id == -1) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
                    return;
                }
                pizza = pizzaDao.findById(id);
                if(pizza == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Pizza not found");
                    return;
                }
                out.println(mapper.writeValueAsString(pizza));
                break;
            case 2:
                id = IngredientREST.parseInt(parts[0]);
                if(id == -1) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
                    return;
                }
                pizza = pizzaDao.findById(id);
                if(pizza == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Pizza not found");
                    return;
                }
                switch(parts[1].toLowerCase()) {
                    case "name":
                        out.println(mapper.writeValueAsString(pizza.getNom()));
                        break;
                    case "pate":
                        out.println(mapper.writeValueAsString(pizza.getPate()));
                        break;
                    case "prixfinal":
                        double prix = pizza.getPrix();
                        for(Ingredient i : pizza.getIngredients().getIngredients()) {
                            prix += i.getPrix();
                        }
                        prix += pizza.getPate().getPrix();
                        out.println(mapper.writeValueAsString(prix));
                        break;
                    case "ingredients":
                        out.println(mapper.writeValueAsString(pizza.getIngredients()));
                        break;
                    default:
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path info");
                        return;
                }
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path info");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
        System.out.println(json);
        if(json == null || json.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
            return;
        }

        String info = req.getPathInfo();
        if(info == null) {
            info = "";
        }
        String[] parts = info.split("/");

        if(parts[0].isEmpty()) {
            String[] newParts = new String[parts.length - 1];
            System.arraycopy(parts, 1, newParts, 0, parts.length - 1);
            parts = newParts;
        }
        System.out.println(List.of(parts));

        ObjectMapper objectMapper = new ObjectMapper();
        Pizza pizza = null;
        IDao<Pizza> dao = new PizzaDaoSQL();

        switch (parts.length) {
            case 0:
                try {
                    pizza = objectMapper.readValue(json, Pizza.class);
                } catch(Exception e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
                    return;
                }
                try {
                    if(!dao.save(pizza)) {
                        resp.sendError(HttpServletResponse.SC_CONFLICT, "Pizza already exists");
                        return;
                    }
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } catch(Exception e) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                }
                break;
            case 1:
                int id = IngredientREST.parseInt(parts[0]);
                if(id == -1) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
                    return;
                }
                pizza = dao.findById(id);
                if(pizza == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Pizza not found");
                    return;
                }
                IDao<Ingredient> ingredientDao = new IngredientDaoSQL();
                Ingredient ingredient = null;
                try {
                    ingredient = objectMapper.readValue(json, Ingredient.class);
                } catch(Exception e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
                    return;
                }
                if(ingredientDao.findById(ingredient.getId()) == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ingredient not found");
                    return;
                }
                ComposeDaoSQL composeDao = new ComposeDaoSQL();
                if(composeDao.save(new Compose(pizza.getId(), new Ingredient[] { ingredient }))) {
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    resp.sendError(HttpServletResponse.SC_CONFLICT, "Ingredient already exists in pizza");
                }
                break;
            default:
                break;
        }
        
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();
        if(info == null) {
            info = "";
        }
        String[] parts = info.split("/");

        if(parts[0].isEmpty()) {
            String[] newParts = new String[parts.length - 1];
            System.arraycopy(parts, 1, newParts, 0, parts.length - 1);
            parts = newParts;
        }

        IDao<Pizza> dao = new PizzaDaoSQL();
        Pizza pizza = null;
        int id = -1;

        switch(parts.length) {
            case 1:
                id = IngredientREST.parseInt(parts[0]);
                if(id == -1) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
                    return;
                }
                pizza = dao.findById(id);
                if(pizza == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Pizza not found");
                    return;
                }
                if(!dao.remove(pizza)) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while deleting pizza");
                    return;
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
                break;
            case 2:
                id = IngredientREST.parseInt(parts[0]);
                if(id == -1) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
                    return;
                }
                pizza = dao.findById(id);
                if(pizza == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Pizza not found");
                    return;
                }
                IDao<Ingredient> ingredientDao = new IngredientDaoSQL();
                Ingredient ingredient = null;
                int idIngredient = IngredientREST.parseInt(parts[1]);
                if(idIngredient == -1) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
                    return;
                }
                ingredient = ingredientDao.findById(idIngredient);
                if(ingredient == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ingredient not found");
                    return;
                }
                ComposeDaoSQL composeDao = new ComposeDaoSQL();
                if(!composeDao.removeSpecifique(id, idIngredient)) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ingredient not found in pizza");
                    return;
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path info");
        }

    }

}
