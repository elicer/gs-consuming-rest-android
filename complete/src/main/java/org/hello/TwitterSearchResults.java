package org.hello;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterSearchResults {

	private List<Tweet> results;

	public List<Tweet> getResults() {
		return results;
	}

	public void setResults(List<Tweet> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Tweet tweet : results) {
			sb.append(tweet.getFromUser()).append(": ").append(tweet.getText()).append("\n");
		}
		return sb.toString();
	}

}