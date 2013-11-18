package gr.vtrip.social.twitter.service;

import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

@ManagedBean
@RequestScoped
public class TwitterService {

	private static final Logger logger = Logger.getLogger(TwitterService.class
			.getName());
	private String twitter_consumer_key = "";
	private String twitter_consumer_secret = "";
	private String access_token = "";
	private String access_token_secret = "";

	List<Status> tweets = null;

	public List<Status> getTweets() {
		return tweets;
	}

	public void setTweets(List<Status> tweets) {
		this.tweets = tweets;
	}

	@PostConstruct
	public void prepare() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		PortletRequest portletRequest = (PortletRequest) externalContext
				.getRequest();
		PortletPreferences portletPreferences = portletRequest.getPreferences();

		try {
			Enumeration<String> preferenceNames = portletPreferences.getNames();

			twitter_consumer_key = portletPreferences.getValue(
					"twitter_consumer_key", "");
			twitter_consumer_secret = portletPreferences.getValue(
					"twitter_consumer_secret", "");
			access_token = portletPreferences.getValue("access_token", "");
			access_token_secret = portletPreferences.getValue(
					"access_token_secret", "");

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		try {
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(twitter_consumer_key,
					twitter_consumer_secret);
			twitter.setOAuthAccessToken(new AccessToken(
					access_token,
					access_token_secret));

			tweets = twitter.getUserTimeline("vtrip", new Paging(1, Integer.parseInt(portletPreferences.getValue("number_of_tweets", "5"))));

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
}