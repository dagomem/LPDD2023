import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
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

}
