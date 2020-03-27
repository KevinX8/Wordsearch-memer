package com.kevinx8;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


public class WordSearchPuzzle {
	private char[][] puzzle;
	private List<String> puzzleWords= new ArrayList<>();
	Locator Tracker = new Locator();

	public WordSearchPuzzle(List<String> userSpecifiedWords) {
		puzzleWords = userSpecifiedWords;
		generateWordSearchPuzzle();
		// puzzle generation using user specified words
            // The user passes in a list of words to be used
            // for generating the word search grid.
	}

	public WordSearchPuzzle(String wordFile, int wordCount, int shortest, int longest) {
		AtomicInteger i = new AtomicInteger();
		AtomicBoolean use = new AtomicBoolean((int)(Math.random() * 2) == 1);
		try (Stream<String> words = Files.lines(Paths.get(wordFile), StandardCharsets.UTF_8)) {
			int randomfactor = (int) (Paths.get(wordFile).toFile().length() / 1024); //nice randomisation factor that scales with file size
			words.forEach(word -> {
				if (word.length() >= shortest && word.length() <= longest && i.get() < wordCount && use.get()) {
					puzzleWords.add(word);
					i.getAndIncrement();
				}
				use.set((int)(Math.random() * randomfactor) == 1);
			});
		} catch (IOException e) {
			System.out.printf("File %s not found!\n",wordFile);
			return;
		}
		generateWordSearchPuzzle();
		// puzzle generation using words from a file
            // The user supplies the filename. In the file
            // the words should appear one per line.
            // The wordCount specifies the number of words
            // to (randomly) select from the file for use in
            // the puzzle.
            // shortest and longest specify the shortest
            // word length to be used and longest specifies
            // the longest word length to be used.
            // SO, using the words in the file randomly select
            // wordCount words with lengths between shortest
            // and longest.

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
			printer.append("| ");
			for (int col= 0; col < puzzle[0].length;col++) {
				printer.append(puzzle[row][col]).append(" ");
			}
			printer.append("|\n");
		}
		return printer.toString();
	}
	public void showWordSearchPuzzle (boolean hide) {
		if (!hide) {
			System.out.println(Tracker);
		} else {
			System.out.println("Word List:");
			for (String word : puzzleWords
				 ) {
				System.out.println(word.toUpperCase());
			}
		}
	}
	private boolean checkspace (int direction, int row, int col, String Word, int Dimensions) {
		int wordsize = Word.length();
		boolean even = wordsize % 2 == 0;
		switch (direction / 2) {
			case 0:
				if (even) {return col + 1 - wordsize / 2 >= 0 && col + wordsize / 2 < Dimensions;} else {return col - wordsize / 2 >= 0 && col + wordsize / 2 < Dimensions;}
			case 1:
				if (even) {return row + 1 - wordsize / 2 >= 0 && row + wordsize / 2 < Dimensions;} else {return row - wordsize / 2 >= 0 && row + wordsize / 2 < Dimensions;}
		}
		return false;
	}
	private void fillwords (int direction, int row, int col, String Word) {
		Word = Word.toUpperCase();
		boolean reverse = direction == 1 || direction == 3, horizontal = direction / 2 == 0,even = Word.length() % 2 == 0;
		int incrementer = col - Word.length()/2;
		if (!horizontal && !even) {
			incrementer = row - Word.length()/2;
		} else if (horizontal && even) {
			incrementer = col + 1 - Word.length()/2;
		} else if (!horizontal) {
			incrementer = row + 1 - Word.length()/2;
		}
		int i = 0;
		if (reverse) i = Word.length() - 1;
		int max = incrementer + Word.length();
		while (incrementer < max) {
			if (horizontal) {
				puzzle[row][incrementer] = Word.charAt(i);
			} else {
				puzzle[incrementer][col] = Word.charAt(i);
			}
			if (!reverse) {i++;} else {i--;}
			incrementer++;
		}
	}
	private boolean Used (int checkrow, int checkcol, int wordsize, int direction) {
		boolean horizontal = direction /2 == 0,even = wordsize % 2 == 0,used = false;
		int incrementer = checkcol - wordsize/2;
		if (!horizontal && !even) {
			incrementer = checkrow - wordsize/2;
		} else if (horizontal && even) {
			incrementer = checkcol + 1 - wordsize/2;
		} else if (!horizontal) {
			incrementer = checkrow + 1 - wordsize/2;
		}
		int max = incrementer + wordsize;
		while (incrementer < max) {
			if (horizontal && puzzle[checkrow][incrementer] != '\0') {
				used = true;
			} else if (puzzle[incrementer][checkcol] != '\0') {
				used = true;
			}
			incrementer++;
		}
		return used;
	}
	private void generateWordSearchPuzzle() {
		int Dimensions = (int) Math.sqrt(puzzleWords.toString().length() * 3);
		puzzle = new char[Dimensions][Dimensions];
		AtomicInteger direction = new AtomicInteger((int) (Math.random() * 4));
		AtomicInteger row = new AtomicInteger((int) (Math.random() * Dimensions));
		AtomicInteger col = new AtomicInteger((int) (Math.random() * Dimensions));
		puzzleWords.forEach(word -> {
			boolean wordplaced = false;
			while (!wordplaced) {
				if (checkspace(direction.get(), row.get(), col.get(), word, Dimensions) && !Used(row.get(),col.get(),word.length(),direction.get())) {
					Tracker.add(direction.get(), row.get(), col.get());
					fillwords(direction.get(), row.get(), col.get(), word);
					wordplaced = true;
				}
				direction.set((int) (Math.random() * 4));
				row.set((int) (Math.random() * Dimensions));
				col.set((int) (Math.random() * Dimensions));
			}
		});
		char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		for (int rowc=0; rowc < puzzle.length;rowc++) {
			for (int colc=0; colc < puzzle[0].length;colc++) {
				if (puzzle[rowc][colc] == '\0') {
					puzzle[rowc][colc] = alphabet[(int) (Math.random() * 26)];
				}
			}
		}
	}
	class Locator {
		List<Integer> Directionlist = new ArrayList<>(),rowlist = new ArrayList<>(),collist = new ArrayList<>();
		public void add (int Direction, int row, int col) {
			Directionlist.add(Direction);
			rowlist.add(row);
			collist.add(col);
		}
		public String toString () {
			StringBuilder printme = new StringBuilder();
			for (int i=0; i < Directionlist.size(); i++) {
				printme.append(puzzleWords.get(i));
				printme.append(" is going ");
				printme.append(this.HumanDirection(Directionlist.get(i)));
				printme.append(" at row ");
				printme.append(this.rowlist.get(i) + 1);
				printme.append(" column ");
				printme.append(this.collist.get(i) + 1);
				printme.append("\n");
			}
			return printme.toString();
		}
		public String HumanDirection (int direction) {
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

      // The dimensions of the puzzle grid should be set
      // by summing the lengths of the words being used in the
      // puzzle and multiplying the sum by 1.5 or 1.75
      // or some other (appropriate) scaling factor to
      // ensure that the grid will have enough additional
      // characters to obscure the puzzle words. Once
      // you have calculated how many characters you are
      // going to have in the grid you can calculate the
      // grid dimensions by getting the square root (rounded up)
      // of the character total.
	//
      // You will also need to add the methods specified below
}
