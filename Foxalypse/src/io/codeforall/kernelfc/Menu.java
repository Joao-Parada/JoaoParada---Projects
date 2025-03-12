package io.codeforall.kernelfc;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;
import org.academiadecodigo.simplegraphics.pictures.Picture;
public class Menu  implements KeyboardHandler {

    private Picture menu;
    private KeyboardEvent keyboardEvent;

    private Keyboard keyboard;

    private boolean menuEnded;

    public Menu(){
            this.keyboardEvent = new KeyboardEvent();
            this.menuEnded = false;



            menu = new Picture(10, 10, "Resources/FrontPage.png");
            menu.draw();


    }
    public void createKeyboardEvent() {

        keyboard = new Keyboard(this);

        // Upgrade flowers
        KeyboardEvent menu = new KeyboardEvent();
        menu.setKey(KeyboardEvent.KEY_ENTER);
        menu.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(menu);
    }


    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()){
            case KeyboardEvent.KEY_ENTER:
                menuEnded = true;
                menu.delete();
                break;
        }

    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }

    public void menuStatus(){
        createKeyboardEvent();
        while(!menuEnded){
            System.out.println("");
        }

    }
}
