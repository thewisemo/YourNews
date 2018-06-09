package com.example.android.yournews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving story data from Guardian.
 */
public final class QueryUtils {

    // Tag for the log messages
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian data set and return an {@link Story} object to represent a single story.
     */
    public static List<Story> fetchStoryData(String requestUrl) {

        Log.d(LOG_TAG,"This is the fetchStoryData QueryUtils Method");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Story> story = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return story;
    }

    /**
            * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Story} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Story> extractFeatureFromJson(String storyJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(storyJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding stories to
        List<Story> stories = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(storyJSON);
            // Create a JSONObject from the base JSON response string
            JSONObject rootJson = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of features (or stories).
            JSONArray storyArray = rootJson.getJSONArray("results");

            // For each earthquake in the storyArray, create an {@link Earthquake} object
            for (int i = 0; i < storyArray.length(); i++) {

                // Get a single story at position i within the list of stories
                JSONObject currentStory = storyArray.getJSONObject(i);

                // Extract the value for the key called "sectionName"
                String sectionName = currentStory.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String date = currentStory.getString("webPublicationDate");

                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.
                JSONObject properties = currentStory.getJSONObject("fields");

                // Extract the value for the key called "headline"
                String headline = properties.getString("headline");

                // Extract the value for the key called "trailText"
                String trailText = properties.getString("trailText");

                // Extract the value for the key called "shortUrl"
                String shortUrl = properties.getString("shortUrl");

                // TODO retrieve the thumbnail image using Glid support library in the StoryAdapter
                // Extract the value for the key called "thumbnail"
                String thumbnailUrl = properties.getString("thumbnail");

                // Create a new {@link Story} object with the: Section name, Date, Time, Headline, Trial text, Short URL
                // and url from the JSON response.
                Story story = new Story(sectionName, date, headline, trailText, shortUrl, thumbnailUrl);

                // Add the new {@link Story} to the list of stories.
                stories.add(story);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the story JSON results", e);
        }

        // Return the list of stories
        return stories;
    }

}
