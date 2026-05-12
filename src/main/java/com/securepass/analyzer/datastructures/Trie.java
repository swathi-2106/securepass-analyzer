package com.securepass.analyzer.datastructures;

import java.util.HashMap;
import java.util.Map;

public class Trie {

    private final TrieNode root = new TrieNode();

    public void insert(String word) {
        if (word == null || word.isBlank()) {
            return;
        }

        TrieNode current = root;
        for (char character : normalize(word).toCharArray()) {
            current = current.children.computeIfAbsent(character, ignored -> new TrieNode());
        }
        current.word = true;
    }

    public boolean search(String word) {
        TrieNode node = findNode(normalize(word));
        return node != null && node.word;
    }

    public boolean startsWith(String prefix) {
        return findNode(normalize(prefix)) != null;
    }

    public boolean containsDictionaryPattern(String password) {
        String normalized = normalize(password);
        for (int start = 0; start < normalized.length(); start++) {
            TrieNode current = root;
            for (int end = start; end < normalized.length(); end++) {
                current = current.children.get(normalized.charAt(end));
                if (current == null) {
                    break;
                }
                if (current.word && end - start >= 3) {
                    return true;
                }
            }
        }
        return false;
    }

    private TrieNode findNode(String value) {
        if (value == null) {
            return null;
        }

        TrieNode current = root;
        for (char character : value.toCharArray()) {
            current = current.children.get(character);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private String normalize(String value) {
        return value == null ? "" : value.toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    private static class TrieNode {
        private final Map<Character, TrieNode> children = new HashMap<>();
        private boolean word;
    }
}
