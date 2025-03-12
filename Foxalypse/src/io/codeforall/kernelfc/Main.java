package io.codeforall.kernelfc;

import io.codeforall.kernelfc.Grid.Grid;

public class Main {

    public static void main(String[] args) {
            newGame();
    }

    private static void newGame() {
        Game game = new Game();
        Menu menu = new Menu();
        menu.menuStatus();
        game.init();
        newGame();
    }
}
