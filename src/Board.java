import java.util.*;

/**
 * Represents a board.
 * @author Alexandre Rodrigues
 * @version 1.0 29/11/2023
 */
public class Board implements ILayout, Cloneable
{
    private ID[][] board;
    private int hash;
    private ID playersTurn;
    private ID winner;
    private HashSet<Integer> availableMoves;
    private HashSet<Integer> unavailableMoves;
    private int moveCount;
    private boolean isGameOver;

    /**
     * Creates a brand-new board
     */
    public Board()
    {
        this.board = new ID[ROWS][COLUMNS];
        this.availableMoves = new HashSet<>();
        this.unavailableMoves = new HashSet<>();
        this.reset();
    }

    /**
     * Set the cells to be blank and load the available moves (all the moves are
     * available at the start of the game).
     */
    private void initialize()
    {
        for (int row = 0; row < ROWS; row++)
            for (int col = 0; col < COLUMNS; col++)
                this.board[row][col] = ID.Blank;

        this.availableMoves.clear();
        for (int i = 0; i < ROWS * COLUMNS; i++)
            this.availableMoves.add(i);
    }

    /**
     * Restart the game with a new blank board.
     */
    private void reset()
    {
        this.hash = 0;
        this.moveCount = 0;
        this.isGameOver = false;
        this.playersTurn = ID.X;
        this.winner = ID.Blank;
        this.initialize();
    }

    /**
     * Places an X or an O on the specified index depending on whose turn it is.
     * @param index position starts in 0 and increases from left to right and from top to bottom
     * @return true if the move has not already been played
     */
    @Override
    public boolean move(int index)
    {
        boolean move = this.move(index % COLUMNS, index / COLUMNS);
        if (move)
        {
            this.availableMoves.remove(index);
            this.unavailableMoves.add(index);
            this.hash += this.playersTurn.ordinal() * (int) Math.pow(3, index);
        }

        return move;
    }

    /**
     * Places an X or an O on the specified location depending on who turn it is.
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @return true if the move has not already been played
     */
    private boolean move(int x, int y)
    {
        if (this.isGameOver)
           throw new IllegalStateException("Game over. No more moves can be played.");

        if (this.board[y][x] == ID.Blank)
           this.board[y][x] = this.playersTurn;
        else
           return false;
        this.moveCount++;

        // The game is a draw.
        if (this.moveCount == ROWS * COLUMNS)
        {
           this.winner = ID.Blank;
           this.isGameOver = true;
        }

        // Check for a winner.
        if (this.checkWinCondition(x, y))
        {
            this.winner = this.playersTurn;
            this.isGameOver = true;
        }

        this.playersTurn = this.playersTurn == ID.X ? ID.O : ID.X;
        return true;
    }

    /**
     * Checks if game has finished by victory for one of the players
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @return true if game ends in a win, false otherwise
     */
    private boolean checkWinCondition(int x, int y)
    {
        return this.checkHorizontalWin(y) || this.checkVerticalWin(x) || this.checkLeftRightDiagonalWin(x, y) || this.checkRightLeftDiagonalWin(x, y);
    }

    /**
     * Checks if the game has finished by victory on the horizontal line for one of the players
     * @param y the y coordinate of the location
     * @return true if game ends in a win, false otherwise
     */
    private boolean checkHorizontalWin(int y)
    {
        int counter = 0;
        for (int x = 0; x < COLUMNS; x++)
        {
            if (this.board[y][x] == this.playersTurn)
                counter++;
            else
                counter = 0;

            if (counter == WIN_CONDITION_LENGTH)
                return true;
        }
        return false;
    }

    /**
     * Checks if the game has finished by victory on the vertical line for one of the players
     * @param x the x coordinate of the location
     * @return true if game ends in a win, false otherwise
     */
    private boolean checkVerticalWin(int x)
    {
        int counter = 0;
        for (int y = 0; y < ROWS; y++)
        {
            if (this.board[y][x] == this.playersTurn)
                counter++;
            else
                counter = 0;

            if (counter == WIN_CONDITION_LENGTH)
                return true;
        }
        return false;
    }

    /**
     * Checks if game the has finished by victory on the left-right diagonal for one of the players
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @return true if game ends in a win, false otherwise
     */
    private boolean checkLeftRightDiagonalWin(int x, int y)
    {
        int counter = 0;
        int yIndex = Math.max(y - x, 0);
        int xIndex = Math.max(x - y, 0);
        for (int i = 0; yIndex + i < ROWS && xIndex + i < COLUMNS; i++)
        {
            if (this.board[yIndex + i][xIndex + i] == this.playersTurn)
                counter++;
            else
                counter = 0;

            if (counter == WIN_CONDITION_LENGTH)
                return true;
        }
        return false;
    }

