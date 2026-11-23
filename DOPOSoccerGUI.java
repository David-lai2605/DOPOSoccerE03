package presentacion;

import dominio.*;
import dominio.Character;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Capa de Presentación - DOPOSoccer
 * 
 * @author Julian David Laiton Laiton
 * @version 1.1
 */
public class DOPOSoccerGUI extends JFrame {
    private DOPOSoccerModel gameModel;
    private GamePanel gamePanel;
    private JButton btnPausa;
    private JButton btnGuardar;
    private JButton btnAbrir;
    private JButton btnSalir;
    private Timer gameLoopTimer;

    public DOPOSoccerGUI(String paisP1, String paisP2, boolean p1Auto, IABehaviors behaviorP1, boolean p2Auto, IABehaviors behaviorP2) {
        setTitle("DOPOSoccer 2026-2");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        Character p1 = crearPersonaje(paisP1, 380, 350);
        Character p2 = crearPersonaje(paisP2, 620, 350);
        gameModel = new DOPOSoccerModel(p1, p2, "TIEMPO", 90);
        gameModel.setPlayerConfigurations(p1Auto, behaviorP1, p2Auto, behaviorP2);

        initComponents();
        startGameLoop();
    }

    private Character crearPersonaje(String pais, double x, double y) {
        if (pais == null) {
            return new KoreaCharacter(x, y);
        }
        switch (pais) {
            case "Corea del Sur": return new KoreaCharacter(x, y);
            case "Camerún": return new CameroonCharacter(x, y);
            case "Nigeria": return new NigeriaCharacter(x, y);
            case "Estados Unidos": return new USACharacter(x, y);
            default: return new KoreaCharacter(x, y);
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Barra Superior de Control Estilizada
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 10));
        topPanel.setBackground(new Color(18, 18, 28));

        btnPausa = crearBotonEstilizado("Pausar (P)", new Color(230, 126, 34));
        btnPausa.addActionListener(e -> {
            gameModel.togglePause();
            btnPausa.setText(gameModel.isPaused() ? "Reanudar (P)" : "Pausar (P)");
            enfocarJuego();
            gamePanel.repaint();
        });

        btnGuardar = crearBotonEstilizado("Guardar Partida", new Color(46, 204, 113));
        btnGuardar.addActionListener(e -> guardarPartidaSegura());

        btnAbrir = crearBotonEstilizado("Abrir Partida", new Color(52, 152, 219));
        btnAbrir.addActionListener(e -> abrirPartidaSegura());

