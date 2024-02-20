import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.valentinthuillier.sae.dao.IDao;
import fr.valentinthuillier.sae.dao.IngredientDaoSQL;
import fr.valentinthuillier.sae.dto.Ingredient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * IngredientREST - Servlet REST permettant de gérer les ingrédients
 * @author Valentin THUILLIER
 * @see Ingredient
 * @see IngredientDaoSQL
 */
@WebServlet("/ingredient/*")
public class IngredientREST extends HttpServlet {
    
    /**
     * GET /ingredients pour obtenir la collection de tous les ingrédients
     * GET /ingredients/{id} pour obtenir un ingrédient particulier
     * GET /ingredients/{id}/name pour obtenir uniquement le nom d’un ingrédient spécifique
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();
        if(info == null) { info = ""; }
        String[] parts = info.split("/");

        if(parts[0].isEmpty()) {
            String[] newParts = new String[parts.length-1];
            for(int i = 1; i < parts.length; i++) { newParts[i-1] = parts[i]; }
            parts = newParts;
        }
        // Affiche ou est ce que le fichier est executé

        IDao<Ingredient> dao = new IngredientDaoSQL();
        ObjectMapper objectMapper = new ObjectMapper();

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        int id = -1;
        Ingredient ingredient = null;

        switch (parts.length) {
            case 0:
                try {
                    String jsonstring = objectMapper.writeValueAsString(dao.findAll());
                    out.println(jsonstring);
                } catch (Exception e) {
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
                    switch (parts[1].toLowerCase()) {
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
                return;
        }

    }

    private int parseInt(String string) throws IOException {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
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
        ObjectMapper objectMapper = new ObjectMapper();
        Ingredient ingredient = null;
        try {
            ingredient = objectMapper.readValue(json, Ingredient.class);
        } catch(Exception e) {
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
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doDelete(req, resp);
    }

}
