package com.kevinx8;
//Paulis Gributs 19250568
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class WordSearchPuzzle {
	private char[][] puzzle;
	private List<String> puzzleWords= new ArrayList<>();
	Locator Tracker = new Locator();

	public WordSearchPuzzle(List<String> userSpecifiedWords) {
		puzzleWords = userSpecifiedWords;
		generateWordSearchPuzzle();
	}

	public WordSearchPuzzle(String wordFile, int wordCount, int shortest, int longest) {
		int i = 0;
		boolean use = (int)(Math.random() * 2) == 1; //randomisation for words to avoid having the words picked in order
		try {
			ArrayList<String> words = new ArrayList<>(Files.readAllLines(Paths.get(wordFile)));
			int randomfactor = (int) (Paths.get(wordFile).toFile().length() / 1024); //nice randomisation factor that scales with file size
			while (puzzleWords.size() < wordCount) { //if not enough words are used in the first iteration repeat until enough words used
				for (String word : words) {
					if (i >= wordCount) { //stops picking words if it's already picked the specified amount
						break;
					}
					if (word.length() >= shortest && word.length() <= longest && use) {
						puzzleWords.add(word);
						i++;
					}
					use = (int) (Math.random() * randomfactor) == 1;
				}
			}
		} catch (IOException e) {
			System.out.printf("File %s not found!\n",wordFile);
			return;
		}
		generateWordSearchPuzzle();
	}
	public List<String> getWordSearchList() {
		return puzzleWords;
	}
	public char[][] getPuzzleAsGrid() {
		return puzzle;
	}
	public String getPuzzleAsString() {
		StringBuilder printer = new StringBuilder();
		for (int row= 0; row < puzzle.length;row++) {
			printer.append("| "); //adds left padding
			for (int col= 0; col < puzzle[0].length;col++) {
				printer.append(puzzle[row][col]).append(" "); //adds the letters with spaces in-between
			}
			printer.append("|\n"); //adds right padding then newlines
		}
		return printer.toString();
	}
	public void showWordSearchPuzzle (boolean hide) {
		if (!hide) {
			System.out.println(Tracker); //calls helper function to generate the list of solutions
		} else {
			System.out.println("Word List:");
			for (String word : puzzleWords
				 ) {
				System.out.println(word.toUpperCase()); //prints just words with no directions
			}
		}
	}
	private void fillwords (int direction, int row, int col, String Word) {
		//this function generates words from the middle with a bottom bias for even words (makes the solutions easier to read imo)
		Word = Word.toUpperCase();
		boolean reverse = direction == 1 || direction == 3, horizontal = direction / 2 == 0,even = Word.length() % 2 == 0;
		int incrementer = col - Word.length()/2; //sets up incrementer to go horizontally for an odd lettered word
		if (!horizontal && !even) {
			incrementer = row - Word.length()/2; //the same but vertically
		} else if (horizontal && even) {
			incrementer = col + 1 - Word.length()/2;
		} else if (!horizontal) {
			incrementer = row + 1 - Word.length()/2; //even and vertical
		}
		int i = 0; //increments from 0 if word is not reversed
		if (reverse) i = Word.length() - 1; // changes to end of word if it is reversed
		int max = incrementer + Word.length(); //sets bounds to beginning of selected position + the word size
		while (incrementer < max) {
			if (horizontal) {
				puzzle[row][incrementer] = Word.charAt(i); //fills word up to down
			} else {
				puzzle[incrementer][col] = Word.charAt(i); //fills word left to right
			}
			if (!reverse) {i++;} else {i--;} //changes i based on reverse status to avoid having repeating code
			incrementer++;
		}
	}
	private boolean Used (int checkrow, int checkcol, int wordsize, int direction) {
		//this function checks if the position chosen has space for the word chosen
		boolean horizontal = direction /2 == 0,even = wordsize % 2 == 0;
		int incrementer = checkcol - wordsize/2; //the same logic from fillwords is used here
		if (!horizontal && !even) {
			incrementer = checkrow - wordsize/2;
		} else if (horizontal && even) {
			incrementer = checkcol + 1 - wordsize/2;
		} else if (!horizontal) {
			incrementer = checkrow + 1 - wordsize/2;
		}
		int max = incrementer + wordsize;
		while (incrementer < max) { //performs a check for space and returns true if the selected area is used
			if (horizontal && puzzle[checkrow][incrementer] != '\0') { //checks left to right for horizontal
				return true;
			} else if (!horizontal && puzzle[incrementer][checkcol] != '\0') {
				return true;
			}
			incrementer++;
		}
		return false;
	}
	private void generateWordSearchPuzzle() {
		int Dimensions = (int) Math.sqrt(puzzleWords.toString().length() * 1.5);
		puzzleWords.sort(Comparator.comparing(String::length).reversed());
		Dimensions = Math.max(puzzleWords.get(0).length(),Dimensions);
		//if (puzzleWords.size() == 1) {Dimensions = puzzleWords.get(0).length();}
		//if (puzzleWords.size() == 2) {Dimensions = Math.max(puzzleWords.get(0).length(),puzzleWords.get(1).length());}
		puzzle = new char[Dimensions][Dimensions];
		int direction = (int) (Math.random() * 4);
		int row = (int) (Math.random() * Dimensions); //sets initial values as for both cases these initial values would be within bounds for either the rows(horizontal) or columns
		int col = (int) (Math.random() * Dimensions);
		for (String word : puzzleWords) {
			boolean wordplaced = false;
			while (!wordplaced) { //keeps trying to place the word and if it fails, it picks a new area to place it until it's finally placed
				boolean even = word.length() % 2 == 0;
				switch (direction / 2) {
					case 0:
						if (even) {
							col = (int) (Math.random() * (Dimensions - word.length() + 1) + word.length()/2 - 1);  //sets correct bounds for horizontal word columns
						} else {
							col = (int) (Math.random() * (Dimensions - word.length()) + word.length()/2);
						}
						break;
					case 1:
						if (even) {
							row = (int) (Math.random() * (Dimensions - word.length() + 1) + word.length()/2 - 1); //sets correct bounds for vertical word rows
						} else {
							row = (int) (Math.random() * (Dimensions - word.length()) + word.length()/2);
						}
				}
				if (!Used(row,col,word.length(),direction)) { //checks if it's already used by another word first before placing
					Tracker.add(direction, row, col); //adds it to the tracking helper function for printing later
					fillwords(direction, row, col, word); //does the actual word placing
					wordplaced = true; //escapes while loop as word is now placed
				}
				direction = (int) (Math.random() * 4); //sets new valid default boundaries
				row = (int) (Math.random() * Dimensions);
				col = (int) (Math.random() * Dimensions);
			}
		}
		for (int rowc=0; rowc < puzzle.length;rowc++) { //this fills the empty spaces with random letters
			for (int colc=0; colc < puzzle[0].length;colc++) {
				if (puzzle[rowc][colc] == '\0') {
					puzzle[rowc][colc] = (char) ('A' + (int) (Math.random() * 26));
				}
			}
		}
	}
	class Locator {
		//this class is used to keep track of what words have been placed where so they can be printed by ShowWordSearch while hide is false
		List<Integer> Directionlist = new ArrayList<>(),rowlist = new ArrayList<>(),collist = new ArrayList<>();
		public void add (int Direction, int row, int col) { //used by generate word search to add new entries to the locator/tracker
			Directionlist.add(Direction);
			rowlist.add(row);
			collist.add(col);
		}
		public String toString () { //used for printing in showwordsearch
			StringBuilder printme = new StringBuilder();
			for (int i=0; i < Directionlist.size(); i++) { //all lists are aligned acting as a 3D array
				printme.append(puzzleWords.get(i));
				printme.append(" is going ");
				printme.append(this.HumanDirection(Directionlist.get(i)));
				printme.append(" at row ");
				printme.append(this.rowlist.get(i) + 1); // starts print from 1 for user readability
				printme.append(" column ");
				printme.append(this.collist.get(i) + 1);
				printme.append("\n");
			}
			return printme.toString();
		}
		public String HumanDirection (int direction) { //coverts the directions to human readable form (stored as ints to allow for as little checks as possible)
			String HumanString = null;
			switch (direction) {
				case 0: HumanString = "Right";
					break;
				case 1: HumanString = "Left";
					break;
				case 2: HumanString = "Down";
					break;
				case 3: HumanString = "Up";
			}
			return HumanString;
		}
	}
}
