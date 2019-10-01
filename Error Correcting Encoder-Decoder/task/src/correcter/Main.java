package correcter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    static char[] triple(char[] input) {
        StringBuilder out = new StringBuilder("");
        for (char c : input) {
            out.append(String.valueOf(c).repeat(3));
        }
        return out.toString().toCharArray();
    }
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
    static char[] readFile() throws IOException {
        try{
            String input = readFileAsString("send.txt");
            return input.toCharArray();
        }catch (IOException e){
            return null;
        }
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

    public static void main(String[] args) throws IOException {
        char[] input = readFile();
        try{
            FileWriter fw = new FileWriter("received.txt",false);
            char[] tmp = bitLevelError(input);
            StringBuilder sb = new StringBuilder();
            for (int i =0;i<tmp.length;i++){
                sb.append(tmp[i]);
            }

            fw.append(sb.toString());
            fw.close();
        }catch (java.io.IOException e){
            System.out.println("FHE");
        }
        System.out.println();
    }
    static char[] bitLevelError(char[] input){
        String[] hex = new String[input.length];
        Random r1 = new Random();
        for (int i=0;i<input.length;i++){
            hex[i]=Integer.toBinaryString(input[i]);
            int ranPos = r1.nextInt(hex[i].length());
            StringBuilder tmp = new StringBuilder();
            tmp.append(hex[i]);
            if(tmp.charAt(ranPos)=='0'){
                tmp.setCharAt(ranPos,'1');
            }else if(tmp.charAt(ranPos)=='1'){
                tmp.setCharAt(ranPos,'0');
            }else{
                System.out.println();
            }
            hex[i]=tmp.toString();
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<hex.length;i++){
            sb.append((char)Integer.parseInt(hex[i],2));
        }
        char[] out = new char[sb.length()];
        for (int i=0;i<sb.length();i++){
            out[i]=sb.charAt(i);
        }
        return out;
    }
}
