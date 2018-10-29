package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


//1280x720 game
//each tile is 40x40 pixels



public class Main extends Application {

    public static final int TILE_SIZE = 40;
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;
    public String tileMapMain[][] = new String[32][18];
    public ImageView decorationTileGrid[][] = new ImageView[32][18]; //THIS IS THE UNIMPLEMENTED DECORATION GRID -- USE FOR PUTTING COSMETIC STUFF ON TILES
    public ImageView eventTileGrid[][] = new ImageView[32][18]; //UNIMPLEMENTED EVENT GRID -- NEED TO FINISH EVENTS!!!!! (SPACE BAR INTERACTION)
    public ImageView imageViewGrid[][] = new ImageView[32][18];
    public ImageView levelEditorClickGrid[][] = new ImageView[32][18]; //for event handlers for clicking on the level editor tiles
    public Level level = new Level(); //LEVEL 1
    public Pane pane = new Pane(); //LEVEL 1
    public int paneObjectCounter = 0; //keeping track of all the stuff added to the pane
    StackPane backgroundPane = new StackPane();
    public boolean levelEditorSelectionMenuIsOpen = false;

    Scene scene = new Scene(pane, 1280, 720); //LEVEL 1
    public Pane selectionPane = new Pane(); //selection thing
    public Player player = new Player();
    public Boolean winCondition = false; //what lets you win for the level
    public Boolean levelEditorMode = false;
    public Boolean eventIsOpen = false; //no arrow keys or spacebar allowed when it's open
    public String levelEditorChoice = "wallTop"; //default value for the level editor tile to put down
    public String levelEditorChoiceType = "tileMap"; //can be tileMap, decorationMap, or eventMap

    //make something that happens when you win the level
    //TO-DO: make an ignoreObstacles mode for level editing, put it in the inventory menu, along with the debug mode button
    public Boolean ignoreObstacles = false; //useful for bug testing, level editing, etc.

    public Boolean debugMode = false; //enables debug features
    //TO-DO: allow player to enable debug mode from the inventory menu
    //TO-DO: enable debug logging
    //maybe instead of println() I use a writing thing that logs to a text file
    //with a filename that is based on the current system time

    //check for movementCollision
    //see if the next move (based on keyboard press) will result in an illegal move
    //you can't move into a boundary

    public void WinGame() {
        winCondition = true;
    }

    Boolean checkMovementCollisionUP() {
        if ((level.getTile(player.getTileX(), player.getTileY() - 1) == "basicObstacle") ||
            (level.getTile(player.getTileX(), player.getTileY() - 1) == "wallTop") ||
            (level.getTile(player.getTileX(), player.getTileY() - 1) == "wallBottom") ||
            (level.getTile(player.getTileX(), player.getTileY() - 1) == "chest") ||
            (level.getTile(player.getTileX(), player.getTileY() - 1) == "floorObstacle") &&
                    !ignoreObstacles) {
            if (debugMode) {
                System.out.println("up collision");
            }
            return true; //true means there IS collision

        } else {
            return false;
        }
    }
    Boolean checkMovementCollisionDOWN() {
        if ((level.getTile(player.getTileX(), player.getTileY() + 1) == "basicObstacle") ||
            (level.getTile(player.getTileX(), player.getTileY() + 1) == "wallTop") ||
            (level.getTile(player.getTileX(), player.getTileY() + 1) == "wallBottom") ||
            (level.getTile(player.getTileX(), player.getTileY() + 1) == "chest") ||
                (level.getTile(player.getTileX(), player.getTileY() - 1) == "floorObstacle") &&
                        !ignoreObstacles) {
            if (debugMode) {
                System.out.println("down collision");
            }
            return true; //true means there IS collision
        } else {
            return false;
        }
    }

    Boolean checkMovementCollisionLEFT() {
        if ((level.getTile(player.getTileX() - 1, player.getTileY()) == "basicObstacle") ||
            (level.getTile(player.getTileX() - 1, player.getTileY()) == "wallTop") ||
            (level.getTile(player.getTileX() - 1, player.getTileY()) == "wallBottom") ||
            (level.getTile(player.getTileX() - 1, player.getTileY()) == "chest") ||
                (level.getTile(player.getTileX(), player.getTileY() - 1) == "floorObstacle") &&
                        !ignoreObstacles) {
            if (debugMode) {
                System.out.println("left collision");
            }
            return true; //true means there IS collision
        } else {
            return false;
        }
    }

    Boolean checkMovementCollisionRIGHT() {
        if ((level.getTile(player.getTileX() + 1, player.getTileY()) == "basicObstacle") ||
            (level.getTile(player.getTileX() + 1, player.getTileY()) == "wallTop")  ||
            (level.getTile(player.getTileX() + 1, player.getTileY()) == "wallBottom")  ||
            (level.getTile(player.getTileX() + 1, player.getTileY()) == "chest") ||
                (level.getTile(player.getTileX(), player.getTileY() - 1) == "floorObstacle") &&
                        !ignoreObstacles) {
            if (debugMode) {
                System.out.println("right collision");
            }
            return true; //true means there IS collision
        } else {
            return false;
        }
    }

    public void putObjectOnMap(int x, int y, String value) {
        int tempTileX = 0;
        int tempTileY = 0;
        double tempImageX = 0;
        double tempImageY = 0;
        String tempDirection = "";

        if (levelEditorMode == true && player.getTileX() == x && player.getTileY() == y) {
            tempDirection = player.getDirection();
            tempTileX = player.getTileX();
            tempTileY = player.getTileY();
            tempImageX = player.getImageX();
            tempImageY = player.getImageY();
            player.setDirection(tempDirection);
            switch (tempDirection) {
                case "up":
                    player.characterView = new ImageView(new Image("/img/character_up3.png"));
                    break;
                case "down":
                    player.characterView = new ImageView(new Image("/img/character_down3.png"));
                    break;
                case "left":
                    player.characterView = new ImageView(new Image("/img/character_left3.png"));
                    break;
                case "right":
                    player.characterView = new ImageView(new Image("/img/character_right3.png"));
                    break;
                default:
                    System.out.println("odd, tempDirection is: " + tempDirection);
                    break;
            }

            pane.getChildren().remove(pane.getChildren().size() - 1);


        }




        level.setTile(x, y, value);
        imageViewGrid[x][y] = new ImageView(new Image("/img/" + value + ".png"));
        pane.getChildren().add(imageViewGrid[x][y]);
        imageViewGrid[x][y].setX(x*40);
        imageViewGrid[x][y].setY(y*40);
        if (debugMode) {
            System.out.println("added object of type " + value + " at (" + x + ", " + y + ")");
        }
        //puts decorations and events back on top
        putDecorationOnMap(x, y, level.getDecoration(x, y));
        putEventOnMap(x, y, level.getEvent(x, y));


        if (levelEditorMode == true && player.getTileX() == x && player.getTileY() == y) {



            pane.getChildren().add(player.getImageView());
            player.setImageY(tempImageY);
            player.setTileY(tempTileY);
            player.setImageX(tempImageX);
            player.setTileX(tempTileX);





        }




    }

