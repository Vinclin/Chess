=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: vinclin
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
is an appropriate use of the concept. Incorporate the feedback you got after
submitting your proposal.

    1. 2D arrays: A chess board is like a 2D array with columns and rows. Therefore, I plan to store
the location of all the chess pieces in a given turn with a 8x8 2D string array.

    2. Collections: Every time, either player makes a move, I plan to use collections to store the
previous state of the board before the move was enacted. This allows me to implement an undo feature
such that if a player wants to revert to the previous move, they can do so.

    3. JUnit Testable Component: My GameTest class includes several tests to test that there are no
chess pieces that can make illegal moves. These tests cover various edge cases, such as check,
checkmate, and castling.

    4. Complex game logic: I plan to implement specific restrictions on how each type of chess piece
can move, unique moves like castling, and check/checkmate states, fulfilling the complex game logic.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
function is in the overall game.

  I utilized nearly identical classes to those provided in the tic-tac-toe example. The Chessboard
class was adapted to accommodate a larger size and incorporate all the new buttons and the entire
board. The Game class is quite similar, but it operates with chess instead of tic-tac-toe. RunChess
closely resembles the tic-tac-toe version, with the addition of support for undo, reset, and
instructions buttons. GameTest is the location for all my JUnit tests. The main class, Chess,
primarily monitors the game state, identifies checkmate situations, determines possible moves, and
keeps track of whose turn it is, among other functions.


- Were there any significant stumbling blocks while you were implementing your
game (related to your design, or otherwise)?

  There were not many substantial obstacles encountered during the development process. However,
numerous edge cases occasionally presented challenges or led to seemingly inexplicable bugs.
Despite these minor issues, the overall progression of the game development was smooth.


- Evaluate your design. Is there a good separation of functionality? How well is
private state encapsulated? What would you refactor if given the chance?

  I believe there is an adequate division of functionality within the project where each class
served a specific unique function. For instance, the Chess class contained all the mechanics of
the game, ChessBoard for the visualization, and RunChess for setting up the game. I ensured
proper encapsulation of the private state, with the majority of variables and functions being
private, except for a select few that were necessary for other classes to access, such as
executing a move or verifying if the game has concluded.
  Given the chance, I would refactor some functions in my classes to decrease the bulkiness of my
implementation. For instance, I would consider creating a separate class for storing collections of
the previous game state, to simplify


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used
while implementing your game.

  I utilized images sourced from chess.com to represent the pieces on my chessboard. Additionally, I
utilized a similar overall data structure from example tic-tac-toe game as Chess is also a
turn-based 2D game in Java.
