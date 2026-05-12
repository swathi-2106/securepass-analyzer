package com.securepass.analyzer.service;

import com.securepass.analyzer.datastructures.Trie;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DictionaryPatternService {

    private final Trie dictionaryTrie = new Trie();

    @PostConstruct
    void loadDictionaryWords() {
        List.of(
                "password", "admin", "welcome", "secure", "login", "user", "root",
                "system", "secret", "company", "spring", "java", "mysql", "security",
                "cyber", "hacker", "guest", "student", "college", "project")
                .forEach(dictionaryTrie::insert);
    }

    public boolean containsPredictablePattern(String password) {
        return dictionaryTrie.containsDictionaryPattern(password);
    }
}
