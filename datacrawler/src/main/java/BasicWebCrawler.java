import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.Executor;

public class BasicWebCrawler {

    private Extractor extractor;

    public BasicWebCrawler(Extractor extractor) {
        this.extractor = extractor;
    }

    public static void main(String args[]) throws IOException {

        Extractor sampleExtractor = new Extractor("https://vnexpress.net/");

        BasicWebCrawler basicWebCrawler = new BasicWebCrawler(sampleExtractor);
        basicWebCrawler.extractor.crawlData();
    }
}