    public void putDecorationOnMap(int x, int y, String value) {
        level.setDecoration(x, y, value);
        decorationTileGrid[x][y] = new ImageView(new Image("/img/" + value + ".png"));
        pane.getChildren().add(decorationTileGrid[x][y]);
        decorationTileGrid[x][y].setX(x*40);
        decorationTileGrid[x][y].setY(y*40);
        if (debugMode) {
            System.out.println("added decoration of type " + value + " at (" + x + ", " + y + ")");
        }
    }


    public void putEventOnMap(int x, int y, String value) { //this hasn't been tested yet
        level.setEvent(x, y, value);
        eventTileGrid[x][y] = new ImageView(new Image("/img/" + value + ".png"));
        pane.getChildren().add(eventTileGrid[x][y]);
        eventTileGrid[x][y].setX(x*40);
        eventTileGrid[x][y].setY(y*40);
        if (debugMode) {
            System.out.println("added event of type " + value + " at (" + x + ", " + y + ")");
            System.out.println("value of eventIsOpen = " + eventIsOpen);
        }
    }


    public void levelEditorFunction() { //I THINK THIS IS BROKEN, MAYBE GET RID OF IT?

        //adding click stuff for level editing for all tiles
        levelEditorClickGrid[1][1].addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("this is where the tile selection thing will go later");
                event.consume();
            }
        });

    }



    public Boolean inventoryOpen = false;

    public int playerLevelSelection = 0; //player will choose which level to play
    public Boolean playerHasSelectedLevel = false;
    public ImageView loadLevelImgView = new ImageView(new Image("/img/load_level.png"));
    public ImageView levelEditorImgView = new ImageView(new Image("/img/level_editor.png"));
    public ImageView controlsImageView = new ImageView(new Image("/img/controlsImageView.png"));

    @Override
    public void start(Stage primaryStage) throws Exception { //throws Exception
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));




