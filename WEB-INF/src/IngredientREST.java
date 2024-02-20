import java.io.IOException;
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
        if(info == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing path info");
            return;
        }
        String[] parts = info.split("/");

        IDao<Ingredient> dao = new IngredientDaoSQL();

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        switch (parts.length) {
            case 0:
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String jsonstring = objectMapper.writeValueAsString(dao.findAll());
                    out.println(jsonstring);
                } catch (Exception e) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                }
                break;
            case 1:
                //TODO: cas GET /ingredients/{id}
                break;
            case 2:
                //TODO: cas GET /ingredients/{id}/name
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path info");
                return;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doDelete(req, resp);
    }

}
