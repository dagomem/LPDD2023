package Servidor;

public class Palabra {
    private static String palabra = "START";
    public static String getPalabra(){
        return palabra;
    }
    public static void setPalabra(String palabra){
        Palabra.palabra = palabra;
    }
}
