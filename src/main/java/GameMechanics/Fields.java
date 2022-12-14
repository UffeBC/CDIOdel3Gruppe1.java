package GameMechanics;
import Files.FileReference;
import TheBoard.Base;
import TheBoard.BoardCreator;
import cardClasses.Chance;
import cardClasses.Chancekort;
import gui_codebehind.GUI_Center;
import gui_fields.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
import player.MjPlayer;

import static TheBoard.Base.AmountofPlayers;
import static TheBoard.Base.fieldNR;
//import static cardClasses.*;

public class Fields {
//
//
//            Checks if anyone owns the space you give it
//
//
    public static boolean noOwnerShipCheck( int cellToCheck){
        for (int i = 0; i <= AmountofPlayers; i++) {
            if (Base.InitializeOwnedStat(AmountofPlayers)[cellToCheck][i]==1  &&  i!=i%3)
                return false;
        }
        return true;
    }




    public static boolean OwnedCheck(int[][] Ownedtrue, int selectedPlayersNR, int spaceNumber) {
        int AmountofPlayerOwnedSpaces=0;
        //boolean[] ArrayofOwnership = new boolean[fieldNR()];
        int[][] ArrayofOwnership = Base.InitializeOwnedStat(AmountofPlayers);
        for (int i = 0; i < fieldNR(); i++) {ArrayofOwnership[i][selectedPlayersNR] = 1;}
        for (int i = 0; i < fieldNR(); i++)
        {
            if (Ownedtrue[i][selectedPlayersNR + 1] == 0){
                AmountofPlayerOwnedSpaces++;
                ArrayofOwnership[i][selectedPlayersNR] = 0;
            }
            //System.out.println(i + " "+ ArrayofOwnership[i]);
        }
        return ArrayofOwnership[spaceNumber][selectedPlayersNR]==1;
        //System.out.println("---------------");
    }


