package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

    @Mock
    CrdDao dao;

    @InjectMocks
    TwitterService service;

    @Test
    public void postTweet() {
        when(dao.create(any())).thenReturn(new Tweet());
        Tweet tweet = service.postTweet(TweetUtil.buildTweet("Test", 50.0, 0.0));
        assertNotNull(tweet);

        String malformedText = "";
        for (int i = 0; i < 140; i++) {
            malformedText = malformedText + "1";
        }
        malformedText = malformedText + " " + System.currentTimeMillis();
        Tweet malformedTweet = TweetUtil.buildTweet(malformedText, 50.0, 0.0);

        try {
            service.postTweet(malformedTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        Double malformedLat = 600d;
        malformedTweet = TweetUtil.buildTweet("Test", malformedLat, 0.0);

        try {
            service.postTweet(malformedTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        Double malformedLon = -600d;
        malformedTweet = TweetUtil.buildTweet("Test", 50.0, malformedLon);

        try {
            service.postTweet(malformedTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void showTweet() {
        when(dao.findById(anyString())).thenReturn(new Tweet());
        Tweet tweet = service.showTweet("123456789", new String[]{});
        assertNotNull(tweet);

        try {
            service.showTweet("applesauce", new String[]{});
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteTweets() {
        when(dao.deleteById(anyString())).thenReturn(new Tweet());
        List<Tweet> tweets = service.deleteTweets(new String[]{"1234", "5678"});
        assertNotNull(tweets);
        assertNotNull(tweets.get(0));
        assertNotNull(tweets.get(1));

        try {
            service.deleteTweets(new String[]{"1234", "applesauce"});
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }
}
