package Servidor;

import java.io.*;
import java.util.Calendar;
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
        String linea = "";
        int nLineas = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(new File(Servidor.HOMEDIR, "spanishFiltrado.txt")))){
            while ((linea = br.readLine()) != null && !linea.isBlank()) nLineas++;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(new File(Servidor.HOMEDIR, "spanishFiltrado.txt")))){
            Random random = new Random();
            Calendar hoy = Calendar.getInstance();
            hoy.set(Calendar.HOUR_OF_DAY,0);
            hoy.set(Calendar.MINUTE,0);
            hoy.set(Calendar.SECOND,0);
            hoy.set(Calendar.MILLISECOND,0);
            random.setSeed(hoy.getTimeInMillis());
            int lineaALeer = random.nextInt(0,nLineas);
            for (int i = 0; i <= lineaALeer; i++){
                linea = br.readLine();
            }
            Palabra.setPalabra(linea);
            /*File f = new File(Servidor.HOMEDIR, "palabra.txt");
            if (!f.exists()) f.createNewFile();
            try (FileWriter fw = new FileWriter(f)){
                fw.write(linea + " " + Calendar.getInstance().getTime());
            }*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
