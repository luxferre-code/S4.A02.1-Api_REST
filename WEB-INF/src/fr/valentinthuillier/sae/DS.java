package fr.valentinthuillier.sae;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DS {

    private static final String FILE_PATH = "/home/infoetu/valentin.thuillier.etu/config.prop";
    private static DS instance = null;
    private final String nom;
    private final String mdp;
    private final String url;


    private DS() throws Exception {
        Properties p = new Properties();
        p.load(new FileInputStream(FILE_PATH));
        Class.forName(p.getProperty("driver"));
        this.url = p.getProperty("url");
        this.nom = p.getProperty("login");
        this.mdp = p.getProperty("password");
    }

    public static Connection getConnection() {
        try {
            if(instance == null)
                instance = new DS();
            return DriverManager.getConnection(instance.url, instance.nom, instance.mdp);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
