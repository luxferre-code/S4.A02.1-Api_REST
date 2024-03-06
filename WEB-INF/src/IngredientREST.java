import com.fasterxml.jackson.databind.ObjectMapper;
import fr.valentinthuillier.sae.dao.IDao;
import fr.valentinthuillier.sae.dao.IngredientDaoSQL;
import fr.valentinthuillier.sae.dto.Ingredient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * IngredientREST - Servlet REST permettant de gérer les ingrédients
 *
 * @author Valentin THUILLIER
 * @see Ingredient
 * @see IngredientDaoSQL
 */
@WebServlet("/ingredients/*")
public class IngredientREST extends HttpServlet {

    static int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch(NumberFormatException e) {
            return -1;
        }
    }

    /**
     * GET /ingredients pour obtenir la collection de tous les ingrédients
     * GET /ingredients/{id} pour obtenir un ingrédient particulier
     * GET /ingredients/{id}/name pour obtenir uniquement le nom d’un ingrédient spécifique
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();
        if(info == null) {
            info = "";
        }
        String[] parts = info.split("/");

        if(parts.length > 0 && parts[0].isEmpty()) {
            String[] newParts = new String[parts.length - 1];
            System.arraycopy(parts, 1, newParts, 0, parts.length - 1);
            parts = newParts;
        }
        // Affiche ou est ce que le fichier est executé

        IDao<Ingredient> dao = new IngredientDaoSQL();
        ObjectMapper objectMapper = new ObjectMapper();

        PrintWriter out = resp.getWriter();

        int id = -1;
        Ingredient ingredient = null;

        switch(parts.length) {
            case 0:
                try {
                    resp.setContentType("application/json;charset=UTF-8");
                    String jsonstring = objectMapper.writeValueAsString(dao.findAll());
                    out.println(jsonstring);
                } catch(Exception e) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                }
                break;
            case 1:
                id = parseInt(parts[0]);
                if(id == -1) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path info");
                    return;
                }
                ingredient = dao.findById(id);
                if(ingredient == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ingredient not found");
                } else {
                    resp.setContentType("application/json;charset=UTF-8");
                    String json = objectMapper.writeValueAsString(ingredient);
                    out.println(json);
                }
                break;
            case 2:
                id = parseInt(parts[0]);
                if(id == -1) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path info");
                    return;
                }
                ingredient = dao.findById(id);
                if(ingredient == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ingredient not found");
                } else {
                    resp.setContentType("text/plain;charset=UTF-8");
                    switch(parts[1].toLowerCase()) {
                        case "name":
                            out.println(objectMapper.writeValueAsString(ingredient.getNom()));
                            break;
                        case "price":
                            out.println(objectMapper.writeValueAsString(ingredient.getPrix()));
                            break;
                        default:
                            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path info");
                            break;
                    }
                }
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path info");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
            json = reader.readLine();
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
            return;
        }
        if (json == null || json.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Ingredient ingredient = null;
        try {
            ingredient = objectMapper.readValue(json, Ingredient.class);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
            return;
        }
        IDao<Ingredient> dao = new IngredientDaoSQL();
        try {
            if(!dao.save(ingredient)) {
                resp.sendError(HttpServletResponse.SC_CONFLICT, "Ingredient already exists");
                return;
            }
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch(Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();
        if(info == null) {
            info = "";
        }
        String[] parts = info.split("/");

        if(parts.length > 0 && parts[0].isEmpty()) {
            String[] newParts = new String[parts.length - 1];
            System.arraycopy(parts, 1, newParts, 0, parts.length - 1);
            parts = newParts;
        }

        IDao<Ingredient> dao = new IngredientDaoSQL();
        Ingredient ingredient = null;

        if(parts.length != 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path info");
            return;
        }

        int id = parseInt(parts[0]);
        if(id == -1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path info");
            return;
        }

        ingredient = dao.findById(id);
        if(ingredient == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ingredient not found");
            return;
        }

        if(!dao.remove(ingredient)) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while deleting ingredient");
        } else {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

    }

}
