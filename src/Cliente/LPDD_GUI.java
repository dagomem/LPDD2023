package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LPDD_GUI extends JFrame implements ActionListener {
    private LPDD lpdd;
    private JTextField inputRespuesta;
    private JPanel tabla;

    public LPDD_GUI(LPDD lpdd) {
        this.lpdd = lpdd;
    }

    public void iniciar(){
        this.initializeComponent();
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
        tabla = new JPanel(new GridLayout(lpdd.getMaxIntentos(), lpdd.getMaxLetras(), 10, 5));
        add(tabla, BorderLayout.CENTER);
        for (int i = 0; i < lpdd.getMaxIntentos(); i++) {
            JPanel fila = new JPanel(new GridLayout(1,  lpdd.getMaxLetras(), 5, 5));
            for (int j = 0; j <  lpdd.getMaxLetras(); j++) {
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
        String respuesta = inputRespuesta.getText().toUpperCase().replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U');
        if (respuesta.length() != lpdd.getMaxLetras()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce una palabra de " +  lpdd.getMaxLetras() + " letras.");
            inputRespuesta.requestFocus();
            return;
        }
        JPanel fila = (JPanel) tabla.getComponent(lpdd.getIntentoActual());
        Component[] casillas = fila.getComponents();
        int [] resultado = lpdd.intentar(respuesta);
        for (int i = 0, max = lpdd.getMaxLetras(); i < max; i++){
            JLabel casilla = (JLabel) casillas[i];
            casilla.setText(String.valueOf(respuesta.charAt(i)));
            if (resultado[i] == 0) casilla.setBackground(Color.decode("#43A047"));
            else if (resultado[i] == 1) casilla.setBackground(Color.decode("#E4A81D"));
            else if (resultado[i] == 2) casilla.setBackground(Color.decode("#757575"));
        }
        if (lpdd.getPalabraSecreta().equals(respuesta)) {
            finDelJuego(true);
        } else if (!lpdd.quedanIntentos()) {
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
            JOptionPane.showMessageDialog(this, "Has perdido. La palabra correcta era: " + lpdd.getPalabraSecreta());
        }
    }

}
