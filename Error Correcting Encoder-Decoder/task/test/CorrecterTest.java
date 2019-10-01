import correcter.Main;

import org.hyperskill.hstest.v5.testcase.CheckResult;
import org.hyperskill.hstest.v5.stage.BaseStageTest;
import org.hyperskill.hstest.v5.testcase.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class TestClue {
    String input;
    String fileContent;

    TestClue(String input, String fileContent) {
        this.input = input;
        this.fileContent = fileContent;
    }
}

public class CorrecterTest extends BaseStageTest<TestClue> {

    private static File received = null;
    private static File encoded = null;
    private static File decoded = null;

    public CorrecterTest() throws Exception {
        super(Main.class);
    }

    @Override
    public List<TestCase<TestClue>> generate() {
        TestClue[] testClues = new TestClue[] {
            new TestClue("encode", "Eat more of these french buns!"),
            new TestClue("send",   "Eat more of these french buns!"),
            new TestClue("decode", "Eat more of these french buns!"),

            new TestClue("encode", "$ome rand0m messAge"),
            new TestClue("send",   "$ome rand0m messAge"),
            new TestClue("decode", "$ome rand0m messAge"),

            new TestClue("encode", "better call Saul 555-00-73!"),
            new TestClue("send",   "better call Saul 555-00-73!"),
            new TestClue("decode", "better call Saul 555-00-73!"),

            new TestClue("encode", "5548172 6548 225147 23656595 5155"),
            new TestClue("send",   "5548172 6548 225147 23656595 5155"),
            new TestClue("decode", "5548172 6548 225147 23656595 5155"),
        };

        List<TestCase<TestClue>> result = new ArrayList<>();

        for (int i = 0; i < testClues.length; i++) {
            result.add(new TestCase<TestClue>()
                .setAttach(testClues[i])
                .setInput(testClues[i].input)
                .addFile("send.txt", testClues[i].fileContent));
        }

        return result;
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        String path = System.getProperty("user.dir");

        received = null;
        encoded = null;
        decoded = null;

        searchReceived(path, "received.txt");
        searchEncoded(path, "encoded.txt");
        searchDecoded(path, "decoded.txt");

        String correctFileBinary = toBinary(clue.fileContent.getBytes());
        String correctFileEncoded = encodeFile(correctFileBinary);

        String action = clue.input;

        if (action.equals("encode")) {

            if (encoded == null) {
                System.out.println("here1");
                return new CheckResult(false,
                    "Can't find encoded.txt file. " +
                        "Make sure your program writes it down or " +
                        "make sure the name of file is correct.");
            }

            byte[] encodedContent;
            FileInputStream encodedStream;

            try {
                encodedStream = new FileInputStream(encoded);
            } catch (FileNotFoundException e) {
                System.out.println("here2");
                return new CheckResult(false,
                    "Can't find encoded.txt file. " +
                        "Make sure your program writes it down or " +
                        "make sure the name of file is correct.");
            }

            try {
                encodedContent = encodedStream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("here3");
                throw new RuntimeException("Can't read the file");
            }

            String encodedBinary = toBinary(encodedContent);

            return new CheckResult(encodedBinary.equals(correctFileEncoded));
        }

        if (action.equals("send")) {

            if (received == null) {
                return new CheckResult(false,
                    "Can't find received.txt file. " +
                        "Make sure your program writes it " +
                        "down or make sure the name of file is correct.");
            }

            byte[] receivedContent;

            FileInputStream receivedStream;

            try {
                receivedStream = new FileInputStream(received);
            } catch (FileNotFoundException e) {
                return new CheckResult(false,
                    "Can't find received.txt file. " +
                        "Make sure your program writes it down or " +
                        "make sure the name of file is correct.");
            }

            try {
                receivedContent = receivedStream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Can't read the file");
            }

            String receivedBinary = toBinary(receivedContent);


            return checkDifference(receivedBinary, correctFileEncoded);
        }



        if (action.equals("decode")) {

            if (decoded == null) {
                return new CheckResult(false,
                    "Can't find decoded.txt file. " +
                        "Make sure your program writes it down or " +
                        "make sure the name of file is correct.");
            }

            byte[] decodedContent;


            FileInputStream decodedStream;

            try {
                decodedStream = new FileInputStream(decoded);
            } catch (FileNotFoundException e) {
                return new CheckResult(false,
                    "Can't find received.txt file. " +
                        "Make sure your program writes it down or " +
                        "make sure the name of file is correct.");
            }

            try {
                decodedContent = decodedStream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Can't read the file");
            }

            String decodedBinary = toBinary(decodedContent);

            if (!decodedBinary.equals(correctFileBinary)) {
                return new CheckResult(false, "The decoded text must match initial text!");
            }

            return CheckResult.TRUE;
        }

        throw new RuntimeException("Can't check the program");
    }

