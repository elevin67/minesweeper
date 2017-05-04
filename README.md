# minesweeper
Minesweeper algorithms for Comp221 final project.

This program can be run by compiling and running the TestSolver class.

Board class creates the board and adds half of the mines in random spots
and the other half of the mines in random spots while making sure that
there is at most only 1 neighboring mine. After adding mines, we iterate through
all the tiles and enter number values by counting how many mines are around each tile.

Solver class contains solutions to solve the board. The first click always uncovers the empty tiles,
and BFS implementation is used for uncovering empty tiles. After the first click, it iterates through
all the tiles and look for the spots that obviously contain mines. If the number of unknown squares
around a specific tile is same as the number written on the tile, we flag all the unknown squares.
After that, we either use random selection approach or optimized random selection approach to solve
the rest of the board. The random selection approach iterates over the board to uncover a
random unknown spot when stuck. On the other hand, optimized random selection approach prioritizes
tiles with lower number of unknown around over tiles with higher number of unknown spots
around it.

Point class is made to represent each tiles. It contains information about the number written in the
tile and tile's x and y coordinate in the board.

TestSolver class generates a board and solve the board using two different approaches from the Solver
class. We also run these solutions through different sized boards.




