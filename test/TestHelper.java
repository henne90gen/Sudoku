import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by henne on 29.10.16.
 */
public class TestHelper {

    public static List<Integer>[] getExpectedScanGrid() {
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
