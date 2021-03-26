# Othello-Reversi-Game

Made in BlueJ

## Description

A game of Reversi (specifically the othello variant) made in Java Swing, it plays like this https://www.webgamesonline.com/reversi/index.php.
The goal is to hold the higher number of pieces by the end of the game (when neither player can make a legal move). This
program lets you play the game as many times as you want, start new sessions whenever, change the board size and save
and load games.

## How To Use

Upon starting the program will have an empty board and on the right side, text fields to enter each players' name and also
labels with their scores and the total number of captured discs.

![](/imgs/img1.JPG)

After entering the player names you could also toggle the Hide/Show Moves button to choose whether to show possible moves
on each player's turn (by default this is on).

![](/imgs/img2.JPG)

On the top left corner there is a menubar with the 'Game' menu, it offers additional functionality to the game:
* **new session** - start and thereby overwrite the current game session with a new one
* **set board size** - set a new board size (also resets the current game), the new size has to be an even number
* **save game** - saves the current game into a textfile with a name and location of your choosing
* **load game** - loads a previously saved game from anywhere you choose on the disk

Below is an example of setting the board size from the default 8x8 to 14x14

![](/imgs/img3.JPG)
