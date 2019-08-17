import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.CSVUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {

    private String inputURL;

    private Set<String> links;

    private static final String DATA_PATH = "src/main/resources/crawler/data.csv";

    public Extractor(String baseUrl) {
        this.inputURL = baseUrl;
        this.links = new HashSet<String>();
    }

    public void getPageLinks(String URL) {
        if (!URL.equals("") && !links.contains(URL)) {
            try {
                links.add(URL);
                //System.out.println(this.getBaseUrl());
                Document document = Jsoup.connect(URL).get();
                Elements otherLinks = document.select("a[href^=\"" + this.getBaseUrl() + "\"][href$=\".html\"]");

                for (Element page: otherLinks) {
                    if (page.attr("abs:href").equals(URL)) continue;
                    System.out.println(page.attr("abs:href"));
                    getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void getPageLinks() {
        this.getPageLinks(this.inputURL);
    }

    public void getArticles() throws IOException{

        this.createCsvFile();

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DATA_PATH), "UTF8"));

        CSVUtils.writeLine(writer, Arrays.asList("URL", "Title", "Author", "Date"));

        links.forEach(
                link -> {
                    Document document;
                    try {
                        document = Jsoup.connect(link).get();

                        List<String> titles = this.getElementByCssSelector(document, "h1.title_news_detail.mb10");
                        List<String> authors = this.getElementByCssSelector(document, "p[style=\"text-align:right;\"] > strong");
                        List<String> dates = this.getElementByCssSelector(document, "span.time.left");

                        CSVUtils.writeLine(writer,
                                Arrays.asList(link, formatOutput(titles.toString()), formatOutput(authors.toString()), formatOutput(dates.toString())));

                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
        );
        writer.flush();
        writer.close();
    }

    public void createCsvFile() throws IOException{
        FileUtils.touch(new File(DATA_PATH));
    }

    public void crawlData() throws IOException{
        this.getPageLinks();
        this.getArticles();
    }

    private List<String> getElementByCssSelector(Document document, String cssSelector) {
        List<String> texts = new ArrayList<>();
        Elements elements = document.select(cssSelector);
        for (Element element: elements) {
            texts.add(element.text());
        }
        return texts;
    }

    private String getBaseUrl() {
        Pattern pattern = Pattern.compile("^(http:\\/\\/|https:\\/\\/)?([A-Za-z0-9_]+[.])*([A-Za-z0-9_]+\\/)");
        Matcher matcher = pattern.matcher(this.inputURL);
        matcher.find();
        return matcher.group();
    }

    private String formatOutput(String result) {
        return result.replaceAll(","," | ");
    }
}
