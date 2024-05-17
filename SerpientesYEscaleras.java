import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class SerpientesYEscaleras extends JFrame implements ActionListener {
    private JButton[] casillas;
    private final int TAM_CASILLA = 80;
    private Map<Integer, Integer> serpientesYEscaleras;
    private JLabel[] lblPosicionJugador;
    private int[] posicionesJugadores;
    private int cantidadJugadores;
    private int jugadorActual;
    private Color[] coloresJugadores;
    private Color[] coloresCasillas;

    public SerpientesYEscaleras() {
        serpientesYEscaleras = new HashMap<>();
        cantidadJugadores = 2;
        posicionesJugadores = new int[cantidadJugadores];
        Arrays.fill(posicionesJugadores, 1);
        jugadorActual = 0;

        coloresJugadores = new Color[]{Color.MAGENTA, Color.GREEN, Color.ORANGE, Color.BLUE};

        JButton btn2Jugadores = new JButton("2 Jugadores");
        JButton btn3Jugadores = new JButton("3 Jugadores");
        JButton btn4Jugadores = new JButton("4 Jugadores");

        JButton btnTablero10x10 = new JButton("10x10");
        JButton btnTablero13x13 = new JButton("13x13");
        JButton btnTablero15x15 = new JButton("15x15");

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(9, 1, 5, 5));
        panelPrincipal.add(new JLabel("Seleccione el número de jugadores:", SwingConstants.CENTER));
        panelPrincipal.add(btn2Jugadores);
        panelPrincipal.add(btn3Jugadores);
        panelPrincipal.add(btn4Jugadores);
        panelPrincipal.add(new JSeparator());
        panelPrincipal.add(new JLabel("Seleccione el tamaño del tablero:", SwingConstants.CENTER));
        panelPrincipal.add(btnTablero10x10);
        panelPrincipal.add(btnTablero13x13);
        panelPrincipal.add(btnTablero15x15);
        
        add(panelPrincipal, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        btn2Jugadores.addActionListener(e -> {
            cantidadJugadores = 2;
            actualizarCantidadJugadores();
        });
        btn3Jugadores.addActionListener(e -> {
            cantidadJugadores = 3;
            actualizarCantidadJugadores();
        });
        btn4Jugadores.addActionListener(e -> {
            cantidadJugadores = 4;
            actualizarCantidadJugadores();
        });

        btnTablero10x10.addActionListener(e -> mostrarOpcionConfiguracion(10, 10));
        btnTablero13x13.addActionListener(e -> mostrarOpcionConfiguracion(13, 13));
        btnTablero15x15.addActionListener(e -> mostrarOpcionConfiguracion(15, 15));
    }

    private void actualizarCantidadJugadores() {
        posicionesJugadores = new int[cantidadJugadores];
        Arrays.fill(posicionesJugadores, 1);
        jugadorActual = 0;
        actualizarInterfaz();
    }

    private void mostrarOpcionConfiguracion(int filas, int columnas) {
        int opcion = JOptionPane.showOptionDialog(this, "¿Cómo desea configurar las serpientes y escaleras?",
                "Configuración", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new String[]{"Manual", "Aleatorio"}, "Manual");

        if (opcion == JOptionPane.YES_OPTION) {
            mostrarConfiguracionTablero(filas, columnas);
        } else {
            iniciarJuego(filas, columnas, true);
        }
    }

    private void mostrarConfiguracionTablero(int filas, int columnas) {
        JFrame configuracionFrame = new JFrame("Configurar Serpientes y Escaleras");
        configuracionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configuracionFrame.setLayout(new BorderLayout());

        JPanel panelConfiguracion = new JPanel();
        panelConfiguracion.setLayout(new GridLayout(0, 2, 5, 5));
        JTextField[] serpientesInicio = new JTextField[5];
        JTextField[] serpientesFin = new JTextField[5];
        JTextField[] escalerasInicio = new JTextField[5];
        JTextField[] escalerasFin = new JTextField[5];

        for (int i = 0; i < 5; i++) {
            serpientesInicio[i] = new JTextField();
            serpientesFin[i] = new JTextField();
            escalerasInicio[i] = new JTextField();
            escalerasFin[i] = new JTextField();

            panelConfiguracion.add(new JLabel("Serpiente " + (i + 1) + " Inicio:"));
            panelConfiguracion.add(serpientesInicio[i]);
            panelConfiguracion.add(new JLabel("Serpiente " + (i + 1) + " Fin:"));
            panelConfiguracion.add(serpientesFin[i]);
            panelConfiguracion.add(new JLabel("Escalera " + (i + 1) + " Inicio:"));
            panelConfiguracion.add(escalerasInicio[i]);
            panelConfiguracion.add(new JLabel("Escalera " + (i + 1) + " Fin:"));
            panelConfiguracion.add(escalerasFin[i]);
        }

        JButton btnIniciarJuego = new JButton("Iniciar Juego");
        btnIniciarJuego.addActionListener(e -> {
            serpientesYEscaleras.clear();
            for (int i = 0; i < 5; i++) {
                if (!serpientesInicio[i].getText().isEmpty() && !serpientesFin[i].getText().isEmpty()) {
                    int inicio = Integer.parseInt(serpientesInicio[i].getText());
                    int fin = Integer.parseInt(serpientesFin[i].getText());
                    serpientesYEscaleras.put(inicio, fin);
                }
                if (!escalerasInicio[i].getText().isEmpty() && !escalerasFin[i].getText().isEmpty()) {
                    int inicio = Integer.parseInt(escalerasInicio[i].getText());
                    int fin = Integer.parseInt(escalerasFin[i].getText());
                    serpientesYEscaleras.put(inicio, fin);
                }
            }
            configuracionFrame.dispose();
            iniciarJuego(filas, columnas, false);
        });

        configuracionFrame.add(panelConfiguracion, BorderLayout.CENTER);
        configuracionFrame.add(btnIniciarJuego, BorderLayout.SOUTH);
        configuracionFrame.pack();
        configuracionFrame.setLocationRelativeTo(null);
        configuracionFrame.setVisible(true);
    }

    private void iniciarJuego(int filas, int columnas, boolean aleatorio) {
        if (aleatorio) {
            generarSerpientesYEscaleras(filas, columnas);
        }

        SwingUtilities.invokeLater(() -> {
            JFrame juegoFrame = new JFrame("Serpientes y Escaleras");
            juegoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panelTablero = new JPanel();
            panelTablero.setLayout(new GridLayout(filas, columnas));
            casillas = new JButton[filas * columnas];
            coloresCasillas = new Color[filas * columnas];
            for (int i = 0; i < filas * columnas; i++) {
                casillas[i] = new JButton(Integer.toString(i + 1));
                casillas[i].setPreferredSize(new Dimension(TAM_CASILLA, TAM_CASILLA));
                casillas[i].setFont(new Font("Arial", Font.PLAIN, 10));
                casillas[i].setBackground(new Color(173, 216, 230));
                coloresCasillas[i] = casillas[i].getBackground();
                panelTablero.add(casillas[i]);

                if (serpientesYEscaleras.containsKey(i + 1)) {
                    int destino = serpientesYEscaleras.get(i + 1);
                    String textoEtiqueta = (destino < i + 1) ? "E " + destino : "S " + destino;
                    casillas[i].setText(textoEtiqueta);
                    casillas[i].setFont(new Font("Arial", Font.BOLD, 12));
                    casillas[i].setVerticalTextPosition(SwingConstants.BOTTOM);
                    casillas[i].setHorizontalTextPosition(SwingConstants.CENTER);
                    casillas[i].setBackground(new Color(255, 160, 122));
                }
            }

            lblPosicionJugador = new JLabel[cantidadJugadores];
            for (int i = 0; i < cantidadJugadores; i++) {
                lblPosicionJugador[i] = new JLabel("Jugador " + (i + 1) + ": Posición 1");
                lblPosicionJugador[i].setFont(new Font("Arial", Font.BOLD, 14));
                lblPosicionJugador[i].setForeground(coloresJugadores[i]);
            }

            JPanel panelLateral = new JPanel();
            panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
            for (int i = 0; i < cantidadJugadores; i++) {
                panelLateral.add(lblPosicionJugador[i]);
            }

            JButton btnLanzarDado = new JButton("Lanzar Dado");
            btnLanzarDado.setFont(new Font("Arial", Font.BOLD, 14));
            btnLanzarDado.setBackground(new Color(255, 255, 224));
            btnLanzarDado.addActionListener(e -> {
                int resultadoDado = lanzarDado();
                moverJugador(jugadorActual, resultadoDado);
                actualizarInterfaz();
                jugadorActual = (jugadorActual + 1) % cantidadJugadores;
            });
            panelLateral.add(btnLanzarDado);

            juegoFrame.add(panelTablero, BorderLayout.CENTER);
            juegoFrame.add(panelLateral, BorderLayout.EAST);
            juegoFrame.pack();
            juegoFrame.setLocationRelativeTo(null);
            juegoFrame.setVisible(true);
        });
    }

    private int lanzarDado() {
        Random rand = new Random();
        return rand.nextInt(6) + 1;
    }

    private int moverJugador(int jugador, int pasos) {
        casillas[posicionesJugadores[jugador] - 1].setBackground(coloresCasillas[posicionesJugadores[jugador] - 1]);

        int nuevaPosicion = posicionesJugadores[jugador] + pasos;
        if (nuevaPosicion > casillas.length) {
            nuevaPosicion = casillas.length;
        }

        if (serpientesYEscaleras.containsKey(nuevaPosicion)) {
            int destino = serpientesYEscaleras.get(nuevaPosicion);
            if (nuevaPosicion < destino) {
                nuevaPosicion = destino;
                JOptionPane.showMessageDialog(null, "¡El jugador " + (jugador + 1) + " (" + obtenerNombreColor(jugador) + ") ha encontrado una escalera! Avanza a la casilla " + nuevaPosicion);
            } else {
                nuevaPosicion = destino;
                JOptionPane.showMessageDialog(null, "¡El jugador " + (jugador + 1) + " (" + obtenerNombreColor(jugador) + ") ha caído en una serpiente! Retrocede a la casilla " + nuevaPosicion);
            }
        }

        posicionesJugadores[jugador] = nuevaPosicion;

        if (nuevaPosicion == casillas.length) {
            JOptionPane.showMessageDialog(null, "¡El jugador " + (jugador + 1) + " (" + obtenerNombreColor(jugador) + ") ha ganado!", "¡Felicidades!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        return nuevaPosicion;
    }

    private String obtenerNombreColor(int jugador) {
        switch (jugador) {
            case 0:
                return "Morado";
            case 1:
                return "Verde";
            case 2:
                return "Naranja";
            case 3:
                return "Azul";
            default:
                return "Jugador " + (jugador + 1);
        }
    }

    private void generarSerpientesYEscaleras(int filas, int columnas) {
        int totalCasillas = filas * columnas;
        int cantidadSerpientes;
        int cantidadEscaleras;

        switch (totalCasillas) {
            case 100:
                cantidadSerpientes = 4;
                cantidadEscaleras = 4;
                break;
            case 169:
                cantidadSerpientes = 5;
                cantidadEscaleras = 5;
                break;
            case 225:
                cantidadSerpientes = 6;
                cantidadEscaleras = 6;
                break;
            default:
                cantidadSerpientes = 4;
                cantidadEscaleras = 4;
                break;
        }

        Random rand = new Random();
        while (cantidadSerpientes > 0) {
            int inicio = rand.nextInt(totalCasillas) + 1;
            int fin = Math.min(inicio + 20, totalCasillas);
            if (!serpientesYEscaleras.containsKey(inicio) && inicio != fin) {
                serpientesYEscaleras.put(inicio, fin);
                cantidadSerpientes--;
            }
        }

        while (cantidadEscaleras > 0) {
            int inicio = rand.nextInt(totalCasillas) + 1;
            int fin = Math.max(inicio - 20, 1);
            if (!serpientesYEscaleras.containsKey(inicio) && inicio != fin) {
                serpientesYEscaleras.put(inicio, fin);
                cantidadEscaleras--;
            }
        }
    }

    private void actualizarInterfaz() {
        for (int i = 0; i < cantidadJugadores; i++) {
            lblPosicionJugador[i].setText("Jugador " + (i + 1) + ": Posición " + posicionesJugadores[i]);
        }
        for (int i = 0; i < posicionesJugadores.length; i++) {
            casillas[posicionesJugadores[i] - 1].setBackground(coloresJugadores[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // No es necesario realizar ninguna acción aquí actualmente
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SerpientesYEscaleras().setVisible(true));
    }
}
