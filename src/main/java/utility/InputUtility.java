package utility;

import java.util.Scanner;

public class InputUtility {

    public static Scanner getKeyboard() {
        return new Scanner(System.in);
    }

    public static String getInputString() {
        return getKeyboard().next();
    }

    public static String getInputText() {
        return getKeyboard().nextLine();
    }

    public static int getInputInt() {
        return getKeyboard().nextInt();
    }

    public static void horLine() {
        System.out.println("-----------------------");
    }


}
