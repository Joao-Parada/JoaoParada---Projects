import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;

import java.util.Arrays;


public class Grid {
    public static final int CELL_SIZE = 30;
    public static final int COLS = 20;
    public static final int ROWS = 20;
    public static final int PADDING = 10;

    private Rectangle[][] rectangleArray;

    public Grid() {
        rectangleArray = new Rectangle[ROWS][COLS];
        drawGrid();
    }
0
    public Rectangle[][] getRectangleArray() {
        return rectangleArray
    }

    private void drawGrid() {
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                Rectangle gridRectangle = new Rectangle(getX(col), getY(row), CELL_SIZE, CELL_SIZE);
                gridRectangle.setColor(Color.BLACK);
                gridRectangle.draw();
                rectangleArray[row][col] = gridRectangle;
            }
        }
    }

    public static int getX(int col) {
        return PADDING + col * CELL_SIZE;
    }

    public static int getY(int row) {
        return PADDING + row * CELL_SIZE;
    }
}




