package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRTUVWXYZ";
        Scanner sc = new Scanner(System.in);
        char[] ogData = sc.nextLine().toCharArray();
        int lowerBound=0;
        int upperBound=2;
        while(true) {
            try{
                ogData[lowerBound + r.nextInt((upperBound - lowerBound) + 1)] = alphabet.charAt(r.nextInt(alphabet.length())) ;
                lowerBound+=3;upperBound+=3;
            }catch (Exception e){
                break;
            }
        }
        System.out.println(ogData);
    }
}