    /**
     * Checks if game the has finished by victory on the right-left diagonal for one of the players
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @return true if game ends in a win, false otherwise
     */
    private boolean checkRightLeftDiagonalWin(int x, int y)
    {
        int counter = 0;
        int yIndex = Math.max(y - COLUMNS + 1 + x, 0);
        int xIndex = Math.min(x + y, COLUMNS - 1);
        for (int i = 0; yIndex + i < ROWS && xIndex - i >= 0; i++)
        {
            if (this.board[yIndex + i][xIndex - i] == this.playersTurn)
                counter++;
            else
                counter = 0;

            if (counter == WIN_CONDITION_LENGTH)
                return true;
        }
        return false;
    }

    /**
     * Check to see if the game is over (if there is a winner or a draw).
     * @return true if the game is over
     */
    @Override
    public boolean isGameOver()
    {
        return this.isGameOver;
    }

    /**
     * Check to see who's turn it is.
     * @return the player whose turn it is
     */
    @Override
    public ID getTurn()
    {
        return this.playersTurn;
    }

    /**
     * @return the player who won (or Blank if the game is a draw)
     */
    @Override
    public ID getWinner()
    {
        if (!this.isGameOver)
            throw new IllegalStateException("Not over yet!");
        return this.winner;
    }

    /**
     * Get the indexes of all the positions on the board that are empty.
     * @return          the empty cells
     */
    @Override
    public HashSet<Integer> getAvailableMoves()
    {
        return this.availableMoves;
    }

    /**
     * @return a deep copy of the board
     */
     @Override
    public Object clone()
    {
    	try
        {
	        Board b = (Board) super.clone();
	        b.board = new ID[ROWS][COLUMNS];
	        for (int i = 0; i < ROWS; i++)
                System.arraycopy(this.board[i], 0, b.board[i], 0, COLUMNS);
            b.hash = this.hash;
	        b.playersTurn = this.playersTurn;
	        b.winner = this.winner;
	        b.availableMoves = new HashSet<>();
	        b.availableMoves.addAll(this.availableMoves);
            b.unavailableMoves = new HashSet<>();
            b.unavailableMoves.addAll(this.unavailableMoves);
	        b.moveCount = this.moveCount;
	        b.isGameOver = this.isGameOver;
	        return b;
    	}
    	catch (Exception e)
        {
    		throw new InternalError();
    	}
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("  ");
        for (int x = 0; x < COLUMNS; x++)
        {
            sb.append(x);
            sb.append(" ");
        }
        sb.append("\n");

        for (int y = 0; y < ROWS; y++)
        {
            sb.append(y);
            sb.append(" ");
            for (int x = 0; x < COLUMNS; x++)
            {
                if (this.board[y][x] == ID.Blank)
                    sb.append("-");
                else
                    sb.append(this.board[y][x].name());
                sb.append(" ");
            }
            if (y != ROWS - 1)
                sb.append("\n");
        }
        return new String(sb);
    }
        
    /**
    * @return the children of the receiver and their respective actions.
    */
    @Override
    public HashMap<ILayout, Integer> getChildrenActions()
    {
        HashMap<ILayout, Integer> children = new HashMap<>();
        if (this.isGameOver())
            return children;

        for (Integer i : this.availableMoves)
        {
            Board child = (Board) this.clone();
            child.move(i);
            children.put(child, i);
        }
        return children;
    }

    /**
     * @return the children of the receiver.
     */
    @Override
    public ArrayList<ILayout> getChildren()
    {
        ArrayList<ILayout> children = new ArrayList<>(this.availableMoves.size());
        if (this.isGameOver())
            return children;

        int checkSymmetryMoveLimit = Math.max(ROWS, COLUMNS);
        for (Integer i : this.availableMoves)
        {
            Board child = (Board) this.clone();
            child.move(i);
            if (this.moveCount >= checkSymmetryMoveLimit || children.stream().noneMatch((board) -> child.hasSymmetry((Board) board)))
                children.add(child);
        }
        return children;
    }

