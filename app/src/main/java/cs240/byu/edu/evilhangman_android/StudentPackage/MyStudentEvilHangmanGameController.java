package cs240.byu.edu.evilhangman_android.StudentPackage;

import java.io.InputStreamReader;
import java.util.Set;

/**
 * Created by Marshall Garey
 */
public class MyStudentEvilHangmanGameController implements StudentEvilHangmanGameController {
    private MyEvilHangmanGame game;
    private int guesses;

    /**
     * This will get called after every valid guess. You should check to see if the user has won,
     * lost, or is still in the heat of battle! (aka they are still playing)
     *
     * @return {@link GAME_STATUS}
     */
    @Override
    public GAME_STATUS getGameStatus() {
        if(game.currPartialWord.matches("[a-z]+")) {
            return GAME_STATUS.PLAYER_WON;
        }
        else if (guesses <= 0) {
            return GAME_STATUS.PLAYER_LOST;
        }
        else {
            return GAME_STATUS.NORMAL;
        }
    }

    /**
     * Simply return the number of guesses the user has left before the game ends
     *
     * @return number of guesses left
     */
    @Override
    public int getNumberOfGuessesLeft() {
        return guesses;
    }

    /**
     * Return what the current word looks like. For example, lets say the game has started and the
     * word length is 5. Then you would return this string "-----" (that's 5 dashes). Now after the
     * user has guess a letter (say the letter a), you would return this string "-a---". When the
     * user guesses the letter z you return the string "-azz-".
     *
     * @return the current word to show on the screen (dashes and letters)
     */
    @Override
    public String getCurrentWord() {
        return game.currPartialWord;
    }

    /**
     * Return a set of characters of all the guesses that have been made already. This is simply to
     * show on the screen what guesses have been made. YOU still need to check on every guess if that
     * character has already been guessed or not
     *
     * @return Set of all characters already guessed
     */
    @Override
    public Set<Character> getUsedLetters() {
        return game.guessedLetters;
    }

    /**
     * This is called at the beginning of the game to let your controller know how many guesses to start
     * the game with.
     *
     * @param numberOfGuessesToStart guesses in the game
     */
    @Override
    public void setNumberOfGuesses(int numberOfGuessesToStart) {
        guesses = numberOfGuessesToStart;
    }

    /**
     * Starts a new game of evil hangman using words from <code>dictionary</code>
     * with length <code>wordLength</code>.
     * <p/>
     * This method should set up everything required to play the game,
     * but should not actually play the game. (ie. There should not be
     * a loop to prompt for input from the user.)
     *
     * @param dictionary Dictionary of words to use for the game
     * @param wordLength Number of characters in the word to guess
     */
    @Override
    public void startGame(InputStreamReader dictionary, int wordLength) {
        game = new MyEvilHangmanGame();
        game.startGame(dictionary, wordLength);
        if (game.workingDictionary.size() == 0) {
            System.out.println("Error: there are zero words in the dictionary of that length!");
        }
    }

    /**
     * Make a guess in the current game.
     *
     * @param guess The character being guessed
     * @return The set of strings that satisfy all the guesses made so far
     * in the game, including the guess made in this call. The game could claim
     * that any of these words had been the secret word for the whole game.
     * @throws GuessAlreadyMadeException If the character <code>guess</code>
     *                                                    has already been guessed in this game.
     */
    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        Set<String> ret_val = game.makeGuess(guess);
        guesses--;
        return ret_val;
    }
}
