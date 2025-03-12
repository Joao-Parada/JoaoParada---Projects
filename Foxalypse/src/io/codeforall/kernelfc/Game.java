package io.codeforall.kernelfc;

import io.codeforall.kernelfc.Grid.Grid;
import io.codeforall.kernelfc.characters.Fox;
import io.codeforall.kernelfc.characters.Plant;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Text;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Game implements KeyboardHandler {
    private Grid grid;
    private List<Fox> foxes;
    private Plant[] plants;
    private Projectile[] projectiles;

    private Projectile[] projectilesCopy;

    private BackgroundSound backgroundSound;
    private BackgroundSound plantUpgrade;
    private BackgroundSound gameOverSound;

    public static Text scoreImg;
    public static Text foxCountImg;


    private double time = 300;
    private boolean loose = false;
    private boolean restart = false;

    private int score;


    private int upgradesMade = 0;
    private final int foxReward = 10;

    private int totalKilledFoxes = 0;

    private int foxDied = 0;

    private int projectileCounter = 1;

    private int plantCounter = 4;

    private boolean[] isPlantCreated = new boolean[4];

    private Keyboard keyboard;
    private KeyboardEvent keyboardEvent;

    private Picture gameOver;


    public Game() {
        scoreImg = new Text(350, 40, "SCORE: 0");
        scoreImg.grow(40, 20);
        scoreImg.setColor(Color.BLACK);
        grid = new Grid();
        backgroundSound = new BackgroundSound("Resources/Thunder-Unison-Action-Dramatic-Epic-Music-chosic.com_.wav");
        plantUpgrade = new BackgroundSound("Resources/mixkit-unlock-game-notification-253.wav");
        this.foxes = new ArrayList<>();
        this.plants = new Plant[4];
        this.projectiles = new Projectile[1000];
        this.projectilesCopy = new Projectile[1000];
        this.score = 0;
        this.keyboardEvent = new KeyboardEvent();
    }

    public void init() {


        plants[0] = new Plant(10, 3);
        plants[1] = new Plant(10, 8);
        plants[2] = new Plant(10, 12);
        plants[3] = new Plant(10, 17);
        createFoxes();
        gameMovement();
    }

    // public int[] foxYPosition = {3,8,12,17};
    // int randomFoxY = foxYPosition[ThreadLocalRandom.current().nextInt(foxYPosition.length)];

    public void createFoxes() {
        int[] foxYPositions = {3, 8, 12, 17}; // Possible Y positions

        // Generate a fox every few seconds
        new Thread(() -> {
            while (!loose) {
                int randomY = foxYPositions[ThreadLocalRandom.current().nextInt(foxYPositions.length)];
                foxes.add(new Fox(35, randomY)); // Spawn fox at random Y position

                try {
                    Thread.sleep(5000); // Spawn new fox every 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void move() {
        // Move projectiles
        for (int i = 0; i < projectileCounter; i++) {
            if (projectiles[i] != null && !projectiles[i].getCollided()) {
                projectileFoxCollision(projectiles[i]);
                projectiles[i].move();
            }
        }

        // Use Iterator to remove foxes while iterating
        Iterator<Fox> iterator = foxes.iterator();
        while (iterator.hasNext()) {
            Fox fox = iterator.next();

            if (!fox.isDead()) {
                for (Plant plant : plants) {
                    if (fox.getIsCollided() && fox.getPosition().equals(plant.getPosition())) {
                        fox.attack(plant);

                    }
                }

                if (!fox.getIsCollided()) {
                    fox.move();
                    plantFoxCollision(fox);
                }
            } else {
                iterator.remove(); // SAFE removal
            }
        }
    }


    public void plantFoxCollision(Fox fox) {
        for (Plant plant : plants) {
            if (plant != null &&
                    fox.getPosition().getCol() == plant.getPosition().getCol() &&
                    fox.getPosition().getRow() == plant.getPosition().getRow()) {

                fox.setCollided(true);
                fox.attack(plant);// Fox attacks the plant when they collide
            }
        }
    }


    public void projectileFoxCollision(Projectile projectile) {
        Iterator<Fox> iterator = foxes.iterator();

        while (iterator.hasNext()) {
            Fox fox = iterator.next();

            if (Math.abs(projectile.getPosition().getCol() - fox.getPosition().getCol()) <= 1 &&
                    projectile.getPosition().getRow() == fox.getPosition().getRow() &&
                    !projectile.getCollided()) {

                int foxDied = fox.takeDamage(projectile.getDamage());

                if (foxDied > 0) { // If the fox takes damage and dies
                    totalKilledFoxes += foxDied;
                    this.foxDied += foxDied;
                    score += (foxDied * foxReward);

                    // Update the score display
                    scoreImg.setText("SCORE: " + score);
                    scoreImg.draw();

                    if (fox.isDead()) {
                        iterator.remove(); // Remove dead fox
                    }
                }

                // Mark projectile as collided and delete it
                projectile.setCollided(true);
                projectile.deleteBullet();
                break; // Stop checking other foxes for this projectile
            }
        }
    }


    public void createKeyboardEvent() {

        keyboard = new Keyboard(this);

        // Upgrade flowers
        KeyboardEvent plant1 = new KeyboardEvent();
        plant1.setKey(KeyboardEvent.KEY_1);
        plant1.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(plant1);

        KeyboardEvent plant2 = new KeyboardEvent();
        plant2.setKey(KeyboardEvent.KEY_2);
        plant2.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(plant2);

        KeyboardEvent plant3 = new KeyboardEvent();
        plant3.setKey(KeyboardEvent.KEY_3);
        plant3.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(plant3);

        KeyboardEvent plant4 = new KeyboardEvent();
        plant4.setKey(KeyboardEvent.KEY_4);
        plant4.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(plant4);

        KeyboardEvent UpgradePlant1 = new KeyboardEvent();
        UpgradePlant1.setKey(KeyboardEvent.KEY_Q);
        UpgradePlant1.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(UpgradePlant1);

        KeyboardEvent upgradePlant2 = new KeyboardEvent();
        upgradePlant2.setKey(KeyboardEvent.KEY_W);
        upgradePlant2.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(upgradePlant2);

        KeyboardEvent upgradePlant3 = new KeyboardEvent();
        upgradePlant3.setKey(KeyboardEvent.KEY_E);
        upgradePlant3.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(upgradePlant3);

        KeyboardEvent upgradePlant4 = new KeyboardEvent();
        upgradePlant4.setKey(KeyboardEvent.KEY_R);
        upgradePlant4.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(upgradePlant4);

        KeyboardEvent restartGame = new KeyboardEvent();
        restartGame.setKey(KeyboardEvent.KEY_SPACE);
        restartGame.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(restartGame);

    }


    private void foxKilledChicken() {
        for (Fox fox : foxes) { // Correct iteration for ArrayList
            if (fox.getPosition().getCol() == 7) { // Check if fox reached column 7
                loose = true;
                break; // No need to continue checking if game is already lost
            }
        }
    }


    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()) {

            case KeyboardEvent.KEY_Q:

                if (score >= 50 + (50 * upgradesMade) && !plants[0].isUpgraded()) {
                    plants[0].updatePlant();
                    upgradesMade++;
                    plantUpgrade.play();
                    plantUpgrade = new BackgroundSound("Resources/mixkit-unlock-game-notification-253.wav");
                }
                break;

            case KeyboardEvent.KEY_W:

                if (score >= 100 + (50 * upgradesMade) && !plants[1].isUpgraded()) {
                    plants[1].updatePlant();
                    upgradesMade++;
                    plantUpgrade.play();
                    plantUpgrade = new BackgroundSound("Resources/mixkit-unlock-game-notification-253.wav");
                }

                break;

            case KeyboardEvent.KEY_E:

                if (score >= 200 + (50 * upgradesMade) && !plants[2].isUpgraded()) {
                    plants[2].updatePlant();
                    upgradesMade++;
                    plantUpgrade.play();
                    plantUpgrade = new BackgroundSound("Resources/mixkit-unlock-game-notification-253.wav");
                }
                break;

            case KeyboardEvent.KEY_R:

                if (score >= 300 + (50 * upgradesMade) && !plants[3].isUpgraded()) {
                    plants[3].updatePlant();
                    upgradesMade++;
                    plantUpgrade.play();
                    plantUpgrade = new BackgroundSound("Resources/mixkit-unlock-game-notification-253.wav");
                }
                break;

            case KeyboardEvent.KEY_1:
                if (plants[0] != null && !plants[0].isDead()) {
                    projectiles[projectileCounter] = new Projectile(12, 3, plants[0].getDamage());
                    projectileCounter++;
                }
                break;

            case KeyboardEvent.KEY_2:
                if (plants[1] != null && !plants[1].isDead()) {
                    projectiles[projectileCounter] = new Projectile(12, 8, plants[1].getDamage());
                    projectileCounter++;
                }
                break;

            case KeyboardEvent.KEY_3:
                if (plants[2] != null && !plants[2].isDead()) {
                    projectiles[projectileCounter] = new Projectile(12, 12, plants[2].getDamage());
                    projectileCounter++;
                }
                break;

            case KeyboardEvent.KEY_4:
                if (plants[3] != null && !plants[3].isDead()) {
                    projectiles[projectileCounter] = new Projectile(12, 17, plants[3].getDamage());
                    projectileCounter++;
                }
                break;

            case KeyboardEvent.KEY_SPACE:
                if (loose) {
                    gameOver.delete();
                    restart = true;

                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }

    private void deleteEverything() {
        // Delete plants
        for (Plant plant : plants) {
            if (plant != null && !plant.isDead()) {
                plant.die();
            }
        }

        // Delete foxes correctly using ArrayList
        for (Fox fox : new ArrayList<>(foxes)) { // Avoid ConcurrentModificationException
            fox.delete();
        }
        foxes.clear(); // Clear list to remove all foxes

        // Delete projectiles
        for (Projectile projectile : projectiles) {
            if (projectile != null && !projectile.getCollided()) {
                projectile.deleteBullet();
            }
        }

        // Reset game elements
        score = 0;
        scoreImg.delete();
        grid.deleteGrid();
    }


    public void gameMovement() {
        createKeyboardEvent();
        while (!loose) {
            backgroundSound.playLoop();
            move();

            if (foxDied > 3) {
                time = time / 1.1;
                foxDied = 0;
            }
            try {
                Thread.sleep((int) time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            foxKilledChicken();
        }
        backgroundSound.stop();
        gameOver = new Picture(10, 10, "Resources/gameOver.png");
        gameOver.draw();
        gameOverSound = new BackgroundSound("Resources/chicken-sounds-farm-background-sounds-ambient-sounds-143091.wav");
        gameOverSound.play();
        foxCountImg = new Text(620, 40, "");
        foxCountImg.grow(55, 30);
        foxCountImg.setColor(Color.BLACK);
        foxCountImg.setText("FOXES KILLED: " + totalKilledFoxes);
        foxCountImg.draw();

        deleteEverything();

        while (!restart) {
            System.out.println("");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        foxCountImg.delete();
        gameOverSound.stop();


    }

}
