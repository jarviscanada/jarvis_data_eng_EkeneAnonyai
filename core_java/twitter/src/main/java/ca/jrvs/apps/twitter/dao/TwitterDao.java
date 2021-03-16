package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String> {

    static final Logger logger = LoggerFactory.getLogger(TwitterDao.class);
    // URI Constants
    private static final String API_BASE_URI = "https://api.twitter.com";
    private static final String POST_PATH = "/1.1/statuses/update.json";
    private static final String SHOW_PATH = "/1.1/statuses/show.json";
    private static final String DELETE_PATH = "/1.1/statuses/destroy";
    // URI Symbols
    private static final String QUERY_SYM = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";
    private static final int HTTP_OK = 200;
    private final HttpHelper httpHelper;

    @Autowired
    public TwitterDao(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    /**
     * Create an entity(Tweet) to the underlying storage
     *
     * @param entity entity that to be created
     * @return created entity
     */
    @Override
    public Tweet create(Tweet entity) {
        // Construct the URI
        URI uri = getTweetUri(entity);

        // Execute HTTP request
        HttpResponse response = httpHelper.httpPost(uri);

        // Validate response and deserialize to a Tweet object
        return parseResponseBody(response, HTTP_OK);
    }

    /**
     * Find an entity(Tweet) by its id
     *
     * @param s entity id
     * @return Tweet entity
     */
    @Override
    public Tweet findById(String s) {
        // Construct the URI
        URI uri;
        try {
            uri = new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + s);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid id input", ex);
        }

        // Execute HTTP request
        HttpResponse response = httpHelper.httpGet(uri);

        // Validate response and deserialize to a Tweet object
        return parseResponseBody(response, HTTP_OK);
    }

    /**
     * Delete an entity(Tweet) by its ID
     *
     * @param s of the entity to be deleted
     * @return deleted entity
     */
    @Override
    public Tweet deleteById(String s) {
        // Construct the URI
        URI uri;
        try {
            uri = new URI(API_BASE_URI + DELETE_PATH + "/" + s + ".json");
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid id input", ex);
        }

        // Execute HTTP request
        HttpResponse response = httpHelper.httpPost(uri);

        // Validate response and deserialize to a Tweet object
        return parseResponseBody(response, HTTP_OK);
    }

    Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode) {
        Tweet tweet;

        // Check response status
        int status = response.getStatusLine().getStatusCode();
        if (status != expectedStatusCode) {
            try {
                logger.error(EntityUtils.toString(response.getEntity()));
            } catch (IOException ex) {
                logger.error("Response has no entity");
            }
            throw new RuntimeException("Unexpected HTTP status: " + status);
        }

        if (response.getEntity() == null) {
            throw new RuntimeException("Empty response body");
        }

        // Convert response entity to String
        String jsonStr;
        try {
            jsonStr = EntityUtils.toString(response.getEntity());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert entity to String", ex);
        }

        // Convert JSON String to Tweet object
        try {
            tweet = JsonUtil.toObjectFromJson(jsonStr, Tweet.class);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert JSON string to object", ex);
        }

        return tweet;
    }

    private URI getTweetUri(Tweet tweet) {
        URI uri;
        PercentEscaper percentEscaper = new PercentEscaper("", false);
        try {
            uri = new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL + percentEscaper
                    .escape(tweet.getText()) + AMPERSAND + "long" + EQUAL + tweet.getCoordinates()
                    .getCoordinates().get(0) + AMPERSAND + "lat" + EQUAL + tweet.getCoordinates()
                    .getCoordinates().get(1));
            return uri;
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid input", ex);
        }
    }
}
