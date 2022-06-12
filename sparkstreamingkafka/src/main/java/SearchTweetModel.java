public class SearchTweetModel {
    private String tweet;
    private String retweet_count;

    public SearchTweetModel(String tweet, String retweet_count) {
        this.tweet = tweet;
        this.retweet_count = retweet_count;
    }

    public SearchTweetModel() {
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(String retweet_count) {
        this.retweet_count = retweet_count;
    }
}
