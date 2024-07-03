import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

/**
 * Game is a controller that run a game in either interactive or batch mode. Unless
 * resized, the game is played on a board of size Constants.DEFAULT_SIZE.
 * <p>
 * Operations are provided to compare run times of different flood functions
 * (using reflection to automatically detect the defined methods in the Board class).
 */

public class Game {
    private int size = Constants.DEFAULT_SIZE;
    private Board board;
    private GUI theView;
    private boolean interactive = true;

    private int currentStep;

    /**
     * Runs a game in interactive mode. See comment for how to run in batch (i.e., testing) mode.
     */
    public static void main(final String... args) {
        System.out.println(Constants.TITLE);

        if (args.length > 0 && args[0].equals("timing")) {
            // run a batch of games and display a graph of the timings:
            new Game(false).batchTest();
        } else {
            // Run a game in interactive mode:
            SwingUtilities.invokeLater(() -> new Game());
        }

    }

    /**
     * Creates an interactive game.
     */
    public Game() {
        this(true);
    }

    /**
     * Creates either an interactive or simulated (autoplayed) game depending on the
     * sense of the argument.
     */
    public Game(boolean interactive) {
        this.interactive = interactive;
        init();
    }

    /**
     * Returns the board associated with this game.
     */

    public Board getBoard() {
        return board;
    }

    /**
     * Updates the board size and restarts.
     */

    void resize(int size) {
        this.size = size;
        if (interactive)
            theView.dispose();
        init();
    }

    /**
     * Initializes this game to a fresh state and starts up the gui.
     */

    private void init() {
        board = new Board(size);
        currentStep = 0;
        if (interactive)
            theView = new GUI(this);
    }

    /**
     * Returns true iff the player has run out of steps.
     */

    public boolean noMoreSteps() {
        return currentStep == getStepLimit();
    }

    /**
     * Returns the number of steps used by the player so far during this game.
     */

    public int getSteps() {
        return currentStep;
    }

    /**
     * Returns the maximum number of steps for this game.
     */

    public int getStepLimit() {
        return size * 25 / 14 + 1;
    }

    /**
     * Processes one step of the game (where the player has selected the
     * given color for their move) using the standard flood function.
     */

    public void select(WaterColor color) {
        select(1, color); // k == 0 means to use Board.flood() as the flood function
    }

    /**
     * Processes one stop of the game (where the player has selected the
     * give color for their move) using the kth flood function (where k
     * = 0, 1, 2, ...)
     */

    public void select(int k, WaterColor color) {
        currentStep++;
        board.flood(k, color);
    }

    /**
     * Plays a series of games in batch mode, where player moves are selected
     * according to the board's suggestions, and the kth flood function is used,
     * and adds the averaged elapsed time to the thisRun list.
     */

    private void autoPlay(int k, List<Double> timings) {
        //long gameTime = 0;
        LinkedList<Long> times = new LinkedList<>();
        for (int i = 0; i < Constants.NUM_GAMES_TO_AUTOPLAY; i++) {
            long startTime = System.nanoTime();
            int repetitions = 0;
            long endTime;
            do {
                while (!board.fullyFlooded())
                    select(k, board.suggest());
                init();
                endTime = System.nanoTime();
                ++repetitions;
            } while ((endTime - startTime)  < 100000000);
            times.add((endTime - startTime) / repetitions);
        }

        //double averageTime = gameTime / Constants.NUM_GAMES_TO_AUTOPLAY;
        double minTime = Collections.min(times);
        timings.add(minTime / Constants.NANOSECONDS_IN_SECCONDS);
    }

    /**
     * Runs a batch of tests, on boards of varying sizes, through
     * autoPlay(), iterating over all defined flood functions, and then
     * displays a graph of run times.
     */

    private void batchTest() {
        List<List<Double>> allTimings = new LinkedList<>();
        List<Integer> numTiles = new LinkedList<>();
        for (int k = 0; k != 2; ++k) {
            String name = "flood" + (k == 0 ? "" : k);
            System.out.println("running with " + name + " as the flood function");
            List<Double> timings = new LinkedList<>();
            int prev_size = 0;
            for (int tiles = 100; tiles <= Constants.MAX_BOARD_SIZE_FOR_AUTOPLAY;
                 tiles += Constants.SIZE_INC_FOR_AUTOPLAY) {
                int size = (int)Math.sqrt(tiles);
                if (size != prev_size) {
                    prev_size = size;
                    System.out.println("testing a board with " + size * size + " tiles");
                    if (k == 0)
                        numTiles.add(size * size);
                    resize(size);
                    autoPlay(k, timings);  // use the kth flood function
                }
            }
            System.out.println("timings for the above boards: " + timings);
            allTimings.add(timings);
        }
        SwingUtilities.invokeLater(() -> new TimingGraph(allTimings, numTiles));

    }
}

