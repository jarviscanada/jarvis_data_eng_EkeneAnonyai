package ca.jrvs.apps.twitter.util;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;

public class TweetUtil {

    public static Tweet buildTweet(String text, Double lat, Double lon) {
        Tweet tweet = new Tweet();
        tweet.setText(text);
        Coordinates coordinates = new Coordinates();
        List<Double> lonLat = new ArrayList<>();
        lonLat.add(lon);
        lonLat.add(lat);
        coordinates.setCoordinates(lonLat);
        tweet.setCoordinates(coordinates);
        return tweet;
    }
}
