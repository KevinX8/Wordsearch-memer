package com.kevinx8;

import java.util.Arrays;
import java.util.List;

public class WordSearchPuzzleDriver {
    public static void main(String[] args) {
        ExampleFile();
        ExampleArray();
    }
    public static void ExampleFile() {
        System.out.println("Generating Puzzle using BasicEnglish.txt with 10 words and 15 letters as longest with solutions hidden");
        WordSearchPuzzle puzzlemethis = new WordSearchPuzzle("BasicEnglish.txt",10,3,15);
        puzzlemethis.showWordSearchPuzzle(true);
        System.out.println(puzzlemethis.getPuzzleAsString());
        System.out.println("Now Generating Puzzle using BNCwords.txt with 50 words and max length 30 per word with solutions shown");
        WordSearchPuzzle puzzlemethat = new WordSearchPuzzle("BNCwords.txt",50,1,30);
        puzzlemethat.showWordSearchPuzzle(false);
        System.out.println(puzzlemethat.getPuzzleAsString());
    }
    public static void ExampleArray() {
        List<String> sample = Arrays.asList("This","Is","A","Simuluation","blue","or","red");
        System.out.println("Generating Puzzle using Sample array with solutions hidden");
        WordSearchPuzzle arraymethis = new WordSearchPuzzle(sample);
        arraymethis.showWordSearchPuzzle(true);
        System.out.println(arraymethis.getPuzzleAsString());
        System.out.println("Now Generating Puzzle using sample array with solutions shown");
        WordSearchPuzzle arraymethat = new WordSearchPuzzle(sample);
        arraymethat.showWordSearchPuzzle(false);
        System.out.println(arraymethat.getPuzzleAsString());
    }
}
