package dungeon.engine;

import java.util.*;
public class Item {



////////////////////////// Empty cell methods ///////////////////////////////////////////
    /**
     * checks if 'cell' is empty
     * @param cell
     * @return true if cell is ' '
     */
    public static boolean isEmpty(char cell){

        return isItem(cell,' ');

    }

    /**
     * Makes a cell "empty"
     * @return ' '
     */
    public static char makeEmpty(){

        return' ';

    }


/////////////////////////////////// Ladder methods ///////////////////////////////////////




/////////////////////////////////// Wall methods/////////////////////////////////////////////


    /**
     * Procedure to follow when the player walks against a wall,
     * reduces HP by 1.
     * @param status
     */
    public static void crashWall(int[] status){
        System.out.println("Despite knowing humans can't walk through walls," +
                " you think you are built different, so you charge against it," +
                " alas, you were not special. You loose "+1+"HP.");
        status[1]=status[1]-1;

    }

    /**
     * Finds out if a cell is a wall
     * @param cell
     * @return true if the cell is '#'
     */
    public static boolean isWall(char cell){

        return isItem(cell,'#');

    }

    /**
     * Turns one of 4 flags into true, to avoid creating walls diagonally of others
     * which could block movement to the point of making the map unplayable.
     * @param map
     * @param flag
     * @param r
     * @param c
     */
    public static void diagonalWall(char [][] map,boolean[] flag, int r,int c){

       try{
           if(isWall(map[r+1][c+1])){
               flag[0]=true;
           }
       }catch(IndexOutOfBoundsException e){

       }
        try{
            if(isWall(map[r+1][c-1])){
                flag[0]=true;
            }
        }catch(IndexOutOfBoundsException e){

        }
        try{
            if(isWall(map[r-1][c+1])){
                flag[0]=true;
            }
        }catch(IndexOutOfBoundsException e){

        }
        try{
            if(isWall(map[r-1][c-1])){
                flag[0]=true;
            }
        }catch(IndexOutOfBoundsException e){

        }






    }






//////////////////////////////// Melee enemy methods ////////////////////////////////////////

    /**
     *
     * @param cell
     * @return true if char cell is 'M'.
     */
    public static boolean isMelee(char cell){

        return isItem(cell,'M');

    }




    /**
     * Procedure to follow when reaching a Melee mutant
     * @param status
     */
    public static void stompMelee(int [] status){
        System.out.println("You defeat a Melee Mutant and earn "+2+" score points. " +
                            " during the fight you lose "+2+"HP");
        status[2]=status[2]+2;
        status[1]=status[1]-2;
        lootMutant(status);




    }



//////////////////////////// Ranged enemy methods  ///////////////////////////////////////////

    /**
     * checks if a cell is a ranged enemy
     * @param cell
     * @return
     */
    public static boolean isRanged(char cell){

        return isItem(cell,'R');

    }


    /**
     * Procedure to follow when reaching a Ranged mutant
     * @param status
     */
    public static void stompRanged(int [] status){
        System.out.println("You defeat a Ranged Mutant and earn "+2+" score points.");
        status[2]=status[2]+2;
        lootMutant(status);




    }

    /**
     * Checks if a coordinate is within ranged enemy reach, and returns the amount of
     * damage incurred if any. Walls block ranged enemies.
     * @param map
     * @param r
     * @param c
     * @return amount of damage by ranged enemy at a coordinate.
     */
    public static int rangedDamage(char [][] map,int r,int c){
        Random rand = new Random();
        int damage=0;

        // Looks down for ranged enemy
        for(int i = r;i<=r+2;i++){
            int a=rand.nextInt(1); //50% chance of getting hit if in range
            try {
                if(isWall(map[i][c])){ //Stop looking if there is a wall
                    break;
                }
                else if (isRanged(map[i][c])&&(a==1)){
                    damage=damage+2;
                }
            }catch(IndexOutOfBoundsException e){

            }

        }

        // Looks up for ranged enemy
        for(int i = r;i>=r-2;i--){
            int a=rand.nextInt(1); //50% chance of getting hit if in range
            try {
                if(isWall(map[i][c])){ //Stop looking if there is a wall
                    break;
                }
                else if (isRanged(map[i][c])&&(a==1)){
                    damage=damage+2;
                }
            }catch(IndexOutOfBoundsException e){

            }

        }


        // Looks right for ranged enemy
        for(int j = c;j<=c+2;j++){
            int b=rand.nextInt(1);//50% chance of getting hit if in range
            try {
                if(isWall(map[r][j])){ //Stop looking if there is a wall
                    break;
                }
                else if (isRanged(map[r][c])&&(b==1)){
                    damage=damage+2;
                }
            }catch(IndexOutOfBoundsException e){

            }

        }

        // Looks left for ranged enemy
        for(int j = c;j>=c-2;j--){
            int b=rand.nextInt(1);//50% chance of getting hit if in range
            try {
                if(isWall(map[r][j])){ //Stop looking if there is a wall
                    break;
                }
                else if (isRanged(map[r][c])&&(b==1)){
                    damage=damage+2;
                }
            }catch(IndexOutOfBoundsException e){

            }

        }

        return  damage;

    }



