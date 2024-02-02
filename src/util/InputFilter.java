package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputFilter {
    public List<String> ar;
    private String s = "";

    /**
     * constructor of filter, which can help us avoid comments in the txt
     * @param file input file with all information
     */
    public InputFilter(File file)  {
        ar = new ArrayList<>();
        try (Scanner sc1 = new Scanner(file)){
            startFiltering(sc1);
            sc1.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     *
     * @param sc1 Scanner
     * @return was made to avoid PMD WARNING about big methods :)
     */
    private boolean filterLowLevelCases(Scanner sc1){
        if (Character.toString(s.charAt(0)).equals("❄")) {
            while (!Character.toString(s.charAt(0)).equals("⛏") ) {
                s = sc1.next();
                System.out.println("skipped: " + s);
            }
        }
        return !Character.toString(s.charAt(0)).equals("⛏") && !Character.toString(s.charAt(s.length() - 1)).equals("⛏");
    }

    /**
     * Starts filtering input to avoid comments
     * @param sc1 Scanner
     */
    private void startFiltering(Scanner sc1) {
        s = "";
        while (sc1.hasNext()) {
            s = sc1.next();

            if(!filterLowLevelCases(sc1)){
                continue;
            }

            int poz = containsOnPoz(s,"❄");
            if(s.contains("❄")){
                System.out.println("added: " + s.substring(0,poz));
                ar.add(s.substring(0,poz));
                s = sc1.next();
                while (!s.contains("⛏")){
                    s = sc1.next();
                }
                if(s.contains("⛏")){
                    System.out.println("added: " + s.substring(containsOnPoz(s,"⛏")+1));
                    ar.add(s.substring(containsOnPoz(s,"⛏")+1));
                    continue;
                }
            }
            System.out.println("added: " + s);
            ar.add(s);

        }
    }

    /**
     *
     * @param s String
     * @param symbol String
     * @return int number of chosen symbol
     */
    private int containsOnPoz(String s, String symbol) {
        int poz = 0;
        String[] tempS = s.split("");
        for (int i = 0; i < tempS.length; i++) {
            if(tempS[i].equals(symbol)){
                poz = i ;
                return poz;
            }

        }
        return -1;
    }

}

