package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelperTest;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterDaoIntTest {

    static final Logger logger = LoggerFactory.getLogger(TwitterHttpHelperTest.class);
    private TwitterDao dao;
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
        this.dao = new TwitterDao(httpHelper);

        String hashtag = "#abc";
        String text = "some text " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        testTweet = dao.create(TweetUtil.buildTweet(text, lat, lon));
    }

    @After
    public void tearDown() {
        try {
            dao.deleteById(testTweet.getId_str());
        } catch (RuntimeException ex) {
            logger.debug("Test tweet already deleted");
        }
    }

    @Test
    public void create() throws Exception {
        String hashtag = "#abc";
        String text = "some text " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        Tweet tweet = TweetUtil.buildTweet(text, lat, lon);

        Tweet postedTweet = dao.create(tweet);

        assertEquals(text, postedTweet.getText());

        assertNotNull(postedTweet.getCoordinates());
        assertEquals(2, postedTweet.getCoordinates().getCoordinates().size());
        assertEquals(lon, postedTweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, postedTweet.getCoordinates().getCoordinates().get(1));

        assertTrue(hashtag.contains(postedTweet.getEntities().getHashtags().get(0).getText()));
    }

    @Test
    public void findById() throws Exception {
        Tweet fetchedTweet = dao.findById(testTweet.getId_str());

        assertEquals(testTweet, fetchedTweet);
    }

    @Test
    public void deleteById() throws Exception {
        Tweet deletedTweet = dao.deleteById(testTweet.getId_str());

        assertEquals(testTweet, deletedTweet);

        try {
            dao.findById(testTweet.getId_str());
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }
}