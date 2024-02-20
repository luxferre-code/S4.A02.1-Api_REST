import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.valentinthuillier.sae.dao.PizzaDaoSQL;
import fr.valentinthuillier.sae.dto.Ingredient;
import fr.valentinthuillier.sae.dto.Pizza;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pizzas/*")
public class PizzaREST extends HttpServlet {
    
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

        resp.setContentType("application/json;charset=UTF-8");
        PizzaDaoSQL pizzaDao = new PizzaDaoSQL();
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = resp.getWriter();
        Pizza pizza = null;
        int id = -1;

        switch(parts.length) {
            case 0:
                Pizza[] pizzas = pizzaDao.findAll();
                if(pizzaDao == null) { pizzas = new Pizza[0]; }
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
                return;
        }

    }

}
