package io.codeforall.kernelfc.characters;


import io.codeforall.kernelfc.Position;
import org.academiadecodigo.simplegraphics.pictures.Picture;

public class Fox {

    private int health;

    private int damage;

    private boolean isDead;

    private boolean isCollided;
    private Position originalPosition;

    private int movesMade;

    private Position position;

    private Picture foxImg;

    public Fox(int x, int y){

        this.movesMade = 0;
        this.health = 50;
        this.damage = 10;
        this.isDead = false;
        this.isCollided = false;
        this.position = new Position(x,y);
        foxImg = new Picture(position.colToX(getPosition().getCol()),position.rowToY(getPosition().getRow()),"Resources/foxSCALED.png");
        this.originalPosition = new Position(x,y);
        foxImg.draw();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isDead(){
        return this.isDead;
    }

    public boolean getIsCollided(){
        return this.isCollided;
    }

    public void setCollided(boolean collided) {
        this.isCollided = collided;
    }


    public void die(){
        foxImg.translate((30* movesMade), 0);
        position =new Position(originalPosition.getCol(),originalPosition.getRow());
        movesMade = 0;

    }

    public void move() {
        if (!isDead) {
            position.setCol(position.getCol() - 1);
            foxImg.translate(-30, 0);
            movesMade++;
        }
    }

    public boolean attack(Plant plant){
        if(plant.getHealth() <= damage) {
            plant.die();
            plant.setHealth(0);
            isCollided = false;
            return true;
        } else {
            plant.setHealth(plant.getHealth() - damage);
            return false;
        }
    }

    public int takeDamage(int damage){
        if(damage >= health){
            this.die();
            setHealth(50);
            return 1;
        } else {
            this.health -= damage;
            return 0;
        }

    }

    public void delete(){
        this.isDead = true;
        foxImg.delete();
    }

}
