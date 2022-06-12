package com.bigdatacompany.twitter;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bson.Document;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Properties;

public class KafkaProducerApp {
    public static void main(String[] args) {


        Properties config = new Properties();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,new StringSerializer().getClass().getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,new StringSerializer().getClass().getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);


/*
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("i4TittN7NKNXcMHdJxLs6DbAZ");
        cb.setOAuthConsumerSecret("9eyxdoz6GCGm8t4gnxKcOzxzE8jG40e7jgIQRldmmcyHw7uXAY");
        cb.setOAuthAccessToken("337343710-wLNbBAhDp0BJKNMTmsjyqzRYMF11J2abeWiy9Y8r");
        cb.setOAuthAccessTokenSecret("aVaC77Z4NZupgofce3k4Cr5Y8grQLf8vCMZzhd2VK6wQA");*/

        ConfigurationBuilder cb = ConfigurationTwitter.getConfig();

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        ArrayList<Status> tweets = ConfigurationTwitter.getAdvancedSearch(twitter);

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
            ProducerRecord<String, String> rec = new ProducerRecord<String,String>("twitter-search", jsonObject.toString());
            producer.send(rec);

        }

    }
}
