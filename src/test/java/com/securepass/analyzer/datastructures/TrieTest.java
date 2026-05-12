package com.securepass.analyzer.datastructures;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TrieTest {

    @Test
    void supportsInsertSearchAndPrefixMatching() {
        Trie trie = new Trie();
        trie.insert("admin");
        trie.insert("password");

        assertThat(trie.search("admin")).isTrue();
        assertThat(trie.search("adm")).isFalse();
        assertThat(trie.startsWith("pass")).isTrue();
    }

    @Test
    void detectsDictionaryPatternInsideDecoratedPassword() {
        Trie trie = new Trie();
        trie.insert("welcome");

        assertThat(trie.containsDictionaryPattern("welcome1")).isTrue();
        assertThat(trie.containsDictionaryPattern("W3lc0me")).isFalse();
    }
}
