package Servidor;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimerTask;

public class ActualizadorPalabra extends TimerTask {
    public ActualizadorPalabra(){
        actualizar();
    }
    @Override
    public void run(){
        actualizar();
    }

    private void actualizar(){
        try (DataInputStream dis = new DataInputStream(new FileInputStream(new File(Servidor.HOMEDIR, "spanishFiltrado.txt")))){
            String linea;
            int nLineas = 0;
            while ((linea = dis.readLine()) != null && !linea.isBlank()) nLineas++;
            dis.reset();
            Random random = new Random();
            Calendar hoy = Calendar.getInstance();

            random.setSeed(hoy.getTime().getTime());
            int lineaALeer = random.nextInt(0,nLineas);
            for (int i = 0; i <= lineaALeer; i++){
                linea = dis.readLine();
            }
            Palabra.setPalabra(linea);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
