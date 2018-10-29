package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Level {

    //LEVEL LAYERS:
    //FROM LOWEST TO HIGHEST:
    //1. TILEMAP -- OBSTACLES
    //2. DECORATIONMAP -- COSMETIC STUFF
    //3. EVENTMAP -- EVENTS FOR THINGS YOU CAN INTERACT WITH VIA SPACE
    //level.tileMap[x][y] and
    public String tileMap[][] = new String[32][18]; //[x][y] obstacles (for collision only)
    public String decorationMap[][] = new String[32][18];//decorations -- cosmetic only
    public String eventMap[][] = new String[32][18]; //events

    public Level() {
        //DEFAULT INITIALIZATION: TILES ARE BLANK, DECORATIONS ARE INVISIBLE, EVENTS ARE NO EVENTS
        //you have to have SOMETHING, not just a "" string

        for (int a = 0; a <= 31; a++) {
            for (int b = 0; b <= 17; b++) {
                tileMap[a][b] = "blank"; //setting everything to be blank
            }
        }

        for (int a = 0; a <= 31; a++) {
            for (int b = 0; b <= 17; b++) {
                decorationMap[a][b] = "invisibleDecoration"; //setting everything to be invisible decoration
            }
        }

        for (int a = 0; a <= 31; a++) {
            for (int b = 0; b <= 17; b++) {
                eventMap[a][b] = "noEvent"; //setting everything to be no events
            }
        }


    }

    public void setTile(int x, int y, String tileValue) {
        tileMap[x][y] = tileValue;
    }

    public void setDecoration(int x, int y, String titleValue) {
        decorationMap[x][y] = titleValue;
    }

    public void setEvent(int x, int y, String titleValue) {
        eventMap[x][y] = titleValue;
    }

    public String getTile(int x, int y) {
        return tileMap[x][y];
    }
    //sets tiles in grid to what they're supposed to be

    public String getDecoration(int x, int y) {
        return decorationMap[x][y];
    }

    public String getEvent(int x, int y) {
        return eventMap[x][y];
    }

    public String[][] refreshTiles() {
        String tilesToReturn[][] = new String[32][18];
        for (int a = 0; a < 31; a++) {
            for (int b = 0; b < 17; b++) {
                tilesToReturn[a][b] = "";
            }
        }


        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < 17; j++) {
                if (tileMap[i][j].equals("blank")) {
                    tilesToReturn[i][j] = "blank";
                } else if (tileMap[i][j].equals("floorObstacle")) {
                    tilesToReturn[i][j] = "floorObstacle"; //this is for when you want an eventMap thing to not be passable through
                                                           //the event doesn't determine whether you can pass through it or not, the tileMap does
                                                           //floor and floorObstacle look the same, but floorObstacle can't be passed through
                                                           //use floorObstacle when you want to put an enemy on the floor
                } else if (tileMap[i][j].equals("basicObstacle")) {
                    tilesToReturn[i][j] = "basicObstacle";
                } else if (tileMap[i][j].equals("wallTop")) {
                    tilesToReturn[i][j] = "wallTop";
                } else if (tileMap[i][j].equals("wallBottom")) {
                    tilesToReturn[i][j] = "wallBottom";
                } else if (tileMap[i][j].equals("floor")) {
                    tilesToReturn[i][j] = "floor";
                } else if (tileMap[i][j].equals("chest")) {
                    tilesToReturn[i][j] = "chest";
                } else if (tileMap[i][j].equals("stairs")) {
                    tilesToReturn[i][j] = "stairs";
                //TO-DO: add enemy stuff
                } else {
                    System.out.println("something weird happened for refreshTiles");
                }
            }
        }
        return tilesToReturn;
    }

    public String[][] refreshDecorations() {
        String tilesToReturn[][] = new String[32][18];
        for (int a = 0; a < 31; a++) {
            for (int b = 0; b < 17; b++) {
                tilesToReturn[a][b] = "";
            }
        }

        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < 17; j++) { //currently only two decorations: cracks and invisible
                if (decorationMap[i][j].equals("cracksDecoration")) {
                    tilesToReturn[i][j] = "cracksDecoration";
                } else if (decorationMap[i][j].equals("invisibleDecoration")) {
                        tilesToReturn[i][j].equals("invisibleDecoration");
                } else {
                    System.out.println("something weird happened for refreshDecorations");
                }
            }
        }
        return tilesToReturn;
    }



    public String[][] refreshEvents() {
        String tilesToReturn[][] = new String[32][18];
        for (int a = 0; a < 31; a++) {
            for (int b = 0; b < 17; b++) {
                tilesToReturn[a][b] = "";
            }
        }

        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < 17; j++) { //currently only one event: none
                if (eventMap[i][j].equals("noEvent")) {
                    tilesToReturn[i][j] = "noEvent";
                } else if (eventMap[i][j].equals("testEvent")) {
                    tilesToReturn[i][j] = "testEvent";
                } else {
                    System.out.println("something weird happened for refreshEvents");
                }
            }
        }
        return tilesToReturn;
    }

    //TO-DO: make refreshEvents() method




    //to-do: add a constructor that can read from a file
    //like CSV or something, to then set the level

}
