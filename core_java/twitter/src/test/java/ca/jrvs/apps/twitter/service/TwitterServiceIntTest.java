package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterServiceIntTest {

    static final Logger logger = LoggerFactory.getLogger(TwitterServiceIntTest.class);
    private TwitterService service;
    private Tweet testTweet;

    @Before
    public void setup() {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        logger.debug(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
                tokenSecret);
        CrdDao dao = new TwitterDao(httpHelper);
        service = new TwitterService(dao);

        String hashtag = "#abc";
        String text = "some text " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        Tweet tweet = TweetUtil.buildTweet(text, lat, lon);
        testTweet = service.postTweet(tweet);
    }

    @After
    public void tearDown() {
        try {
            service.deleteTweets(new String[]{testTweet.getId_str()});
        } catch (RuntimeException ex) {
            logger.debug("Test tweet already deleted");
        }
    }

    @Test
    public void postTweet() {
        String hashtag = "#abc";
        String text = "some text " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        Tweet tweet = TweetUtil.buildTweet(text, lat, lon);

        Tweet postedTweet = service.postTweet(tweet);

        assertEquals(text, postedTweet.getText());

        assertNotNull(postedTweet.getCoordinates());
        assertEquals(2, postedTweet.getCoordinates().getCoordinates().size());
        assertEquals(lon, postedTweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, postedTweet.getCoordinates().getCoordinates().get(1));

        assertTrue(hashtag.contains(postedTweet.getEntities().getHashtags().get(0).getText()));

        String malformedText = "";
        for (int i = 0; i < 140; i++) {
            malformedText = malformedText + "1";
        }
        malformedText = malformedText + " " + System.currentTimeMillis();
        Tweet malformedTweet = TweetUtil.buildTweet(malformedText, lat, lon);

        try {
            service.postTweet(malformedTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        Double malformedLat = 600d;
        malformedTweet = TweetUtil.buildTweet(text, malformedLat, lon);

        try {
            service.postTweet(malformedTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        Double malformedLon = -600d;
        malformedTweet = TweetUtil.buildTweet(text, lat, malformedLon);

        try {
            service.postTweet(malformedTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void showTweet() {
        Tweet fetchedTweet = service.showTweet(testTweet.getId_str(), new String[]{});

        assertEquals(testTweet, fetchedTweet);

        String malformedId = "applesauce";
        try {
            service.showTweet(malformedId, new String[]{});
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteTweets() {
        String[] deleteIds = new String[]{testTweet.getId_str()};
        List<Tweet> deletedTweets = service.deleteTweets(deleteIds);

        assertEquals(testTweet, deletedTweets.get(0));

        try {
            service.deleteTweets(deleteIds);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        String[] malformedIds = new String[]{"applesauce"};
        try {
            service.deleteTweets(malformedIds);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }
}