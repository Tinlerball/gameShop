package web.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
public class MongoService{
    Logger logger = LoggerFactory.getLogger(MongoService.class);

    private DBCollection table;

    private Gson gson = new Gson();

    public MongoService() {
        try {
            MongoClient mongoClient = new MongoClient("localhost", Integer.valueOf("27017"));

            DB db = mongoClient.getDB("gameDb");
            db.authenticate("mongo", "mongo".toCharArray());
            table = db.getCollection("games");
        } catch (UnknownHostException e) {
            logger.error("Don't connect!");
        }
    }

    public void addGame(String game) {
            if (findOneGameViaProductID(game) == null){
                        table.insert((DBObject) JSON.parse(game));
                        logger.info("Add game: " + game);
        }
    }

    public void checkPriceAndUpdate(String game){
        DBObject dbObject = findOneGameViaProductID(game);
            if (!dbObject.get("productPriceLocal").equals(getGameFromJSON(game).get("productPriceLocal").getAsString())){
                logger.info("Change price for game: " + game);
                table.update(dbObject,
                        (DBObject) JSON.parse(game));
        }
    }

    public DBObject findOneGameViaProductID(String game){
        return table.findOne((DBObject) JSON.parse("{ \"productId\" : " +
                gson.fromJson(new JsonParser().parse(game), JsonObject.class).get("productId")
                +"}"));
    }

    public JsonObject getGameFromJSON(String game){
        return gson.fromJson(new JsonParser().parse(game), JsonObject.class);
    }

}
