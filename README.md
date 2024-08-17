# MnkGameAI

Forked project from an AI class assignment I made. The assignment consisted in implementing an AI for any instance of an m,n,k-game, a more generalized version of the infamous tic-tac-toe game.

From wikipidea [[1]](https://en.wikipedia.org/wiki/M,n,k-game) :
"An m,n,k-game is an abstract board game in which two players take turns in placing a stone of their color on an m-by-n board, the winner being the player who first gets k stones of their own color in a row, horizontally, vertically, or diagonally. Thus, tic-tac-toe is the 3,3,3-game and free-style gomoku is the 15,15,5-game."

This implementation uses the Minimax with alpha-beta pruning adversarial search algorithm.

## Wanna try it?

Simply create a Java project on your system and copy the *src* folder to the just created project. The driver code is located in the *Console* class. By default, the player gets the first move and the AI plays next, this however can be easily changed by commenting/uncommenting the following lines of code: 
```   
private void playMove() 
{
    ...

    position = this.getHumanMove();
    // position = MiniMaxAgent.play(this.board);

    ...

    // position = this.getHumanMove();
    position = MiniMaxAgent.play(this.board);

    ...
}
```

The default game configuration is a 4,4,4-game one, to change this all you have to do is change the following constants in the *ILayout* interface:

```
interface ILayout
{
    int ROWS = 4;
    int COLUMNS = 4;
    int WIN_CONDITION_LENGTH = 4;
    
    ...
}
```

Be wary of changing these values as the depth of the search is set to the maximum possible by default, if you do change the board size it is recommended you change the depth to something lower.

```
public class MiniMaxAgent
{
    public static int play(ILayout board)
    {
        return board.getChildrenActions().get(new MiniMax().miniMax(board, ILayout.ROWS * ILayout.COLUMNS));
    }

    ...
}
```

Feel free to play around with the code or improve it. Have fun! :D

## Changelog

### v1.0 (Current)

First final version, the one delivered for the assignment, including:
- The base game implementation
- A Basic CLI client
- The AI agent

This version already has some optimizations applied, such as:
- Transposition caching
- "Bitboard" hashing
- Symmetry redundancy checks
- Heuristic evaluation

## To do

- [ ] Implement an iterative deepening approach
- [ ] Dynamically determine the search depth
- [ ] Improve CLI experience
- [ ] Improve overall performance