    public static String wannaBuyDoYou(int[][] Ownedtrue,
                                       MjPlayer selectedPlayer,
                                       //boolean boolforBUY,
                                       MjPlayer[] PlayerArray,
                                       int CurrentSpaceForSelectedPlayer,
                                       int[] PlayerSpaceNRexcact,
                                       boolean[] JailOn, Chance chankort, gui_main.GUI gui, GUI_Street[] fields/*NEW*/) throws FileNotFoundException {
        boolean wannaBuy = false;
        boolean[] Playerboughtspace = new boolean[AmountofPlayers];
        int THEfieldsNR = 0;
        int ny_bilPos;
        String NewBal;
        for (int i = 0; i < Base.fieldNR(); i++) {
            if (/*Base.fields[i].hasCar(selectedPlayer)*/fields[i].hasCar(selectedPlayer))
                THEfieldsNR = i;
        }

        boolean PassedGo = false;
        //  defines the exact locations for each player (used for the jail and Go spaces)
        if (PlayerSpaceNRexcact[selectedPlayer.getNumber()] < THEfieldsNR)
            PlayerSpaceNRexcact[selectedPlayer.getNumber()] = THEfieldsNR;
        else if (PlayerSpaceNRexcact[selectedPlayer.getNumber()] > THEfieldsNR){
            PlayerSpaceNRexcact[selectedPlayer.getNumber()] = THEfieldsNR;
            PassedGo = true;
        }


        if (Objects.equals(fields[THEfieldsNR].getTitle(), "JAIL")){
            //System.out.println("GameMechanics.Jail space reached");    // | EMPTY NOTE |
            //  Finds the Space with JailVisit
            int JailVisitSpace=0;
            for (int i = 0; i < Base.fieldNR(); i++) {
                if (fields[i] == fields[6])
                    JailVisitSpace = i;
            }
            //  Moves car to JailVisitSpace
            Cars.moveCarTo(AmountofPlayers, PlayerArray, CurrentSpaceForSelectedPlayer, selectedPlayer, JailVisitSpace, fields);
            //  GameMechanics.Jail Register
            PlayerSpaceNRexcact[selectedPlayer.getNumber()] = 6;

                JailOn[selectedPlayer.getNumber()]=true;
            System.out.println("player "+selectedPlayer.getNumber()+ " got jailed");
                //System.out.println("JailOn is set TRUE   Player"+(selectedPlayer.getNumber()+1)); // EMPTY NOTE
                return "-1";  // return value to add to someones balance





//-----------------------------------------------------------------------------------------------------
//
//      HER SKAL DER ST?? HVAD DER SKER P?? JAIL
//      spiller lander p?? JAIL, bil placeres p?? JAIL VISIT,      SKIP TURN - boolean value og NO MONEY AT NEXT GO - boolean
//      hvis skip selectplayer+1 spiller neste gang og SKIP TURN boolean bliver false - n??ste gang omkring GO ??ndres boolean og der tr??kkes 2 fra balancen
//
//-----------------------------------------------------------------------------------------------------

        }
        if (Objects.equals(fields[THEfieldsNR].getTitle(), "JAIL VISIT"))
//-----------------------------------------------------------------------------------------------------
//
//      HER SKAL DER ST?? HVAD DER SKER P?? JAILVISIT     (intet skal ske)
//
//-----------------------------------------------------------------------------------------------------
            return "0";

        if (PassedGo){
            PlayerArray[selectedPlayer.getNumber()].setBalance(selectedPlayer.getBalance()+2);
            //  sets balance according to jail status - and removes jail status for next trip around the board
            //System.out.println("BOARD PASSED");    // | EMPTY NOTE |

            if (JailOn[selectedPlayer.getNumber()]){
                //System.out.println("Subtracted 2 from balance cause JAIL");       //  | EMPTY NOTE |
                selectedPlayer.setBalance(selectedPlayer.getBalance()-2);
                JailOn[selectedPlayer.getNumber()] = false;
            }  //else System.out.println("Did not subtract 2 Player"+(selectedPlayer.getNumber()+1));
        }
        PassedGo = false;
        //  This checks if the field is even when devided by 3 twice - the location of the chance spaces

        if ((THEfieldsNR%3)%3==0 && THEfieldsNR!=6 && THEfieldsNR!=0 && THEfieldsNR!=12&& THEfieldsNR!=18) {
//-----------------------------------------------------------------------------------------------------
//
//      HER SKAL DER ST?? HVAD DER SKER P?? CHANCEKORT
//
//-----------------------------------------------------------------------------------------------------


 //           Chance landetPaaChance = new Chance();
 //           landetPaaChance.traekEtChanceKort();


            //bilen har muligvis f??et ny positon efter chancekortet er eksekveret
            ny_bilPos=chankort.chanceFieldIsHit(selectedPlayer, PlayerArray,CurrentSpaceForSelectedPlayer, AmountofPlayers,3,gui,fields);

            if (CurrentSpaceForSelectedPlayer != ny_bilPos)
            {
                if (CurrentSpaceForSelectedPlayer+3>fieldNR())
                    CurrentSpaceForSelectedPlayer-=fieldNR();
                Cars.moveCarTo(AmountofPlayers, PlayerArray, CurrentSpaceForSelectedPlayer, selectedPlayer, ny_bilPos, fields);
                CurrentSpaceForSelectedPlayer=ny_bilPos;

                 THEfieldsNR = 0;

                for (int i = 0; i < Base.fieldNR(); i++) {
                    if (/*Base.fields[i].hasCar(selectedPlayer)*/fields[i].hasCar(selectedPlayer))
                        THEfieldsNR = i;
                }

            }
            else {
                //System.out.println(landetPaaChance.traekEtChanceKort());
                //System.out.println(" 3%3 ");     // | EMPTY NOTE |
                return "0";
            }
        }
        //  Defines an owner of a given space
        int SpaceOwner = 0;
        boolean GoOn= true;
        //  Checks if someone owns the space
        for (int i=0;i<AmountofPlayers;i++) {
            if (Ownedtrue[THEfieldsNR][i + 1] == 0){
            GoOn = true;
            }
            //  Found an owner - SpaceOwner
            else {
                GoOn = false;
                SpaceOwner=i;
                break;
            }
        }

        //  This checks if the field is owned, and continues if it is not
        if (GoOn)
            //  This is a check for if the player wants to buy, ((It does not function because the player is forced to buy))
            ///if (boolforBUY) {
                //  This checks if the selected player has enough money, And buys the space if it does.
                if (PlayerArray[selectedPlayer.getNumber()].getBalance() >= BoardCreator.CostofField()[THEfieldsNR] && GoOn) {
                    //System.out.println("you bought the space");   | EMPTY NOTE |
                    Ownedtrue[CurrentSpaceForSelectedPlayer][selectedPlayer.getNumber()+1] = 1;
                    //  Puts the name of the player who bought the space onto the title of the field
                    fields[THEfieldsNR].setTitle(fields[THEfieldsNR].getTitle()+" "+selectedPlayer.getName());
                    //  Returns a string that is used to add to the amount of money for the selected player
                    //System.out.println(-CosttoOwn[THEfieldsNR]);      | EMPTY NOTE |
                    if (fields[THEfieldsNR].getSubText().substring(1,2).equals("M"))
                      gui.showMessage("Du er landet p?? "+fields[THEfieldsNR].getTitle() +" som er du k??ber. Det koster "+fields[THEfieldsNR].getSubText()+" som bliver trukket p?? din konto");
                    return String.valueOf(-BoardCreator.CostofField()[THEfieldsNR]);
                    // OVENFOR KAN K??BSV??RDI ??NDRES
                ///}
            }
            else return "0"; // returns error message in case there is an error

        //  Knows that someone owns the field, Pays rent and adds the rent to the SpaceOwners balance
        else {
            NewBal = String.valueOf(-BoardCreator.CostofField()[THEfieldsNR]);
            if (DoubleProperty.DoubleCost(Ownedtrue, selectedPlayer.getNumber(),CurrentSpaceForSelectedPlayer, fields)) {
                NewBal = String.valueOf(-BoardCreator.CostofField()[THEfieldsNR]*2);
                PlayerArray[SpaceOwner].setBalance(PlayerArray[SpaceOwner].getBalance()+BoardCreator.CostofField()[THEfieldsNR]*2);}
            else {NewBal = String.valueOf(-BoardCreator.CostofField()[THEfieldsNR]);
                PlayerArray[SpaceOwner].setBalance(PlayerArray[SpaceOwner].getBalance()+BoardCreator.CostofField()[THEfieldsNR]);}
            //System.out.println(-CosttoOwn[THEfieldsNR] + "   " +CosttoOwn[THEfieldsNR]);      | EMPTY NOTE |
           gui.showMessage("Du er landet p?? "+fields[THEfieldsNR].getTitle() +" som er ejet af "+PlayerArray[SpaceOwner].getName()+ ". Det koster "+NewBal+"M som bliver trukket p?? din konto");
            return NewBal;
        }
        //  The selected player has landed on their own field and 0 is added to their account
        //System.out.println("    0");      | EMPTY NOTE |
        //return "0";
    }


