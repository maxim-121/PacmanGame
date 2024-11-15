// Pac-Man: Next Gen
// Maksym Yakushechkin CTS3

/** This class contains methods of ghost movements and drawing */

package game.entities;

import game.GamePanel;

import java.awt.*;

public class GhostController {

    public int ghostX, ghostY;
    private int speed = 1;
    private int direction;
    private GamePanel gamePanel = new GamePanel();
    private int cellSize = 24;

    public GhostController(int gostX, int gostY, int direction) {
        this.ghostX = gostX;
        this.ghostY = gostY;
        this.direction = direction;
    }

    public int getX(){
        return ghostX;
    }

    public int getY(){
        return ghostY;
    }

    public int getDirection(){
        return direction;
    }

    public boolean checkNextStep(int direction, String wall) {  // ghost's collision, similarly to pacman's collision

        int nextX1 = 0;
        int nextY1 = 0;
        int nextX2 = 0;
        int nextY2 = 0;
        String[][] maze = gamePanel.getMaze();

        if (direction == 0){
            nextY1 = (ghostY - 3) / cellSize;
            nextY2 = (ghostY - 3) / cellSize;
            nextX1 = ghostX / cellSize;
            nextX2 = (ghostX + cellSize - 3) / cellSize;
        } else if (direction == 1) {
            nextY1 = (ghostY + cellSize) / cellSize;
            nextY2 = (ghostY + cellSize) / cellSize;
            nextX1 = ghostX / cellSize;
            nextX2 = (ghostX + cellSize - 3) / cellSize;
        } else if (direction == 2) {
            nextY1 = ghostY / cellSize;
            nextY2 = (ghostY + cellSize - 3) / cellSize;
            nextX1 = (ghostX + cellSize) / cellSize;
            nextX2 = (ghostX + cellSize) / cellSize;
        } else if (direction == 3) {
            nextY1 = ghostY / cellSize;
            nextY2 = (ghostY + cellSize - 3) / cellSize;
            nextX1 = (ghostX - 3) / cellSize;
            nextX2 = (ghostX - 3) / cellSize;
        }

        if (nextX1 < 0 || nextX1 >= maze[0].length || nextY1 < 0 || nextY1 >= maze.length ||
                nextX2 < 0 || nextX2 >= maze[0].length || nextY2 < 0 || nextY2 >= maze.length) {
            return false;
        }

        if (!maze[nextY1][nextX1].equals("H") && !maze[nextY2][nextX2].equals("H") &&
                !maze[nextY1][nextX1].equals("V") && !maze[nextY2][nextX2].equals("V") &&
                !maze[nextY1][nextX1].equals("TR") && !maze[nextY2][nextX2].equals("TR") &&
                !maze[nextY1][nextX1].equals("TL") && !maze[nextY2][nextX2].equals("TL") &&
                !maze[nextY1][nextX1].equals("BR") && !maze[nextY2][nextX2].equals("BR") &&
                !maze[nextY1][nextX1].equals("BL") && !maze[nextY2][nextX2].equals("BL") &&
                !maze[nextY1][nextX1].equals(wall)
        ) {
            move(direction);
            return true;
        }
        return false;
    }

    public void setDirectionGhost(String wall) {        // find random direction for ghosts
        if(!checkNextStep(direction, wall)) {
            direction = (int)(Math.random() * 4);
        }
    }

    public void move(int direction) {           // set direction
        if (direction == 0){
            this.ghostY -= speed;
        } else if (direction == 1) {
            this.ghostY += speed;
        } else if (direction == 2) {
            this.ghostX += speed;
        } else if (direction == 3) {
            this.ghostX -= speed;
        }
    }

    public void draw(Graphics g, Color gostColor) {         // draw ghost
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(1));

        g.setColor(gostColor);
        int diameter = 24;      // diameter of top hemisphere

        int x1 = this.ghostX;
        int y1 = this.ghostY;

        g.fillArc(x1, y1, diameter, diameter, 0, 180);      // top hemisphere
        g.fillRect(x1, y1 + 12, diameter, 10);      // main body rectangle

        // for bottom of ghost we are using sin(x) function
        int amplitude = 3;
        int frequency = 7;
        int yOffset = y1 + 26;

        // sin(x) function
        for (int x2 = x1; x2 < x1 + diameter; x2++) {
            int y2 = yOffset + (int) (amplitude * Math.sin(x2 * 2 * Math.PI / frequency));
            g.drawLine(x2, y2, x2, yOffset - 6);    // we draw line from the sine wave to main body rectangle
        }

        //drawing eyes
        g.setColor(Color.WHITE);
        g.fillArc(x1 + 4, y1 + 9, 8, 8, 0, 360);
        g.fillArc(x1 + 12, y1 + 9, 8, 8, 0, 360);

        g.setColor(Color.BLACK);
        g.fillArc(x1 + 6, y1 + 11, 4, 4, 0, 360);
        g.fillArc(x1 + 14, y1 + 11, 4, 4, 0, 360);

        g2d.setStroke(new BasicStroke(8));
    }
}