package ca.jrvs.apps.example;

import com.google.gdata.util.common.base.PercentEscaper;
import java.util.Arrays;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterApiTest {
    static final Logger logger = LoggerFactory.getLogger(TwitterApiTest.class);

    private static String CONSUMER_KEY = System.getenv("consumerKey");
    private static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private static String ACCESS_TOKEN = System.getenv("accessToken");
    private static String TOKEN_SECRET = System.getenv("tokenSecret");

    public static void main(String[] args) throws Exception {
        //setup oauth
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.setTokenWithSecret(ACCESS_TOKEN, TOKEN_SECRET);

        // create a HTTP GET request
        String status = "Today is a good day";
        PercentEscaper percentEscaper = new PercentEscaper("", false);
        HttpPost request = new HttpPost(
                "https://api.twitter.com/1.1/statuses/update.json?status=" + percentEscaper.escape(status));

        //sign the request(add headers)
        consumer.sign(request);

        logger.debug("Http Request Headers:");
        Arrays.stream(request.getAllHeaders()).forEach(s -> logger.debug(s.toString()));

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        logger.debug(EntityUtils.toString(response.getEntity()));

    }

}