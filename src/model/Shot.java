package model;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Shot {

    double posX, posY, speed = 10;
    int size = 100;

    Rectangle rectangle;


    public Shot(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        this.rectangle = new Rectangle(posX,posY,size,size);
        this.rectangle.setFill(Color.RED);


    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void update(){
        rectangle.setLayoutX(posX - 1);
        rectangle.setLayoutY(posY - 1);
    }
}
