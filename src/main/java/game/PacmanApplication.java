// Pac-Man: Next Gen
// Maksym Yakushechkin CTS3

package game;

import javax.swing.*;

public class PacmanApplication {        //this class is to run application
    private JFrame frame;
    private GamePanel gamePanel;

    public PacmanApplication() {
        frame = new JFrame("Pac-Man: Next Gen");
        gamePanel = new GamePanel();

        frame.add(gamePanel);
        frame.setSize(686, 840);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        gamePanel.startGame();
    }

    public static void main(String[] args) {
        new PacmanApplication();
    }
}