package io.codeforall.kernelfc.Grid;

import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.pictures.Picture;

public class Grid {

    private static final int CELL_SIZE = 30;  // Tamanho de cada célula
    private static final int PADDING = 10;    // Margem da grid


    private Picture frontyard;


    public Grid() {

        drawBackground();
    }

    public void deleteGrid (){
        frontyard.delete();
    }

    public void drawBackground() {
        frontyard = new Picture(PADDING, PADDING, "Resources/FrontyardNumSmall.png");
        frontyard.draw();

    }

    // Calcula a coordenada X com base na coluna
    public static int getX(int col) {
        return PADDING + col * CELL_SIZE;
    }

    public int rowToY(int row) {
        return PADDING + CELL_SIZE * row;
    }


    // Calcula a coordenada Y com base na linha
    public static int getY(int row) {
        return PADDING + row * CELL_SIZE;
    }

    public int colToX(int col) {
        return PADDING + CELL_SIZE * col;
    }


}










