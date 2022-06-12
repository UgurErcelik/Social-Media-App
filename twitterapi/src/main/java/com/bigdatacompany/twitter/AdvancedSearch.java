package com.bigdatacompany.twitter;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;

public class AdvancedSearch {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient(Constants.MONGO_HOST,Constants.MONGO_PORT);
        MongoDatabase twitterDB = mongoClient.getDatabase("twitterDB");
        MongoCollection<Document> searchCollection = twitterDB.getCollection("search");


        ConfigurationBuilder cb = ConfigurationTwitter.getConfig();

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        ArrayList<Status> tweets = ConfigurationTwitter.getAdvancedSearch(twitter);

        /*
        Query query = new Query("Türkiye");
        int numberOfTweets = 5000;
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

         */

        for(int i = 0; i < tweets.size(); i++) {

            Status st = (Status) tweets.get(i);
            //System.out.println(st.getText());
            //System.out.println("Tarih : " + st.getCreatedAt() + ", Tweet: " + st.getText() + " User: " + st.getUser().getScreenName());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("screen_name", st.getUser().getScreenName());
            jsonObject.put("tweet", st.getText());
            jsonObject.put("create_date", st.getCreatedAt().getTime());
            jsonObject.put("followers_count", st.getUser().getFollowersCount());
            jsonObject.put("friends_count", st.getUser().getFriendsCount());
            jsonObject.put("description", st.getUser().getDescription());
            jsonObject.put("favourite_count", st.getFavoriteCount());
            jsonObject.put("retweet_count", st.getRetweetCount());

            String email = st.getUser().getEmail();

            if (email != null) {
                jsonObject.put("email", email);
            }
            System.out.println(jsonObject.toString());
            searchCollection.insertOne(Document.parse(jsonObject.toString()));

        }



    }
}
