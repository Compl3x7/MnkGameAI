import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Minimax implementation
 * @author Alexandre Rodrigues
 * @version 1.0 29/11/2023
 */
public class MiniMax
{
    private HashMap<ILayout, State> evaluated;

    /**
     * Public call to miniMax
     * @param board current position
     * @param depth search depth limit
     * @return the layout that corresponds to the move that miniMax calculated to be the best
     */
    public ILayout miniMax(ILayout board, int depth)
    {
        if (depth < 1)
            throw new IllegalArgumentException("Invalid depth");

        return this.miniMax(new State(board, true, ILayout.MIN_EVALUATION, ILayout.MAX_EVALUATION), depth).layout;
    }

    /**
     * Initial call to miniMax
     * @param board current position
     * @param depth search depth limit
     * @return the state that corresponds to the move that miniMax calculated to be the best
     */
    private State miniMax(State board, int depth)
    {
        this.evaluated = new HashMap<>();

        if (board.isMax)
            return this.maxValue(board, depth);
        else
            return this.minValue(board, depth);
    }

    /**
     * Logic to be applied on every maximizing state during the search
     * @param current current position on the search
     * @param depth search depth limit
     * @return the state that corresponds to the move calculated to be the best from current state
     */
    private State maxValue(State current, int depth)
    {
        if (!current.isMax)
            throw new IllegalArgumentException("State isn't maximizing state");
        if (current.layout.isGameOver() || depth <= 0)
        {
            current.evaluation = current.layout.getEvaluation();
            return current;
        }

        ArrayList<ILayout> children = current.layout.getChildren();
        if (current.isRoot)
            children.sort((c1, c2) -> c2.getEvaluation() - c1.getEvaluation());

        State bestMove = null;
        for (ILayout child : children)
        {
            State childState = this.evaluate(child, current, depth);
            if (childState.evaluation > current.evaluation)
            {
                current.evaluation = childState.evaluation;
                bestMove = childState;
            }
            current.alpha = Math.max(current.alpha, current.evaluation);
            if (current.alpha >= current.beta)
                break;
        }
        return bestMove;
    }

    /**
     * Logic to be applied on every minimizing state during the search
     * @param current current position on the search
     * @param depth search depth limit
     * @return the state that corresponds to the move calculated to be the best from current state
     */
    private State minValue(State current, int depth)
    {
        if (current.isMax)
            throw new IllegalArgumentException("State isn't minimizing state");
        if (current.layout.isGameOver() || depth <= 0)
        {
            current.evaluation = current.layout.getEvaluation();
            return current;
        }

        ArrayList<ILayout> children = current.layout.getChildren();
        if (current.isRoot)
            children.sort(Comparator.comparingInt(ILayout::getEvaluation));

        State bestMove = null;
        for (ILayout child : children)
        {
            State childState = this.evaluate(child, current, depth);
            if (childState.evaluation < current.evaluation)
            {
                current.evaluation = childState.evaluation;
                bestMove = childState;
            }
            current.beta = Math.min(current.beta, current.evaluation);
            if (current.beta <= current.alpha)
                break;
        }
        return bestMove;
    }

    /**
     * Evaluate a given move from current position
     * @param child possible move to play
     * @param current current position
     * @param depth search depth limit
     * @return state of given possible move
     */
    private State evaluate(ILayout child, State current, int depth)
    {
        State childState;
        if (this.evaluated.containsKey(child))
            childState = this.evaluated.get(child);
        else
        {
            childState = new State(child, false, current.alpha, current.beta);
            if (childState.isMax)
                this.maxValue(childState, depth - 1);
            else
                this.minValue(childState, depth - 1);
            this.evaluated.put(child, childState);
        }
        return childState;
    }

    // TODO: 29/11/23 Improve iterative deepening approach for competition

    /**
     * Iterative deepening approach of miniMax
     * @param board current position
     * @param depth search depth limit
     * @return the layout that corresponds to the move that miniMax calculated to be the best
     */
    public ILayout IterativeDeepeningMiniMax(ILayout board, int depth)
    {
        if (depth < 1)
            throw new IllegalArgumentException("Invalid depth");

        State bestMove = null;
        for (int iDepth = 1; iDepth <= depth; iDepth++)
        {
            State previousBestMove = bestMove;
            State boardState = new State(board, true, ILayout.MIN_EVALUATION, ILayout.MAX_EVALUATION);
            bestMove = this.miniMax(boardState, iDepth);

            if (this.isGuaranteedVictory(boardState))
                break;
            if (this.isGuaranteedLost(boardState))
            {
                bestMove = previousBestMove != null ? previousBestMove : bestMove;
                break;
            }
        }
        return bestMove.layout;
    }

    /**
     * Checks if for a given state the game will end in victory for the player at turn no matter what
     * @param s State
     * @return true if it's a guaranteed win from current state, false otherwise
     */
    private boolean isGuaranteedVictory(State s)
    {
        return (s.isMax && s.evaluation == ILayout.MAX_EVALUATION) ||
                (!s.isMax && s.evaluation == ILayout.MIN_EVALUATION);
    }

    /**
     * Checks if for a given state the game will end in a defeat for the player at turn no matter what
     * @param s State
     * @return true if it's a guaranteed lost from current state, false otherwise
     */
    private boolean isGuaranteedLost(State s)
    {
        return (s.isMax && s.evaluation == ILayout.MIN_EVALUATION) ||
                (!s.isMax && s.evaluation == ILayout.MAX_EVALUATION);
    }

    /**
     * State class to wrap information useful for the search
     * @author Alexandre Rodrigues
     * @version 1.0 29/11/2023
     */
    private static class State
    {
        private final ILayout layout;
        private final boolean isRoot;
        private final boolean isMax;
        private int evaluation;
        private int alpha;
        private int beta;

        /**
         * Creates new state
         * @param layout Layout
         * @param alpha alpha value
         * @param beta beta value
         */
        private State(ILayout layout, boolean isRoot, int alpha, int beta)
        {
            this.layout = layout;
            this.isRoot = isRoot;
            this.isMax = layout.getTurn() == ILayout.ID.X;
            this.evaluation = this.isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            this.alpha = alpha;
            this.beta = beta;
        }
    }
}
