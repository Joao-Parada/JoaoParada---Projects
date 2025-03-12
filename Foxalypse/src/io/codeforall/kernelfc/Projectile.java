package io.codeforall.kernelfc;

import org.academiadecodigo.simplegraphics.pictures.Picture;

public class Projectile {

    private int damage;

    private Position position;

    private boolean isCollided;

    private Picture projectile;

    public Projectile(int x , int y, int damage){
        this.damage = damage;
        this.isCollided = false;
        this.position = new Position(x,y);
        projectile = new Picture(position.colToX(getPosition().getCol()),position.rowToY(getPosition().getRow()), "Resources/hitballRESIZE.png");
        projectile.draw();
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean getCollided() {
        return this.isCollided;
    }

    public void deleteBullet() {

        isCollided = true;
        projectile.delete();
    }

    public void move() {
        if (!isCollided) {
            position.setCol(position.getCol() + 1);
            projectile.translate(30, 0);

        }

        if(position.getCol() >= 35){
            this.deleteBullet();
        }

    }

    public void setCollided(boolean b) {
        this.isCollided =b;
    }
}
