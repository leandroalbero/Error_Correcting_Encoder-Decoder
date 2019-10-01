package correcter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static char[] triple(char[] input) {
        StringBuilder out = new StringBuilder("");
        for (char c : input) {
            out.append(String.valueOf(c).repeat(3));
        }
        return out.toString().toCharArray();
    }

    static char[] readFile() {
        File f1 = new File("correcter/send.txt");
        String input = "";
        try {
            Scanner sc = new Scanner(f1);
            input = sc.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return input.toCharArray();
    }

    static char[] scram(char[] input) {
        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRTUVWXYZ";
        int lowerBound = 0;
        int upperBound = 2;
        while (true) {
            try {
                input[lowerBound + r.nextInt((upperBound - lowerBound) + 1)] = alphabet.charAt(r.nextInt(alphabet.length()));
                lowerBound += 3;
                upperBound += 3;
            } catch (Exception e) {
                break;
            }
        }
        return input;
    }

    static char[] errorCorrect(char[] input) {
        int lowerBound = 0;
        StringBuilder out = new StringBuilder("");
        while (true) {
            try {
                if (input[lowerBound] == input[lowerBound + 1]) {
                    out.append(input[lowerBound]);
                } else if (input[lowerBound] == input[lowerBound + 2]) {
                    out.append(input[lowerBound]);
                } else {
                    out.append(input[lowerBound + 1]);
                }
                lowerBound += 3;
            } catch (Exception e) {
                break;
            }
        }
        return (out.toString().toCharArray());
    }

    public static void main(String[] args) {
        char[] input = readFile();
        try{
            FileWriter fw = new FileWriter("correcter/received.txt");
            fw.write(bitLevelError(input));
            fw.close();
        }catch (java.io.IOException e){
            System.out.println("FHE");
        }
    }
    static char[] bitLevelError(char[] input){
        String hexChars = "0123456789ABCDEF";
        String[] hex = new String[input.length];
        Random r1 = new Random();
        for (int i=0;i<input.length;i++){
            hex[i]=Integer.toHexString(input[i]);
            hex[i]=hex[i].replace(hex[i].charAt(r1.nextInt(2)),hexChars.charAt(r1.nextInt(2)));
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<hex.length;i++){
            sb.append((char)Integer.parseInt(hex[i],16));
        }
        char[] out = new char[sb.length()];
        for (int i=0;i<sb.length();i++){
            out[i]=sb.charAt(i);
        }
        return out;
    }
}
