import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class FloodTest {

    // Students write more complicated tests based on this one:
    @Test
    public void testSimple() {
        WaterColor[][] colors = {
                { WaterColor.BLUE, WaterColor.RED},
                { WaterColor.CYAN, WaterColor.YELLOW}
        };

        Board b = new Board(2, colors);
        b.tiles[0][0].setColor(WaterColor.RED);
        b.flood(0, WaterColor.RED);

        boolean [] [] flooded_solution = {
                { true,  true},
                { false, false}
        };

        for (int y = 0; y < b.size; y++) {
            for (int x = 0; x < b.size; x++) {
                assertEquals(b.flooded.contains(new Coord(x,y)), flooded_solution[y][x]);
            }
        }
    }

    // a helper function that students can use
    private boolean check_colors(Board b, WaterColor[][] ref) {
        for (int x = 0; x < b.getSize(); ++x) {
            for (int y = 0; y < b.getSize(); ++y) {
                if (b.get(new Coord(x, y)).getColor() != ref[y][x]) {
                    return false;
                }
            }
        }
        return true;
    }

    // YOUR CODE

}
