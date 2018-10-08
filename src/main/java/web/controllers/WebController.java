package web.controllers;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.htmlService.HtmlParser;

import java.io.IOException;

@RestController
public class WebController {
    Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private HtmlParser htmlParser;

    @GetMapping("/get")
    public String getGames() throws IOException {

        StringBuilder resultArray = new StringBuilder();
        for (Element game: htmlParser.parseAllContentInSite()){
            resultArray.append(game.toString()).append("\n");
        }
        return resultArray.toString();
    }
}
