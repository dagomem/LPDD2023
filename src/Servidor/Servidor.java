package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {

    public final static String HOMEDIR = "home";

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        configurarActualizacionPalabra();
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket ss = new ServerSocket(8080);){
            while (true) {
                Socket s = ss.accept();
                AtenderPeticion at = new AtenderPeticion(s);
                pool.execute(at);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    private static void configurarActualizacionPalabra() {
        Timer t = new Timer();
        Calendar manyana = Calendar.getInstance();
        manyana.add(Calendar.DAY_OF_MONTH,1);
        manyana.set(Calendar.MILLISECOND,1);
        manyana.set(Calendar.SECOND,0);
        manyana.set(Calendar.HOUR,0);
        t.scheduleAtFixedRate(new ActualizadorPalabra(),manyana.getTime(),24*60*60*1000);
    }

}
