package Servidor;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Servidor {
    public final static String HOMEDIR = "home";
    private static Timer actualizacionPalabra;
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        configurarActualizacionPalabra();
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket ss = new ServerSocket(8080)){
            modificarIndex(ss);
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

    private static void modificarIndex(ServerSocket ss) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(HOMEDIR, "indexPlantilla.html")));
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(HOMEDIR, "index.html")));
        String linea = "";
        StringBuilder nuevo = new StringBuilder();
        while ((linea = br.readLine()) != null) {
            nuevo.append(linea.replace("DIRECCION", InetAddress.getLocalHost().getHostAddress()).replace("PUERTO",String.valueOf(ss.getLocalPort())));
            nuevo.append("\n");
        }
        dos.write(nuevo.toString().getBytes());
        dos.flush();
        dos.close();
        br.close();
    }

    private static void configurarActualizacionPalabra() {
        Calendar manyana = Calendar.getInstance();
        manyana.add(Calendar.DAY_OF_MONTH,1);
        manyana.set(Calendar.HOUR_OF_DAY,0);
        manyana.set(Calendar.MINUTE,0);
        manyana.set(Calendar.SECOND,0);
        manyana.set(Calendar.MILLISECOND,0);
        actualizacionPalabra = new Timer();
        actualizacionPalabra.scheduleAtFixedRate(new ActualizadorPalabra(),manyana.getTimeInMillis() - Calendar.getInstance().getTimeInMillis(),24*60*60*1000);
    }

}