    /**
     * Used to check if the player is within Ranged enemy reach.
     * @param map
     * @param r
     * @param c
     * @return true if in range, false otherwise
     */
    public static boolean inRange(char [][] map,int r,int c){
        int dmg=rangedDamage( map, r, c);
        if(dmg>0){
            return true;
        }
        else{
            return false;
        }


    }


    /**
     * Procedure to follow when the player is shot by ranged mutants.
     * @param status
     * @param damage
     */
    public static void getShot(int[] status,int damage){
        if(damage == 0){
            System.out.println("You were shot by Ranged mutants, but you managed to dodge the arrows!!" +
                    " sustaining 0 Damage");
        }
        else{
            System.out.println("You were shot by Ranged mutants, loosing "+damage+"HP");
            status[1]=status[1]-damage;
        }

    }

//////////////////////////////Gold item methods///////////////////////////////////////////


    /**
     * Checks if a cell is gold
     * @param cell
     * @return true if cell is 'G'
     */
    public static boolean isGold(char cell){

        return isItem(cell,'G');

    }


    /**
     * Procedure to follow if the player picks up gold
     * @param status
     */
    public static void grabGold(int [] status){

        status[2]=status[2]+1;
        System.out.println("You have found gold! You get "+2+" score points");

    }



///////////////////////////// Health Potion item methods /////////////////////////////////


    /**
     * Checks if the cell contains a health potion
     * @param cell
     * @return true if 'H'
     */
    public static boolean isHealth(char cell){

        return isItem(cell,'H');

    }


    /**
     * Procedure to follow when the player finds a Health potion
     * @param status
     */
    public static void drinkPotion(int [] status){
        System.out.println("You have found and drank a Health potion, recovering "+4+"HP");
        status[1]=status[1]+4;

    }



//////////////////////////// Trap Item methods ///////////////////////////////////////////


    /**
     * Checks if a cell is a Trap
     * @param cell
     * @return true if the cell is 'T'
     */
    public static boolean isTrap(char cell){

        return isItem(cell,'T');

    }


    /**
     * Reduces health by 2 points if player steps on a trap
     * @param status
     */
    public static void springTrap(int[] status){

        status[1] = status[1]-2;
        System.out.println("You've sprung a trap!!\nYou loose "+2+"HP");

    }




//////////////////////////// General Item methods ///////////////////////////////////////////





    /**
     * Can be used to check if a coordinate is out of bounds.
     * @param map
     * @param r
     * @param c
     * @return true if coordinate is out of bounds
     */
    public static boolean isEdge(char[][] map,int r,int c){

        try{
            char check=map[r][c];
            return false;
        }catch(IndexOutOfBoundsException e){
            return true;
        }

    }


    /**
     *
     * @param cell
     * @param compare
     * @return true if item in cell is equal to compared item
     */
    public static boolean isItem(char cell,char compare){

        if (cell==compare){

            return true;

        }
        else{
            return false;
        }

    }

    /**
     * puts a 'count' amount of 'cell' items into random positions of 'map'
     * @param count
     * @param map
     * @param cell
     */
    public void putItem(int count, char [][] map, char cell){


        int i=0;
        Random rand = new Random();
        int r,c;  // r = row && c = column
        while (i<count){

            r = rand.nextInt(map.length-1);
            c = rand.nextInt(map.length-1);

            if ( isEmpty(map[r][c]) ) {

                map[r][c]=cell;
                i++;

            }

        }

    }

    /**
     * Find the coordinates of an element in the map, intended for unique elements.
     * @param map
     * @param item
     * @return [row][column] of chosen element
     */
    public int[] findLocation(char [][] map,char item){
        int c [] = new int[2];
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map.length;j++){
                if (map[i][j]==item){
                    c= new int[]{i, j};
                    break;
                }
            }
        }
        return c;
    }


    /**
     * Chance to get an item, inteded for health and extra gold after defeating mutants
     * @return true if random numbers are equal.
     */
    public static boolean chanceLoot(){
        Random rand = new Random();
        int a = rand.nextInt(10);
        int b = rand.nextInt(10);
        if(a==b){
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * Loots items from mutants that can help recover HP, Steps or earn extra Score.
     * @param status
     */
    public static void lootMutant(int [] status){
        if(chanceLoot()){
            System.out.println("You have found a small health potion in the defeated" +
                    " mutant corpse, healing you for "+1+"HP");
            status[1]=status[1]+1;
        }
        if(chanceLoot()){
            System.out.println("You have found some food in the defeated" +
                    " mutant corpse, recovering "+3+" Stamina points");
            status[0]=status[0]+3;
        }
        if(chanceLoot()){
            System.out.println("You have found some hidden gold in the defeated" +
                    " mutant corpse, earning "+1+" extra Score point");
            status[2]=status[2]+1;
        }


    }







}
