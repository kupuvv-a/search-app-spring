package search.engine.microservice.lemmatizer;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.*;

public class LemmaFinder {
    private final LuceneMorphology luceneMorphology;
    private static final String WORD_TYPE_REGEX = "\\W\\w&&[^а-яА-Я\\s]";
    private static final String[] particlesNames = new String[]{"МЕЖД", "ПРЕДЛ", "СОЮЗ"};

    public static LemmaFinder getInstance() throws IOException {
        final LuceneMorphology morphology = new RussianLuceneMorphology();
        return new LemmaFinder(morphology);
    }

    private LemmaFinder(final LuceneMorphology luceneMorphology) {
        this.luceneMorphology = luceneMorphology;
    }

    private LemmaFinder() {
        throw new RuntimeException("Disallow construct");
    }

    public Map<String, Integer> collectLemmas(final String text) {
        final String[] words = arrayContainsRussianWords(text);
        final HashMap<String, Integer> lemmas = new HashMap<>();

        for (String word : words) {
            if (word.isBlank()) continue;

            List<String> wordBaseForms = luceneMorphology.getMorphInfo(word);
            if (anyWordBaseBelongToParticle(wordBaseForms)) continue;

            List<String> normalForms = luceneMorphology.getNormalForms(word);
            if (normalForms.isEmpty()) continue;

            final String normalWord = normalForms.get(0);

            if (lemmas.containsKey(normalWord)) lemmas.put(normalWord, lemmas.get(normalWord) + 1);
            else lemmas.put(normalWord, 1);
        }
        return lemmas;
    }

    //todo unused
    public Set<String> getLemmaSet(final String text) {
        final String[] textArray = arrayContainsRussianWords(text);
        final Set<String> lemmaSet = new HashSet<>();
        for (String word : textArray) {
            if (!word.isEmpty() && isCorrectWordForm(word)) {
                final List<String> wordBaseForms = luceneMorphology.getMorphInfo(word);
                if (anyWordBaseBelongToParticle(wordBaseForms)) continue;
                lemmaSet.addAll(luceneMorphology.getNormalForms(word));
            }
        }
        return lemmaSet;
    }

    private boolean anyWordBaseBelongToParticle(final List<String> wordBaseForms) {
        return wordBaseForms.stream().anyMatch(this::hasParticleProperty);
    }

    private boolean hasParticleProperty(final String wordBase) {
        for (String property : particlesNames)
            if (wordBase.toUpperCase().contains(property)) return true;
        return false;
    }

    private String[] arrayContainsRussianWords(String text) {
        return text.toLowerCase(Locale.ROOT)
                .replaceAll("([^а-я\\s])", " ")
                .trim()
                .split("\\s+");
    }

    private boolean isCorrectWordForm(String word) {
        List<String> wordInfo = luceneMorphology.getMorphInfo(word);
        for (String morphInfo : wordInfo)
            if (morphInfo.matches(WORD_TYPE_REGEX)) return false;

        return true;
    }
}