    public static void displayDescriptions(GUI_Street[] fields,int currentLocation, int TimesAroundBoard) throws FileNotFoundException {
        //  Changes the color of the Discription space
        Color NewColor = Colors.ColorSpace(Integer.parseInt(fields[currentLocation].getDescription())/*Integer.parseInt(textReaderClass.textRDR(FileReference.DescriptionF*/, Integer.parseInt(String.valueOf(currentLocation+1)));
        //System.out.println(textReaderClass.textRDR(FileReference.DescriptionF, String.valueOf(currentLocation+1)));
        GUI_Center.getInstance().setBGColor(NewColor);
}


    public static void RestartFieldTitles(/*GUI_Street[]*/File file, int AmountofSpaces, GUI_Street[] fields) throws FileNotFoundException {
        //  Resets all titles
        for (int i=0;i<AmountofSpaces;i++){
            fields[i].setTitle(textReaderClass.textRDR(file, String.valueOf(i+1)));
        }
        // Backup -
        /*for (int i = 0; i < fieldNR(); i++) {
            fields[i].setTitle(String.valueOf(file));
        }*/
    }

    public static void RestartOwnStatus(int[][] OwnedtrueOwnedFalse, int fieldNR, int AmountofPlayers) {
        //  Goes through all fields and sets owned status to "Not Owned" - with an int 0
        for (int n = 0; n < fieldNR; n++) {
            //OwnedtrueOwnedFalse[n][0] = n;
            for (int i = 1; i < AmountofPlayers+1; i++) {
                OwnedtrueOwnedFalse[n][i] = 0;
            }
        }
    }
}
/*

            FEJL DER H??BES AT BLIVE FIXED

1. - FIXED -
ved start af spil 2 pladerne har sv??rere ved at s??tte pladerne
2. - TB -
Terningen virker men skal s??ttes ned til 1  ( spil er bedre )
3. - FIXED -
forskellige sprog skal fungerer bedre
4.  - FIXED -
Vinderen skal vises rigtigt ( det g?? den ikke ved flere end 2 spillere )
5. - FIXED -
spiller 4 ser ud til altid at vinde
6. - FIXED -
spillere dupleres ved skift til fengsel



            TING TIL AT TILF??GE
1.
Forklaring m??ske? om hvordan spillet virker.
2.
Spring en tur over hvis spilleren kommer i fenglsel
3.
Vis flere vindere hvis de har samme m??ngde penge
4.
Kartoffel           Priority one
5.




*/
