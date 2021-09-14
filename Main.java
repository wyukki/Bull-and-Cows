package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static int turnNumber = 1;
    private static int codeLength;
    private static int possSymbols;


    public static void main(String[] args) {
        try {
            System.out.println("Input the length of the secret code:");
            codeLength = Integer.parseInt(sc.nextLine());
            System.out.println("Input the number of possible symbols in the code:");
            possSymbols = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("error: write a number!");
            sc.close();
            return;
        }
        if (codeLength > 36) {
            System.out.println("Error: can't generate a secret number" +
                    " with a length of 37 because there aren't enough unique symbols.");
            sc.close();
            return;
        }else if (codeLength == 0){
            System.out.println("Error: can't generate a code with length 0!");
            sc.close();
            return;
        } else if (codeLength > possSymbols) {
            System.out.printf("Error: it's not possible to generate a code" +
                    "with a length of %d with %d unique symbols\n", codeLength, possSymbols);
            sc.close();
            return;
        } else if (possSymbols > 36) {
            System.out.println("Error: maximum number of possible symbols " +
                    "in the code is 36 (0-9, a-z).");
            sc.close();
            return;
        }
        char[] code = getCode(codeLength);
        char[] guess;
        do {
            guess = getGuess();
            if (guess == null) {
                sc.close();
                return;
            }
        } while (!gradeGuess(code, guess));
    }

    private static char[] getCode(int len) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        long pseudoRandomNumber = System.nanoTime();
        StringBuilder s = new StringBuilder(Long.toString(pseudoRandomNumber)).reverse();
        int i = 0;
        while (code.length() != len) {
            int index = s.length() - i - 1;
            if (index == 0) {
                pseudoRandomNumber = System.nanoTime();
                s = new StringBuilder(Long.toString(pseudoRandomNumber)).reverse();
            }
            char c = s.charAt(index);
            if (!code.toString().contains("" + c)) {
                if (i == 0 && c == '0') {
                    c += 1;
                }
                code.append(c);
            }
            i++;
        }
        if (codeLength != 1 && possSymbols > 10) {
            int index = random.nextInt(codeLength);
            char c = (char) (random.nextInt(('z' - 'a' + 1)) + 'a');
            code.replace(index, index, c + "");
        }
        System.out.print("The secret code is prepared: ");
        for (int k = 0; k < codeLength; ++k) {
            System.out.print("*");
        }
        if (possSymbols == 10) {
            System.out.println(" (0-9).");
        } else if (possSymbols > 10) {
            System.out.printf(" (0-9, a-%c).\n", possSymbols - 11 + 'a');
        }
        System.out.println("Okay, let's start a game!");
        return code.toString().toCharArray();
    }

    private static char[] getGuess() {
        System.out.printf("Turn %d:\n", turnNumber);
        String guess = sc.next();
        if (guess.length() != codeLength) {
            System.out.printf("Error: enter a guess of %d length!\n", codeLength);
            return null;
        }
        turnNumber++;
        return guess.toCharArray();
    }

    private static boolean gradeGuess(char[] code, char[] guess) {
        boolean guessed = false;
        int bulls = 0;
        int cows = 0;
        for (int i = 0; i < codeLength; ++i) {
            if (code[i] == guess[i]) {
                bulls++;
            }
        }
        for (int i = 0; i < codeLength; ++i) {
            for (int j = 1; j < codeLength - 1; ++j) {
                if (guess[i] == code[j]) {
                    cows++;
                }
            }
        }
        System.out.println("CODE: " + new String(code));
        if (cows == 0 && bulls == 0) {
            System.out.println("Grade: None.");
        } else if (bulls == codeLength) {
            System.out.printf("Grade: %d bulls\n" +
                    "Congratulations! You guessed the secret code.", codeLength);
            guessed = true;
        } else if (bulls == 0 && cows > 0) {
            System.out.println("Grade: " + cows + " cow(s).");
        } else if (cows == 0 && bulls > 0) {
            System.out.println("Grade: " + bulls + " bull(s).");
        } else {
            System.out.println("Grade: " + bulls +
                    " bull(s) and " + cows + " cow(s).");
        }
        return guessed;
    }
}
