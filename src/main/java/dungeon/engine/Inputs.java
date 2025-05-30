package dungeon.engine;

import java.util.*;

/**
 * Various functions and methods that will handle user inputs.
 */
public class Inputs {


    /**
     * Ask the user to choose difficulty
     * @return integer value of difficulty
     */
   public static int askDifficulty(){
       Scanner s = new Scanner(System.in);
       boolean flag=false;
       int diff=0;
       while(flag == false){
           System.out.println("Choose difficulty level from 0 to 10\n>>> ");
           try {

               diff = s.nextInt();

               if(diff<0 || diff>10){

                   System.out.println("You need to enter an Integer between 0 and 10");

               }
               else{
                   flag = true;
               }
           }
           catch (InputMismatchException e){
               System.out.println("You need to enter an Integer type value.");
               s.next();
           }

       }

       System.out.println("You chose to play on difficulty level "+diff+"\n");

       return diff;


   }

    /**
     * ask the player if they wish to continue with current map or re-roll
     * @return true if they wish to continue, false if they want to re-roll
     */
   public static boolean askReroll(){
       Scanner s = new Scanner(System.in);
       int flag=0;
       boolean choice=false;
       String input = new String();
       while(flag==0){
           System.out.println("Do you wish to continue playing the current map?\n" +
                              "Type 'Yes' to continue or 'No' to re-roll map\n>>> ");
           input = s.nextLine();

           if(checkString(input,"yes")==true){
               flag=1;
               choice=true;


           }
           else if(checkString(input,"no")==true){
               flag=1;
               choice=false;


           }
           else{
               System.out.println("Please enter a valid command");
           }


       }
       return choice;
   }

    /**
     * Get next move from player
     * @return string of chosen direction
     */
   public static String getNextMove(){
       Scanner s = new Scanner(System.in);
       String move=new String();

       while(true){
           System.out.println("Type 'Up', 'Down','Right' or 'Left' for your next move:\n>>> ");
           move = s.nextLine();
           if(checkString(move,"up") || checkString(move,"down")
              ||checkString(move,"left") || checkString(move,"right")){


               break;

           }
           else{
               System.out.println("You have to enter a valid command.");
           }
       }
       System.out.println("You decided to go "+move);
       return move;


    }


    /**
     * Check if an input string is equal to the expected one
     * @param input
     * @param expected
     * @return true if they are equal, false otherwise
     */
   public static boolean checkString(String input,String expected){

     if (input.trim().toLowerCase().equals(expected)){
         return true;

     }
     else{
         return false;
     }



   }




}
