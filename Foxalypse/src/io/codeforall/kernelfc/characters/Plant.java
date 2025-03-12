package io.codeforall.kernelfc.characters;

import io.codeforall.kernelfc.Position;
import io.codeforall.kernelfc.Projectile;
import org.academiadecodigo.simplegraphics.pictures.Picture;


public class Plant {

    private int health;

    private boolean isDead;

    private Position position;

    private int damage;

    private Picture basicPlantImg;

    private Picture upgradePlantImg;

    private boolean isUpgraded;

    public Plant(int x, int y){

        this.health = 20;
        this.damage = 10;
        this.isDead = false;
        this.position = new Position(x, y);
        basicPlantImg = new Picture(position.colToX(getPosition().getCol()),position.rowToY(getPosition().getRow()),"Resources/plant flower BASIC.png");
        basicPlantImg.draw();
    }

    public void updatePlant(){
        if(!isUpgraded) {
            basicPlantImg.delete();
            upgradePlantImg = new Picture(position.colToX(getPosition().getCol()), position.rowToY(getPosition().getRow()), "Resources/plant flower TRINTONA.png");
            upgradePlantImg.draw();
            this.damage += 10;
            this.isUpgraded = true;
        }
    }

    public boolean isUpgraded() {
        return isUpgraded;
    }

    public int getDamage() {
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }


    public boolean isDead(){
        return this.isDead;
    }

    public void die() {
        this.isDead = true;
        if (isUpgraded) {
            upgradePlantImg.delete();
        } else{
            basicPlantImg.delete();
        }


    }
}
