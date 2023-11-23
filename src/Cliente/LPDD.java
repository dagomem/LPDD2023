package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LPDD extends JFrame implements ActionListener {
    private final int maxIntentos;
    private final int maxLetras;
    private final String palabraSecreta;
    private int intentoActual;
    private JTextField inputRespuesta;
    private JPanel tabla;

    public LPDD(String palabraSecreta){
        this.palabraSecreta = palabraSecreta;
        this.intentoActual = 0;
        this.maxLetras = 5;
        this.maxIntentos = 6;
        initializeComponent();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        comprobarRespuesta();
    }
    private void initializeComponent() {
        setTitle("La palabra del día");
        setSize(300, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("La palabra del día", SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        tabla = new JPanel(new GridLayout(maxIntentos, maxLetras, 10, 5));
        add(tabla, BorderLayout.CENTER);
        for (int i = 0; i < maxIntentos; i++) {
            JPanel fila = new JPanel(new GridLayout(1, maxLetras, 5, 5));
            for (int j = 0; j < maxLetras; j++) {
                JLabel casilla = new JLabel("", SwingConstants.CENTER);
                casilla.setPreferredSize(new Dimension(50, 50));
                casilla.setOpaque(true);
                casilla.setForeground(Color.white);
                casilla.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                fila.add(casilla);
            }
            tabla.add(fila);
        }
        inputRespuesta = new JTextField(15);
        JButton botonIntentar = new JButton("Intentar");
        botonIntentar.addActionListener(this);
        JPanel panelRespuesta = new JPanel();
        panelRespuesta.add(inputRespuesta);
        panelRespuesta.add(botonIntentar);
        add(panelRespuesta, BorderLayout.SOUTH);
        setVisible(true);
    }
    private void comprobarRespuesta() {
        String respuesta = inputRespuesta.getText().toUpperCase().replace('Á','A').replace('É','E').replace('Í','I').replace('Ó','O').replace('Ú','U');
        if (respuesta.length() != maxLetras) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce una palabra de " + maxLetras + " letras.");
            inputRespuesta.requestFocus();
            return;
        }
        JPanel fila = (JPanel) tabla.getComponent(intentoActual);
        Component[] casillas = fila.getComponents();
        String palabraAux = palabraSecreta;
        for (int i = 0; i < maxLetras; i++) {
            char letra = respuesta.charAt(i);
            char letraCorrecta = palabraSecreta.charAt(i);
            JLabel casilla = (JLabel) casillas[i];
            casilla.setText(String.valueOf(letra));
            if (letra == letraCorrecta) {
                palabraAux = palabraAux.replaceFirst(String.valueOf(letra), "");
                casilla.setBackground(Color.decode("#43A047"));
            }
        }
        for (int i = 0; i < maxLetras; i++) {
            char letra = respuesta.charAt(i);
            char letraCorrecta = palabraSecreta.charAt(i);

            JLabel casilla = (JLabel) casillas[i];
            if (letra != letraCorrecta) {
                if (palabraAux.contains(String.valueOf(letra))) {
                    palabraAux = palabraAux.replaceFirst(String.valueOf(letra), "");
                    casilla.setBackground(Color.decode("#E4A81D"));
                } else {
                    casilla.setBackground(Color.decode("#757575"));
                }
            }
        }
        intentoActual++;
        if (palabraSecreta.equals(respuesta)) {
            finDelJuego(true);
        } else if (intentoActual == maxIntentos) {
            finDelJuego(false);
        }
        inputRespuesta.setText("");
        inputRespuesta.requestFocus();
    }
    private void finDelJuego(boolean ganado) {
        inputRespuesta.setEnabled(false);
        if (ganado) {
            JOptionPane.showMessageDialog(this, "¡¡¡Has ganado!!!");
        } else {
            JOptionPane.showMessageDialog(this, "Has perdido. La palabra correcta era: " + palabraSecreta);
        }
    }
}
