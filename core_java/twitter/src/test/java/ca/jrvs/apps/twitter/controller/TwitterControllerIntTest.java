package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterControllerIntTest {

    static final Logger logger = LoggerFactory.getLogger(TwitterControllerIntTest.class);
    private TwitterController controller;
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
        Service service = new TwitterService(dao);
        controller = new TwitterController(service);

        String[] args = new String[]{"post", "test text" + System.currentTimeMillis(), "-1:1"};
        testTweet = controller.postTweet(args);
    }

    @After
    public void tearDown() {
        try {
            controller.deleteTweet(new String[]{testTweet.getId_str()});
        } catch (RuntimeException ex) {
            logger.debug("Test tweet already deleted");
        }
    }

    @Test
    public void postTweet() {
        String[] args = new String[]{"post", "test text" + System.currentTimeMillis(), "-1:1"};

        Tweet postedTweet = controller.postTweet(args);

        assertEquals(args[1], postedTweet.getText());

        assertNotNull(postedTweet.getCoordinates());
        assertEquals(2, postedTweet.getCoordinates().getCoordinates().size());
        assertEquals(Double.valueOf(1), postedTweet.getCoordinates().getCoordinates().get(0));
        assertEquals(Double.valueOf(-1), postedTweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void showTweet() {
        String[] args = new String[]{"show", testTweet.getId_str()};
        Tweet fetchedTweet = controller.showTweet(args);

        assertEquals(testTweet, fetchedTweet);
    }

    @Test
    public void deleteTweet() {

        String[] args = new String[]{"delete", testTweet.getId_str()};
        List<Tweet> deletedTweets = controller.deleteTweet(args);

        assertEquals(testTweet, deletedTweets.get(0));

        try {
            controller.deleteTweet(args);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }
}
