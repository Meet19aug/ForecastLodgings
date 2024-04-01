package org.example;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Represents a node in the Trie data structure
class TrieNode {
    char data;
    boolean isEndOfWord;
    TrieNode[] children;

    // Constructor to initialize a TrieNode
    public TrieNode(char data) {
        this.data = data;
        this.isEndOfWord = false;
        this.children = new TrieNode[26];
// Assuming only lowercase alphabetical characters
    }
}

// Implements the Trie data structure
class Trie {
    private final TrieNode root;

    // Constructor to initialize the Trie
    public Trie() {
        root = new TrieNode('\0');
// Initialize root node with null character
    }

    // Inserts a word into the Trie
    public void insert(String word) {
        TrieNode current = root;

// Traverse through each character in the word
        for (char ch : word.toCharArray()) {
            int idx = ch - 'a'; // Calculate index based on character's ASCII value
            if (current.children[idx] == null) {
                current.children[idx] = new TrieNode(ch);
// Create a new node if it doesn't exist
            }
            current = current.children[idx];
// Move to the next node
        }

        current.isEndOfWord = true;
// Mark the last node as end of word
    }

    // Suggests words based on a given prefix
    public int suggestWords(String prefix, String originalWord) {
        TrieNode prefixNode = findNode(prefix);

        if (prefixNode != null) {
            return suggestWordsHelper(prefix, prefixNode, originalWord);
        } else {
            System.out.println("No suggestions for the given prefix.");
            return 1;
        }
    }

    // Helper method to search for a node in the Trie
    private TrieNode findNode(String word) {
        TrieNode current = root;

// Traverse through each character in the word
        for (char ch : word.toCharArray()) {
            int index = ch - 'a'; // Calculate index based on character's ASCII value
            current = current.children[index];
            if (current == null) {
                return null; // Prefix not found
            }
        }

        return current;
    }

    // Helper method to recursively suggest words
    private int suggestWordsHelper(String prefix, TrieNode node, String originalWord) {
        if (node.isEndOfWord) {
// Check if the suggested word is the same as the original word
            if (originalWord.equals(prefix)) {
                return 3; // Return code 3 if the suggested word is the same as the original word
            }
            System.out.println(prefix); // Print the suggested word
            return 0; // Return code 0 for successful suggestion
        }

// Iterate through each child node
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                int x = suggestWordsHelper(prefix + node.children[i].data, node.children[i], originalWord); // Recursively suggest words
            }
        }
        return 0; // Return code 0 for successful suggestion
    }
}

// Main class for city name completion functionality
public class CityNameCompletion {
    public static void main(String[] args) {
// Define the prefix to search for
        String searchTitle = "win";
        System.out.println("User Entered word is : " + searchTitle);

// Initialize a Trie
        Trie trie = new Trie();

// Get the current directory path
        Path currentPath = Paths.get(System.getProperty("user.dir"));

// Define the path to the Excel file containing city names
        Path dirPath = Paths.get(currentPath.toString(), "assets");
        String filePath = dirPath.toString() + "/Book1.xlsx";

// Create a list to store file paths
        List<String> filePaths = new ArrayList<>();
        filePaths.add(filePath);

// Read data from Excel file and insert city names into the Trie
        List<Map<String, String>> products = FetchDataFromExcel.readData(filePaths);
        for (Map<String, String> product : products) {
            String title = product.get("City");
            String[] titleWords = title.split("\\s+");
            for (String titleWord : titleWords) {
// Remove non-alphabetic characters and convert to lowercase
                titleWord = titleWord.replaceAll("[^a-zA-Z]", "").toLowerCase();
                if (!titleWord.isEmpty()) {
                    trie.insert(titleWord); // Insert the word into the Trie
                }
            }
        }

// Suggest words based on the prefix and print the result
        int x = trie.suggestWords(searchTitle, searchTitle);
        System.out.println(x);
    }
}