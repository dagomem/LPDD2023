package Servidor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Date;

public class AtenderPeticion implements Runnable {

    private Socket s;

    public AtenderPeticion(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try (BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
             DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {
            String peticion = br.readLine();
            if (peticion.startsWith("GET")) {
                if (peticion.startsWith("GET /palabrasecreta")) {
                    String palabra = Palabra.getPalabra();
                    sendMIMEHeading(dos, 200, "text/plain", palabra.getBytes().length);
                    dos.write(palabra.getBytes());
                } else {
                    File f = buscaFichero(peticion);
                    if (f.exists()) {
                        sendMIMEHeading(dos, 200, URLConnection.guessContentTypeFromName(f.getName()), f.length());
                        try (FileInputStream fis = new FileInputStream(f)) {
                        	byte [] buffer = new byte[1024];
                        	int leidos;
                        	while ((leidos = fis.read(buffer, 0, 1024))!= -1) {
                        		dos.write(buffer);
                        	}
                        }
                    } else {
                        String error = makeHTMLErrorText(404, "File Not Found");
                        sendMIMEHeading(dos, 404, peticion, error.getBytes().length);
                        dos.write(error.getBytes());
                    }
                }
            } else if (peticion.startsWith("HEAD")) {
                File f = buscaFichero(peticion);
                if (f.exists()) {
                    sendMIMEHeading(dos, 200, URLConnection.guessContentTypeFromName(f.getName()), f.length());

                } else {
                    String error = makeHTMLErrorText(404, "File Not Found");
                    sendMIMEHeading(dos, 404, peticion, error.getBytes().length);
                }
            } else {
                String error = makeHTMLErrorText(501, "Not Implemented");
                sendMIMEHeading(dos, 501, peticion, error.getBytes().length);
                dos.write(error.getBytes());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }

    private File buscaFichero(String m) {
        String fileName = "";
        if (m.startsWith("GET ")) {
            // A partir de una cadena de mensaje (m) correcta (comienza por GET)
            fileName = m.substring(4, m.indexOf(" ", 5));
            if (fileName.equals("/")) {
                fileName += "index.html";
            }
        }
        if (m.startsWith("HEAD ")) {
            // A partir de una cadena de mensaje (m) correcta (comienza por HEAD)
            fileName = m.substring(6, m.indexOf(" ", 7));
            if (fileName.equals("/")) {
                fileName += "index.html";
            }
        }
        return new File(Servidor.HOMEDIR, fileName);
    }

    private void sendMIMEHeading(OutputStream os, int code, String cType, long fSize) {
        sendMIMEHeading(os, code, cType, fSize, 0, 0);
    }

    private void sendMIMEHeading(OutputStream os, int code, String cType, long fSize, int bInicial, int bFinal) {
        PrintStream dos = new PrintStream(os);
        dos.print("HTTP/1.1 " + code + " ");
        if (code == 200) {
            dos.print("OK\r\n");
            dos.print("Date: " + new Date() + "\r\n");
            dos.print("Server: Cutre http Server ver. -6.0\r\n");
            dos.print("Connection: close\r\n");
            dos.print("Content-length: " + fSize + "\r\n");
            dos.print("Content-type: " + cType + "\r\n");
            dos.print("\r\n");
        } else if (code == 206) {
            dos.print("OK\r\n");
            dos.print("Date: " + new Date() + "\r\n");
            dos.print("Server: Cutre http Server ver. -6.0\r\n");
            dos.print("Connection: close\r\n");
            dos.print("Content-Range: bytes " + bInicial + "-" + bFinal + "/" + (bFinal + 1));
            dos.print("Content-length: " + fSize + "\r\n");
            dos.print("Content-type: " + cType + "\r\n");
            dos.print("\r\n");
        } else if (code == 404) {
            dos.print("File Not Found\r\n");
            dos.print("Date: " + new Date() + "\r\n");
            dos.print("Server: Cutre http Server ver. -6.0\r\n");
            dos.print("Connection: close\r\n");
            dos.print("Content-length: " + fSize + "\r\n");
            dos.print("Content-type: " + "text/html" + "\r\n");
            dos.print("\r\n");
        } else if (code == 501) {
            dos.print("Not Implemented\r\n");
            dos.print("Date: " + new Date() + "\r\n");
            dos.print("Server: Cutre http Server ver. -6.0\r\n");
            dos.print("Connection: close\r\n");
            dos.print("Content-length: " + fSize + "\r\n");
            dos.print("Content-type: " + "text/html" + "\r\n");
            dos.print("\r\n");
        }
        dos.flush();
    }

    private String makeHTMLErrorText(int code, String txt) {
        StringBuffer msg = new StringBuffer("<HTML>\r\n");
        msg.append(" <HEAD>\r\n");
        msg.append(" <TITLE>" + txt + "</TITLE>\r\n");
        msg.append(" </HEAD>\r\n");
        msg.append(" <BODY>\r\n");
        msg.append(" <H1>HTTP Error " + code + ": " + txt + "</H1>\r\n");
        msg.append(" </BODY>\r\n");
        msg.append("</HTML>\r\n");
        return msg.toString();
    }
}