/**
 * Reactive agents that plays according to the miniMax algorithm
 * @author Alexandre Rodrigues
 * @version 1.0 29/11/2023
 */
public class MiniMaxAgent
{
    /**
     * Plays a move accordingly to miniMax calculations, using the standard minimax approach
     * @param board the board to play on
     * @return move to play
     */
    public static int play(ILayout board)
    {
        return board.getChildrenActions().get(new MiniMax().miniMax(board, ILayout.ROWS * ILayout.COLUMNS));
    }

    /**
     * Plays a move accordingly to miniMax calculations, using the iterative deepening approach
     * @param board the board to play on
     * @param searchDepth search Depth
     * @return move to play
     */
    public static int play(ILayout board, int searchDepth)
    {
        return board.getChildrenActions().get(new MiniMax().IterativeDeepeningMiniMax(board, searchDepth));
    }
}
