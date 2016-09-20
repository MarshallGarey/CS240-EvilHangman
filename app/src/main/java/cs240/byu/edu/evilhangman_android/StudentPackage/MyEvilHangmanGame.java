package cs240.byu.edu.evilhangman_android.StudentPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Marshall Garey
 */
public class MyEvilHangmanGame implements IEvilHangmanGame {

    public TreeSet<Character> guessedLetters;
    public String currPartialWord;
    public TreeSet<String> workingDictionary;
    public int wordLength;
    public Map<String, TreeSet<String>> subPartitions;

    public MyEvilHangmanGame() {}

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
        // Create the working dictionary, empty map of keys-subPartitions, and currPartialWord
        this.wordLength = wordLength;
        Scanner s;
        BufferedReader bufferedReader = new BufferedReader(dictionary);


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            sb.append("-");
        }
        currPartialWord = sb.toString();

        subPartitions = new HashMap<String, TreeSet<String>>();

        String word;
        workingDictionary = new TreeSet<String>();
        guessedLetters = new TreeSet<Character>();

        try {
            while ((word = bufferedReader.readLine()) != null) {
                word = word.toLowerCase();
                if (word.length() == wordLength) {
                    workingDictionary.add(word);
                }
            }
        bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        // make it lower case
        guess = Character.toLowerCase(guess);
        // check if the guess is valid
        if (!isValidGuess(guess)) {
            throw new GuessAlreadyMadeException(); // not a valid guess - throw exception
        }
        // It's valid. Add guess to set of guessed letters,
        // create subPartitions of the workingDictionary based on the guess,
        // get the best key,
        // update the working dictionary based on that key,
        // update partial word,
        // done.
        guessedLetters.add(guess);
        createSubPartitions(guess);
        String key = getBestPartition(guess);
        updateBestPartition(key);
        updatePartialWord(key);
        return workingDictionary;
    }

    private void updateBestPartition(String key) {
        workingDictionary = new TreeSet<String>(subPartitions.get(key));
    }

    private boolean isValidGuess(char guess) {
        if (!Character.isLetter(guess)) {
            System.out.println("Inavlid input");
            return false;
        }
        else if (guessedLetters.contains(guess)) {
            System.out.println("You already used that letter");
            return false;
        }
        return true;
    }

    private void updatePartialWord(String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            if (key.charAt(i) != '-') {
                sb.append(key.charAt(i));
            }
            else {
                sb.append(currPartialWord.charAt(i));
            }
        }
        currPartialWord = sb.toString();
    }

    private String getBestPartition(char guess) {
        // 1. most words
        TreeSet<String> keys = findPartitionWithMostWords();
        if (keys.size() == 1) {
            return keys.iterator().next();
        }

        // 2. fewest of the guess
        TreeSet<String> betterKeys = new TreeSet<String>();
        int currLeast = this.wordLength+1;
        int curr;
        for (String str : keys) {
            curr = getNumCharInStr(str, guess);
            if (curr < currLeast) {
                currLeast = curr;
                betterKeys = new TreeSet<String>();
                betterKeys.add(str);
            }
        }

        // 3. guess farthest to the right (alphabetical)
        return betterKeys.iterator().next();
    }

    private int getNumCharInStr(String str, char guess) {
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == guess) {
                num++;
            }
        }
        return num;
    }

    private TreeSet<String> findPartitionWithMostWords() {
        int currMost = 0;
        int curr;
        TreeSet<String> bestKeys = new TreeSet<String>();
        for (String str : subPartitions.keySet()) {
            curr = subPartitions.get(str).size();
            if (curr > currMost) {
                currMost = curr;
                bestKeys = new TreeSet<String>();
                bestKeys.add(str);
            }
            else if (curr == currMost) {
                bestKeys.add(str);
            }
        }
        return bestKeys;
    }

    private void createSubPartitions(char guess) {
        subPartitions = new HashMap<String, TreeSet<String>>();
        String key;
        for (String str : workingDictionary) {
            key = getKeyForString(str, guess);
            if (!subPartitions.containsKey(key)) { // new key
                subPartitions.put(key, new TreeSet<String>());
                subPartitions.get(key).add(str);

            }
            else { // key exists, just add it to the current partition
                subPartitions.get(key).add(str);
            }
        }

    }

    private String getKeyForString(String str, char guess) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == guess) {
                sb.append(guess);
            }
            else {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public int getGuessFreqInWord(char guess) {
        int num = 0;
        for (int i = 0; i < wordLength; i++) {
            if (currPartialWord.charAt(i) == guess) {
                num++;
            }
        }
        return num;
    }


}

