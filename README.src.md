Getting Started: Consuming REST Services with Spring
====================================================

What you'll build
-----------------

This Getting Started guide will walk you through the process of consuming a REST service using Spring for Android's `RestTemplate`.

What you'll need
----------------

- About 15 minutes
- {!include#prereq-editor-android-buildtools}

## {!include#how-to-complete-this-guide}

<a name="scratch"></a>
## {!include#android-dev-env}


Set up the project
------------------

{!include#android-build-system-intro}

{!include#create-directory-structure-org-hello}

### Create a Maven POM

    {!include:complete/pom.xml}

{!include#create-android-manifest}

    {!include:complete/AndroidManifest.xml}

### Create a String Resource

    {!include:complete/res/values/strings.xml}

### Create a Layout

    {!include:complete/res/layout/hello_layout.xml}

<a name="initial"></a>
Create a representation class
-----------------------------

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

    {!include:complete/src/main/java/org/hello/TwitterSearchResults.java}


### Tweet

The second representation class is for each individual tweet. Again you see `@JsonIgnoreProperties` being used, and additionally, the `@JsonProperty` annotation allows allows us to map specific fields in the JSON data to fields in the representational class which have different names.

    {!include:complete/src/main/java/org/hello/Tweet.java}


Invoke a REST service with RestTemplate
---------------------------------------

Spring provides a convenient template class called `RestTemplate`. `RestTemplate` makes interacting with most RESTful services a simple process. In the example below, we establish a few variables and then make a request of our simple REST service. As mentioned earlier, we will use Jackson to marshal the JSON response data into our representation classes.

    {!include:complete/src/main/java/org/hello/HelloActivity.java}

So far, we have only used the HTTP verb `GET` to make calls, but we could just as easily have used `POST`, `PUT`, etc.


## {!include#start-android-virtual-device}


## {!include#build-and-run-android}


Summary
-------

Congratulations! You have just developed a simple REST client using Spring.

There's more to building and working with REST APIs than is covered here.

[zip]: https://github.com/springframework-meta/gs-consuming-rest-android/archive/master.zip
