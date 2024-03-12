import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.CharSequenceTranslator;

import fr.valentinthuillier.sae.DS;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/users/token")
public class UsersToken extends HttpServlet {
    
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Récupération des informations de connexion de l'utilisateur
        String login = req.getParameter("login");
        String pwd = req.getParameter("pwd");

        // Vérification de la validité des informations
        if(login == null || login.isBlank() || pwd == null || pwd.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Échappement des caractères spéciaux pour éviter une injection SQL
        CharSequenceTranslator cst = StringEscapeUtils.ESCAPE_HTML4;
        login = cst.translate(login);
        pwd = cst.translate(pwd);

        try(Connection con = DS.getConnection()) {

            // Requête SQL pour mettre à jour le token de l'utilisateur
            PreparedStatement ps = con.prepareStatement("UPDATE users SET token = ? WHERE login = ? AND pwd = ?");
            UUID token = UUID.randomUUID();
            ps.setString(1, token.toString());
            ps.setString(2, login);
            ps.setString(3, pwd);

            // Vérification de la mise à jour
            int res = ps.executeUpdate();
            if(res == 0) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Envoi du token à l'utilisateur
            PrintWriter out = resp.getWriter();
            resp.setContentType("text/plain");
            out.println(token.toString());

        } catch(Exception e) {

            System.out.println("Error on /users/token : " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }

    }

}
