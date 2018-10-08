package web.htmlService;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.utils.MongoService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

@Component
public class HtmlParser {

    @Autowired
    private MongoService mongoService;

    Logger logger = LoggerFactory.getLogger(HtmlParser.class);

    private final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";
    private final String MVIDEO_URL = "https://www.mvideo.ru/playstation/ps4-igry-4331/f/category=igry-dlya-playstation-4-ps4-4343/page=";

    public Elements parseSite(String url) throws IOException {
        Document document = Jsoup.connect(url)
                .timeout(100000)
                .userAgent(USER_AGENT).get();

        Elements games = document.body().getElementsByClass("sel-product-tile-title");

        ArrayList<String> listGames = new ArrayList<>();
        games.forEach(it -> listGames.add(it.attr("data-product-info")));

        //listGames.forEach(it -> mongoService.checkPriceAndUpdate(it));
        listGames.forEach(it -> mongoService.addGame(it));
        return games;
    }

    public Elements parseAllContentInSite() throws IOException {
        Elements games = parseSite(MVIDEO_URL + 1);
        for (int i = 2; i < 38; i++){
            games.addAll(parseSite(MVIDEO_URL + i));
        }
        return games;
    }


}
