import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fr.valentinthuillier.sae.dao.CommandeDaoSQL;
import fr.valentinthuillier.sae.dao.IDao;
import fr.valentinthuillier.sae.dto.Commande;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/commandes/*")
public class CommandesREST extends HttpServlet {
    
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

        IDao<Commande> dao = new CommandeDaoSQL();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        int id = -1;

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        Commande commande = null;

        switch (parts.length) {
            case 0:
                // Renvoie la liste des commandes en JSON
                Commande[] commandes = dao.findAll();
                System.out.println(List.of(commandes));
                System.out.println(commandes[0].getDate());
                out.println(objectMapper.writeValueAsString(commandes));
                break;
            
            case 1:
                id = IngredientREST.parseInt(parts[0]);
                if(id == -1) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id invalide");
                    return;
                }
                commande = dao.findById(id);
                if(commande == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Commande non trouvée");
                    return;
                }
                out.println(objectMapper.writeValueAsString(commande));
                break;

            case 2:
                id = IngredientREST.parseInt(parts[0]);
                if(id == -1) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id invalide");
                    return;
                }
                commande = dao.findById(id);
                if(commande == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Commande non trouvée");
                    return;
                }
                switch(parts[1].toLowerCase()) {
                    case "date":
                        out.println(objectMapper.writeValueAsString(commande.getDate()));
                        break;
                    case "prixfinal":
                        out.println(objectMapper.writeValueAsString(commande.prixFinal()));
                        break;
                    case "quantite":
                        out.println(objectMapper.writeValueAsString(commande.quantite()));
                        break;
                    default:
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mauvaise requête");
                        break;
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        @SuppressWarnings("resource")
        String json = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();

        IDao<Commande> dao = new CommandeDaoSQL();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        int id = -1;

        PrintWriter out = resp.getWriter();
        Commande commande = null;

        switch (parts.length) {
            case 0:
                commande = objectMapper.readValue(json, Commande.class);
                System.out.println(commande);
                dao.save(commande);
                System.out.println(objectMapper.writeValueAsString(commande));
                resp.setStatus(HttpServletResponse.SC_CREATED);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mauvaise requête");
                break;
        }

    }

}
