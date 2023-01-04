import GameMechanics.*;
import TheBoard.Base;
import TheBoard.BoardCreator;
import TheBoard.Language;
import cardClasses.Chance;
import gui_fields.GUI_Player;
import gui_fields.GUI_Street;
import gui_main.GUI;
import gui_fields.GUI_Car;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static Files.FileReference.*;
import static GameMechanics.Die.getSum;
import static GameMechanics.textReaderClass.textRDR;
import static TheBoard.Base.*;
import static TheBoard.BoardCreator.JailInit;
import static TheBoard.Language.dialog;
import player.MjPlayer;

//v1.2

public class Main {

// kalhauge/BoardEngine

    public static void main(String[] args) throws IOException {
        String string_in, language, answer_game;
        int antal_kant, j, DialogNR=2;
        boolean game_running, answerGameOk;
        game_running = true;
        //int fieldNR = 24;
        ArrayList<String> userRoles = new ArrayList<>(Arrays.asList("Bil","Skib","Hund","Kat"));
        //String[] userRoles={"Bil","Skib","Hund","Kat"};
        String[] freeUserRoles;


//-------------------------------------------------------------------------------------------
//
//          Game Definitions
//
//-------------------------------------------------------------------------------------------


        //  Initialises the TheBoard.Base.fields with values from txt files in - src/main/Field-Guts - and - Color.Colorspace
        //TheBoard.BoardCreator.InitBoardFieldsGuts();
    GUI_Street[] fields = TheBoard.BoardCreator.InitBoardFieldsGuts();
        //System.out.println(DescriptionF);

        //BoardCreator.SetGUItext();
        //  Sets up the background GUI (Graphical User Interface) to a plain white
//        GUI gui = new GUI(Base.fields, Color.WHITE);
        GUI gui = new GUI(fields, Color.WHITE);

        //  Asks if the language has been initialised and makes a button for user to select language
            language = gui.getUserButtonPressed("Select Langage:", "Dansk", "English", "Francias", "German"); // Select language for the game dialog

        //  Initialize the game dialog
        TheBoard.Language.initializeDialog(dialog, language);
/*
        do {
            //  Asks for the number of dots on the sides of the dice - 2 dices
            string_in = gui.getUserButtonPressed(dialog[1], "2", "3", "4", "5", "6"); //Quest the number of sides for the dice
            //Set the number sides for the dices
            if (string_in.length() > 0) {
                antal_kant = (int) string_in.charAt(0) - '0';
            } else antal_kant = 6;
        } while (antal_kant < 2 || antal_kant > 6);
*/

        antal_kant = 6;

            //Asks how many players, and sets cars and players
            String Players = gui.getUserButtonPressed(dialog[DialogNR], "2", "3", "4"); DialogNR++;
            AmountofPlayers = Integer.parseInt(Players);


        boolean[] JailOn = new boolean[AmountofPlayers];
        JailInit( JailOn);
        MjPlayer[] PlayerArray = new MjPlayer[AmountofPlayers];
        GUI_Car[] playerCars = new GUI_Car[AmountofPlayers];
        String[] PlayerName = new String[AmountofPlayers];

        BoardCreator.PersonCreator(AmountofPlayers,PlayerArray,PlayerName,playerCars);



//  Sets names for each player in a for loop and gives an adjacent car with a private color
        int k = AmountofPlayers;
        for (int i = 0; i < AmountofPlayers; i++) {
            //  Sets the car of each player
            PlayerName[i] = (gui.getUserString(dialog[DialogNR]+(i+1)+"?"));
            if (PlayerName[i].length() == 0) PlayerName[i] = ("Player" + (i + 1));
            playerCars[i] = new GUI_Car(Color.RED, Color.BLACK, Cars.setCarType(i+1), GUI_Car.Pattern.FILL);
            PlayerArray[i] = new MjPlayer(PlayerName[i], 20 - ((AmountofPlayers - 2) * (2)), playerCars[i]);
            GameMechanics.Cars.CarColor(playerCars, PlayerArray, String.valueOf(AmountofPlayers), i, fields);
            //Set users role
            int first = 0; for (int l = 0; l < AmountofPlayers; l++) {if (userRoles.size()>AmountofPlayers)
                userRoles.remove(first); first++;}
            String[] RoleArray = new String[k];
            PlayerArray[i].setUserRole(gui.getUserButtonPressed(dialog[DialogNR+1], userRoles.toArray(RoleArray)));
            //Remove this role from the list.
            for (j=0;j<userRoles.size();j++)
            {
                if (userRoles.get(j).equals(PlayerArray[i].getUserRole())) {
                    userRoles.remove(j);
                    k--;
                    //userRoles[j] = userRoles[j-1];
                }
            }
            gui.addPlayer(PlayerArray[i]);
        }
        DialogNR+=2;
        Cars.restart(PlayerArray,fields, AmountofPlayers,fieldNR());


        int[][] OwnedtrueOwnedFalse = Base.InitializeOwnedStat(AmountofPlayers).clone();

        //Initialise true false for OwnedNotOwnedFields
        //int[][] OwnedtrueOwnedFalse = new int[Base.fieldNR()][AmountofPlayers + 1];
        for (int n = 0; n < Base.fieldNR(); n++) {
            OwnedtrueOwnedFalse[n][0] = n;
            for (int i = 0; i < AmountofPlayers; i++) {
                OwnedtrueOwnedFalse[n][i] = 0;
            }
        }
        // The player who starts has been selected
        boolean selection = true;
        //Sets all player locations on the board to space 0
        for (int i = 0; i < AmountofPlayers; i++) {
            if (i > 0 && Base.fields[i] != null) Base.fields[0].setCar(PlayerArray[i], true);
        }


        int intselect = 0;
        //Create a selected player that will point at active player
        MjPlayer selectedPlayer;
        boolean gameEnd = false; //, lastMax = false;

        //Create the dices. Default 6 sides
        //String AmountofDice = gui.getUserButtonPressed("how many dice?", "1", "2");
            Die d1 = new Die();
            Die d2 = new Die(0);


        // If sides are different from 6, set the number of sides.
        if (antal_kant != 6) {
            d1.setNumberOfSides(antal_kant);
            //d2.setNumberOfSides(antal_kant);
        }

        int playingPlayer = intselect;
        int playingPlayer2;

        //  Initialising something for GameMechanics.Jail and Start field
        int[] PlayerSpaceNRexcact = new int[AmountofPlayers];
        for (int i = 0; i < AmountofPlayers; i++) {
            PlayerSpaceNRexcact[i] =0;
        }
        //TheBoard.BoardCreator.JailInit(0);
        Chance chankort=new Chance();
        boolean skipPlayer = false;

//-------------------------------------------------------------------------------------------
//
//          Game Starts officially
//
//-------------------------------------------------------------------------------------------
        int amountOfGameLoops = 0;
        while (!gameEnd) {
            //while (PlayerArray[0].getBalance() < 3000 && PlayerArray[1].getBalance() < 3000 && !gameEnd) {
            DialogNR = 5;
                if (amountOfGameLoops == AmountofPlayers)
                    amountOfGameLoops = 0;
            if (playingPlayer == AmountofPlayers)
                playingPlayer = 0;

            playingPlayer2 = playingPlayer;
            if (selection) selectedPlayer = PlayerArray[playingPlayer];
            else selectedPlayer = PlayerArray[playingPlayer2];

            //skipPlayer = (Jail.jailed(selectedPlayer,skipPlayer));
            if (selectedPlayer.getAmnistiKortHaves() && JailOn[selectedPlayer.getNumber()]) {
                Jail.bailOut(selectedPlayer, skipPlayer);
                JailOn[selectedPlayer.getNumber()]=false;
                //System.out.println("Spiller skippes ikke pga. GOJF kort");
            } else if (JailOn[selectedPlayer.getNumber()]){
                skipPlayer = true; JailOn[selectedPlayer.getNumber()]=false;}
            if (skipPlayer) {
                playingPlayer++;
                skipPlayer=false;
                //System.out.println("Player "+selectedPlayer.getNumber()+" smoked in jail");

                if (amountOfGameLoops == AmountofPlayers)
                    amountOfGameLoops = 0;
                if (playingPlayer == AmountofPlayers)
                    playingPlayer = 0;

                playingPlayer2 = playingPlayer;
                if (selection) selectedPlayer = PlayerArray[playingPlayer];
                else selectedPlayer = PlayerArray[playingPlayer2];
            }
            //if (amountOfGameLoops == 0);
            //GameMechanics.Jail.JailRegister(AmountofPlayers, TheBoard.Base.fieldNR(), fields);
            //roll the dices
            d1.dice_roll();
            //d2.dice_roll();

            //Inform which user is playing
            gui.getUserButtonPressed(dialog[DialogNR] + " " + selectedPlayer.getName() + dialog[DialogNR+1], dialog[DialogNR+2]); DialogNR+=3;
            //Uses balance value in GUI, since it displays on GUI at all times, and works like a score.

            int DieSum = getSum(d1,d2) + 1; /*, d2*/

            //if the game hasn't ended, continue
            int CurrentSpaceForSelectedPlayer = 0;
            if (!gameEnd) {
                // Moves the cars around the field and gives consequence- see the GameMechanics.Cars Class under - src/main/java/GameMechanics.Cars
                Cars.moveCars(DieSum, selectedPlayer, PlayerArray, /*fields*/ AmountofPlayers, Base.fieldNR());

                //  Sets the current space for the selected player to a value
                CurrentSpaceForSelectedPlayer = 0;
                for (int i = 0; i < Base.fieldNR(); i++) {
                    if (Base.fields[i].hasCar(selectedPlayer)/*fields[i].hasCar(selectedPlayer)*/)
                        CurrentSpaceForSelectedPlayer = i;
                }


                //  You get forced to buy the field, therefor (you want to buy)
                boolean wanttobuyYesNo = true;


                //  This handles the trades with rent and buying of fields - see at - src/main/java/GameMechanics.Fields
                if (wanttobuyYesNo) {
                    String NewBalance = Fields.wannaBuyDoYou(OwnedtrueOwnedFalse,
                            selectedPlayer,
                            wanttobuyYesNo,
                            PlayerArray,
                            CurrentSpaceForSelectedPlayer,
                            PlayerSpaceNRexcact,
                            JailOn, chankort, gui, fields);
                    selectedPlayer.setBalance(selectedPlayer.getBalance() + Integer.parseInt(NewBalance));
                    //System.out.println(NewBalance);       | EMPTY NOTE |
                }
//                GameMechanics.textReaderClass.textRDR(DescriptionF, "12");
                amountOfGameLoops++;

                //Fields.OwnedCheck(OwnedtrueOwnedFalse,selectedPlayer.getNumber(), CurrentSpaceForSelectedPlayer);
                //DoubleProperty.CostCheck(CurrentSpaceForSelectedPlayer);
                //DoubleProperty.DoubleCost(OwnedtrueOwnedFalse,selectedPlayer.getNumber(),CurrentSpaceForSelectedPlayer);

                //Negative balance is not allowed
                if (selectedPlayer.getBalance() < 0) selectedPlayer.setBalance(0);
            }

            //Shows description of the space you land on, and changes color
            //Skal flyttes til Chance. Min mening Torben
           if (fields[PlayerSpaceNRexcact[selectedPlayer.getNumber()]].getTitle() == "CHANCE") {
            //    gui.displayChanceCard(Chance.chanceCards[DieSum - 5].getKortNavnavn());
            } else
                gui.displayChanceCard(selectedPlayer.getName() + " | " + fields[PlayerSpaceNRexcact[selectedPlayer.getNumber()]
                        ].getTitle() + "\n" + fields[PlayerSpaceNRexcact[selectedPlayer.getNumber()]].getSubText());
            Fields.displayDescriptions(fields, CurrentSpaceForSelectedPlayer, amountOfGameLoops);
            //Display GameMechanics.Die on the Board
            GameMechanics.Die.OnBoard(d1, gui);/*, d2*/

            //Changes currentSpaceForSelected Player to the new location
            if (CurrentSpaceForSelectedPlayer + DieSum > Base.fieldNR())
                CurrentSpaceForSelectedPlayer = CurrentSpaceForSelectedPlayer + DieSum - Base.fieldNR();


            //Switch selected player
            if (!(Objects.equals(fields[DieSum].getTitle(), "extra"))) {
                selection = !selection;
                playingPlayer++;
            }
            //Extra tour
            else if ((selectedPlayer.getBalance() <= -1)) {
                gui.showMessage(selectedPlayer.getName() + dialog[DialogNR]); DialogNR++;
            }
            answerGameOk = false;







            GameMechanics.Fields.ResetOnePlayerOwnStatus(selectedPlayer,OwnedtrueOwnedFalse);
//-------------------------------------------------------------------------------------------
//
//          Game Winner display
//
//-------------------------------------------------------------------------------------------
            //  Initialises values for displaying a winner
            String Winner = " ";
            int WinnerMoney = 0;
            int WinnerInt = 0;

            //  if someone loses, the game ends and a winner/ winners are decided
            if (selectedPlayer.getBalance() < 1) {

                //  Makes an array of potential winners
                String[] Winners = new String[AmountofPlayers];
                //  Sets the winner to player1
                Winners[0] = PlayerArray[0].getName();
                WinnerMoney = PlayerArray[0].getBalance();
                for (int i = 1; i < AmountofPlayers; i++) {
                    //  if the player has more money, set it as the new winner, by resetting the array and putting the new value in
                    if (PlayerArray[i].getBalance() > WinnerMoney) {
                        for (int b = 0; b < AmountofPlayers; b++) {
                            Winners[b] = " ";
                        }
                        Winners[i] = PlayerArray[i].getName();
                        WinnerMoney = PlayerArray[i].getBalance();
                        WinnerInt = i;
                    }
                    // if the next player has the same balance as previous player, set both as winners
                    else if (PlayerArray[i].getBalance() == WinnerMoney)
                        for (int l = 1; l < AmountofPlayers; l++) {
                            Winners[i] = (Winners[i - 1] + " " + PlayerArray[i].getName());
                        }
                }
                //  Displaying the Winners
                gui.showMessage(Winners[WinnerInt] + dialog[DialogNR] + WinnerMoney); DialogNR++;
//-------------------------------------------------------------------------------------------
//
//          Game End
//
//-------------------------------------------------------------------------------------------
                do {
                    //  Ask for new game
                    answer_game = gui.getUserButtonPressed(dialog[DialogNR], dialog[DialogNR+1], dialog[DialogNR+2]);
                    //  if anwser to "a new game" is no - stop the game
                    if (answer_game.equals(dialog[DialogNR+2])) {
                        game_running = false;
                        answerGameOk = true;
                    }   //  else restart the game
                    else {
                        answerGameOk = true;
                        GameMechanics.Cars.restart(PlayerArray, Base.fields, AmountofPlayers, Base.fieldNR());
                        GameMechanics.Fields.RestartFieldTitles(/*fields*/TitleF, Base.fieldNR(), Base.fields);
                        GameMechanics.Fields.RestartOwnStatus(OwnedtrueOwnedFalse, Base.fieldNR(), AmountofPlayers);
                        for (int i = 0; i < AmountofPlayers; i++) {
                            PlayerSpaceNRexcact[i] = 0;
                        }
                    }
                }
                while (!answerGameOk);
            }
            //end game if last selection to (wanna keep playing?) is no
            if (!game_running)
                System.exit(0);
        }
    }
    //-------------------------------------------------------------------------------------
    //
    //          Text reader
    //
    //-------------------------------------------------------------------------------------
    public static String txtReadAndReturn(File file, String LineNR) throws FileNotFoundException {
        Scanner TXTRDRscanner = new Scanner(file);
        int ReadLineNR = Integer.parseInt(LineNR);

        for (int i = 0; i <= ReadLineNR; i++) {
            if (i == ReadLineNR - 1) {
                //
                return TXTRDRscanner.nextLine();
            } else TXTRDRscanner.nextLine();
        }
        //  Closes the scanner after use
        TXTRDRscanner.close();
        TXTRDRscanner = new Scanner(file);
        return LineNR;
    }
}