    /**
     * Checks for symmetry relations
     * @param that Board
     * @return true if board has a symmetry relation with other, false otherwise
     */
    //Not a huge fan of this code right here ;(
    private boolean hasSymmetry(Board that)
    {
        boolean horizontalSymmetry = true;
        boolean verticalSymmetry = true;
        boolean halfRotation = true;
        boolean leftRightDiagonalSymmetry = ROWS == COLUMNS;
        boolean rightLeftDiagonalSymmetry = ROWS == COLUMNS;
        boolean clockwiseRotation = ROWS == COLUMNS;
        boolean counterClockwiseRotation = ROWS == COLUMNS;

        for (int y = 0; y < ROWS; y++)
            for (int x = 0; x < COLUMNS; x++)
            {
                if (horizontalSymmetry && this.board[y][x] != that.board[y][COLUMNS - x - 1])
                    horizontalSymmetry = false;
                if (verticalSymmetry && this.board[y][x] != that.board[ROWS - y - 1][x])
                    verticalSymmetry = false;
                if (halfRotation && this.board[y][x] != that.board[ROWS - y - 1][COLUMNS - x - 1])
                    halfRotation = false;
                if (leftRightDiagonalSymmetry && this.board[y][x] != that.board[x][y])
                    leftRightDiagonalSymmetry = false;
                if (rightLeftDiagonalSymmetry && this.board[y][x] != that.board[ROWS - x - 1][COLUMNS - y - 1])
                    rightLeftDiagonalSymmetry = false;
                if (clockwiseRotation && this.board[y][x] != that.board[x][COLUMNS - y - 1])
                    clockwiseRotation = false;
                if (counterClockwiseRotation && this.board[y][x] != that.board[ROWS - x - 1][y])
                    counterClockwiseRotation = false;
            }

        return horizontalSymmetry || verticalSymmetry || halfRotation || leftRightDiagonalSymmetry ||
                rightLeftDiagonalSymmetry || clockwiseRotation || counterClockwiseRotation;
    }

    @Override
	public boolean equals(Object other)
    {
		if (other == this)
            return true;
		if (other == null || getClass() != other.getClass())
            return false;

		Board that = (Board) other;
        if (this.hash != that.hash)
            return false;
        for (int y = 0; y < ROWS; y++)
            for (int x = 0; x < COLUMNS; x++)
                if (this.board[y][x] != that.board[y][x])
                    return false;
        return true;
	}
		
	@Override
	public int hashCode()
    {
        return this.hash;
	}

    /**
     * Checks if given position is blank
     * @param index index of position
     * @return true if position is blank, false otherwise
     */
	public boolean isBlank(int index)
    {
		int x = index % COLUMNS;
		int y = index / COLUMNS;
        return (this.board[y][x] == ID.Blank);
	}

    private static final int WIN_POTENTIAL_ADJACENCY_MULTIPLIER = 10;

    // TODO: 29/11/23 Improve heuristic evaluation for competition

    /**
     * @return the layout's evaluation.
     */
    @Override
    public int getEvaluation()
    {
        if (this.isGameOver)
            return this.getUtility();
        if (this.unavailableMoves.isEmpty())
            return 0;

        return this.improveEvaluationAccuracy(this.getSuperficialEvaluation());
    }

    /**
     * @return the layout's utility
     */
    private int getUtility()
    {
        if (!this.isGameOver() || this.winner == ID.Blank)
            return 0;
        if (this.winner == ID.X)
            return MAX_EVALUATION;
        else
            return MIN_EVALUATION;
    }

    /**
     * @return a superficial evaluation of the board
     */
    private int getSuperficialEvaluation()
    {
        int evaluation = 0;
        for (Integer i : this.unavailableMoves)
        {
            int x = i % COLUMNS;
            int y = i / COLUMNS;
            ID player = this.board[y][x];
            ID oppositePlayer = player == ID.X ? ID.O : ID.X;
            evaluation += this.checkHorizontalWinPotential(x, y, player, oppositePlayer);
            evaluation += this.checkVerticalWinPotential(x, y, player, oppositePlayer);
            evaluation += this.checkLeftRightDiagonalWinPotential(x, y, player, oppositePlayer);
            evaluation += this.checkRightLeftDiagonalWinPotential(x, y, player, oppositePlayer);
        }
        return evaluation;
    }

    /**
     * Improves accuracy of a given evaluation value by taking into account whose turn is
     * @param evaluation Evaluation value
     * @return an improved evaluation value for the given board
     */
    private int improveEvaluationAccuracy(int evaluation)
    {
        int magnitude = 0;
        while (Math.pow(WIN_POTENTIAL_ADJACENCY_MULTIPLIER, magnitude + 1) <= Math.abs(evaluation))
            magnitude++;

        int power = (int) Math.pow(WIN_POTENTIAL_ADJACENCY_MULTIPLIER, magnitude);
        int higherPower = (int) Math.pow(WIN_POTENTIAL_ADJACENCY_MULTIPLIER, magnitude + 1);

        if (evaluation > 0)
            evaluation = this.playersTurn == ID.X ? evaluation - power + higherPower : evaluation - power;
        else if (evaluation < 0)
            evaluation = this.playersTurn == ID.O ? evaluation + power - higherPower : evaluation + power;
        return evaluation;
    }

