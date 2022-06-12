package com.bigdatacompany.twitter;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;

public class ConfigurationTwitter {

    public static ConfigurationBuilder getConfig()
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("i4TittN7NKNXcMHdJxLs6DbAZ");
        cb.setOAuthConsumerSecret("9eyxdoz6GCGm8t4gnxKcOzxzE8jG40e7jgIQRldmmcyHw7uXAY");
        cb.setOAuthAccessToken("337343710-wLNbBAhDp0BJKNMTmsjyqzRYMF11J2abeWiy9Y8r");
        cb.setOAuthAccessTokenSecret("aVaC77Z4NZupgofce3k4Cr5Y8grQLf8vCMZzhd2VK6wQA");
        return cb;
    }

    public static ArrayList<Status> getAdvancedSearch(Twitter twitter)
    {
        Query query = new Query("Türkiye");
        int numberOfTweets = 100;
        long lastID = Long.MAX_VALUE;
        ArrayList<Status> tweets = new ArrayList<>();
        while(tweets.size() < numberOfTweets)
        {
            if(numberOfTweets - tweets.size() > 100)
            {
                query.setCount(100);
            }
            else
            {
                query.setCount(numberOfTweets - tweets.size());
            }
            try{
                QueryResult result = twitter.search(query);
                tweets.addAll(result.getTweets());
                System.out.println(tweets.size() + " adet toplandı");
                for(Status t : tweets)
                    if(t.getId() < lastID)
                    {
                        lastID = t.getId();
                    }
            } catch (TwitterException e) {
                System.out.println("Couldn't connect :" + e);
            }

            query.setMaxId(lastID - 1);
        }
        return tweets;
    }
}
