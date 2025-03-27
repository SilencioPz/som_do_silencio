package org.example.somdosilencio;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.FileInputStream;

//JLAYER, "tocador de música" super simples que funciona! XD

public class AquelaFaltaDeSilencio extends JPanel{
    private static AdvancedPlayer advancedPlayer;
    private static boolean isPlaying;
    private static JLabel statusLabel;
    private static JSlider[] eqSliders = new JSlider[5];
    private final Timer timer;
    private float angle = 0;
    private final Color[] cores = {
            new Color(255, 0, 0),    // Vermelho
            new Color(255, 165, 0),  // Laranja
            new Color(255, 255, 0),  // Amarelo
            new Color(0, 255, 0),    // Verde
            new Color(0, 0, 255),    // Azul
            new Color(128, 0, 128)   // Roxo
    };

    public AquelaFaltaDeSilencio() {
        timer = new Timer(50, e -> {
            angle += 0.1f;
            repaint();
        });
        timer.start();
    }

    private static void adicionarLogo(JFrame frame) {
        ImageIcon logoIcon = new ImageIcon(AquelaFaltaDeSilencio.class.getResource("/images/silenciopz-logo-150.png"));
        if (logoIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar a logo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(logoLabel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Som do Silêncio --- Só toca MP3 blz?! 🎵");
        UIManager.put("Label.font", new Font("Serif", Font.BOLD, 25));
        UIManager.put("Button.font", new Font("Serif", Font.PLAIN, 25));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new BorderLayout());

        AquelaFaltaDeSom1 visualizador = new AquelaFaltaDeSom1();

        JPanel controlPanel = new JPanel();
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");
        JFileChooser fileChooser = new JFileChooser();

        statusLabel = new JLabel("Por favor, escolha arquivos MP3 - não funciona MP4 e derivados!!!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Serif", Font.BOLD, 25));

        playButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File musica = fileChooser.getSelectedFile();
                new Thread(() -> playMusic(musica)).start();
                statusLabel.setText("Tocando: " + musica.getName());
            }
        });

        stopButton.addActionListener(e -> {
            stopMusic();
            statusLabel.setText("Reprodução parada");
        });

        controlPanel.add(playButton);
        controlPanel.add(stopButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(controlPanel, BorderLayout.NORTH);
        centerPanel.add(visualizador, BorderLayout.CENTER);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);
        adicionarLogo(frame);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void playMusic(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            isPlaying = true;
            advancedPlayer = new AdvancedPlayer(fis);
            advancedPlayer.play();
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> statusLabel.setText("Erro: " + e.getMessage()));
            e.printStackTrace();
        } finally {
            isPlaying = false;
        }
    }

    public static void stopMusic() {
        if (advancedPlayer != null) {
            advancedPlayer.close();
            isPlaying = false;
        }
    }

    private static class AquelaFaltaDeSom1 extends JPanel {
        private float angle = 0;
        private final Color[] cores = {
                new Color(255, 0, 0), new Color(255, 165, 0),
                new Color(255, 255, 0), new Color(0, 255, 0),
                new Color(0, 0, 255), new Color(128, 0, 128)
        };

        public AquelaFaltaDeSom1() {
            new Timer(50, e -> {
                angle += 0.1f;
                repaint();
            }).start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int size = Math.min(getWidth(), getHeight()) / 3;

            for (int i = 0; i < 12; i++) {
                AffineTransform transform = new AffineTransform();
                transform.translate(centerX, centerY);
                transform.rotate(angle + i * Math.PI / 6);
                transform.scale(0.8, 0.8);

                g2d.setTransform(transform);
                g2d.setColor(cores[(i + (int) (angle * 10)) % cores.length]);

                Path2D forma = new Path2D.Float();
                forma.moveTo(0, -size);
                for (int j = 1; j <= 5; j++) {
                    double angulo = j * 2 * Math.PI / 5;
                    forma.lineTo(size * 0.5 * Math.sin(angulo), -size * 0.5 * Math.cos(angulo));
                }
                forma.closePath();
                g2d.fill(forma);
            }
        }
    }
}