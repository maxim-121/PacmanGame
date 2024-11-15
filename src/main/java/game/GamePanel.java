// Pac-Man: Next Gen
// Maksym Yakushechkin CTS3

/** This class contains game loop, maze array, the logic of drawing: maze and hearts */

package game;

import game.entities.GhostController;
import game.entities.PacmanController;

import java.awt.event.ActionEvent;
import javax.swing.JPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {
    private Timer timer;

    private PacmanController pacman;
    private GhostController ghost1;
    private GhostController ghost2;
    private GhostController ghost3;
    private GhostController ghost4;

    private int cellSize = 24;  //size of cell inside maze
    private boolean isGameOver = false; //boolean value to check if game over
    public int bufferedDirection = 5;

    private int points = 0;
    private int lives = 3;

    /** Maze contains:
     * H - horizontal line for maze wall
     * V - vertical line for maze wall
     * TR - top-right corner
     * TL - top-left corner
     * BR - bottom-right corner
     * BL - bottom-left corner
     * 2, 3 - the dots that packman eats
     * 0 - empty places
     * 7, 8 - are used to teleport form one side to another one
     * 9 - spawn for pacman
     * G1, G2, G3, G4 - spawn for ghosts
     * W1, W2 - invisible walls for ghosts
     * D - doors near ghosts spawn
     * BLT - bottom left corner of the whole maze
     * */
    private String[][] maze = {
            {"TL","H","H","H","H","H","H","H","H","H","H","H","H","TR","TL","H","H","H","H","H","H","H","H","H","H","H","H","TR"},
            {"V","2","2","2","2","2","2","2","2","2","2","2","2","V","V","2","2","2","2","2","2","2","2","2","2","2","2","V"},
            {"V","2","TL","H","H","TR","2","TL","H","H","H","TR","2","V","V","2","TL","H","H","H","TR","2","TL","H","H","TR","2","V"},
            {"V","3","V","0","0","V","2","V","0","0","0","V","2","V","V","2","V","0","0","0","V","2","V","0","0","V","3","V"},
            {"V","2","BL","H","H","BR","2","BL","H","H","H","BR","2","BL","BR","2","BL","H","H","H","BR","2","BL","H","H","BR","2","V"},
            {"V","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","V"},
            {"V","2","TL","H","H","TR","2","TL","TR","2","TL","H","H","H","H","H","H","TR","2","TL","TR","2","TL","H","H","TR","2","V"},
            {"V","2","BL","H","H","BR","2","V","V","2","BL","H","H","TR","TL","H","H","BR","2","V","V","2","BL","H","H","BR","2","V"},
            {"V","2","2","2","2","2","2","V","V","2","2","2","2","V","V","2","2","2","2","V","V","2","2","2","2","2","2","V"},
            {"BL","H","H","H","H","TR","2","V","BL","H","H","TR","0","V","V","0","TL","H","H","BR","V","2","TL","H","H","H","H","BR"},
            {"0","0","0","0","0","V","2","V","TL","H","H","BR","0","BL","BR","0","BL","H","H","TR","V","2","V","0","0","0","0","0"},
            {"0","0","0","0","0","V","2","V","V","0","0","0","0","0","0","0","0","0","0","V","V","2","V","0","0","0","0","0"},
            {"0","0","0","0","0","V","2","V","V","0","TL","H","H","D","D","H","H","TR","0","V","V","2","V","0","0","0","0","0"},
            {"H","H","H","H","H","BR","2","BL","BR","0","V","W2","W1","G4","G3","W2","W1","V","0","BL","BR","2","BL","H","H","H","H","H"},
            {"7","2","2","2","2","2","2","0","0","0","V","W2","W1","G2","G1","W2","W1","V","0","0","0","2","2","2","2","2","2","8"},
            {"H","H","H","H","H","TR","2","TL","TR","0","V","W2","W1","W2","W1","W2","W1","V","0","TL","TR","2","TL","H","H","H","H","H"},
            {"0","0","0","0","0","V","2","V","V","0","BL","H","H","H","H","H","H","BR","0","V","V","2","V","0","0","0","0","0"},
            {"0","0","0","0","0","V","2","V","V","0","0","0","0","0","0","0","0","0","0","V","V","2","V","0","0","0","0","0"},
            {"0","0","0","0","0","V","2","V","V","0","TL","H","H","H","H","H","H","TR","0","V","V","2","V","0","0","0","0","0"},
            {"TL","H","H","H","H","BR","2","BL","BR","0","BL","H","H","TR","TL","H","H","BR","0","BL","BR","2","BL","H","H","H","H","TR"},
            {"V","2","2","2","2","2","2","2","2","2","2","2","2","V","V","2","2","2","2","2","2","2","2","2","2","2","2","V"},
            {"V","2","TL","H","H","TR","2","TL","H","H","H","TR","2","V","V","2","TL","H","H","H","TR","2","TL","H","H","TR","2","V"},
            {"V","2","BL","H","TR","V","2","BL","H","H","H","BR","2","BL","BR","2","BL","H","H","H","BR","2","V","TL","H","BR","2","V"},
            {"V","3","2","2","V","V","2","2","2","2","2","2","2","9","2","2","2","2","2","2","2","2","V","V","2","2","3","V"},
            {"BL","H","TR","2","V","V","2","TL","TR","2","TL","H","H","H","H","H","H","TR","2","TL","TR","2","V","V","2","TL","H","BR"},
            {"TL","H","BR","2","BL","BR","2","V","V","2","BL","H","H","TR","TL","H","H","BR","2","V","V","2","BL","BR","2","BL","H","TR"},
            {"V","2","2","2","2","2","2","V","V","2","2","2","2","V","V","2","2","2","2","V","V","2","2","2","2","2","2","V"},
            {"V","2","TL","H","H","H","H","BR","BL","H","H","TR","2","V","V","2","TL","H","H","BR","BL","H","H","H","H","TR","2","V"},
            {"V","2","BL","H","H","H","H","H","H","H","H","BR","2","BL","BR","2","BL","H","H","H","H","H","H","H","H","BR","2","V"},
            {"V","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","V"},
            {"BLT","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","H","BR"}
    };

    public GamePanel(String[][] maze){
        this.maze = maze;
    }

    public String[][] getMaze(){
        return maze.clone();
    }

    public GamePanel() {
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                setDirection(e.getKeyCode());
            }
        });

        timer = new Timer(1000 / 62, this);
    }

    public void startGame() {

        // spawn entities
        for (int i = 0; i < maze.length; i++) {
            for (int k = 0; k < maze[i].length; k++) {
                if (maze[i][k].equals("9")) {
                    pacman = new PacmanController(k * cellSize, i * cellSize, 4);
                } if (maze[i][k].equals("G1")) {
                    ghost1 = new GhostController(k * cellSize, i * cellSize, 0);
                } if (maze[i][k].equals("G2")) {
                    ghost2 = new GhostController(k * cellSize, i * cellSize, 0);
                } if (maze[i][k].equals("G3")) {
                    ghost3 = new GhostController(k * cellSize, i * cellSize, 0);
                } if (maze[i][k].equals("G4")) {
                    ghost4 = new GhostController(k * cellSize, i * cellSize, 0);
                }
            }
        }
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void endGame(Graphics g){
        isGameOver = true;
        Color textColor = new Color(170, 150, 0);
        if(!areDotesExist()){
            g.setColor(textColor);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString("YOU WОN", 265, 433);
        } else {
            g.setColor(textColor);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString("GAME OVER", 250, 433);
        }
    }
    
    public boolean areDotesExist(){     // to finish the game, if dotes are not exists anymore 
        for(int i = 0; i < maze.length; i++) {
            for(int k = 0; k < maze[i].length; k++) {
                if(maze[i][k].equals("2") || maze[i][k].equals("3")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void isSameCellWithDotesOrGhosts(Graphics g){
        int currentX = pacman.getX() / cellSize;
        int currentY = pacman.getY() / cellSize;

        if (maze[currentY][currentX].equals("2")) {     // if pacman has eaten dote, it become to be just black cell
            maze[currentY][currentX] = "0";
            points = points + 5;
        } else if (maze[currentY][currentX].equals("3")) {
            maze[currentY][currentX] = "0";
            points = points + 20;
        } else if ((currentX == ghost1.getX()/ cellSize && currentY == ghost1.getY()/ cellSize) ||  // if was eaten by ghost, it will restart game
                (currentX == ghost2.getX()/ cellSize && currentY == ghost2.getY()/ cellSize) ||
                (currentX == ghost3.getX()/ cellSize && currentY == ghost3.getY()/ cellSize) ||
                (currentX == ghost4.getX()/ cellSize && currentY == ghost4.getY()/ cellSize) )  {

                lives--;
                if (lives == 0) {   // if pacman don't have lives anymore -> game over
                    endGame(g);
                    return;
                }
                startGame();

        } else if (maze[currentY][currentX].equals("7")) {  // if pacman at cell 7, he teleports to cell 8
            for (int i = 0; i < maze.length; i++) {
                for (int k = 0; k < maze[i].length; k++) {
                    if (maze[i][k].equals("8")) {
                        pacman.setX(k*cellSize - cellSize);
                        pacman.setY(i*cellSize);
                    }
                }
            }
        }else if (maze[currentY][currentX].equals("8")) {   // if pacman at cell 8, he teleports to cell 7
            for (int i = 0; i < maze.length; i++) {
                for (int k = 0; k < maze[i].length; k++) {
                    if (maze[i][k].equals("7")) {
                        pacman.setX(k*cellSize + cellSize);
                        pacman.setY(i*cellSize);
                    }
                }
            }
        }
    }

    public void setDirection(int key) {

        int oldDirection = pacman.getDirection();

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            bufferedDirection = 0;
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            bufferedDirection = 1;
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            bufferedDirection = 2;
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            bufferedDirection = 3;
        }

        if(!pacman.checkNextStep(bufferedDirection)) {
            pacman.setDirection(oldDirection);
        } else {
            pacman.setDirection(bufferedDirection);
        }
    }

    public void setNewDirection() {

        int oldDirection = pacman.getDirection();

        if(!pacman.checkNextStep(bufferedDirection)) {
            pacman.setDirection(oldDirection);
        } else {
            pacman.setDirection(bufferedDirection);
            bufferedDirection = 5;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));

        Color darkBlue = new Color(0, 0, 150);
        Color background = new Color(0,0, 30);

        for (int i = 0; i < maze.length; i++) { // draw maze, score and hearts
            for (int k = 0; k < maze[i].length; k++) {

                g2d.setStroke(new BasicStroke(8));

                if (maze[i][k].equals("H")) {   // blue horizontal line on a black background
                    g.setColor(background);
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);
                    int currentY = i * cellSize + (cellSize / 2) - 2;

                    g.setColor(darkBlue);
                    g.fillRect(k * cellSize, currentY, cellSize, 8);

                } else if (maze[i][k].equals("D")) {    // white horizontal line on a black background
                    g.setColor(background);
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);
                    int currentY = i * cellSize + (cellSize / 2) - 2 + 2;

                    g.setColor(Color.WHITE);
                    g.fillRect(k * cellSize, currentY, cellSize, 4);

                } else if (maze[i][k].equals("V")) {    // blue vertical line on a black background
                    g.setColor(background);
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);

                    int currentX = k * cellSize + (cellSize / 2) - 2;

                    g.setColor(darkBlue);
                    g.fillRect(currentX, i * cellSize, 8, cellSize);

                } else if(maze[i][k].equals("TL")) {        // blue quarter circle for top-left corner
                    g.setColor(background);                 // on a black background
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);

                    g.setColor(darkBlue);

                    g.drawArc(k * cellSize + cellSize/2 + 2, i * cellSize + cellSize/2 + 2,
                            cellSize, cellSize, 90, 90);

                } else if(maze[i][k].equals("TR")) {        // blue quarter circle for top-right corner
                    g.setColor(background);                 // on a black background
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);

                    g.setColor(darkBlue);

                    g.drawArc(k * cellSize - cellSize/2 + 2, i * cellSize + cellSize/2 + 2,
                            cellSize, cellSize, 0, 90);

                } else if(maze[i][k].equals("BL")) {        // blue quarter circle for bottom-left corner
                    g.setColor(background);                 // on a black background
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);

                    g.setColor(darkBlue);

                    g.drawArc(k * cellSize + cellSize/2 + 2, i * cellSize - cellSize/2 + 2,
                            cellSize, cellSize, 180, 90);

                } else if(maze[i][k].equals("BR")) {        // blue quarter circle for bottom-right corner
                    g.setColor(background);                 // on a black background
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);

                    g.setColor(darkBlue);

                    g.drawArc(k * cellSize - cellSize/2 + 2, i * cellSize - cellSize/2 + 2,
                            cellSize, cellSize, -90, 90);

                } else if (maze[i][k].equals("0") || maze[i][k].equals("9") ||  // all of this is just black cell
                        maze[i][k].equals("7") || maze[i][k].equals("8")
                        || maze[i][k].equals("G1") || maze[i][k].equals("G2")
                        || maze[i][k].equals("G3") || maze[i][k].equals("G4")
                        || maze[i][k].equals("W1") || maze[i][k].equals("W2")) {

                    g.setColor(background);
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);

                } else if (maze[i][k].equals("2")) {        // small white rectangle on a black background
                    g.setColor(background);
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);

                    g.setColor(Color.WHITE);

                    g.fillRect(k * cellSize + 12, i * cellSize + 12, 4, 4);

                } else if (maze[i][k].equals("3")) {        // big white circle on a black background
                    g.setColor(background);
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);

                    g.setColor(Color.WHITE);
                    int width = 14;

                    g.fillArc(k * cellSize + (cellSize - width)/2, i * cellSize + (cellSize - width)/2,
                            width, width, 0, 360);
                } else if(maze[i][k].equals("BLT")) {       // bottom left corner of the whole maze
                    g.setColor(background);
                    g.fillRect(k * cellSize, i * cellSize, cellSize, cellSize);     // draw black cell

                    g.setColor(darkBlue);

                    g.drawArc(k * cellSize + cellSize/2 + 2, i * cellSize - cellSize/2 + 2,
                            cellSize, cellSize, 180, 90);           // blue quarter circle for bottom-left corner
                    g.setColor(Color.BLACK);
                    g.fillRect(k * cellSize, i * cellSize + cellSize, 900, 400); // draw black rectangle under maze

                    Color textColor = new Color(170, 150, 0);
                    g.setColor(textColor);
                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.drawString("SCORE: " + points, k * cellSize + cellSize, i * cellSize + cellSize + 30);//write score

                    g.setColor(textColor);
                    g.setFont(new Font("Arial", Font.PLAIN, 30));

                    if(lives > 0) { //draw hearts
                        g.setColor(Color.RED);
                        int diameter = 16;

                        for(int s = 0; s < lives; s++) {    // draw hearts
                            int x1 = k * cellSize + 500 + 100 - 50 * s;
                            int y1 = i * cellSize + cellSize + 5;

                            // draw two semicircles in the top heart
                            g.fillArc(x1, y1, diameter, diameter, 0, 180);
                            g.fillArc(x1 + diameter, y1, diameter, diameter, 0, 180);

                            // draw semicircle left to the middle rectangle
                            g.fillArc(x1, y1 - diameter / 2, diameter, diameter * 2, 180, 90);

                            // draw rectangle in the middle of heart
                            g.fillRect(x1 + diameter / 2, y1 + diameter / 2, diameter, diameter);

                            // draw semicircle right to the middle rectangle
                            g.fillArc(x1 + diameter, y1 - diameter / 2, diameter, diameter * 2, 270, 90);


                            // arrays for triangle for bottom of heart
                            int[] xPoints = {x1 + diameter / 2 - diameter / 4, x1 + diameter / 2 + diameter + diameter / 4,
                                    x1 + diameter / 2 + diameter / 2};
                            int[] yPoints = {y1 + diameter / 2 + diameter - diameter / 12, y1 + diameter / 2 + diameter - diameter / 12,
                                    y1 + diameter / 2 + diameter + diameter / 2};

                            // draw bottom triangle
                            g.fillPolygon(xPoints, yPoints, 3);
                        }
                    }
                }