    private static String toBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }

    private static byte[] fromBinary(String s) {
        int sLen = s.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for (int i = 0; i < sLen; i++)
            if ((c = s.charAt(i)) == '1')
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            else if (c != '0')
                throw new IllegalArgumentException();
        return toReturn;
    }

    private static void searchReceived(String dirName, String fileName) {
        File dir = new File(dirName);
        File[] list = dir.listFiles();

        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) {
                    searchReceived(f.getAbsolutePath(), fileName);
                } else if (f.getAbsolutePath().contains(fileName)) {
                    received = f;
                }
            }
        }
    }

    private static void searchEncoded(String dirName, String fileName) {
        File dir = new File(dirName);
        File[] list = dir.listFiles();

        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) {
                    searchEncoded(f.getAbsolutePath(), fileName);
                } else if (f.getAbsolutePath().contains(fileName)) {
                    encoded = f;
                }
            }
        }
    }

    private static void searchDecoded(String dirName, String fileName) {
        File dir = new File(dirName);
        File[] list = dir.listFiles();

        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) {
                    searchDecoded(f.getAbsolutePath(), fileName);
                } else if (f.getAbsolutePath().contains(fileName)) {
                    decoded = f;
                }
            }
        }
    }

    private String encodeFile(String binaryString) {

        String encoded = "";

        for (int i = 0; i < binaryString.length(); i += 3) {

            int startSubIndex = i;
            int stopSubIndex = Math.min(i+3, binaryString.length());

            String currSub = binaryString.substring(startSubIndex, stopSubIndex);

            String encodedPart;

            if (currSub.length() == 3) {
                encodedPart =
                    currSub.substring(0, 1).repeat(2) +
                    currSub.substring(1, 2).repeat(2) +
                    currSub.substring(2, 3).repeat(2);
            } else if (currSub.length() == 2) {
                encodedPart =
                    currSub.substring(0, 1).repeat(2) +
                    currSub.substring(1, 2).repeat(2) + "00";
            } else if (currSub.length() == 1) {
                encodedPart =
                    currSub.substring(0, 1).repeat(2) + "0000";
            } else {
                encodedPart = "000000";
            }

            int parityCounts = 0;

            if (encodedPart.charAt(0) == '1') {
                parityCounts++;
            }

            if (encodedPart.charAt(2) == '1') {
                parityCounts++;
            }

            if (encodedPart.charAt(4) == '1') {
                parityCounts++;
            }

            if (parityCounts % 2 == 1) {
                encodedPart += "11";
            } else {
                encodedPart += "00";
            }

            encoded += encodedPart;
        }

        return encoded;
    }

    private CheckResult checkDifference(String output, String correct) {
        if (output.isEmpty() && correct.isEmpty()) return CheckResult.TRUE;

        if (output.length() != correct.length()) {
            return new CheckResult(false,
                "The program was expected to output " +
                    correct.length() / 8 +
                    " bytes, but output " +
                    output.length() / 8);
        }

        for (int i = 0; i < output.length(); i += 8) {
            String currOutputByte = output.substring(i, i+8);
            String currCorrectByte = correct.substring(i, i+8);

            int difference = 0;
            for (int j = 0; j < currCorrectByte.length(); j++) {
                char currOutputBit = currOutputByte.charAt(j);
                char currCorrectBit = currCorrectByte.charAt(j);

                if (currCorrectBit != currOutputBit) {
                    difference++;
                }
            }

            if (difference == 0) {
                return new CheckResult(false,
                    "One of bytes from the input stayed the same but should be changed");
            }

            if (difference != 1) {
                return new CheckResult(false,
                    "One of bytes from the input was changes in more than one bit");
            }
        }

        return CheckResult.TRUE;
    }
}

