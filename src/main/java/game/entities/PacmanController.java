/** This class contains methods of pacman movements and drawing */

package game.entities;

import game.GamePanel;

import java.awt.*;

public class PacmanController {
    public int pacmanX, pacmanY;
    private int speed = 4;
    private int direction;
    private GamePanel gamePanel = new GamePanel();
    private int cellSize = 24;
    private boolean isOpen = true;
    private int mouthAngle1 = 90;
    private int mouthAngle2 = 0;

    public PacmanController(int pacmanX, int pacmanY, int direction) {
        this.pacmanX = pacmanX;
        this.pacmanY = pacmanY;
        this.direction = direction;
    }

    public int getX(){
        return pacmanX;
    }

    public int getY(){
        return pacmanY;
    }

    public int getDirection(){
        return direction;
    }

    public void setX(int pacmanX){
        this.pacmanX = pacmanX;
    }

    public void setY(int pacmanY){
        this.pacmanY = pacmanY;
    }

    public void setDirection(int direction){
        this.direction = direction;
    }

    public boolean checkNextStep(int direction) {   //to check collision

        int nextX1 = 0;         // nextX1 and nextY1 are for one corner of pacman rectangle
        int nextY1 = 0;
        int nextX2 = 0;         // nextX2 and nextY2 are for second corner of pacman rectangle
        int nextY2 = 0;
        String[][] maze = gamePanel.getMaze();

        /**
         * Idea is to choose one side of pacman hitbox (rectangle). It depends on pacman direction.
         * After that we have to save relatively close to both corners coordinates to nextX1, nextY1,
         * nextX2 and nextY2. But important, if direction equal:
         *  0) if pacman go up, we save top-left corner (-3) by y-axis and top-right corner also (-3) by y-axis
         *  1) if pacman go down, we save bottom-left corner (+3) by y-axis and bottom-right corner also (+3) by y-axis
         *  2) if pacman go right, we save top-right corner (+3) by x-axis and bottom-right corner also (+3) by x-axis
         *  3) if pacman go left, we save top-left corner (-3) by x-axis and bottom-left corner also (-3) by x-axis
         */

        if (direction == 0){
            nextY1 = (pacmanY - 3) / cellSize;
            nextY2 = (pacmanY - 3) / cellSize;
            nextX1 = pacmanX / cellSize;
            nextX2 = (pacmanX + cellSize - 3) / cellSize;
        } else if (direction == 1) {
            nextY1 = (pacmanY + cellSize + 2) / cellSize;
            nextY2 = (pacmanY + cellSize + 2) / cellSize;
            nextX1 = pacmanX / cellSize;
            nextX2 = (pacmanX + cellSize - 3) / cellSize;
        } else if (direction == 2) {
            nextY1 = pacmanY / cellSize;
            nextY2 = (pacmanY + cellSize - 3) / cellSize;
            nextX1 = (pacmanX + cellSize + 2) / cellSize;
            nextX2 = (pacmanX + cellSize + 2) / cellSize;
        } else if (direction == 3) {
            nextY1 = pacmanY / cellSize;
            nextY2 = (pacmanY + cellSize - 3) / cellSize;
            nextX1 = (pacmanX - 3) / cellSize;
            nextX2 = (pacmanX - 3) / cellSize;
        }

        // check "is pacman inside maze or not"
        if (nextX1 < 0 || nextX1 >= maze[0].length || nextY1 < 0 || nextY1 >= maze.length ||
                nextX2 < 0 || nextX2 >= maze[0].length || nextY2 < 0 || nextY2 >= maze.length) {
            return false;
        }

        // check "is next cell barrier or not", if it's not -> move forward
        if (!maze[nextY1][nextX1].equals("H") && !maze[nextY2][nextX2].equals("H") &&
                !maze[nextY1][nextX1].equals("V") && !maze[nextY2][nextX2].equals("V") &&
                !maze[nextY1][nextX1].equals("TR") && !maze[nextY2][nextX2].equals("TR") &&
                !maze[nextY1][nextX1].equals("TL") && !maze[nextY2][nextX2].equals("TL") &&
                !maze[nextY1][nextX1].equals("BR") && !maze[nextY2][nextX2].equals("BR") &&
                !maze[nextY1][nextX1].equals("BL") && !maze[nextY2][nextX2].equals("BL") &&
                !maze[nextY2][nextX2].equals("D")
        ) {
            move(direction);
            return true;
        }
        return false;
    }

    public void move(int direction) {
        if (direction == 0){    // go up
            this.pacmanY -= speed;
        } else if (direction == 1) {    // go down
            this.pacmanY += speed;
        } else if (direction == 2) {    // go right
            this.pacmanX += speed;
        } else if (direction == 3) {    // go left
            this.pacmanX -= speed;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color pacmanColor = new Color(220, 175, 0);
        g2d.setColor(pacmanColor);
        int pacmanX = this.pacmanX;
        int pacmanY = this.pacmanY;

        int startAngle = switch (direction) {   // at what side mouth is open depends on his direction
            case 0 -> 135;
            case 1 -> 315;
            case 2 -> 45;
            case 3 -> 225;
            default -> 45;
        };

        int arcAngle = 360 - mouthAngle1;

        // drawing pacman
        g2d.fillArc(pacmanX, pacmanY, cellSize + 3, cellSize + 3, startAngle, arcAngle);
        g2d.fillArc(pacmanX, pacmanY, cellSize + 3, cellSize + 3, startAngle, mouthAngle2);

        // set new angeles for mouth
        if(direction != 4) {
            if (isOpen) {
                mouthAngle1 -= 5;
                mouthAngle2 -= 5;
                if (mouthAngle1 <= 45) {
                    isOpen = false;
                }
            } else {
                mouthAngle1 += 5;
                mouthAngle2 += 5;
                if (mouthAngle1 >= 90) {
                    isOpen = true;
                }
            }
        }
    }
}