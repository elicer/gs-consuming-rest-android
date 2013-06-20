package org.hello;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.os.Bundle;

public class HelloActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String url = "http://search.twitter.com/search.json?q={query}";

		String queryParameter = "@gopivotal";

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		TwitterSearchResults results = restTemplate.getForObject(url, TwitterSearchResults.class, queryParameter);
	}

}
