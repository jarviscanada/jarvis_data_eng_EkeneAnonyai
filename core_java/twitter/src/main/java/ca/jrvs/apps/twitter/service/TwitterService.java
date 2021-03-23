package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class TwitterService implements Service {

    private final CrdDao dao;

    @Autowired
    public TwitterService(CrdDao dao) {
        this.dao = dao;
    }

    /**
     * Validate and post a user input Tweet
     *
     * @param tweet tweet to be created
     * @return created tweet
     * @throws IllegalArgumentException if text exceed max number of allowed characters or lat/long
     *                                  out of range
     */
    @Override
    public Tweet postTweet(Tweet tweet) {
        validateTweet(tweet);

        return (Tweet) dao.create(tweet);
    }

    /**
     * Search a tweet by ID
     *
     * @param id     tweet id
     * @param fields set fields not in the list to null
     * @return Tweet object which is returned by the Twitter API
     * @throws IllegalArgumentException if id or fields param is invalid
     */
    @Override
    public Tweet showTweet(String id, String[] fields) {
        validateId(id);
        return (Tweet) dao.findById(id);
    }

    /**
     * Delete Tweet(s) by id(s).
     *
     * @param ids tweet IDs which will be deleted
     * @return A list of Tweets
     * @throws IllegalArgumentException if one of the IDs is invalid.
     */
    @Override
    public List<Tweet> deleteTweets(String[] ids) {
        Arrays.stream(ids).forEach(s -> validateId(s));
        List<Tweet> deletedTweets = new ArrayList<>();
        Arrays.stream(ids).forEach(s -> deletedTweets.add((Tweet) dao.deleteById(s)));
        return deletedTweets;
    }

    private void validateTweet(Tweet tweet) throws IllegalArgumentException {
        if (tweet.getText().length() > 140) {
            throw new IllegalArgumentException("Tweet text cannot be longer than 140 characters");
        } else if (tweet.getCoordinates().getCoordinates().get(0) > 180
                || tweet.getCoordinates().getCoordinates().get(0) < -180) {
            throw new IllegalArgumentException("Longitude must be in range [-180, 180]");
        } else if (tweet.getCoordinates().getCoordinates().get(1) > 90
                || tweet.getCoordinates().getCoordinates().get(0) < -90) {
            throw new IllegalArgumentException("Latitude must be in range [-90, 90]");
        }
    }

    private void validateId(String id) throws IllegalArgumentException {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ID " + id + " is invalid");
        }
    }
}