package correcter;

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

    private static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    private static char[] readFile() throws IOException {
        try {
            String input = readFileAsString("/Users/leandroalbero/IdeaProjects/Error Correcting Encoder-Decoder/Error Correcting Encoder-Decoder/task/send.txt");
            return input.toCharArray();
        } catch (IOException e) {
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

    private static char[] bitLevelError(char[] input) {
        String[] hex = new String[input.length];
        Random r1 = new Random();
        for (int i = 0; i < input.length; i++) {
            hex[i] = Integer.toBinaryString(input[i]);
            int ranPos = r1.nextInt(hex[i].length());
            StringBuilder tmp = new StringBuilder();
            tmp.append(hex[i]);
            if (tmp.charAt(ranPos) == '0') {
                tmp.setCharAt(ranPos, '1');
            } else if (tmp.charAt(ranPos) == '1') {
                tmp.setCharAt(ranPos, '0');
            } else {
                System.out.println();
            }
            hex[i] = tmp.toString();
        }
        StringBuilder sb = new StringBuilder();
        for (String s : hex) {
            sb.append((char) Integer.parseInt(s, 2));
        }
        char[] out = new char[sb.length()];
        for (int i = 0; i < sb.length(); i++) {
            out[i] = sb.charAt(i);
        }
        return out;
    }

    static String[] stringToHex (char[] input){
        String[] hex =new String[input.length];
        for (int i=0;i<input.length;i++){
            hex[i]=String.format("%x",(int)input[i]);
        }
        return hex;
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Write a mode: ");
        String mode = sc.nextLine();
        if (mode.equals("encode")) { //Read sent.txt, convert to rts form and save into encoded.txt
            char[] input = readFile();
            System.out.print("\nsend.txt:\ntext view: ");
            System.out.println(input);
            System.out.print("hex view: ");
            String[] hex = stringToHex(input);
            for (String s:hex) {
                System.out.print(s+' ');
            }
            System.out.println("encoded.txt: ");


        } else if (mode.equals("send")) { //Read encoded.txt and simulate errors, then save into received.txt

        } else if (mode.equals("decode")) { //Read from received.txt and save normal text into decoded.txt

        }
        try {
            char[] input = readFile();
            FileWriter fw = new FileWriter("received.txt", false);
            char[] tmp = bitLevelError(input);
            StringBuilder sb = new StringBuilder();
            for (char c : tmp) {
                sb.append(c);
            }
            fw.append(sb.toString());
            fw.close();
        } catch (java.io.IOException e) {
            System.out.println("FHE");
        }
        System.out.println();
    }
}