//               __  __          _____  _____ _____ _   _  _____
//              |  \/  |   /\   |  __ \|  __ \_   _| \ | |/ ____|
//              | \  / |  /  \  | |__) | |__) || | |  \| | |  __
//              | |\/| | / /\ \ |  ___/|  ___/ | | | . ` | | |_ |
//              | |  | |/ ____ \| |    | |    _| |_| |\  | |__| |
//              |_|  |_/_/    \_\_|    |_|   |_____|_| \_|\_____|


                //BEGINNING OF MAP SPAGHETTI (including selection)


        //LEVEL 1: DUNGEON-LIKE
        //LEVEL 2: OUTDOORS/FOREST-LIKE
        //LEVEL 3: ????


        Scene selectionScene = new Scene(selectionPane, 1280, 720); //can you chance scenes out???


        //prompt player for selecting which map to play
        //making 3 levels to start with (2 will be empty placeholders for the time being)
        //TO-DO: save which levels have been unlocked to a file, also have to read it to see which ones
        //the player is allowed to pick
        primaryStage.setTitle("Infosec Quest RPG");
        primaryStage.setScene(selectionScene); //just for the selection stuff

        if (playerLevelSelection == 0) {
            if (debugMode) {
                System.out.println("playerLevelSelection == 0 and you're in the loop for it");
            }
            //playerLevelSelection = 1;
            //TO-DO: RENAME LEVELS AS "CAMPAIGNS"
            //HAVE MULTIPLE FLOORS PER THING?
            Image levelOneImage = new Image("/img/level_one.png");
            ImageView levelOneImageView = new ImageView(levelOneImage);

            Image levelTwoImage = new Image("/img/level_two.png");
            ImageView levelTwoImageView = new ImageView(levelTwoImage);

            Image levelThreeImage = new Image("/img/level_three.png");
            ImageView levelThreeImageView = new ImageView(levelThreeImage);
            //eventually I should have the ability for the player to select a level from a file
            //CSV for levels, maybe make user-defined weapons/items/enemies too?



            //event listener

            //level 1 selection button event handler
            levelOneImageView.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    playerLevelSelection = 1;
                    //PUT MAP STUFF HERE

                    //BEGIN POPULATING LEVEL

                    for (int x = 0; x <= 31; x++) {
                        for (int y = 0; y <= 17; y++) {
                            putObjectOnMap(x, y, "floor");

                        }
                    }

                    //delete this!!!!!!
                    //pane.getChildren().add(decorationTileGrid[1][1]);

                    //delete the above

                    for (int i = 0; i <= 17; i++) {
                        putObjectOnMap(0, i, "wallTop");

                    }

                    for (int i = 0; i <= 31; i++) {
                        putObjectOnMap(i, 0, "wallTop");

                    }

                    for (int i = 0; i <= 17; i++) {
                        putObjectOnMap(31, i, "wallTop");
                    }
                    for (int i = 0; i < 31; i++) {
                        putObjectOnMap(i, 17, "wallTop");

                    }

                    for (int i = 1; i <= 30; i++) {
                        putObjectOnMap(i, 1, "wallBottom");

                    }

                    for (int i = 1; i <= 8; i++) {
                        putObjectOnMap(2, i, "wallTop");

                    }

                    putObjectOnMap(10, 7, "wallTop");


                    for (int i =2; i <= 10; i++) {
                        putObjectOnMap(i, 8, "wallTop");
                    }

                    for (int i =2; i <= 10; i++) {
                        putObjectOnMap(i, 9, "wallBottom");
                    }
                    //(2, 11) to (2, 16) wallTop
                    for (int i = 11; i <= 16; i++) {
                        putObjectOnMap(2, i, "wallTop");
                    }
                    //(3,11) to (11,11) wallTop
                    for (int i = 3; i <= 11; i++) {
                        putObjectOnMap(i, 11, "wallTop");
                    }
                    //(3, 12) to (10, 12) wallBottom
                    for (int i = 3; i <=10; i++) {
                        putObjectOnMap(i, 12, "wallBottom");
                    }
                    //(4,15) wallBottom
                    putObjectOnMap(4, 15, "wallBottom");
                    //(4, 14) to (9, 14) wallTop
                    for (int i = 4; i <= 9; i++) {
                        putObjectOnMap(i, 14, "wallTop");
                    }
                    //(6, 15) to (9, 15) wallBottom
                    for (int i = 6; i <= 9; i++) {
                        putObjectOnMap(i, 15, "wallBottom");
                    }
                    //(5, 15) to (5, 16) wallTop
                    putObjectOnMap(5, 15, "wallTop");
                    putObjectOnMap(5, 16, "wallTop");

                    //(11, 12) to (11, 14) wallTop
                    for (int i = 12; i <= 14; i++) {
                        putObjectOnMap(11, i, "wallTop");
                    }
                    //(11, 15) wallBottom
                    putObjectOnMap(11, 15, "wallBottom");
                    //(6,1) to (6,2) wallTop
                    putObjectOnMap(6,1,"wallTop");
                    putObjectOnMap(6,2,"wallTop");
                    //(6,3) wallBottom
                    putObjectOnMap(6,3,"wallBottom");
                    //(4,3) to (4,5) wallTop
                    for (int i = 3; i<=5; i++) {
                        putObjectOnMap(4, i, "wallTop");
                    }
                    //(5,5) to (8,5) wallTop
                    for (int i = 5; i<=8; i++) {
                        putObjectOnMap(i,5,"wallTop");
                    }
                    //(8,4) wallTop
                    putObjectOnMap(8,4,"wallTop");
                    //(8,3) to (22,3) wallTop
                    for (int i = 8; i<=22; i++) {
                        putObjectOnMap(i,3,"wallTop");
                    }
                    //(4,6) to (8,6) wallBottom
                    for (int i = 4; i<=8; i++) {
                        putObjectOnMap(i,6,"wallBottom");
                    }
                    //(9,4) to (20,4) wallBottom
                    for (int i = 9; i<=20; i++) {
                        putObjectOnMap(i,4,"wallBottom");
                    }
                    //(12,4) to (12,7) wallTop
                    for (int i = 4; i<=7; i++) {
                        putObjectOnMap(12,i,"wallTop");
                    }
                    //(12,8) to (13,8) wallBottom
                    for (int i = 12; i<=13; i++) {
                        putObjectOnMap(i,8,"wallBottom");
                    }
                    //(14,7) wallBottom
                    putObjectOnMap(14,7,"wallBottom");
                    //(13,6) to (13,7) wallTop
                    for (int i = 6; i<=7; i++) {
                        putObjectOnMap(13,i,"wallTop");
                    }
                    //(14,6) wallTop
                    putObjectOnMap(14,6,"wallTop");
                    //(13,10) to (13,11) wallTop
                    for (int i = 10; i<=11; i++) {
                        putObjectOnMap(13,i,"wallTop");
                    }
                    //(13,12) wallBottom
                    putObjectOnMap(13,12,"wallBottom");
                    //(14,11) to (15,11) wallBottom
                    for (int i = 14; i<=15; i++) {
                        putObjectOnMap(i,11,"wallBottom");
                    }
                    //(14,10) to (15,10) wallTop
                    for (int i = 14; i<=15; i++) {
                        putObjectOnMap(i,10,"wallTop");
                    }
                    //(15,9) to (17,9) wallTop
                    for (int i = 15; i<=17; i++) {
                        putObjectOnMap(i,9,"wallTop");
                    }
                    //(16,10) wallBottom
                    putObjectOnMap(16,10,"wallBottom");
                    //(17,10) wallTop
                    putObjectOnMap(17,10,"wallTop");
                    //(17,11) to (20,11) wallTop
                    for (int i = 17; i<=20; i++) {
                        putObjectOnMap(i,11,"wallTop");
                    }
                    //(17,12) to (19,12) wallBottom
                    for (int i = 17; i<=19; i++) {
                        putObjectOnMap(i,12,"wallBottom");
                    }
                    //(20,12) to (20,13) wallTop
                    for (int i = 12; i<=13; i++) {
                        putObjectOnMap(20,i,"wallTop");
                    }
                    //(20,14) wallBottom
                    putObjectOnMap(20,14,"wallBottom");
                    //(21,13) to (21,16) wallTop
                    for (int i = 13; i<=16; i++) {
                        putObjectOnMap(21,i,"wallTop");
                    }
                    //(20,16) wallTop
                    putObjectOnMap(20,16,"wallTop");
                    //(13, 14) to (18,14) wallTop
                    for (int i =13;i<=18;i++) {
                        putObjectOnMap(i,14,"wallTop");
                    }
                    //(13,15) to (18,15) wallBottom
                    for (int i =13;i<=18;i++) {
                        putObjectOnMap(i,15,"wallBottom");
                    }
                    //(15,13) wallTop
                    putObjectOnMap(15,13,"wallTop");
                    //(21,4) to (22,4) wallTop
                    for (int i =21;i<=22;i++) {
                        putObjectOnMap(i,4,"wallTop");
                    }
                    //(21,5) to (22,5) wallBottom
                    for (int i =21;i<=22;i++) {
                        putObjectOnMap(i,5,"wallBottom");
                    }
                    //(16,4) to (16,6) wallTop
                    for (int i =4;i<=6;i++) {
                        putObjectOnMap(16,i,"wallTop");
                    }
                    //(16,7) wallBottom
                    putObjectOnMap(16,7,"wallBottom");
                    //(18,6) to (19,6) wallTop
                    for (int i =18;i<=19;i++) {
                        putObjectOnMap(i,6,"wallTop");
                    }
                    //(19,7) to (20,7) wallTop
                    for (int i =19;i<=20;i++) {
                        putObjectOnMap(i,7,"wallTop");
                    }
                    //(20,8) to (21,8) wallTop
                    for (int i =20;i<=21;i++) {
                        putObjectOnMap(i,8,"wallTop");
                    }
                    //(22,7) to (22,10) wallTop
                    for (int i =7;i<=10;i++) {
                        putObjectOnMap(22,i,"wallTop");
                    }
                    //(18,7) wallBottom
                    putObjectOnMap(18,7,"wallBottom");
                    //(19,8) wallBottom
                    putObjectOnMap(19,8,"wallBottom");
                    //(20,9) to (21,9) wallBottom
                    for (int i =20;i<=21;i++) {
                        putObjectOnMap(i,9,"wallBottom");
                    }
                    //(22,11) wallBottom
                    putObjectOnMap(22,11,"wallBottom");
                    //(23,13) to (23,14) wallTop
                    for (int i =13;i<=14;i++) {
                        putObjectOnMap(23,i,"wallTop");
                    }
                    //(23,15) wallBottom
                    putObjectOnMap(23,15,"wallBottom");
                    //(24,10) to (24,16) wallTop
                    for (int i =10;i<=16;i++) {
                        putObjectOnMap(24,i,"wallTop");
                    }
                    //(24,9) doorBottom
                    putObjectOnMap(24,9,"basicObstacle");
                    //(24,8) doorTop
                    putObjectOnMap(24,8,"basicObstacle");
                    //(24,4) to (24,7) wallTop
                    for (int i =4;i<=7;i++) {
                        putObjectOnMap(24,i,"wallTop");
                    }
                    //(25,5) wallBottom
                    putObjectOnMap(25,5,"wallBottom");
                    //(26,3) wallBottom
                    putObjectOnMap(26,3,"wallBottom");
                    //(25,1) to (25,4) wallTop
                    for (int i =1;i<=4;i++) {
                        putObjectOnMap(25,i,"wallTop");
                    }
                    //(26,1) to (26,2) wallTop
                    for (int i =1;i<=2;i++) {
                        putObjectOnMap(26,i,"wallTop");
                    }
                    //(25,14) to (25,16) wallTop
                    for (int i =14;i<=16;i++) {
                        putObjectOnMap(25,i,"wallTop");
                    }
                    //(26,14) to (26,16) wallTop
                    for (int i =14;i<=16;i++) {
                        putObjectOnMap(26,i,"wallTop");
                    }
                    //(27,16) wallTop
                    putObjectOnMap(27,16,"wallTop");
                    //(30,1) wallTop
                    putObjectOnMap(30,1,"wallTop");
                    //(30,2) wallBottom
                    putObjectOnMap(30,2,"wallBottom");
                    //(30,6) wallTop
                    putObjectOnMap(30,6,"wallTop");
                    //(30,7) wallBottom
                    putObjectOnMap(30,7,"wallBottom");
                    //(30,11) wallTop
                    putObjectOnMap(30,11,"wallTop");
                    //(30,12) wallBottom
                    putObjectOnMap(30,12,"wallBottom");

                        //21,2
                    putObjectOnMap(21,2,"wallTop");
                    putObjectOnMap(21,1,"wallTop");
                    //CHESTS
                    putObjectOnMap(1, 16, "chest");
                    putObjectOnMap(4, 16, "chest");
                    putObjectOnMap(6, 16, "chest");
                    putObjectOnMap(9, 5, "chest");
                    putObjectOnMap(13, 5, "chest");
                    putObjectOnMap(16, 11, "chest");
                    putObjectOnMap(18, 10, "chest");
                    putObjectOnMap(20, 15, "chest");
                    putObjectOnMap(23, 16, "chest");
                    putObjectOnMap(21, 7, "chest");
                    putObjectOnMap(20, 2, "chest");
                    putObjectOnMap(22, 2, "chest");



                    //PUT DECORATIONS ONLY AFTER ALL OF THE TILES HAVE BEEN PLACED
                    putDecorationOnMap(5,5,"cracksDecoration");
                    putEventOnMap(5,5,"testEvent");
                    putEventOnMap(4,3,"testEvent");
                    putEventOnMap(2,2,"testEvent");
                    putEventOnMap(3,3,"testEvent");


                    //PUT EVENTS ONLY AFTER ALL THE DECORATIONS HAVE BEEN PLACED

                    //END POPULATING LEVEL

                    //I NEED TO ADD NPCS WHO WILL TELL YOU ABOUT SECURITY WHEN YOU ARE FACING THEM AND PRESS SPACE
                    //INSTEAD OF IT ONLY BEING THE CASE THAT YOU JUST LEARN FROM THE ENEMY QUESTIONS


                    //adding player to the map (need to do this for every single level)
                    //setting starting position to 1,2 so I can have walls all around
                    player.setImageY(40); //use 80 for 40x40 images, or 40 for 80 height images
                    player.setTileY(2);
                    player.setImageX(40);
                    player.setTileX(1);

                    pane.getChildren().add(player.getImageView());
                    //level.refreshTiles();
                    //level.refreshDecorations();
                    playerHasSelectedLevel = true;
                    primaryStage.setScene(scene);


                    if (debugMode) {
                        System.out.println("player level selection has been changed to 1");
                    }


                    event.consume();


                }
            });
            //level 2 selection button event handler
            levelTwoImageView.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    playerLevelSelection = 1; //goes to 1 for the time being because I have no level 2
                    //TO-DO: CHANGE ABOUT LINE WHEN I MAKE THE LEVEL
                    if (debugMode) {
                        System.out.println("player selected level 2 but it's not implemented yet");
                    }



                    event.consume();
                }
            });
            //level 3 selection button event handler
            levelThreeImageView.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    playerLevelSelection = 1; //goes to 1 for the time being because I have no level 3
                    //TO-DO: CHANGE ABOUT LINE WHEN I MAKE THE LEVEL
                    if (debugMode) {
                        System.out.println("player selected level 3 but it's not implemented yet");
                    }
                    event.consume();
                }
            });


            loadLevelImgView.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    playerLevelSelection = 1; //goes to 1 for the time being because I have no level 3
                    //TO-DO: CHANGE ABOUT LINE WHEN I MAKE THE LEVEL
                    if (debugMode) {
                        System.out.println("player selected level loader but it's not implemented yet");
                    }



                    event.consume();
                }
            });


            Button closeControlsButton = new Button("OK");
            closeControlsButton.setOnAction(e -> {
                backgroundPane.getChildren().remove(backgroundPane.getChildren().size() - 1);
                backgroundPane.getChildren().remove(backgroundPane.getChildren().size() - 1);
            });


            controlsImageView.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (debugMode) {
                        System.out.println("player selected controls from main menu");
                    }
                    pane.getChildren().add(new ImageView(new Image("/img/controls.png")));
                    backgroundPane.getChildren().add(new ImageView(new Image("/img/controls.png")));
                    closeControlsButton.setScaleX(2.0);
                    closeControlsButton.setScaleY(2.0);
                    backgroundPane.getChildren().add(closeControlsButton);
                    //pane.getChildren().add(closeControlsButton);




                    event.consume();
                }
            });









            //LEVEL EDITOR
            levelEditorImgView.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    playerLevelSelection = 1; //goes to 1 for the time being because I have no level 3
                    playerHasSelectedLevel = true;
                    //TO-DO: CHANGE ABOUT LINE WHEN I MAKE THE LEVEL
                    if (debugMode) {
                        System.out.println("player selected level editor but it's not implemented yet");

                    }

                    //TILES
                    for (int x = 0; x <= 31; x++) {
                        for (int y = 0; y <= 17; y++) {
                            putObjectOnMap(x, y, "floor");
                        }
                    }

                    for (int i = 0; i <= 17; i++) {
                        putObjectOnMap(0, i, "wallTop");

                    }

                    for (int i = 0; i <= 31; i++) {
                        putObjectOnMap(i, 0, "wallTop");

                    }

                    for (int i = 0; i <= 17; i++) {
                        putObjectOnMap(31, i, "wallTop");
                    }
                    for (int i = 0; i < 31; i++) {
                        putObjectOnMap(i, 17, "wallTop");

                    }

                    for (int x = 0; x <= 31; x++) {
                        for (int y = 0; y <= 17; y++) {
                            putDecorationOnMap(x,y,"cracksDecoration");
                        }
                    }

                    for (int x = 0; x <= 31; x++) {
                        for (int y = 0; y <= 17; y++) {
                            putEventOnMap(x,y,"noEvent");
                        }
                    }

                    putEventOnMap(6,6,"testEvent");

                    //end of adding click events for tiles for level editor

                    player.setImageY(40); //use 80 for 40x40 images, or 40 for 80 height images
                    player.setTileY(2);
                    player.setImageX(40);
                    player.setTileX(1);
                    pane.getChildren().add(player.getImageView());
                    level.refreshTiles();



                    playerHasSelectedLevel = true;
                    levelEditorMode = true;


                    primaryStage.setScene(scene);

                    //loop through 2D logic array and setOnAction for everything?
                    //then have an event that shows what you can put there

                    //test thing: for 0,0 put an event that makes a new thing in the stackpane pop up





                    event.consume();
                }
            });








            ImageView backgroundImageview = new ImageView(new Image("/img/background1.jpg"));
            backgroundPane.getChildren().add(backgroundImageview);




            VBox vBox = new VBox();
            HBox hboxThing = new HBox();
            hboxThing.getChildren().addAll(levelOneImageView, levelTwoImageView, levelThreeImageView);
            BorderPane bPane = new BorderPane();
            bPane.setPrefSize(1280,720);
            //bPane.setLeft();
            Pane spacerPane2 = new Pane(new Label(" "));
            spacerPane2.setPrefHeight(180);
            vBox.getChildren().add(spacerPane2);
            vBox.getChildren().add(hboxThing);


            HBox hboxTwoElectricBoogaloo = new HBox(loadLevelImgView, levelEditorImgView, controlsImageView);



            vBox.getChildren().add(hboxTwoElectricBoogaloo); //put level editor placeholder here

            bPane.setCenter(vBox);
            Pane spacerPane1 = new Pane(new Label(" "));
            spacerPane1.setPrefWidth(160);
            bPane.setLeft(spacerPane1);
            //bPane.setRight();
            //selectionPane.getChildren().add(bPane);
            //ADD BPANE TO BACKGROUND PANE
            ImageView gameTitleImageView = new ImageView(new Image("/img/title_image.png"));
            Pane titlePaneStuff = new Pane(gameTitleImageView);
            titlePaneStuff.setPrefSize(640, 120);
            gameTitleImageView.setX(320);
            gameTitleImageView.setY(60);
            backgroundPane.getChildren().add(titlePaneStuff);

            ImageView levelSelectImageView = new ImageView(new Image("/img/select_a_level.png"));
            Pane levelSelectPane = new Pane(levelSelectImageView);
            levelSelectPane.setPrefSize(640, 120);
            levelSelectImageView.setX(320);
            levelSelectImageView.setY(540);
            backgroundPane.getChildren().add(levelSelectPane);

            backgroundPane.getChildren().add(bPane);









            selectionPane.getChildren().add(backgroundPane); //backgroundPane, was bPane before
            Button debugModeButton = new Button();
            debugModeButton.setText("Click for debug mode");
            //debugModeButton.setLayoutX(50);

            debugModeButton.setOnAction(e -> {
                debugMode = true;
                System.out.println("enabled debug mode");

            });
            selectionPane.getChildren().add(debugModeButton);


            if (debugMode) {
                System.out.println("selectionPane stuff done");
            }


        }

        //END OF MAP SPAGHETTI




        //putting the scene in the stage (before it had the selectionPane)





        //arrow key movement
        //TO-DO: ADD IN-BETWEEN MOVEMENT (so it looks more natural)
        //you can only end up on actual spaces, but make an "animation" of adding and removing character
        //in multiple in-between X and Y positions in between the old and new tiles



        scene.setOnKeyPressed(ke -> {
            KeyCode keyCode = ke.getCode();
            //stick with 4-way movement for the time being, keeps collision detection easier

            //TO-DO: NEED TO ADD BOUNDS CHECKING FOR ARROW KEY AND SPACEBAR STUFF
            if (keyCode.equals(KeyCode.UP) && (inventoryOpen == false) && (playerHasSelectedLevel)  && !(eventIsOpen)  && !levelEditorSelectionMenuIsOpen) {

                if (player.getTileY() == 0) {
                    //stays the same if you're at a boundary or trying to move to an occupied space
                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("up");
                    player.characterView = new ImageView(new Image("/img/character_up3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);

                    if (debugMode) {
                        System.out.println("failed move up");
                        System.out.println("collided with object type: screenBoundary");
                    }
                } else if (checkMovementCollisionUP() && !ignoreObstacles) {



                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("up");
                    player.characterView = new ImageView(new Image("/img/character_up3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);


                    if (debugMode) {
                        System.out.println("failed move up");
                        System.out.println("collided with object type: " + level.getTile(player.getTileX(), player.getTileY() - 1));
                    }
                } else {



                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("up");
                    player.characterView = new ImageView(new Image("/img/character_up3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);



                    player.setImageY(player.getImageY() - TILE_SIZE);
                    player.setTileY(player.getTileY() - 1);
                }
                if (debugMode) {
                    System.out.println("moved up, (" + player.getTileX() + ", " + player.getTileY() + ")");

                }
                return;
            }
            if (keyCode.equals(KeyCode.DOWN) && (inventoryOpen == false) && (playerHasSelectedLevel) && !(eventIsOpen) && !levelEditorSelectionMenuIsOpen) {

                if (player.getTileY() == 17) {
                    //stays the same if you're at a boundary


                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("down");
                    player.characterView = new ImageView(new Image("/img/character_down3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);

                    if (debugMode) {
                        System.out.println("can't make that move down");
                        System.out.println("collided with object type: screenBoundary");
                    }
                } else if (checkMovementCollisionDOWN() && !ignoreObstacles) {


                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("down");
                    player.characterView = new ImageView(new Image("/img/character_down3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);
                    if (debugMode) {
                        System.out.println("can't make that move down");
                        System.out.println("collided with object type: " + level.getTile(player.getTileX(), player.getTileY() + 1));
                    }
                } else {


                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("down");
                    player.characterView = new ImageView(new Image("/img/character_down3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);


                    player.setImageY(player.getImageY() + TILE_SIZE);
                    player.setTileY(player.getTileY() + 1);
                }

                if (debugMode) {
                    System.out.println("moved down, (" + player.getTileX() + ", " + player.getTileY() + ")");

                }
                return;
            }
            if (keyCode.equals(KeyCode.LEFT) && (inventoryOpen == false) && (playerHasSelectedLevel) && !(eventIsOpen) && !levelEditorSelectionMenuIsOpen) {

                if (player.getTileX() == 0) {
                    //stays the same if you're at a boundary



                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("left");
                    player.characterView = new ImageView(new Image("/img/character_left3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);

                    if (debugMode) {
                        System.out.println("failed move left");
                        System.out.println("collided with object type: screenBoundary");
                    }
                } else if (checkMovementCollisionLEFT() && !ignoreObstacles) {


                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("left");
                    player.characterView = new ImageView(new Image("/img/character_left3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);


                    if (debugMode) {
                        System.out.println("failed move left");
                        System.out.println("collided with object type: " + level.getTile(player.getTileX() - 1, player.getTileY()));
                    }
                } else {



                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("left");
                    player.characterView = new ImageView(new Image("/img/character_left3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);



                    player.setImageX(player.getImageX() - TILE_SIZE);
                    player.setTileX(player.getTileX() - 1);
                }
                if (debugMode) {
                    System.out.println("moved left, (" + player.getTileX() + ", " + player.getTileY() + ")");
                }
                return;
            }
            if (keyCode.equals(KeyCode.RIGHT) && (inventoryOpen == false) && (playerHasSelectedLevel) && !(eventIsOpen) && !levelEditorSelectionMenuIsOpen) {

                if (player.getTileX() == 31 ) {
                    //stays the same if you're at a boundary



                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("right");
                    player.characterView = new ImageView(new Image("/img/character_right3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);



                    if (debugMode) {
                        System.out.println("failed move right");
                        System.out.println("collided with object type: screenBoundary");
                    }
                } else if (checkMovementCollisionRIGHT() && !ignoreObstacles) {


                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("right");
                    player.characterView = new ImageView(new Image("/img/character_right3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);


                    if (debugMode) {
                        System.out.println("failed move left");
                        System.out.println("collided with object type: " + level.getTile(player.getTileX() + 1, player.getTileY()));
                    }
                } else {

                    int tempTileX = player.getTileX();
                    int tempTileY = player.getTileY();
                    double tempImageX = player.getImageX();
                    double tempImageY = player.getImageY();
                    player.setDirection("right");
                    player.characterView = new ImageView(new Image("/img/character_right3.png"));
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().add(player.getImageView());
                    player.setImageY(tempImageY);
                    player.setTileY(tempTileY);
                    player.setImageX(tempImageX);
                    player.setTileX(tempTileX);



                    player.setImageX(player.getImageX() + TILE_SIZE);
                    player.setTileX(player.getTileX() + 1);
                }
                if (debugMode) {
                    System.out.println("moved right, (" + player.getTileX() + ", " + player.getTileY() + ")");
                }
                return;
            }

            //to-do: spacebar for events, such as NPCs, doors, encounters, etc.

            //test event
            if (keyCode.equals(KeyCode.SPACE) && (inventoryOpen == false) && (!eventIsOpen) && !levelEditorSelectionMenuIsOpen) {
                if (debugMode) {
                    System.out.println("testing: " + eventIsOpen);
                }


                if (debugMode) {
                    //System.out.println("You pressed the space bar. This \nwill be used for actions eventually.");
                    //System.out.println("ties in with direction: only looks\nfor event handler on space that\nplayer is facing");
                    System.out.println("pressed space bar with direction: " + player.getDirection());
                }
                //directional stuff for testEvent
                //testEvent in
                if (debugMode) {
                    System.out.println("got here");
                }

                Button removeFromPane = new Button("OK");
                if (player.getDirection() == "up") {
                    //check for events above
                    if (level.getEvent(player.getTileX(), player.getTileY() - 1).equals("testEvent")) {
                        if (!levelEditorMode) {
                            eventIsOpen = true;
                        }

                        if (debugMode) {
                            System.out.println("There is a test event at direction: " + player.getDirection());
                        }
                        if (!levelEditorMode) {
                            pane.getChildren().add(new ImageView(new Image("/img/testEventImage.png")));
                            pane.getChildren().add(removeFromPane);
                            removeFromPane.setOnAction(e -> {
                                pane.getChildren().remove(pane.getChildren().size() - 1);
                                pane.getChildren().remove(pane.getChildren().size() - 1);

                                eventIsOpen = false;

                            });
                        } else {

                            System.out.println("However, you cannot interact with events in level editor mode");

                        }


                    } else {
                        if (debugMode) {
                            System.out.println("There is no event at direction: " + player.getDirection());
                        }

                    }
                } else if (player.getDirection() == "down") {
                    //check for events below
                    if (level.getEvent(player.getTileX(), player.getTileY() + 1).equals("testEvent")) {
                        if (!levelEditorMode) {
                            eventIsOpen = true;
                        }
                        if (debugMode) {
                            System.out.println("There is a test event at direction: " + player.getDirection());
                        }
                        if (!levelEditorMode) {
                            pane.getChildren().add(new ImageView(new Image("/img/testEventImage.png")));
                            pane.getChildren().add(removeFromPane);
                            removeFromPane.setOnAction(e -> {
                                pane.getChildren().remove(pane.getChildren().size() - 1);
                                pane.getChildren().remove(pane.getChildren().size() - 1);
                                eventIsOpen = false;


                            });
                        } else {

                            System.out.println("However, you cannot interact with events in level editor mode");


                        }


                    } else {
                        if (debugMode) {
                            System.out.println("There is no event at direction: " + player.getDirection());
                        }
                    }
                } else if (player.getDirection() == "right") {
                    //check for events to the right
                    if (level.getEvent(player.getTileX() + 1, player.getTileY()).equals("testEvent")) {
                        if (!levelEditorMode) {
                            eventIsOpen = true;
                        }
                        if (debugMode) {
                            System.out.println("There is a test event at direction: " + player.getDirection());
                        }
                        if (!levelEditorMode) {
                            pane.getChildren().add(new ImageView(new Image("/img/testEventImage.png")));
                            pane.getChildren().add(removeFromPane);
                            removeFromPane.setOnAction(e -> {
                                pane.getChildren().remove(pane.getChildren().size() - 1);
                                pane.getChildren().remove(pane.getChildren().size() - 1);
                                eventIsOpen = false;


                            });
                        } else {

                            System.out.println("However, you cannot interact with events in level editor mode");


                        }

                    } else {
                        if (debugMode) {
                            System.out.println("There is no event at direction: " + player.getDirection());
                        }
                    }
                } else if (player.getDirection() == "left") {
                    //check for events to the left
                    if (level.getEvent(player.getTileX() - 1, player.getTileY()).equals("testEvent")) {
                        if (!levelEditorMode) {
                            eventIsOpen = true;
                        }
                        if (debugMode) {
                            System.out.println("There is a test event at direction: " + player.getDirection());
                        }
                        if (!levelEditorMode) {
                            pane.getChildren().add(new ImageView(new Image("/img/testEventImage.png")));
                            pane.getChildren().add(removeFromPane);
                            removeFromPane.setOnAction(e -> {
                                pane.getChildren().remove(pane.getChildren().size() - 1);
                                pane.getChildren().remove(pane.getChildren().size() - 1);

                                eventIsOpen = false;

                            });
                        } else {

                            System.out.println("However, you cannot interact with events in level editor mode");


                        }

                    } else {
                        if (debugMode) {
                            System.out.println("There is no event at direction: " + player.getDirection());
                        }
                    }
                }
            }

            ImageView invImgView = new ImageView(new Image("/img/inventory_menu.png"));

            //INVENTORY MENU STUFF
            //HIT I TO OPEN THE INVENTORY MENU
            if (keyCode.equals(KeyCode.I) && (inventoryOpen == false) && !(eventIsOpen) && !levelEditorSelectionMenuIsOpen) {
                //make the inventory menu

                inventoryOpen = true;
                pane.getChildren().add(invImgView);
                if (debugMode) {
                    System.out.println("opening inventory");
                }
                Button ignoreObstaclesButton = new Button();
                ignoreObstaclesButton.setText("Ignore obstacles");
                if (debugMode) {
                    pane.getChildren().add(ignoreObstaclesButton);
                }

                ignoreObstaclesButton.setOnAction(e -> {
                    if (ignoreObstacles == true) {
                        ignoreObstacles = false;
                    } else {
                        ignoreObstacles = true;
                    }
                    if (debugMode) {
                        System.out.println("ignoreObstacles has been set to " + ignoreObstacles);
                    }

                });

                //ignoreObstacles

                //closing when the inventory menu is opened

            }
            //HIT ESCAPE TO CLOSE THE INVENTORY MENU
            if ((keyCode.equals(KeyCode.ESCAPE)) && (inventoryOpen == true) && !levelEditorSelectionMenuIsOpen) {
                inventoryOpen = false;
                if (debugMode) {
                    System.out.println("closing inventory");
                }
                //pane.getChildren().remove(invImgView);

                if (debugMode) {
                    pane.getChildren().remove(pane.getChildren().size() - 1); //get rid of ignoreObstacles button
                }
                pane.getChildren().remove(pane.getChildren().size() - 1); //get rid of invImgView
            }

            //else if (levelEditorChoice.equals("wallBottom")) {
            //    putObjectOnMap(player.getTileX(), player.getTileY(), "wallTop");
            //    if (debugMode) {
            //        System.out.println("wallTop");
            //    }
            //}

            //NOTE THAT I WILL HAVE TO MAKE DECORATIONS PUT EVENTS ON TOP TOO, OR ELSE I WILL HAVE THE SAME PROBLEM AS
            //WITH THE DECORATIONS BEING COVERED UP WHEN USING putObjectOnMap() THAT I HAD BEFORE

            FlowPane buttonFlowPane = new FlowPane();

            if (keyCode.equals(KeyCode.E) && (inventoryOpen == false) && !(eventIsOpen) && levelEditorMode) {
                //player.setImage(new Image("/img/character_blank.png"));
                //player.setImageView(new ImageView(new Image("/img/character_blank.png")));
                levelEditorSelectionMenuIsOpen = true;
                if (debugMode) {
                    System.out.println("levelEditorSelectionMenuIsOpen = true;");
                    System.out.println("You opened the tile selection menu with E");
                }
                //tileMap:
                //wallTop
                //wallBottom
                //basicObstacle
                //chest
                //floorObstacle
                //stairs
                //blank

                //decorationMap:
                //cracksDecoration
                //invisibleDecoration

                //eventMap:
                //noEvent
                //testEvent

                //tileMap buttons
                Button wallTopButton = new Button("wallTop");
                Button wallBottomButton = new Button("wallBottom");
                Button floorButton = new Button("floor");
                Button basicObstacleButton = new Button("basicObstacle");
                Button chestButton = new Button("chest");
                Button floorObstacleButton = new Button("floorObstacle");
                Button stairsButton = new Button("stairs");
                Button blankbutton = new Button("blank");
                //decorationMap buttons
                Button cracksDecorationButton = new Button("cracksDecoration");
                Button invisibleDecorationButton = new Button("invisibleDecoration");
                //eventMap buttons
                Button noEventButton = new Button("noEvent");
                Button testEventButton = new Button("testEvent");
                //11 things so far

                //buttonFlowPane.getChildren().add(wallTopButton);
                //buttonFlowPane.getChildren().add(wallBottomButton);

                //need to add events for the buttons now
                wallTopButton.setOnAction(e -> {
                    if (debugMode) {
                        System.out.println("wallTopButton was pressed in level editor mode");
                    }
                    levelEditorChoice = "wallTop";
                    levelEditorChoiceType = "tileMap";
                    //can be tileMap, decorationMap, or eventMap
                    //buttonFlowPane.getChildren().remove(wallTopButton);
                    //buttonFlowPane.getChildren().remove(wallBottomButton);
                    //buttonFlowPane.getChildren().remove(floorButton);
                    //buttonFlowPane.getChildren().remove(basicObstacleButton);
                    //buttonFlowPane.getChildren().remove(chestButton);
                    //buttonFlowPane.getChildren().remove(floorObstacleButton);
                    //buttonFlowPane.getChildren().remove(stairsButton);
                    //buttonFlowPane.getChildren().remove(blankbutton);
                    //end of tileMap removals

                    player.setImage(new Image("/img/character_blank.png"));
                });
                wallBottomButton.setOnAction(e -> {
                    if (debugMode) {
                        System.out.println("wallBottomButton was pressed in level editor mode");
                    }
                    levelEditorChoice = "wallBottom";
                    levelEditorChoiceType = "tileMap";
                    //can be tileMap, decorationMap, or eventMap

                    //end of tileMap removals

                    player.setImage(new Image("/img/character_blank.png"));

                });

                floorButton.setOnAction(e -> {
                    if (debugMode) {
                        System.out.println("floorButton was pressed in level editor mode");
                    }
                    levelEditorChoice = "floor";
                    levelEditorChoiceType = "tileMap";
                    //can be tileMap, decorationMap, or eventMap

                    //end of tileMap removals

                    player.setImage(new Image("/img/character_blank.png"));

                });
                basicObstacleButton.setOnAction(e -> {
                    if (debugMode) {
                        System.out.println("basicObstacleButton was pressed in level editor mode");
                    }
                    levelEditorChoice = "basicObstacle";
                    levelEditorChoiceType = "tileMap";
                    //can be tileMap, decorationMap, or eventMap

                    //end of tileMap removals

                    player.setImage(new Image("/img/character_blank.png"));
                });
                chestButton.setOnAction(e -> {
                    if (debugMode) {
                        System.out.println("chestButton was pressed in level editor mode");
                    }
                    levelEditorChoice = "chest";
                    levelEditorChoiceType = "tileMap";
                    //can be tileMap, decorationMap, or eventMap

                    //end of tileMap removals

                    player.setImage(new Image("/img/character_blank.png"));
                });
                floorObstacleButton.setOnAction(e -> {
                    if (debugMode) {
                        System.out.println("floorObstacleButton was pressed in level editor mode");
                    }
                    levelEditorChoice = "floorObstacle";
                    levelEditorChoiceType = "tileMap";
                    //can be tileMap, decorationMap, or eventMap

                    //end of tileMap removals

                    player.setImage(new Image("/img/character_blank.png"));
                });
                stairsButton.setOnAction(e -> {
                    if (debugMode) {
                        System.out.println("stairsButton was pressed in level editor mode");
                    }
                    levelEditorChoice = "stairs";
                    levelEditorChoiceType = "tileMap";
                    //can be tileMap, decorationMap, or eventMap

                    //end of tileMap removals

                    player.setImage(new Image("/img/character_blank.png"));
                });
                blankbutton.setOnAction(e -> {
                    if (debugMode) {
                        System.out.println("blankbutton was pressed in level editor mode");
                    }
                    levelEditorChoice = "blank";
                    levelEditorChoiceType = "tileMap";
                    //can be tileMap, decorationMap, or eventMap

                    //end of tileMap removals

                    player.setImage(new Image("/img/character_blank.png"));
                });



                putObjectOnMap(player.getTileX(), player.getTileY(), level.getTile(player.getTileX(), player.getTileY()));

                buttonFlowPane.getChildren().add(wallTopButton);
                buttonFlowPane.getChildren().add(wallBottomButton);
                buttonFlowPane.getChildren().add(floorButton);
                buttonFlowPane.getChildren().add(basicObstacleButton);
                buttonFlowPane.getChildren().add(chestButton);
                buttonFlowPane.getChildren().add(floorObstacleButton);
                buttonFlowPane.getChildren().add(stairsButton);
                buttonFlowPane.getChildren().add(blankbutton);
                pane.getChildren().add(buttonFlowPane);

                int tempPlayerX = player.getTileX();
                int tempPlayerY = player.getTileY();
                //if Y is zero, just update the tile the player is currently on
                //if Y is greater than zero, update the tile the player is on, and the tile with Y-1
                //if (tempPlayerY == 0) {
                //    //update the tileMap object for now, do deco and event ImageViews later
                //    //Image and/or ImageView only, no actual setting the grid or level stuff
                //    putObjectOnMap(player.getTileX(), player.getTileY(), level.getTile(player.getTileX(), player.getTileY()));
                //}

                //putObjectOnMap(player.getTileX(), player.getTileY(), level.getTile(player.getTileX(), player.getTileY()));

            }

            if (keyCode.equals(KeyCode.Q) && levelEditorMode && levelEditorSelectionMenuIsOpen) {
                pane.getChildren().remove(pane.getChildren().size() - 1);
                levelEditorSelectionMenuIsOpen = false;
            }

            if (keyCode.equals(KeyCode.SPACE) && levelEditorMode == true) {
                if (debugMode) {
                    System.out.print("you placed a(n) ");
                }
                /*if (levelEditorChoiceType.equals("tileMap")) {
                    putObjectOnMap(player.getTileX(), player.getTileY(), levelEditorChoice);

                } else if (levelEditorChoiceType.equals("decorationMap")) {
                    putDecorationOnMap(player.getTileX(), player.getTileY(), levelEditorChoice);

                }*/

                switch (levelEditorChoiceType) {
                    case "tileMap":
                        //ImageView blankImageView = new ImageView();
                        //player.setImageView(blankImageView);
                        player.setImage(new Image("/img/character_blank.png"));
                        putObjectOnMap(player.getTileX(), player.getTileY(), levelEditorChoice);
                        //================================
                        int tempTileXTM = player.getTileX();
                        int tempTileYTM = player.getTileY();
                        double tempImageXTM = player.getImageX();
                        double tempImageYTM = player.getImageY();
                        //player.setDirection(player.getDirection());
                        switch (player.getDirection()) {
                            case "up":
                                player.characterView = new ImageView(new Image("/img/character_up3.png"));
                                break;
                            case "down":
                                player.characterView = new ImageView(new Image("/img/character_down3.png"));
                                break;
                            case "left":
                                player.characterView = new ImageView(new Image("/img/character_left3.png"));
                                break;
                            case "right":
                                player.characterView = new ImageView(new Image("/img/character_right3.png"));
                                break;
                            default:
                                System.out.println("error with player.getDirection switch value");
                        }
                        pane.getChildren().remove(pane.getChildren().size() - 1);
                        pane.getChildren().add(player.getImageView());
                        player.setImageY(tempImageYTM);
                        player.setTileY(tempTileYTM);
                        player.setImageX(tempImageXTM);
                        player.setTileX(tempTileXTM);
                        //================================


                        break;
                    case "decorationMap":
                        putDecorationOnMap(player.getTileX(), player.getTileY(), levelEditorChoice);
                        //================================
                        int tempTileXDM = player.getTileX();
                        int tempTileYDM = player.getTileY();
                        double tempImageXDM = player.getImageX();
                        double tempImageYDM = player.getImageY();
                        //player.setDirection(player.getDirection());
                        switch (player.getDirection()) {
                            case "up":
                                player.characterView = new ImageView(new Image("/img/character_up3.png"));
                                break;
                            case "down":
                                player.characterView = new ImageView(new Image("/img/character_down3.png"));
                                break;
                            case "left":
                                player.characterView = new ImageView(new Image("/img/character_left3.png"));
                                break;
                            case "right":
                                player.characterView = new ImageView(new Image("/img/character_right3.png"));
                                break;
                            default:
                                System.out.println("error with player.getDirection switch value");
                        }
                        pane.getChildren().remove(pane.getChildren().size() - 1);
                        pane.getChildren().add(player.getImageView());
                        player.setImageY(tempImageYDM);
                        player.setTileY(tempTileYDM);
                        player.setImageX(tempImageXDM);
                        player.setTileX(tempTileXDM);
                        //================================


                        break;
                    case "eventMap":
                        putDecorationOnMap(player.getTileX(), player.getTileY(), levelEditorChoice);
                        //================================
                        int tempTileXEM = player.getTileX();
                        int tempTileYEM = player.getTileY();
                        double tempImageXEM = player.getImageX();
                        double tempImageYEM = player.getImageY();
                        //player.setDirection(player.getDirection());
                        switch (player.getDirection()) {
                            case "up":
                                player.characterView = new ImageView(new Image("/img/character_up3.png"));
                                break;
                            case "down":
                                player.characterView = new ImageView(new Image("/img/character_down3.png"));
                                break;
                            case "left":
                                player.characterView = new ImageView(new Image("/img/character_left3.png"));
                                break;
                            case "right":
                                player.characterView = new ImageView(new Image("/img/character_right3.png"));
                                break;
                            default:
                                System.out.println("error with player.getDirection switch value");
                        }
                        pane.getChildren().remove(pane.getChildren().size() - 1);
                        pane.getChildren().add(player.getImageView());
                        player.setImageY(tempImageYEM);
                        player.setTileY(tempTileYEM);
                        player.setImageX(tempImageXEM);
                        player.setTileX(tempTileXEM);
                        //================================


                        break;
                    default:
                        System.out.println("error with the levelEditorChoiceType switch!!!!");
                }


                //THIS STUFF IS TO GET RID OF THE PLAYER PNG ARTIFACTING GLITCH
                /*if (tempPlayerX == 0) { //lower bounds for X
                    //need to update all adjacent tile imageView objects (tile, deco, event) except for x-1

                } else if (tempPlayerX == 31) { //upper bounds for X
                    //need to update all adjacent tile imageView objects (tile, deco, event) except for x+1
                }*/






                /*if (tempPlayerY == 0) { //lower bounds for Y
                    //need to update all adjacent tile imageView objects (tile, deco, event) except for y-1

                } else if (tempPlayerY == 17) { //upper bounds for Y
                    //need to update all adjacent tile imageView objects (tile, deco, event) except for y+1
                }*/
                //otherwise do everything (all adjacencies)



                /*if (levelEditorChoice.equals("wallTop")) {
                    putObjectOnMap(player.getTileX(), player.getTileY(), "wallTop");
                    if (debugMode) {
                        System.out.println("wallTop");
                    }
                } else if (levelEditorChoice.equals("wallBottom")) {
                    putObjectOnMap(player.getTileX(), player.getTileY(), "wallBottom");
                    if (debugMode) {
                        System.out.println("wallBottom");
                    }
                } else if (levelEditorChoice.equals("basicObstacle")) {
                    putObjectOnMap(player.getTileX(), player.getTileY(), "basicObstacle");
                    if (debugMode) {
                        System.out.println("basicObstacle");
                    }
                } else if (levelEditorChoice.equals("chest")) {
                    putObjectOnMap(player.getTileX(), player.getTileY(), "chest");
                    if (debugMode) {
                        System.out.println("chest");
                    }
                } else if (levelEditorChoice.equals("floorObstacle")) {
                    putObjectOnMap(player.getTileX(), player.getTileY(), "floorObstacle");
                    if (debugMode) {
                        System.out.println("floorObstacle");
                    }
                }*/







                if (debugMode) {
                    System.out.println(levelEditorChoice + " on (" + player.getTileX() + ", " + player.getTileY() + ")");
                }

            }


            //ADD E FOR LEVEL EDITOR MENU
            //IT OPENS A MENU THAT LETS YOU SELECT WHICH TILE OR DECORATION OR EVENT YOU WANT TO USE
            //ONLY WORKS IN LEVEL EDITOR MODE


            //if ((keyCode.equals(KeyCode.E)) && (inventoryOpen == false) && levelEditorMode) {
            //    if (debugMode) {
            //        System.out.println("This is a stub for the level editor menu");
            //    }
            //}

            //TO-DO: ADD SPACE BAR AND ARROW KEY STUFF FOR THE INVENTORY MENU
            //ALSO ADD ACTUAL STUFF TO THE INVENTORY MENU INSTEAD OF IT ONLY BEING
            //A NON-FUNCTIONAL PNG IMAGE (placeholder)


            //TO-DO: MAKE IT SO YOU CAN'T MOVE AROUND WHEN ANY EVENT IS OPEN (SUCH AS THE TEST EVENT WHICH HAS THE IMAGE ON-SCREEN)
            //YOU SHOULD NOT BE ABLE TO MOVE AND HAVE THE CHARACTER REDRAWN ON THE SCREEN DURING SUCH TIME

        });

        primaryStage.show();
        primaryStage.setResizable(false);
        //primaryStage.setFullScreen(true); //later I will need to add a button for this in the inventory menu
        //I will also need to let things scale instead of having hard-coded values for the 1280x720
    }

    public static void main(String[] args) {
        launch(args);
    }
}
