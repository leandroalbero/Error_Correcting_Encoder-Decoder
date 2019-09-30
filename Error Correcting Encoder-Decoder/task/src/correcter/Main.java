package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static char[] triple(char[] input){
        StringBuilder out=new StringBuilder("");
        for (char c : input) {
            out.append(String.valueOf(c).repeat(3));
        }
        return out.toString().toCharArray();
    }
    static char[] scram(char[] input){
        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRTUVWXYZ";
        int lowerBound=0;
        int upperBound=2;
        while(true) {
            try{
                input[lowerBound + r.nextInt((upperBound - lowerBound) + 1)] = alphabet.charAt(r.nextInt(alphabet.length())) ;
                lowerBound+=3;upperBound+=3;
            }catch (Exception e){
                break;
            }
        }
        return input;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char[] input = sc.nextLine().toCharArray();
        //System.out.println(input);
        System.out.println(triple(input));
        char[] scrambled = scram(triple(input));
        System.out.println(scrambled);
        char[] ecc=errorCorrect(scrambled);
        System.out.println(ecc);
    }
    static char[] errorCorrect(char[] input){
        int lowerBound=0;
        StringBuilder out = new StringBuilder("");
        while(true) {
            try {
                if (input[lowerBound] == input[lowerBound + 1]) {
                    out.append(input[lowerBound]);
                } else if (input[lowerBound] == input[lowerBound+2]) {
                    out.append(input[lowerBound]);
                } else {
                    out.append(input[lowerBound + 1]);
                }
                lowerBound += 3;
            }catch (Exception e){
                break;
            }
        }
        return(out.toString().toCharArray());
    }
}
