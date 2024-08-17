import java.util.Scanner;

/**
 * For playing in the console.
 */
public class Console
{
    private final Board board;
    private final Scanner sc = new Scanner(System.in);

    private Console()
    {
        board = new Board();
    }

    /**
     * Game on
     */
    private void play()
    {
        while (true)
        {
            this.playMove();
            if (this.board.isGameOver())
            {
                this.printWinner();
                break;
            }
        }
    }

    /**
     * Handle the move to be played
     */
    private void playMove()
    {
    	int position;
    	
        if (this.board.getTurn() == ILayout.ID.X)
        {
            position = this.getHumanMove();
            // position = MiniMaxAgent.play(this.board);
        	this.board.move(position);
        }
        else
        {
            // position = this.getHumanMove();
            position = MiniMaxAgent.play(this.board);
        	this.board.move(position);
        }
        // this.printGameStatus();
    }
 
    private void printGameStatus()
    {
        System.out.println("\n" + this.board + "\n");
        System.out.println(this.board.getTurn().name() + "'s turn.");
    }

    /**
     * For reading in and interpreting the move that the user types into the console.
     */
    private int getHumanMove()
    {
        this.printGameStatus();
        System.out.println("Coordinates of move: ");

        int x = this.sc.nextInt();
        int y = this.sc.nextInt();
        int move = y * ILayout.COLUMNS + x;

        if (move < 0 || move >= ILayout.ROWS * ILayout.COLUMNS)
        {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe index of the move must be between 0 and "
                    + (ILayout.ROWS * ILayout.COLUMNS - 1) + ", inclusive.");
            return this.getHumanMove();
        }
        else if (!this.board.isBlank(move))
        {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe selected index must be blank.");
        }
        return move;
    }

    private void printWinner()
    {
        ILayout.ID winner = this.board.getWinner();

        System.out.println("\n" + this.board + "\n");

        if (winner == ILayout.ID.Blank)
            System.out.println("It's a draw.");
        else
            System.out.println("Player " + winner.toString() + " wins!");
    }

    public static void main(String[] args)
    {
        Console game = new Console();
        game.play();
    }
}
