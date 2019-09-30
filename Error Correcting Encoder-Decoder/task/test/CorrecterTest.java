import correcter.Main;

import org.hyperskill.hstest.v5.testcase.CheckResult;
import org.hyperskill.hstest.v5.stage.BaseStageTest;
import org.hyperskill.hstest.v5.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

class TestClue {
    String input;

    TestClue(String input) {
        this.input = input;
    }
}

public class CorrecterTest extends BaseStageTest<TestClue> {

    public CorrecterTest() throws Exception {
        super(Main.class);
    }

    @Override
    public List<TestCase<TestClue>> generate() {
        TestClue[] testClues = new TestClue[]{
            new TestClue("Some text to test"),
            new TestClue("send message to user with id #42354"),
            new TestClue("thq")
        };

        List<TestCase<TestClue>> result = new ArrayList<>();

        for (int i = 0; i < testClues.length; i++) {
            result.add(new TestCase<TestClue>()
                .setAttach(testClues[i])
                .setInput(testClues[i].input));
        }

        return result;
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        String cleanedReply = reply.trim().replaceAll("\\n", "");
        return checkMatches(cleanedReply, clue.input);
    }

    private CheckResult checkMatches(String userOutput,
                                 String correctOutput) {

        if (userOutput.length() != correctOutput.length()) {
            return new CheckResult(false,
                "Input length and output length should be the same!\n" +
                    "Input length: " + correctOutput.length() +
                    "Output length: " + userOutput.length());
        }

        for (int i = 0; i < userOutput.length(); i+=3) {

            int from = i;
            int to = Math.min(i+3, userOutput.length());

            String currUserPart = userOutput.substring(from, to);
            String currCorrectPart = correctOutput.substring(from, to);

            if (currUserPart.length() != 3) {
                break;
            }

            int errors = 0;

            for (int j = 0; j < currUserPart.length(); j++) {
                if (currUserPart.charAt(j) != currCorrectPart.charAt(j)) {
                    errors++;
                }
            }

            if (errors != 1) {
                return new CheckResult(false,
                    "One of the triples contain "
                        + errors + " errors, but it should always be 1 error");
            }
        }

        return CheckResult.TRUE;
    }
}

