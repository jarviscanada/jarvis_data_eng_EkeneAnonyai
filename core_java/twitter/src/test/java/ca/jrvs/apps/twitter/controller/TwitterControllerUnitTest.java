package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

    @Mock
    Service service;

    @InjectMocks
    TwitterController controller;

    @Test
    public void postTweet() {
        when(service.postTweet(any())).thenReturn(new Tweet());
        Tweet tweet = controller.postTweet(new String[]{"post", "test message", "12:21"});
        assertNotNull(tweet);
    }

    @Test
    public void showTweet() {
        when(service.showTweet(anyString(), any())).thenReturn(new Tweet());
        Tweet tweet = controller.showTweet(new String[]{"show", "2533412"});
        assertNotNull(tweet);
    }

    @Test
    public void deleteTweet() {
        when(service.deleteTweets(any())).thenReturn(Arrays.asList(new Tweet(), new Tweet()));
        List<Tweet> tweets = controller.deleteTweet(new String[]{"delete", "1234,5678"});
        assertNotNull(tweets);
        assertNotNull(tweets.get(0));
        assertNotNull(tweets.get(1));
    }
}
