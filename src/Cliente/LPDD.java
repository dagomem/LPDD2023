package Cliente;

import javax.swing.*;

public class LPDD {
    private final int maxIntentos;
    private final int maxLetras;
    private final String palabraSecreta;
    private int intentoActual;

    public LPDD(String palabraSecreta){
        this.palabraSecreta = palabraSecreta;
        this.intentoActual = 0;
        this.maxLetras = 5;
        maxIntentos = 6;
    }
    public int[] intentar(String respuesta) {
        String palabraAux = palabraSecreta;
        int[] res = new int[maxLetras];
        for (int i = 0; i < maxLetras; i++) {
            char letra = respuesta.charAt(i);
            char letraCorrecta = palabraSecreta.charAt(i);
            if (letra == letraCorrecta) {
                palabraAux = palabraAux.replaceFirst(String.valueOf(letra), "");
                res[i] = 0;
            }
        }
        for (int i = 0; i < maxLetras; i++) {
            char letra = respuesta.charAt(i);
            char letraCorrecta = palabraSecreta.charAt(i);
            if (letra != letraCorrecta) {
                if (palabraAux.contains(String.valueOf(letra))) {
                    palabraAux = palabraAux.replaceFirst(String.valueOf(letra), "");
                    res[i] = 1;
                } else {
                    res[i] = 2;
                }
            }
        }
        intentoActual++;
        return res;
    }
    public String getPalabraSecreta() {return  this.palabraSecreta;}
    public boolean quedanIntentos(){
        return this.intentoActual<maxIntentos;
    }
    public int getIntentoActual() {
        return intentoActual;
    }
    public int getMaxIntentos(){
        return maxIntentos;
    }
    public int getMaxLetras(){
        return maxLetras;
    }
}
