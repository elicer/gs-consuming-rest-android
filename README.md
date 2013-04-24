# Getting Started: Consuming REST Services with Spring


This Getting Started guide will walk you through the process of consuming a REST service using Spring for Android's `RestTemplate`.

To help you get started, we've provided an initial project structure for you in GitHub:

```sh
$ git clone https://github.com/springframework-meta/gs-consuming-rest-android.git
```

Before we can consume a RESTful service, there is some initial project setup that is required. Or, you can skip straight to the [fun part]().


## Selecting Dependencies

The sample in this Getting Started Guide will leverage Spring for Android and the Jackson JSON processor. Therefore, you'll need to declare the following library dependencies in your build:

  - org.springframework.android:spring-android-rest-template:1.0.1.RELEASE
  - com.fasterxml.jackson.core:jackson-databind:2.2.0

Click here for details on how to map these dependencies to your specific build tool.


## Creating a Representation Class

With the Android project configured, it's time to create our REST request. Before we can do that though, we need to consider the data we are wanting to consume. 


### Twitter JSON Data

In the case of Twitter search, JSON data is returned which looks like the following:

```json
{
    "completed_in": 0.036,
    "max_id": 326785345083568100,
    "max_id_str": "326785345083568130",
    "next_page": "?page=2&max_id=326785345083568130&q=gopivotal",
    "page": 1,
    "query": "gopivotal",
    "refresh_url": "?since_id=326785345083568130&q=gopivotal",
    "results": [
        {
            "created_at": "Tue, 23 Apr 2013 19:51:12 +0000",
            "from_user": "CetasAnalytics",
            "from_user_id": 353318641,
            "from_user_id_str": "353318641",
            "from_user_name": "Cetas",
            "geo": null,
            "id": 326785345083568100,
            "id_str": "326785345083568130",
            "iso_language_code": "en",
            "metadata": {
                "result_type": "recent"
            },
            "profile_image_url": "http://a0.twimg.com/profile_images/2269920546/p3949ch8l877idajhez6_normal.png",
            "profile_image_url_https": "https://si0.twimg.com/profile_images/2269920546/p3949ch8l877idajhez6_normal.png",
            "source": "&lt;a href=&quot;http://www.hootsuite.com&quot;&gt;HootSuite&lt;/a&gt;",
            "text": "Ready. Set. Go. Pivotal is now launching April 24th. Join the live broadcast at http://t.co/qqfl3zSXN3"
        }        
    ],
    "results_per_page": 15,
    "since_id": 0,
    "since_id_str": "0"
}
```

As you can see, Twitter returns quite a bit of information. Do not worry if some of this data appears unfamiliar. For the purposes of this guide, we are only going to concern ourselves with a few parts of it.

The `from_user` field represents the user who posted the tweet, and `text` is the actual text of the tweet. To model the tweet representation, we’ll create two representation classes which define these fields. In this example, we are making use of a few Jackson annotations. Jackson is a powerful JSON processor for Java, and can be utilized within Spring.

### Twitter Search Results

The first representation class defines a single property which contains the list of tweets from the search. The `@JsonIgnoreProperties` annotation says to ignore all the other fields in the JSON response data. We are only concerned with the tweets.

```java
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

}
```

### Tweet

The second representation class is for each individual tweet. Again you see `@JsonIgnoreProperties` being used, and additionally, the `@JsonProperty` annotation allows allows us to map specific fields in the JSON data to fields in the representational class which have different names.

```java
package org.hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {

	@JsonProperty("from_user")
	private String fromUser;

	private String text;

	public String getFromUser() {
		return fromUser;
	}
	
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

}
```

## Invoking REST services with the RestTemplate

Spring provides a convenient template class called the `RestTemplate`. The `RestTemplate` makes interacting with most RESTful services a one-liner incantation. In the example below, we establish a few variables and then make a request of the Twitter search service. As mentioned earlier, we will use Jackson to marshal the JSON response data into our representation classes.

```java
package org.hello;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.os.Bundle;

public class HelloActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String url = "http://search.twitter.com/search.json?q={query}";

		String queryParameter = "@gopivotal";

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		TwitterSearchResults results = restTemplate.getForObject(url, TwitterSearchResults.class, queryParameter);
	}

}
```

Thus far, we've only used the HTTP verb `GET` to make calls, but we could just as easily have used `POST`, `PUT`, etc.


## Building and Running the Client

To invoke the code and see the results of the search, simply run it from the command line, like this:

```sh
$ mvn clean install android:deploy android:run
```
	
This will compile the Android app and then run it in the emulator.


## Next Steps

Congratulations! You have just developed a simple REST client using Spring.

There's more to building and working with REST APIs than is covered here. You may want to continue your exploration of Spring and REST with the following Getting Started guides:

* Handling POST, PUT, and GET requests in REST endpoints
* Consuming an HTTP Basic Auth secured REST endpoint
* Consuming an OAuth secured REST endpoint

