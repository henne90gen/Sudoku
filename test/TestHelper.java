import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by henne on 29.10.16.
 */
class TestHelper {

    public static TestHelper INSTANCE = new TestHelper();

    private final Integer[] easy = {
            0, 4, 3, 0, 0, 0, 6, 7, 0,
            0, 0, 0, 2, 9, 3, 0, 0, 4,
            2, 8, 0, 0, 0, 0, 3, 1, 9,
            0, 9, 0, 6, 0, 0, 0, 0, 0,
            1, 0, 0, 5, 0, 7, 0, 0, 8,
            0, 0, 0, 0, 0, 4, 0, 0, 0,
            0, 5, 6, 0, 0, 0, 0, 4, 1,
            3, 0, 9, 4, 5, 2, 0, 0, 0,
            0, 2, 7, 0, 0, 0, 9, 5, 0};

    private final Integer[] easySolution = {
            9, 4, 3, 1, 8, 5, 6, 7, 2,
            6, 7, 1, 2, 9, 3, 5, 8, 4,
            2, 8, 5, 7, 4, 6, 3, 1, 9,
            7, 9, 4, 6, 2, 8, 1, 3, 5,
            1, 6, 2, 5, 3, 7, 4, 9, 8,
            5, 3, 8, 9, 1, 4, 7, 2, 6,
            8, 5, 6, 3, 7, 9, 2, 4, 1,
            3, 1, 9, 4, 5, 2, 8, 6, 7,
            4, 2, 7, 8, 6, 1, 9, 5, 3};

    private final Integer[] medium = {
            0, 8, 0, 0, 0, 4, 5, 6, 0,
            0, 6, 0, 0, 0, 0, 0, 1, 7,
            9, 0, 0, 0, 7, 0, 0, 8, 2,
            0, 0, 0, 7, 0, 0, 6, 0, 0,
            0, 0, 5, 1, 0, 6, 7, 0, 0,
            0, 0, 7, 0, 0, 3, 0, 0, 0,
            5, 1, 0, 0, 2, 0, 0, 0, 6,
            3, 2, 0, 0, 0, 0, 0, 4, 0,
            0, 7, 6, 4, 0, 0, 0, 9, 0};
    private final Integer[] hard = {
            5, 0, 0, 0, 0, 0, 0, 3, 6,
            0, 0, 8, 0, 7, 0, 0, 0, 0,
            0, 1, 4, 0, 0, 5, 0, 0, 0,
            9, 0, 0, 6, 0, 0, 0, 0, 0,
            4, 0, 0, 0, 8, 0, 1, 5, 0,
            1, 0, 0, 0, 0, 2, 0, 0, 7,
            8, 4, 0, 1, 0, 0, 9, 0, 0,
            7, 0, 0, 0, 4, 0, 8, 0, 0,
            2, 3, 0, 0, 0, 0, 0, 0, 0};
    private final Integer[] hardSolution = {
            5, 9, 7, 8, 2, 1, 4, 3, 6,
            3, 2, 8, 4, 7, 6, 5, 9, 1,
            6, 1, 4, 3, 9, 5, 2, 7, 8,
            9, 7, 5, 6, 1, 4, 3, 8, 2,
            4, 6, 2, 7, 8, 3, 1, 5, 9,
            1, 8, 3, 9, 5, 2, 6, 4, 7,
            8, 4, 6, 1, 3, 7, 9, 2, 5,
            7, 5, 1, 2, 4, 9, 8, 6, 3,
            2, 3, 9, 5, 6, 8, 7, 1, 4};
    private final Integer[] hard2 = {
            8, 0, 0, 1, 0, 0, 0, 0, 6,
            0, 0, 2, 0, 0, 9, 0, 0, 4,
            3, 0, 0, 0, 0, 6, 2, 5, 0,
            0, 2, 5, 0, 0, 0, 0, 0, 0,
            4, 0, 0, 0, 1, 0, 0, 0, 3,
            0, 0, 0, 0, 0, 0, 1, 6, 0,
            0, 4, 1, 3, 0, 0, 0, 0, 7,
            5, 0, 0, 8, 0, 0, 4, 0, 0,
            9, 0, 0, 0, 0, 4, 0, 0, 1};
    private Integer[] mediumSolution = {
            7, 8, 1, 2, 3, 4, 5, 6, 9,
            4, 6, 2, 9, 5, 8, 3, 1, 7,
            9, 5, 3, 6, 7, 1, 4, 8, 2,
            1, 3, 8, 7, 9, 2, 6, 5, 4,
            2, 9, 5, 1, 4, 6, 7, 3, 8,
            6, 4, 7, 5, 8, 3, 9, 2, 1,
            5, 1, 4, 3, 2, 9, 8, 7, 6,
            3, 2, 9, 8, 6, 7, 1, 4, 5,
            8, 7, 6, 4, 1, 5, 2, 9, 3};

    private TestHelper() {
    }

    Integer[] getEasy() {
        return easy;
    }

    Integer[] getEasySolution() {
        return easySolution;
    }

    Integer[] getMedium() {
        return medium;
    }

    Integer[] getMediumSolution() {
        return mediumSolution;
    }

    Integer[] getHard() {
        return hard;
    }

    Integer[] getHardSolution() {
        return hardSolution;
    }

