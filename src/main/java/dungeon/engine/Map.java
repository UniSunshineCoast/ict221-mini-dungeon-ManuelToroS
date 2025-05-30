package dungeon.engine;

import static dungeon.engine.Item.*;
import java.util.*;
public class Map {


    /**
    Returns a [size]x[size] map/matrix containing ' ' on every coordinate by default.
     */
    public char[][] getMap(int size) {      //Could implement an extra parameter for non-square maps
        char [][] baseMap = new char[size][size];
        for(int i=0;i<baseMap.length;i++){
            for(int j=0;j<baseMap.length;j++){
                baseMap[i][j] = ' ';
            }
        }

        return baseMap;

    }

    /**
    Print the map.
     */
    public void printMap(char[][] map){
        System.out.println();

        for(int i=0;i<map.length;i++){
            for(int j=0;j<map.length;j++){ //Can use map[0].length for non-square maps
                System.out.print("   ["+map[i][j]+"]   ");
            }
            System.out.println();
            System.out.println();
            System.out.println();

        }

    }


    /**
     * Puts the entrance at the bottom left corner
     * @param map
     */
    public void putEntry(char map[][]){

        map[map.length-1][0] = 'E';

    }

    /**
     * Assigns 30 walls to the map
     * @param map
     */
    public void putWall(char[][] map){
        
        Random rand = new Random();
        Item item = new Item();
        int count=0;
        int r,c;

        while(count<30){
            r= rand.nextInt(map.length);
            c= rand.nextInt(map.length);
            boolean flag[] = {false,false,false,false};
            item.diagonalWall(map,flag,r,c);

            if (item.isEmpty(map[r][c])==false){
                continue;

            } else if (flag[0] || flag[1] || flag[2] || flag[3]) {
                continue;

            }else{
                  map[r][c]='#';
                  count ++;
            }

        }
        

        



    }


    /**
     * Assign 3 melee mutants to random positions
     * @param map
     */
    public void putMelee(char[][] map){
        Item item=new Item();
        item.putItem(3,map,'M');

    }

    /**
     * Assign 5 gold items to random positions
     * @param map
     */
    public void putGold(char[][] map){
        Item item=new Item();
        item.putItem(5,map,'G');

    }

    /**
     * Assign 5 traps to random positions
     * @param map
     */
    public void putTrap(char[][] map){
        Item item=new Item();
        item.putItem(5,map,'T');

    }

    /**
     * Assigns an amount of ranged mutants, directly proportional to difficulty
     * @param map
     * @param diff
     */
    public void putRanged(char[][] map, int diff){
        Item item=new Item();
        item.putItem(diff,map,'R');

    }

    /**
     * Assigns 1 ladder to the map
     * @param map
     */
    public void putLadder(char[][] map){
        Item item = new Item();
        item.putItem(1,map,'L');

    }

    /**
     * Assigns 2 health potions to the map.
     * @param map
     */
    public void putHealth(char[][] map){
        Item item = new Item();
        item.putItem(2,map,'H');

    }

    /**
     * Spawns player on 'E' initially or on 'L' for second floor.
     * @param map
     */
    public void putPlayer(char [][] map){
        Item item = new Item();
        if(map[map.length-1][0]=='E'){
            map[map.length-1][0] = 'P';
        }
        else{
           int c []= item.findLocation(map,'L');
           map[c[0]][c[1]] = 'P';

        }
    }

    /**
     * Player location in a 1D array [row][column]
     * @param map
     * @return Player location
     */
    public int[] playerLocation(char [][]map){
        Item item = new Item();
        int[] location= item.findLocation(map,'P');
        return location;


    }

    /**
     * Returns the destination coordinate depending on movement command.
     * @param r
     * @param c
     * @param move
     * @return new player coordinate [row][column]
     */
    public int[] newLocation(int r,int c, String move){
    int [] coordinate = new int[2];
        switch(move){
            case "up":
                coordinate= new int[]{r - 1, c};

            case "down":
                coordinate= new int[]{r + 1, c};

            case "right":
                coordinate= new int[]{r , c + 1};

            case "left":
                coordinate= new int[]{r , c - 1};

        }

        return coordinate;

    }


    public void executeMove(int[] plocation, char [][] map, int[] status, String move, char oldItem){

        //Captures the next coordinate in direction >> {row,column}
        int[] direction = newLocation(plocation[0],plocation[1],move);
        char currentItem=oldItem;
        int [] origin=plocation;
        boolean still=false;

        //Statement if next intended direction is boundary or a wall.
        if(isEdge(map,direction[0],direction[1]) || isWall(map[direction[0]][direction[1]])){

            crashWall(status);


            // Execute spring trap if player moves against a wall over a Trap cell.
            if (isTrap(oldItem)){
                springTrap(status);
            }
            //If the player remains within Ranged mutant reach, gets shot again.
            if (inRange(map,plocation[0],plocation[1])){
                int rdamage = rangedDamage(map,plocation[0],plocation[1] );
                getShot(status,rdamage);

            }

            still = true; //Flag indicates there was no movement.

        }
        else {
            //Player gets shot after stepping into Ranged mutant reach.
            if (inRange(map, direction[0], direction[1])) {
                int rdamage = rangedDamage(map, direction[0], direction[1]);
                getShot(status, rdamage);

            }
            //Player defeats ranged mutant
            if (isRanged(map[direction[0]][direction[1]])) {

                stompRanged(status);
                currentItem = 'R';
                plocation = direction;
            }
            //Player fights melee mutant.
            if (isMelee(map[direction[0]][direction[1]])) {

                stompMelee(status);
                currentItem = 'M';
                plocation = direction;
            }
            //Player steps on a trap.
            if (isTrap(map[direction[0]][direction[1]])) {

                springTrap(status);
                currentItem = 'T';
                plocation = direction;
            }
            //Player finds gold.
            if (isGold(map[direction[0]][direction[1]])) {

                grabGold(status);
                currentItem = 'G';
                plocation = direction;
            }
            //Player finds a Health potion
            if (isHealth(map[direction[0]][direction[1]])) {

                drinkPotion(status);
                currentItem = 'H';
                plocation = direction;
            }
            if (isEmpty(map[direction[0]][direction[1]])) {
                currentItem = ' ';
                plocation = direction;

            }

        }

        // If the player moved, execute:
        if(still==false) {
            map[plocation[0]][plocation[1]] = 'P';

            //If old item was a trap, leave trap on origin cell, if not, leave cell empty
            if (isTrap(oldItem)) {
                map[origin[0]][origin[1]] = 'T';
            }
            else{
                map[origin[0]][origin[1]] = ' ';
            }
        }
        status[0]=status[0]-1; //Use 1 energy point/step after any action.

        if(status[1]>10){ //Trim health back to 10 in case it goes over.
            status[1]=10;
        }

        System.out.println("After your move, you have:\n"+status[0]+" Energy points\n"
                            +status[1]+" HP\nScore : "+status[2]);












    }










}