    /**
     * Calculates the win potential in a given horizontal line
     * @param x x coordinate
     * @param y y coordinate
     * @param player whose turn is to play
     * @param oppositePlayer whose turn isn't to play
     * @return the horizontal win potential for the given horizontal line
     */
    private int checkHorizontalWinPotential(int x, int y, ID player, ID oppositePlayer)
    {
        if (player == ID.Blank)
            throw new IllegalArgumentException("Can't check win potential for BLANK position");
        if (this.board[y][x] != player)
            throw new IllegalArgumentException("Position doesn't match with given player");

        int counter = 1;
        int winPotential = 1;
        for (int i = x - 1; i >= 0 && this.board[y][i] != oppositePlayer; i--)
        {
            if (this.board[y][i] == player)
                return 0; //goofy
            counter++;
        }
        for (int i = x + 1; i < COLUMNS && this.board[y][i] != oppositePlayer; i++)
        {
            if (this.board[y][i] == player)
                winPotential *= WIN_POTENTIAL_ADJACENCY_MULTIPLIER;
            counter++;
        }

        winPotential = player == ID.X ? winPotential : - winPotential;
        return counter >= WIN_CONDITION_LENGTH ? winPotential : 0;
    }

    /**
     * Calculates the win potential in a given vertical line
     * @param x x coordinate
     * @param y y coordinate
     * @param player whose turn is to play
     * @param oppositePlayer whose turn isn't to play
     * @return the horizontal win potential for the given vertical line
     */
    private int checkVerticalWinPotential(int x, int y, ID player, ID oppositePlayer)
    {
        if (player == ID.Blank)
            throw new IllegalArgumentException("Can't check win potential for BLANK position");
        if (this.board[y][x] != player)
            throw new IllegalArgumentException("Position doesn't match with given player");

        int counter = 1;
        int winPotential = 1;
        for (int i = y - 1; i >= 0 && this.board[i][x] != oppositePlayer; i--)
        {
            if (this.board[i][x] == player)
                return 0; //goofy
            counter++;
        }
        for (int i = y + 1; i < ROWS && this.board[i][x] != oppositePlayer; i++)
        {
            if (this.board[i][x] == player)
                winPotential *= WIN_POTENTIAL_ADJACENCY_MULTIPLIER;
            counter++;
        }

        winPotential = player == ID.X ? winPotential : - winPotential;
        return counter >= WIN_CONDITION_LENGTH ? winPotential : 0;
    }

    /**
     * Calculates the win potential in a given left-right diagonal line
     * @param x x coordinate
     * @param y y coordinate
     * @param player whose turn is to play
     * @param oppositePlayer whose turn isn't to play
     * @return the horizontal win potential for the given horizontal line
     */
    private int checkLeftRightDiagonalWinPotential(int x, int y, ID player, ID oppositePlayer)
    {
        if (player == ID.Blank)
            throw new IllegalArgumentException("Can't check win potential for BLANK position");
        if (this.board[y][x] != player)
            throw new IllegalArgumentException("Position doesn't match with given player");

        int counter = 1;
        int winPotential = 1;
        for (int i = 1; x - i >= 0 && y - i >= 0 && this.board[y - i][x - i] != oppositePlayer; i++)
        {
            if (this.board[y - i][x - i] == player)
                return 0; //goofy
            counter++;
        }
        for (int i = 1; x + i < COLUMNS && y + i < ROWS && this.board[y + i][x + i] != oppositePlayer; i++)
        {
            if (this.board[y + i][x + i] == player)
                winPotential *= WIN_POTENTIAL_ADJACENCY_MULTIPLIER;
            counter++;
        }

        winPotential = player == ID.X ? winPotential : - winPotential;
        return counter >= WIN_CONDITION_LENGTH ? winPotential : 0;
    }

    /**
     * Calculates the win potential in a given right-left diagonal line
     * @param x x coordinate
     * @param y y coordinate
     * @param player whose turn is to play
     * @param oppositePlayer whose turn isn't to play
     * @return the horizontal win potential for the given right-left line
     */
    private int checkRightLeftDiagonalWinPotential(int x, int y, ID player, ID oppositePlayer)
    {
        if (player == ID.Blank)
            throw new IllegalArgumentException("Can't check win potential for BLANK position");
        if (this.board[y][x] != player)
            throw new IllegalArgumentException("Position doesn't match with given player");

        int counter = 1;
        int winPotential = 1;
        for (int i = 1; x + i < COLUMNS && y - i >= 0 && this.board[y - i][x + i] != oppositePlayer; i++)
        {
            if (this.board[y - i][x + i] == player)
                return 0; //goofy
            counter++;
        }
        for (int i = 1; x - i >= 0 && y + i < ROWS && this.board[y + i][x - i] != oppositePlayer; i++)
        {
            if (this.board[y + i][x - i] == player)
                winPotential *= WIN_POTENTIAL_ADJACENCY_MULTIPLIER;
            counter++;
        }

        winPotential = player == ID.X ? winPotential : - winPotential;
        return counter >= WIN_CONDITION_LENGTH ? winPotential : 0;
    }
}