        btnSalir = crearBotonEstilizado("Salir del Juego", new Color(231, 76, 60));
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de salir?", "Salir", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
            enfocarJuego();
        });

        topPanel.add(btnPausa);
        topPanel.add(btnGuardar);
        topPanel.add(btnAbrir);
        topPanel.add(btnSalir);
        add(topPanel, BorderLayout.NORTH);

        // Panel Gráfico con foco de teclado asegurado
        gamePanel = new GamePanel();
        gamePanel.setFocusable(true);
        add(gamePanel, BorderLayout.CENTER);

        KeyAdapter controlTeclado = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    btnPausa.doClick();
                    return;
                }

                if (gameModel.isPaused() || gameModel.isGameOver()) {
                    return;
                }

                // Jugador 1 normal (A/D, J=Patear, K=Poder)
                if (!gameModel.isPlayer1Auto()) {
                    if (e.getKeyCode() == KeyEvent.VK_A) {
                        gameModel.getPlayer1().getPosition().x = Math.max(60, gameModel.getPlayer1().getPosition().x - 16);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                        gameModel.getPlayer1().getPosition().x = Math.min(920, gameModel.getPlayer1().getPosition().x + 16);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_J) {
                        gameModel.getPlayer1().kick(gameModel.getPlayer2(), gameModel.getBall());
                    }
                    if (e.getKeyCode() == KeyEvent.VK_K) {
                        gameModel.getPlayer1().activatePower();
                    }
                }

                // Jugador 2 normal (Flechas Izq/Der, Abajo=Patear, M=Poder)
                if (!gameModel.isPlayer2Auto()) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        gameModel.getPlayer2().getPosition().x = Math.max(60, gameModel.getPlayer2().getPosition().x - 16);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        gameModel.getPlayer2().getPosition().x = Math.min(920, gameModel.getPlayer2().getPosition().x + 16);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        gameModel.getPlayer2().kick(gameModel.getPlayer1(), gameModel.getBall());
                    }
                    if (e.getKeyCode() == KeyEvent.VK_M) {
                        gameModel.getPlayer2().activatePower();
                    }
                }
            }
        };

        addKeyListener(controlTeclado);
        gamePanel.addKeyListener(controlTeclado);

        setFocusable(true);
        enfocarJuego();
    }

    private void enfocarJuego() {
        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
            if (gamePanel != null) {
                gamePanel.requestFocusInWindow();
            }
        });
    }

    private void guardarPartidaSegura() {
        boolean estabaPausado = gameModel.isPaused();
        gameModel.setPaused(true);
        btnPausa.setText("Reanudar (P)");
        gamePanel.repaint();

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Partidas DOPOSoccer (*.dopo)", "dopo"));
        chooser.setSelectedFile(new File("partida_doposoccer.dopo"));

        int option = chooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
            try {
                gameModel.saveGame(chooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "¡Partida guardada con éxito!");
            } catch (DOPOSoccerException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
            }
        }

        gameModel.setPaused(estabaPausado);
        btnPausa.setText(gameModel.isPaused() ? "Reanudar (P)" : "Pausar (P)");
        gamePanel.repaint();
        enfocarJuego();
    }

    private void abrirPartidaSegura() {
        boolean estabaPausado = gameModel.isPaused();
        gameModel.setPaused(true);
        btnPausa.setText("Reanudar (P)");
        gamePanel.repaint();

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Partidas DOPOSoccer (*.dopo)", "dopo"));

        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
            try {
                DOPOSoccerModel cargada = DOPOSoccerModel.loadGame(chooser.getSelectedFile().getAbsolutePath());
                this.gameModel = cargada;
                this.gameModel.setPaused(false);
                btnPausa.setText("Pausar (P)");

                if (gameLoopTimer != null) {
                    if (!gameLoopTimer.isRunning()) {
                        gameLoopTimer.start();
                    }
                } else {
                    startGameLoop();
                }

                gamePanel.repaint();
                enfocarJuego();
                JOptionPane.showMessageDialog(this, "¡Partida cargada con éxito!");
            } catch (DOPOSoccerException ex) {
                gameModel.setPaused(estabaPausado);
                btnPausa.setText(gameModel.isPaused() ? "Reanudar (P)" : "Pausar (P)");
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al abrir", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            gameModel.setPaused(estabaPausado);
            btnPausa.setText(gameModel.isPaused() ? "Reanudar (P)" : "Pausar (P)");
        }

        gamePanel.repaint();
        enfocarJuego();
    }

    private JButton crearBotonEstilizado(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFocusable(false);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        return btn;
    }

    private void startGameLoop() {
        if (gameLoopTimer != null && gameLoopTimer.isRunning()) {
            gameLoopTimer.stop();
        }
        gameLoopTimer = new Timer(16, e -> {
            gameModel.updateGame(0.016f);
            gamePanel.repaint();

            if (gameModel.isGameOver()) {
                gameLoopTimer.stop();
                JOptionPane.showMessageDialog(this, gameModel.getWinnerMessage(), "¡Fin del Partido!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gameLoopTimer.start();
    }

    /**
     * Panel Gráfico Dinámico
     */
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            int horizonteCesped = (int) (h * 0.35);
            int altoCancha = h - horizonteCesped - 40;

            GradientPaint skyGradient = new GradientPaint(0, 0, new Color(15, 32, 67), 0, horizonteCesped, new Color(44, 83, 100));
            g2d.setPaint(skyGradient);
            g2d.fillRect(0, 0, w, horizonteCesped);

            g2d.setColor(new Color(25, 25, 35));
            g2d.fillRect(0, horizonteCesped - 90, w, 90);
            g2d.setColor(new Color(255, 255, 255, 40));
            for (int i = 70; i < w; i += 150) {
                g2d.fillOval(i, horizonteCesped - 110, 24, 24);
            }

            g2d.setColor(new Color(46, 139, 87));
            g2d.fillRect(0, horizonteCesped, w, h - horizonteCesped);
            g2d.setColor(new Color(34, 115, 70));
            for (int i = 0; i < w; i += 80) {
                g2d.fillRect(i, horizonteCesped, 40, h - horizonteCesped);
            }

            g2d.setColor(new Color(255, 255, 255, 180));
            g2d.setStroke(new BasicStroke(3));
            int margenX = 50;
            g2d.drawRect(margenX, horizonteCesped + 20, w - (margenX * 2), altoCancha);
            g2d.drawLine(w / 2, horizonteCesped + 20, w / 2, horizonteCesped + 20 + altoCancha);
            g2d.drawOval(w / 2 - 70, horizonteCesped + 20 + (altoCancha / 2) - 70, 140, 140);

            int altoArco = 180;
            int yArcosCentro = horizonteCesped + 20 + (altoCancha / 2) - (altoArco / 2);
            int yCentroSuelo = yArcosCentro + (altoArco / 2);

            dibujarArco(g2d, margenX, yArcosCentro, altoArco);
            dibujarArco(g2d, w - margenX - 50, yArcosCentro, altoArco);

            if (gameModel.getBall().getPosition().x == 500 && gameModel.getBall().getPosition().y == 350) {
                gameModel.getBall().getPosition().x = w / 2;
                gameModel.getBall().getPosition().y = yCentroSuelo;

                gameModel.getPlayer1().getPosition().x = (w / 2) - 120;
                gameModel.getPlayer1().getPosition().y = yCentroSuelo;

                gameModel.getPlayer2().getPosition().x = (w / 2) + 120;
                gameModel.getPlayer2().getPosition().y = yCentroSuelo;
            }

            Color colorP1 = gameModel.isPlayer1Auto() ? new Color(130, 130, 130) : new Color(41, 128, 185);
            Color colorP2 = gameModel.isPlayer2Auto() ? new Color(130, 130, 130) : new Color(192, 57, 43);

            String tagP1 = gameModel.isPlayer1Auto() && gameModel.getBehaviorP1() != null
                    ? "P1 (" + gameModel.getBehaviorP1().getNombreComportamiento() + ")"
                    : "P1";
            String tagP2 = gameModel.isPlayer2Auto() && gameModel.getBehaviorP2() != null
                    ? "P2 (" + gameModel.getBehaviorP2().getNombreComportamiento() + ")"
                    : "P2";

            dibujarPersonajeEstilizado(g2d, gameModel.getPlayer1(), colorP1, tagP1, h, horizonteCesped);
            dibujarPersonajeEstilizado(g2d, gameModel.getPlayer2(), colorP2, tagP2, h, horizonteCesped);
            dibujarBalonEstilizado(g2d, gameModel.getBall());

            dibujarHUDModerno(g2d, w);

            if (gameModel.isPaused()) {
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRect(0, 0, w, h);
                g2d.setColor(Color.YELLOW);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 42));
                String pausaTxt = "PARTIDO PAUSADO";
                g2d.drawString(pausaTxt, (w - g2d.getFontMetrics().stringWidth(pausaTxt)) / 2, h / 2);
            }
        }

        private void dibujarArco(Graphics2D g2d, int x, int y, int alto) {
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(5));
            g2d.drawRect(x, y, 50, alto);

            g2d.setColor(new Color(255, 255, 255, 90));
            g2d.setStroke(new BasicStroke(1));
            for (int i = y; i <= y + alto; i += 15) {
                g2d.drawLine(x, i, x + 50, i);
            }
        }

        private void dibujarPersonajeEstilizado(Graphics2D g2d, Character c, Color colorBase, String tag, int h, int horizonteCesped) {
            int x = (int) c.getPosition().x;

            int altoCancha = h - horizonteCesped - 40;
            int altoArco = 180;
            int yArcosCentro = horizonteCesped + 20 + (altoCancha / 2) - (altoArco / 2);
            int y = yArcosCentro + (altoArco / 2);

            if (c.hasPowerActive()) {
                g2d.setColor(new Color(241, 196, 15, 150));
                g2d.fillOval(x - 38, y - 38, 76, 76);
            }

            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillOval(x - 18, y + 16, 36, 10);

            g2d.setColor(colorBase);
            g2d.fillOval(x - 24, y - 26, 48, 48);
            g2d.setColor(Color.WHITE);
            g2d.drawOval(x - 24, y - 26, 48, 48);

            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(x - 16, y + 16, 32, 8);

            g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g2d.setColor(Color.WHITE);
            int anchoTag = g2d.getFontMetrics().stringWidth(tag);
            g2d.drawString(tag, x - (anchoTag / 2), y - 33);

            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRect(x - 25, y - 48, 50, 6);
            g2d.setColor(new Color(46, 204, 113));
            double maxV = c.getStats().getMaxHealth();
            int vidaW = (int) (50 * (c.getCurrentHealth() / maxV));
            g2d.fillRect(x - 25, y - 48, Math.max(0, vidaW), 6);
        }

        private void dibujarBalonEstilizado(Graphics2D g2d, Ball b) {
            int bx = (int) b.getPosition().x;
            int by = (int) b.getPosition().y;

            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.fillOval(bx - 12, by + 12, 24, 8);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(bx - 14, by - 14, 28, 28);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(bx - 14, by - 14, 28, 28);
            g2d.fillOval(bx - 4, by - 4, 8, 8);
        }

        private void dibujarHUDModerno(Graphics2D g2d, int w) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRoundRect(w / 2 - 110, 12, 220, 55, 15, 15);

            g2d.setFont(new Font("Segoe UI", Font.BOLD, 26));
            g2d.setColor(Color.WHITE);
            String marcador = gameModel.getPlayer1().getGoal() + "   -   " + gameModel.getPlayer2().getGoal();
            g2d.drawString(marcador, w / 2 - g2d.getFontMetrics().stringWidth(marcador) / 2, 38);

            int tiempoRestante = Math.max(0, (int) gameModel.getMatchTimer());
            int min = tiempoRestante / 60;
            int seg = tiempoRestante % 60;
            String tiempoTxt = String.format("%02d:%02d", min, seg);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2d.setColor(new Color(241, 196, 15));
            g2d.drawString(tiempoTxt, w / 2 - g2d.getFontMetrics().stringWidth(tiempoTxt) / 2, 57);

            dibujarBarraPoderHUD(g2d, 25, 15, "P1 Power", gameModel.getPlayer1().getEnergyBar().getEnergy(), new Color(52, 152, 219));
            dibujarBarraPoderHUD(g2d, w - 175, 15, "P2 Power", gameModel.getPlayer2().getEnergyBar().getEnergy(), new Color(231, 76, 60));
        }

        private void dibujarBarraPoderHUD(Graphics2D g2d, int x, int y, String label, float energia, Color colorBarra) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRoundRect(x, y, 150, 45, 10, 10);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2d.setColor(Color.WHITE);
            g2d.drawString(label, x + 10, y + 18);

            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(x + 10, y + 25, 130, 10);
            g2d.setColor(colorBarra);
            g2d.fillRect(x + 10, y + 25, (int) (1.3 * energia), 10);
        }
    }

    private static IABehaviors elegirComportamientoIA(String jugadorLabel) {
        String[] opcionesIA = {"AITorpe", "AIDefensiva", "AIExperta", "AIOfensiva"};
        String elegido = (String) JOptionPane.showInputDialog(
                null,
                "Elige el comportamiento de IA para " + jugadorLabel + ":",
                "Comportamiento IA - " + jugadorLabel,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesIA,
                opcionesIA[1]
        );
        if (elegido == null) {
            return null;
        }
        switch (elegido) {
            case "AITorpe": return new AITorpe();
            case "AIDefensiva": return new AIDefensiva();
            case "AIExperta": return new AIExperta();
            case "AIOfensiva": return new AIOfensiva();
            default: return new AIDefensiva();
        }
    }

    private static String elegirPaisSiEsNormal(String jugadorLabel, int indicePorDefecto) {
        String[] paises = {"Corea del Sur", "Camerún", "Nigeria", "Estados Unidos"};
        return (String) JOptionPane.showInputDialog(
                null,
                "País " + jugadorLabel + ":",
                "Selección " + jugadorLabel,
                JOptionPane.QUESTION_MESSAGE,
                null,
                paises,
                paises[indicePorDefecto]
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String controlesMsg = "¡BIENVENIDO A DOPOSOCCER 2026-2!\n\n" +
                "--- JUGADOR 1 (Izquierda) ---\n" +
                "• Moverse: Teclas A / D\n" +
                "• Patear: Tecla J\n" +
                "• Poder Especial: Tecla K\n\n" +
                "--- JUGADOR 2 (Derecha) ---\n" +
                "• Moverse: Flechas Izquierda / Derecha\n" +
                "• Patear: Flecha Abajo\n" +
                "• Poder Especial: Tecla M\n\n" +
                "Si eliges 'Jugador IA', elegirás el comportamiento de IA.\n" +
                "¡Presiona Aceptar para configurar los jugadores!";

            JOptionPane.showMessageDialog(null, controlesMsg, "Controles del Partido", JOptionPane.INFORMATION_MESSAGE);

            String[] tiposJugador = {"Normal", "Jugador IA"};

            // Jugador 1
            String tipoP1 = (String) JOptionPane.showInputDialog(
                    null, "Elige el tipo para el Jugador 1:", "Tipo de Jugador 1",
                    JOptionPane.QUESTION_MESSAGE, null, tiposJugador, tiposJugador[0]
            );
            if (tipoP1 == null) {
                return;
            }

            boolean p1Auto = "Jugador IA".equals(tipoP1);
            IABehaviors compP1 = null;
            String paisP1 = "Corea del Sur";
            if (p1Auto) {
                compP1 = elegirComportamientoIA("Jugador 1");
                if (compP1 == null) {
                    return;
                }
            } else {
                paisP1 = elegirPaisSiEsNormal("Jugador 1", 0);
                if (paisP1 == null) {
                    return;
                }
            }

            // Jugador 2
            String tipoP2 = (String) JOptionPane.showInputDialog(
                    null, "Elige el tipo para el Jugador 2:", "Tipo de Jugador 2",
                    JOptionPane.QUESTION_MESSAGE, null, tiposJugador, tiposJugador[1]
            );
            if (tipoP2 == null) {
                return;
            }

            boolean p2Auto = "Jugador IA".equals(tipoP2);
            IABehaviors compP2 = null;
            String paisP2 = "Camerún";
            if (p2Auto) {
                compP2 = elegirComportamientoIA("Jugador 2");
                if (compP2 == null) {
                    return;
                }
            } else {
                paisP2 = elegirPaisSiEsNormal("Jugador 2", 1);
                if (paisP2 == null) {
                    return;
                }
            }

            DOPOSoccerGUI gui = new DOPOSoccerGUI(paisP1, paisP2, p1Auto, compP1, p2Auto, compP2);
            gui.setVisible(true);
            gui.enfocarJuego();
        });
    }
}