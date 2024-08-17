import java.util.*;

/**
 * Interface that provides a blueprint for adversarial search like algorithms
 * @author Alexandre Rodrigues
 * @version 1.0 29/11/2023
 */
interface ILayout
{
    int ROWS = 4;
    int COLUMNS = 4;
    int WIN_CONDITION_LENGTH = 4;
    enum ID {Blank, X, O}
    int MAX_EVALUATION = Integer.MAX_VALUE - 1;
    int MIN_EVALUATION = Integer.MIN_VALUE + 1;

    /**
     * Places an X or an O on the specified index depending on whose turn it is.
     * @param index the position on the board (example: index BOARD_WIDTH is location (0, 1))
     * @return true if the move has not already been played
     */
    boolean move(int index);

    /**
     * @return true if the game is over; false otherwise
     */
    boolean isGameOver();

    /**
     * @return the ID for the current Turn
     */
    ID getTurn();

    /**
     * @return the player who won (or Blank if the game is a draw)
     */
    ID getWinner();

    /**
     * Get the indexes of all the positions on the board that are empty.
     * @return the empty cells
     */
    HashSet<Integer> getAvailableMoves();

    /**
     * @return the children of the receiver.
     */
    ArrayList<ILayout> getChildren();

    /**
     * @return the children of the receiver and their respective actions.
    */
    HashMap<ILayout, Integer> getChildrenActions();

    /**
     * @return the layout's evaluation.
     */
    int getEvaluation();
}
