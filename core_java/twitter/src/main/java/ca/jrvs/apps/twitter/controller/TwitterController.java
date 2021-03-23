package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller {

    private static final String COORD_SEP = ":";
    private static final String COMMA = ",";

    private final Service service;

    @Autowired
    public TwitterController(Service service) {
        this.service = service;
    }

    /**
     * Parse user argument and post a tweet by calling service classes
     *
     * @param args
     * @return a posted tweet
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public Tweet postTweet(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException(
                    "USAGE: TwitterCLIApp post \"tweet message\" \"latitude:longitude\"");
        }

        String message = args[1];
        String[] latLon = args[2].split(COORD_SEP);

        if (latLon.length != 2) {
            throw new IllegalArgumentException(
                    "ERROR: Latitude and longitude formatted incorrectly. Proper format is \"latitude:longitude\"");
        }

        Double lat, lon;
        try {
            lat = Double.valueOf(latLon[0]);
            lon = Double.valueOf(latLon[1]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ERROR: Latitude and longitude must both be numbers");
        }
        return service.postTweet(TweetUtil.buildTweet(message, lat, lon));
    }

    /**
     * Parse user argument and search a tweet by calling service classes
     *
     * @param args
     * @return a tweet
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public Tweet showTweet(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException(
                    "USAGE: TwitterCLIApp show \"id\"");
        }

        String id = args[1];

        return service.showTweet(id, new String[]{});
    }

    /**
     * Parse user argument and delete tweets by calling service classes
     *
     * @param args
     * @return a list of deleted tweets
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public List<Tweet> deleteTweet(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException(
                    "USAGE: TwitterCLIApp delete \"id1,id2,id3...\"");
        }

        String idsString = args[1];
        String[] ids = idsString.split(COMMA);

        return service.deleteTweets(ids);
    }
}