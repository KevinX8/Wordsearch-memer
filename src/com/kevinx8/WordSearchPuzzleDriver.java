package com.kevinx8;
//Paulis Gributs 19250568
import java.util.Arrays;
import java.util.List;

public class WordSearchPuzzleDriver {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ExampleFile();
        ExampleArray();
        Breaker();
        long finish = System.currentTimeMillis();
        System.out.println("Completed in " + (finish - start) + " ms");
    }
    public static void ExampleFile() {
        System.out.println("Generating Puzzle using BasicEnglish.txt with 10 words and 15 letters as longest with solutions hidden");
        WordSearchPuzzle puzzlemethis = new WordSearchPuzzle("BasicEnglish.txt",19,3,15);
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
    public static void Breaker() {
        System.out.println("Generating Puzzle using extremely high limits to see how well it's handled, 5000 words with max length 100");
        WordSearchPuzzle Breaker1 = new WordSearchPuzzle("BNCwords.txt",5000,1,100);
        Breaker1.showWordSearchPuzzle(true);
        System.out.println(Breaker1.getPuzzleAsString());
        System.out.println("Now Generating Puzzle using 1 word with max size 100");
        WordSearchPuzzle Breaker2 = new WordSearchPuzzle("BNCwords.txt",1,1,100);
        Breaker2.showWordSearchPuzzle(false);
        System.out.println(Breaker2.getPuzzleAsString());
        System.out.println("Now Generating Puzzle using 2 words with max size 10");
        WordSearchPuzzle Breaker3 = new WordSearchPuzzle("BNCwords.txt",2,1,10);
        Breaker3.showWordSearchPuzzle(true);
        System.out.println(Breaker3.getPuzzleAsString());
        System.out.println("Now Generating Puzzle using 9 words with size 3");
        WordSearchPuzzle Breaker4 = new WordSearchPuzzle("BNCwords.txt",9,3,3);
        Breaker4.showWordSearchPuzzle(false);
        System.out.println(Breaker4.getPuzzleAsString());
    }
}
