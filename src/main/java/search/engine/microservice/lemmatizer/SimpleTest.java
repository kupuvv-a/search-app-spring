package search.engine.microservice.lemmatizer;

import java.io.IOException;
import java.util.Map;

public class SimpleTest {
    public static void main(String[] args) throws IOException {
        final String text = "Повторное появление леопарда в Осетии позволяет предположить, что леопард постоянно обитает в некоторых районах Северного Кавказа.";
        final LemmaFinder lemmaFinder = LemmaFinder.getInstance();
        final Map<String, Integer> lemmaMap = lemmaFinder.collectLemmas(text);
        lemmaMap.forEach((k, v) -> System.out.println(k + " : " + v));
    }
}
