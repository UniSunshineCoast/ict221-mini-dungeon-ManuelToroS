package dungeon.engine;


import static dungeon.engine.Inputs.*;
import java.util.*;

public class GameProc {

    public static void main (String[]args) {

        Scanner entry = new Scanner(System.in);

        Map world = new Map();

        boolean create = false;


        char[][] floor1 = new char[0][];

        while (create == false) {


            int diff = askDifficulty();

            int size = 10;

            floor1 = world.getMap(size);

            world.putEntry(floor1);

            world.putWall(floor1);

            world.putRanged(floor1, diff);

            world.putTrap(floor1);

            world.putMelee(floor1);

            world.putGold(floor1);

            world.putHealth(floor1);

            world.putLadder(floor1);

            world.printMap(floor1);

            create = askReroll();

        }

        world.putPlayer(floor1);
        char lastCell='E';
        world.printMap(floor1);

        int [] status =  new int[]{100, 10, 0}; // {steps,HP,score}
        int [] plocation = world.playerLocation(floor1);

        String move = new String();

        while(status[0]>0 && status[1]>0){

            move = getNextMove();

            world.executeMove(plocation,floor1,status,move,lastCell);


            if(status[0]==0 || status[1]==0){
                System.out.println(" GAME IS OVER ");
                break;
            }

            world.printMap(floor1);




        }
    }

}
