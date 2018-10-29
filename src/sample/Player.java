package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {
    //variables
    Image characterImage = new Image("/img/character_down3.png");
    ImageView characterView = new ImageView(characterImage);

    private int tileX;
    private int tileY;
    String direction = "";

    //constructors
    public Player() {
        this.tileX = 1; //starting X and Y positions must be consistent with main.java positions
        this.tileY = 2;
        this.direction = "down";
    }

    //getters
    public int getTileX() {
        return this.tileX;
    }

    public int getTileY() {
        return this.tileY;
    }

    public Image getImage() {
        return this.characterImage;
    }

    public ImageView getImageView() {
        return this.characterView;
    }

    public double getImageX() {
        return this.characterView.getX();
    }

    public double getImageY() {
        return this.characterView.getY();
    }

    public String getDirection() {
        return this.direction;
    }

    //setters

    public void setTileX(int newTileX) {
        this.tileX = newTileX;
    }

    public void setTileY(int newTileY) {
        this.tileY = newTileY;
    }

    public void setImage(Image img) {
        this.characterImage = img;
    }

    public void setImageView(ImageView imgView) {
        this.characterView = imgView;
    }

    public void setImageX(double amount) {
        this.characterView.setX(amount);
    }

    public void setImageY(double amount) {
        this.characterView.setY(amount);
    }

    public void setDirection(String dir) {
        //System.out.println(this.getDirection());
        switch(dir) {
            case "up":
                this.direction = dir;
                break;
            case "down":
                this.direction = dir;
                break;
            case "right":
                this.direction = dir;
                break;
            case "left":
                this.direction = dir;
                break;
            default:
                System.out.println("error, invalid direction" + dir);
                break;
        }

    }
}