//                g2d.setStroke(new BasicStroke(2));
//                g.setColor(Color.BLACK);
//                g.drawRect(k * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        if(!areDotesExist()){
            endGame(g);
            return;
        }

        if (!isGameOver && pacman.getDirection() != 4 && pacman != null) {
            isSameCellWithDotesOrGhosts(g);

            setNewDirection();
            pacman.checkNextStep(pacman.getDirection());

            ghost1.setDirectionGhost("W2");
            ghost1.checkNextStep(ghost1.getDirection(), "W2");
            ghost2.setDirectionGhost("W1");
            ghost2.checkNextStep(ghost2.getDirection(), "W1");
            ghost3.setDirectionGhost("W1");
            ghost3.checkNextStep(ghost3.getDirection(), "W1");
            ghost4.setDirectionGhost("W2");
            ghost4.checkNextStep(ghost4.getDirection(), "W2");

        } else if (pacman.getDirection() == 4){
            Color textColor = new Color(170, 150, 0);
            g.setColor(textColor);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString("GET READY", 255, 433);
        }


        ghost1.draw(g, Color.RED);
        ghost2.draw(g, Color.PINK);
        ghost3.draw(g, Color.BLUE);
        Color ghostColor4 = new Color(230, 150, 60);
        ghost4.draw(g, ghostColor4);

        if (isGameOver) {
            endGame(g);
            return;
        }

        pacman.draw(g);

    }
}




