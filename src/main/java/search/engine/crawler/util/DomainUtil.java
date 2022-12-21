package search.engine.crawler.util;

public class DomainUtil {

    public static String getDomainFromUrl(String url) {

        int slashCount = 0;
        int lastSlash = 0;
        for (int i = 0; i < url.length(); i++) {
            if (url.charAt(i) == '/') {
                slashCount++;
            }
            if (slashCount == 3) {
                lastSlash = i;
                break;
            }
        }
        if (slashCount == 3) return url.substring(0, lastSlash);
        return url;
    }
}
