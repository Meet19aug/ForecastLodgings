package org.example;

//TrieNode1 class represents a node in the Trie data structure
class NodeOfTrie1 {
    char data;
    boolean isEndOfWord;
    NodeOfTrie1[] children;

    public NodeOfTrie1(char data) {
        this.data = data;
        this.isEndOfWord = false;
        this.children = new NodeOfTrie1[26]; // Assuming only lowercase alphabetical characters
    }
}

//Trie class implements the Trie data structure
class DSTrie {
    private final NodeOfTrie1 rt;

    public DSTrie() {
        rt = new NodeOfTrie1('\0');
    }

    // Insert a word into the Trie
    public void wordInsert(String word) {
        NodeOfTrie1 current = rt;

        for (char ch : word.toCharArray()) {
            int idx = ch - 'a';
            if (current.children[idx] == null) {
                current.children[idx] = new NodeOfTrie1(ch);
            }
            current = current.children[idx];
        }

        current.isEndOfWord = true;
    }

    // Suggest words based on a given prefix
    public int suggestWords(String prefix,String originalword) {
        NodeOfTrie1 prefixNode = nodeFinding(prefix);

        if (prefixNode != null) {
            return suggestWordsHelper(prefix, prefixNode,originalword);
        } else {
            System.out.println("No suggestions for the given prefix.");
            return 1;
        }
    }

    // Helper method to search for a node in the Trie
    private NodeOfTrie1 nodeFinding(String wd) {
        NodeOfTrie1 current = rt;

        for (char ch : wd.toCharArray()) {
            int index = ch - 'a';
            current = current.children[index];
            if (current == null) {
                return null; // Prefix not found
            }
        }

        return current;
    }

    // Helper method to recursively suggest words
    private int suggestWordsHelper(String prefix, NodeOfTrie1 node,String originalWord) {
        if (node.isEndOfWord) {
            if(originalWord==prefix){
                return 3;
            }
            System.out.println(prefix);
            return 0;
        }

        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                int x = suggestWordsHelper(prefix + node.children[i].data, node.children[i],originalWord);
            }
        }
        return 0;
    }
}

public class CityNameCompletion {

}
