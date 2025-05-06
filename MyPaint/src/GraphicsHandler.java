import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;

import javax.print.DocFlavor;
import java.io.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;


public class GraphicsHandler extends Rectangle implements KeyboardHandler {


    private Rectangle rectangle;
    private final Keyboard keyboard;
    private Rectangle newRectangle;
    private Grid grid;
   //SHOULD USE THIS TO SAVE THE PAINTED SQUARES COORDINATES?
   private Set<String> paintedCoordinates = new HashSet<>();



    public GraphicsHandler() {
        this.keyboard = new Keyboard((KeyboardHandler) this);



    }

    public void innit() {
        this.grid = new Grid();

        rectangle = new Rectangle(10, 10, 30, 30);
        rectangle.setColor(Color.GREEN);
        rectangle.fill();

    }


    public void fillCellAtRectanglePosition() {
        int col = (rectangle.getX() - Grid.PADDING) / Grid.CELL_SIZE;
        int row = (rectangle.getY() - Grid.PADDING) / Grid.CELL_SIZE;

        // Check array bounds clearly
        if (col >= 0 && col < Grid.COLS && row >= 0 && row < Grid.ROWS) {
            Rectangle cellToFill = grid.getRectangleArray()[row][col];
            cellToFill.setColor(Color.BLACK);
            cellToFill.fill();

            int x = Grid.getX(col);
            int y = Grid.getY(row);
            String coordinate = x + "," + y;
            paintedCoordinates.add(coordinate);

        }

    }


    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("coordinates.txt"))) {
            for (String coordinate : paintedCoordinates) {
                writer.write(coordinate);
                writer.newLine();
            }
            System.out.println("Coordinates saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("coordinates.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                fillCellAtPosition(x, y);
                paintedCoordinates.add(line); // Populate the gridf
            }
            System.out.println("Coordinates loaded from file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillCellAtPosition(int x, int y) {
        // x, y coordinates to row and col
        int col = (x - Grid.PADDING) / Grid.CELL_SIZE;
        int row = (y - Grid.PADDING) / Grid.CELL_SIZE;

        // Ensure the coordinates are within the grid's bounds
        if (col >= 0 && col < Grid.COLS && row >= 0 && row < Grid.ROWS) {
            Rectangle cellToFill = grid.getRectangleArray()[row][col];
            cellToFill.setColor(Color.BLACK);
            cellToFill.fill();
        }
    }



        public void createKeyboardEvent() {

            KeyboardEvent keyboardEventDown = new KeyboardEvent();
            keyboardEventDown.setKey(KeyboardEvent.KEY_DOWN);
            keyboardEventDown.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            keyboard.addEventListener(keyboardEventDown);

            KeyboardEvent keyboardEventUp = new KeyboardEvent();
            keyboardEventUp.setKey(KeyboardEvent.KEY_UP);
            keyboardEventUp.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            keyboard.addEventListener(keyboardEventUp);

            KeyboardEvent keyboardEventRight = new KeyboardEvent();
            keyboardEventRight.setKey(KeyboardEvent.KEY_RIGHT);
            keyboardEventRight.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            keyboard.addEventListener(keyboardEventRight);
            //this

            KeyboardEvent keyboardEventLeft = new KeyboardEvent();
            keyboardEventLeft.setKey(KeyboardEvent.KEY_LEFT);
            keyboardEventLeft.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            keyboard.addEventListener(keyboardEventLeft);

            KeyboardEvent keyboardEventSpace = new KeyboardEvent();
            keyboardEventSpace.setKey(KeyboardEvent.KEY_SPACE);
            keyboardEventSpace.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            keyboard.addEventListener(keyboardEventSpace);

            KeyboardEvent keyboardEventS = new KeyboardEvent();
            keyboardEventS.setKey(KeyboardEvent.KEY_S);
            keyboardEventS.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            keyboard.addEventListener(keyboardEventS);

            KeyboardEvent keyboardEventL = new KeyboardEvent();
            keyboardEventL.setKey(KeyboardEvent.KEY_L);
            keyboardEventL.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            keyboard.addEventListener(keyboardEventL);


        }




        @Override
        public void keyPressed (KeyboardEvent keyboardEvent){

            switch (keyboardEvent.getKey()) {
                case KeyboardEvent.KEY_DOWN:
                    rectangle.translate(0, +30);
                    System.out.println(rectangle);
                    break;

                case KeyboardEvent.KEY_UP:
                    rectangle.translate(0, -30);
                    System.out.println(rectangle);
                    break;

                case KeyboardEvent.KEY_RIGHT:
                    rectangle.translate(+30, 0);
                    System.out.println(rectangle);
                    break;

                case KeyboardEvent.KEY_LEFT:
                    rectangle.translate(-30, 0);
                    System.out.println(rectangle);
                    break;

                case KeyboardEvent.KEY_SPACE:

                        fillCellAtRectanglePosition();

                        break;

                case KeyboardEvent.KEY_S:

                    saveData();
                    break;

                case KeyboardEvent.KEY_L:

                    loadData();

                    break;


            }


        }

        @Override
        public void keyReleased (KeyboardEvent keyboardEvent){

        }


    }

