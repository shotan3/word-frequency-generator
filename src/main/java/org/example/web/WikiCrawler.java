package org.example.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

import static org.example.consts.Constants.ALREADY_VISITED_PAGES_FILE;
import static org.example.data.TextParser.analyzeText;
import static org.example.file.FileUtils.loadDataIntoSet;
import static org.example.file.FileUtils.saveSetToFile;

public final class WikiCrawler {
    public static final String BASE_URL = "https://en.wikipedia.org/wiki";

    public static final int MAX_DEPTH = 10000;

    private static final Set<String> alreadyVisitedURLs;

    static {
        alreadyVisitedURLs = loadDataIntoSet(ALREADY_VISITED_PAGES_FILE);
    }

    //If it's not first run it will actually visit MAX_DEPTH - 1 links
    public static void crawlWikipedia() throws IOException {
        String urlString = BASE_URL.concat("/Wikipedia");
        Stack<String> urlStack = new Stack<>();
        urlStack.push(urlString);
        int depth = 0;

        while (!urlStack.isEmpty() && depth != MAX_DEPTH) {
            String currentURL = urlStack.pop();
            Document document = Jsoup.connect(currentURL).get();
            List<String> links = getLinks(document);
            for (String link : links) {
                if (!alreadyVisitedURLs.contains(link)) {
                    urlStack.add(link);
                    alreadyVisitedURLs.add(currentURL);
                }
            }

            String text = getText(document);
            analyzeText(text);
            depth++;
        }
        saveSetToFile(ALREADY_VISITED_PAGES_FILE, alreadyVisitedURLs);
    }

    public static List<String> getLinks(Document document) {
        List<String> result = new ArrayList<>();
        Elements links = document.select("a");
        for (Element link : links) {
            String href = link.attr("abs:href");
            if (passesFilter(href)) {
                result.add(href);
            }
        }
        return result;
    }


    public static String getText(Document document) {
        return document.select("#mw-content-text div.mw-parser-output p").text();
    }


    private static boolean passesFilter(String href) {
        return href.startsWith(BASE_URL) &&
                href.lastIndexOf(":") < href.indexOf("wiki") &&
                !href.contains("Main_Page") && !
                href.contains("#");
    }
}
