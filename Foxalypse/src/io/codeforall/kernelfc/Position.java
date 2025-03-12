package io.codeforall.kernelfc;

import io.codeforall.kernelfc.Grid.Grid;
import org.academiadecodigo.simplegraphics.pictures.Picture;

public class Position {

    private int row;
    private int col;
    private static final int CELL_SIZE = 30;  // Tamanho de cada célula
    private static final int PADDING = 10;    // Margem da grid




    public Position(int col, int row){
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
    public static int getX(int col) {
        return PADDING + col * CELL_SIZE;
    }

    public static int getY(int row) {
        return PADDING + row * CELL_SIZE;
    }

    public int rowToY(int row) {
        return PADDING + CELL_SIZE * row;
    }

    public int colToX(int col) {
        return PADDING + CELL_SIZE * col;
    }

}
