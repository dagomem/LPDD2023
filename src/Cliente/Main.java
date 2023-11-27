package Cliente;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        try {
            URL u = new URL("http://localhost:8080/palabrasecreta");
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            String palabra = "";
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream())))
            {
                palabra = br.readLine();
            }
            LPDD lpdd = new LPDD(palabra);
            LPDD_GUI lpddGui = new LPDD_GUI(lpdd);
            lpddGui.iniciar();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