    Integer[] getHard2() {
        return hard2;
    }

    List<Integer>[] getExpectedScanGrid() {
        //noinspection unchecked
        List<Integer>[] result = new ArrayList[81];
        for (int i = 0; i < result.length; i++) {
            result[i] = null;
        }
        result[0] = new ArrayList<>();
        Collections.addAll(result[0], 5, 9);
        result[3] = new ArrayList<>();
        Collections.addAll(result[3], 1, 8);
        result[4] = new ArrayList<>();
        Collections.addAll(result[4], 1, 8);
        result[5] = new ArrayList<>();
        Collections.addAll(result[5], 1, 5, 8);
        result[8] = new ArrayList<>();
        Collections.addAll(result[8], 2, 5, 9);
        result[9] = new ArrayList<>();
        Collections.addAll(result[9], 5, 6, 7);
        result[10] = new ArrayList<>();
        Collections.addAll(result[10], 1, 6, 7);
        result[11] = new ArrayList<>();
        Collections.addAll(result[11], 1, 5);
        result[15] = new ArrayList<>();
        Collections.addAll(result[15], 5, 8);
        result[16] = new ArrayList<>();
        Collections.addAll(result[16], 8);
        result[20] = new ArrayList<>();
        Collections.addAll(result[20], 5, 9);
        result[21] = new ArrayList<>();
        Collections.addAll(result[21], 7);
        result[22] = new ArrayList<>();
        Collections.addAll(result[22], 4, 6, 7);
        result[23] = new ArrayList<>();
        Collections.addAll(result[23], 5, 6);
        result[26] = new ArrayList<>();
        Collections.addAll(result[26], 5, 9);
        result[27] = new ArrayList<>();
        Collections.addAll(result[27], 4, 5, 7, 8, 9);
        result[28] = new ArrayList<>();
        Collections.addAll(result[28], 3, 7, 9);
        result[29] = new ArrayList<>();
        Collections.addAll(result[29], 2, 4, 5, 8, 9);
        result[31] = new ArrayList<>();
        Collections.addAll(result[31], 1, 2, 3, 8);
        result[32] = new ArrayList<>();
        Collections.addAll(result[32], 1, 8, 9);
        result[33] = new ArrayList<>();
        Collections.addAll(result[33], 1, 2, 4, 5, 7);
        result[34] = new ArrayList<>();
        Collections.addAll(result[34], 2, 3, 9);
        result[35] = new ArrayList<>();
        Collections.addAll(result[35], 2, 3, 5, 7, 9);
        result[37] = new ArrayList<>();
        Collections.addAll(result[37], 3, 6, 9);
        result[38] = new ArrayList<>();
        Collections.addAll(result[38], 2, 4, 9);
        result[40] = new ArrayList<>();
        Collections.addAll(result[40], 2, 3);
        result[42] = new ArrayList<>();
        Collections.addAll(result[42], 2, 4);
        result[43] = new ArrayList<>();
        Collections.addAll(result[43], 2, 3, 6, 9);
        result[45] = new ArrayList<>();
        Collections.addAll(result[45], 5, 6, 7, 8, 9);
        result[46] = new ArrayList<>();
        Collections.addAll(result[46], 3, 6, 7, 9);
        result[47] = new ArrayList<>();
        Collections.addAll(result[47], 2, 5, 8, 9);
        result[48] = new ArrayList<>();
        Collections.addAll(result[48], 1, 3, 8, 9);
        result[49] = new ArrayList<>();
        Collections.addAll(result[49], 1, 2, 3, 8);
        result[51] = new ArrayList<>();
        Collections.addAll(result[51], 1, 2, 5, 7);
        result[52] = new ArrayList<>();
        Collections.addAll(result[52], 2, 3, 6, 9);
        result[53] = new ArrayList<>();
        Collections.addAll(result[53], 2, 3, 5, 6, 7, 9);
        result[54] = new ArrayList<>();
        Collections.addAll(result[54], 8, 9);
        result[57] = new ArrayList<>();
        Collections.addAll(result[57], 3, 7, 8, 9);
        result[58] = new ArrayList<>();
        Collections.addAll(result[58], 3, 7, 8);
        result[59] = new ArrayList<>();
        Collections.addAll(result[59], 8, 9);
        result[60] = new ArrayList<>();
        Collections.addAll(result[60], 2, 7, 8);
        result[64] = new ArrayList<>();
        Collections.addAll(result[64], 1, 9);
        result[65] = new ArrayList<>();
        Collections.addAll(result[65], 1, 8, 9);
        result[69] = new ArrayList<>();
        Collections.addAll(result[69], 7, 8);
        result[70] = new ArrayList<>();
        Collections.addAll(result[70], 6, 8);
        result[71] = new ArrayList<>();
        Collections.addAll(result[71], 6, 7);
        result[72] = new ArrayList<>();
        Collections.addAll(result[72], 4, 8);
        result[75] = new ArrayList<>();
        Collections.addAll(result[75], 1, 3, 8);
        result[76] = new ArrayList<>();
        Collections.addAll(result[76], 1, 3, 6, 8);
        result[77] = new ArrayList<>();
        Collections.addAll(result[77], 1, 6, 8);
        result[80] = new ArrayList<>();
        Collections.addAll(result[80], 3, 6);
        return result;
    }
}
