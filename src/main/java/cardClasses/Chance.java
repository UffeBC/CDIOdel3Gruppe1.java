package cardClasses;
import GameMechanics.Fields;
import TheBoard.Base;
import gui_main.GUI;
import player.MjPlayer;
import gui_fields.*;
import TheBoard.Base;

import java.util.Objects;


public  class






















































Chance {
    private static Chancekort[] chanceCards=new Chancekort[20];
    int testKortMode=0;



    public Chance()
    {
        //System.out.println("Chance initialiseres");

        chanceCards[0]=new ChanceOverdragelseskort(".Til Bil","Dette chance kort er givet til Bilen. Tag et chancekort mere Bil: På din næste tur skal du drøne frem til et hvilket som helst ledigt felt og købe det. Hvis det ikke er nogen ledige felter skal du købe et fra en anden spiller!", "Bil");

        chanceCards[1]=new ChanceRykFremTilFelt("Start","Du rykkes frem til start.", 0);
        chanceCards[2]=new ChanceRyk05("Ryk05","Du Rykkes op til 5 felter frem");
        chanceCards[3]=new ChanceKortFarve("Ryk orange","Gratis felt. Du RykKes frem til et orange felt. Hvis det der ledigt, får du det gratis. Ellers skal du betale leje ejeren.", 3);
        chanceCards[4]=new ChanceRyk1ElChMemere("Ryk 1 eller change mere","Ryk 1 felt frem eller tag et chancekort mere");
        chanceCards[5]=new ChanceOverdragelseskort("Til Skibet","Dette kort er givet til skibet. Tag et chancekort mere. Skib: På den næste skal du sejle frem til hvilket som helst ledigt felt og købe det. Hvis der ikke er nogen ledige felter, skal du købe et af en anden spiller.","Skibet");
        chanceCards[6]=new ChanceBanktrans("For meget slik","Du har spist for meget slik. Betal M2 til banken.", -2,1,0);
        chanceCards[7]=new Chance2Farver("Vælg orange eller grønt felt","Gratis felt. Ryk frem til et orange eller grønt felt. Hvis det der ledigt, får du det gratis. Ellers skal du betale leje ejeren.", 3, 8);
        chanceCards[8]=new ChanceKortFarve("Ryk lyseblåt","Gratis felt. Ryk frem til et lyseblåt felt. Hvis det der ledigt, får du det gratis. Ellers skal du betale leje ejeren.",5);
        chanceCards[9]=new ChanceAmnistiFeng("Amnesti","Du løslades uden omkostninger. Du har dette kort til du får brug for det.");
        chanceCards[10]=new ChanceRykFremTilFelt("ryk tilStrandpromenaden","Du Rykkes frem til Strandpromenaden.", 23);
        chanceCards[11]=new ChanceOverdragelseskort("Til Katten","Dette kort er givet til Katten. Tag et chancekort mere. Kat: På den næste skal du sejle frem til hvilket som helst ledigt felt og købe det. Hvis der ikke er nogen ledige felter, skal du købe et af en anden spiller.","Katten");
        chanceCards[12]=new ChanceOverdragelseskort("Til Hunden","Dette kort er givet til Hunden.Tag et chancekort mere. Hund: På den næste skal du sejle frem til hvilket som helst ledigt felt og købe det. Hvis der ikke er nogen ledige felter, skal du købe et af en anden spiller.","Hunden");
        chanceCards[13]=new ChanceBanktrans("Fødselsdag","Det er din fødselsdag. Alle giver dig 1 M. Tillyke med fødselsdagen",1,0,2);
        chanceCards[14]=new Chance2Farver("Ryk til Pink eller Mørkeblåt","Gratis felt. Du Rykkes frem til et pink eller mørkeblåt felt. Hvis det der ledigt, får du det gratis. Ellers skal du betale leje ejeren.",4,2);
        chanceCards[15]=new ChanceBanktrans("Alle lektier lavet","Du har lavet alle dine lektier. Modtag 2M fra banken",2,0,1);
        chanceCards[16]=new ChanceKortFarve("Ryk til rødt felt","Gratis felt. du rykkes Ryk frem til et rødt felt. Hvis det der ledigt, får du det gratis. Ellers skal du betale leje ejeren.",1);
        chanceCards[17]=new ChanceRykFremTilFelt("Ryk frem til Skateparken","Gratis felt. Du Rykkes frem til Skaterparken for at lave det perfekte grind. Hvis ingen ejer den, får du den gratis. Ellers skal du betale leje ejeren.",10);
        chanceCards[18]=new Chance2Farver("Ryk frem til lyseblåt eller rødt","Gratis felt. Du Rykkes frem til et lyseblåt eller rødt felt. Hvis det der ledigt, får du det gratis. Ellers skal du betale leje ejeren.",5,1);
        chanceCards[19]=new Chance2Farver("Ryk frem til brunt eller gult felt","Gratis felt. Du Rykkes frem til et brunt eller gult felt. Hvis det der ledigt, får du det gratis. Ellers skal du betale leje ejeren.",7,6);


    }

    public Chancekort traekEtChanceKort()
    {
        int kort_nr;
        int  kOk=0;

        do {
            kOk=0;

            if (testKortMode==0) {
                kort_nr = (int) (Math.random() * 20);
                //System.out.println("Normalt kort trukket");
            }
            else
            {
                kort_nr=testKortMode;
                testKortMode=0;
                //System.out.println("Testkort trukket");
            }
            //System.out.println("Chancekort nr: "+kort_nr+" trukket");

            if (kort_nr==0|| kort_nr == 5 || kort_nr == 11 || kort_nr == 12 || kort_nr == 9)
            {


                if (chanceCards[kort_nr].getClass().equals(ChanceOverdragelseskort.class))
                {
                    if (chanceCards[kort_nr] instanceof ChanceOverdragelseskort==true)
                    {
                        if(((ChanceOverdragelseskort) chanceCards[kort_nr]).getAktivt()) kOk=11;
                    }

                }
                else if (chanceCards[kort_nr] instanceof ChanceAmnistiFeng)
                {
                    if (((ChanceAmnistiFeng) chanceCards[kort_nr]).getAktivt()) kOk=11;
                }
                else kOk=11;



            }

        } while (kOk > 0);

        return chanceCards[kort_nr];



    }


    public int chanceFieldIsHit(MjPlayer actPlayer,MjPlayer[] players, int actField,
                                int AmountofPlayers, int AmountofSpaces, GUI gui, GUI_Street[] fields)
    {
        int i, j, bilPos;
        int slut=0;
        String farvCod, modtRolle, valg;
        MjPlayer playModt;

        bilPos=actField;
        //System.out.println("bilPos "+bilPos);

        do {

            slut=0;

            //System.out.println("Chancekort trækkes");
            Chancekort actKort = traekEtChanceKort();

            gui.showMessage(actKort.getKortInfo());

            if (actKort instanceof Chance2Farver==true)
            {
                if (((Chance2Farver) actKort).getFarve1()== 3 && ((Chance2Farver) actKort).getFarve2()==8)
                   valg = gui.getUserButtonPressed("Vælg om du vil rykke til", "Orange felt", "Grønt felt");
                else if (((Chance2Farver) actKort).getFarve1()==4)
                    valg = gui.getUserButtonPressed("Vælg om du vil rykke til", "Pink felt", "Mørkeblåt felt");
                else if (((Chance2Farver) actKort).getFarve1()==5)
                   valg = gui.getUserButtonPressed("Vælg om du vil rykke til", "Lyseblå felt", "Rødt felt");
                else //if (((Chance2Farver) actKort).getFarve1()==9)
                   valg = gui.getUserButtonPressed("Vælg om du vil rykke til", "Brunt felt", "Gult felt");


                if (valg.equals("Orange felt") || valg.equals ("Pink felt") || valg.equals("Lyseblå felt")|| valg.equals("Brunt felt"))
                {
              //      farvCod=(""+((Chance2Farver) actKort).getFarve1());
                    farvCod=String.valueOf(((Chance2Farver) actKort).getFarve1());
       //             System.out.println("Farve 1 valgt kode "+farvCod);
                }
                else if (valg.equals("Grønt felt") || valg.equals ("Mørkeblåt felt") || valg.equals("Rødt felt")|| valg.equals("Gult felt"))
                {
               //     farvCod=(""+((Chance2Farver) actKort).getFarve2());
                    farvCod=String.valueOf(((Chance2Farver) actKort).getFarve2());
      //              System.out.println("Farve 2 valgt");
                }
                else farvCod="Ingen farve";
                i=bilPos;
                j=0;
  //              System.out.println("Brugt farve kode "+farvCod);
                do
                {
                    i++;
                    j++;
                    if (i>23) i=0;
      //              System.out.println(Base.fields[i].getDescription());

                }while( fields[i].getDescription().equals(farvCod)==false && j < 25);

                bilPos=i;


            }

            if (actKort instanceof ChanceOverdragelseskort==true)
            {
      //          gui.showMessage(actKort.getKortInfo());
                actPlayer.setKortModtaget(true);
                actPlayer.setActChancekort(actKort);
                modtRolle=((ChanceOverdragelseskort) actKort).getModtager();
                for (i=0;i>AmountofPlayers;i++)
                {
                    if (players[i].getUserRole().equals(modtRolle)==true)
                    {
                        players[i].setActChancekort(actKort);
                        players[i].setKortModtaget(true);
                    }
                    else
                    {
                        //System.out.println("Modtager af overdragelseskort er ikke fundet. Ingen spillere har rollen"+((ChanceOverdragelseskort) actKort).getModtager());
                    }
                }
                slut=11;
                //System.out.println("ChanceOverdragelseskort eksekveret");
            }


            if (actKort instanceof ChanceAmnistiFeng==true) {

                actPlayer.setAmnistkortHaves(true);
                ((ChanceAmnistiFeng) actKort).setAktivt(true);
                ((ChanceAmnistiFeng) actKort).setIndehaver(actPlayer.getNumber());
                //System.out.println("ChanceAmnistiFeng");
            }


            if (actKort instanceof ChanceBanktrans==true)
            {
                //  Betal 2 til banken.
                if (Objects.equals(actKort.getKortNavnavn(),chanceCards[6].getKortNavnavn()))
                    actPlayer.setBalance(actPlayer.getBalance()-2);
                //  Alle Betaler til 1 person.
                else if (Objects.equals(actKort.getKortNavnavn(),chanceCards[13].getKortNavnavn())){
                    for (int k = 0; k < AmountofPlayers; k++)
                        if (players[k].getUserRole()==actPlayer.getUserRole()) // Hvis spilleren er den som har kortet
                            players[k].setBalance(players[k].getBalance()+AmountofPlayers-1);
                        else players[k].setBalance(players[k].getBalance()-1);//    hvis spiller ikke har kortet
                    }
                //  Få 2 fra banken.
                else if (Objects.equals(actKort.getKortNavnavn(),chanceCards[15].getKortNavnavn())){
                    actPlayer.setBalance(actPlayer.getBalance()+2);
                    //System.out.println("+2 has been registered");
                }
            }

            if (actKort instanceof ChanceRyk05==true)
            {
                bilPos+=5;
            }

            if (actKort instanceof ChanceKortFarve==true)
            {

                //System.out.println("ChanceKortFarve");

                farvCod=(""+((ChanceKortFarve) actKort).getFarvekode());
                i=bilPos;
                j=0;
                do
                {
                    i++;
                    j++;
                    if (i>23) i=0;

                }while( fields[i].getDescription().equals(farvCod)==false && j < 25);

                        System.out.println(fields[bilPos+1].getDescription());

                bilPos=i;
            }

           if (actKort instanceof ChanceRykFremTilFelt==true) {


                bilPos = ((ChanceRykFremTilFelt) actKort).getDestinationsFelt();
                //System.out.println("ChanceRykFremTilFelt. bilPos "+ bilPos);

            }
            if (actKort instanceof ChanceRyk1ElChMemere==true) {
                valg = gui.getUserButtonPressed("Vælg", "En chance mere", "Ryk et felt frem");
                if (valg.equals("En chance mere")) {
                   slut=11;
                } else {
                    bilPos = actField + 1;
                }
                //System.out.println("ChanceRyk1ElChMemere");


            }

        }while (slut > 1);
        //System.out.println("While slut");
        //System.out.println("bilPos "+bilPos);
        return bilPos;

    }

    public void setTestKortMode(int testNr)
    {
        testKortMode=testNr;
    }


}


