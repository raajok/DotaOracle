package com.raajok.api.Dotabuff;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides records from the player's Dotabuff page.
 */
public final class Records {

    private final String URL;

    public Records(int steamID) {
        this.URL = "https://www.dotabuff.com/players/" + steamID + "/records";
    }

    public String author() throws IOException {
        Document doc = Jsoup.connect(URL).get();

        Elements headerTitle = doc.getElementsByClass("header-content-title");
        String text = headerTitle.text();
        text = text.substring(0, text.length() - 7); // remove "Records" word

        return text;
    }

    public List<Record> records() throws IOException {
        Document doc = Jsoup.connect(URL).get();
        String author = this.author();

        Elements records = doc.getElementsByClass("player-records");
        Elements titles = records.get(0).getElementsByClass("title");
        Elements values = records.get(0).getElementsByClass("value");

        ArrayList<Record> recordList = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).text().equals("Best KDA Ratio") || titles.get(i).text().equals("Highest Kill Participation")) {
                continue;
            }

            if (titles.get(i).text().equals(("Longest Match"))) {
                recordList.add(new TimeRecord(titles.get(i).text(), values.get(i).text(), author));
                continue;
            }
            recordList.add(new NumberRecord(titles.get(i).text(), Integer.parseInt(values.get(i).text().replace(",", "")), author));
        }

        return recordList;
    }
}
