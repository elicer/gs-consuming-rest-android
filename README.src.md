Getting Started: Consuming REST Services with Spring
====================================================

What you'll build
-----------------

This Getting Started guide will walk you through the process of consuming a REST service using Spring for Android's `RestTemplate`.

What you'll need
----------------

- About 15 minutes
- {!include#prereq-editor-jdk-buildtools}

## {!include#how-to-complete-this-guide}

<a name="scratch"></a>
Installing the Android Development Environment
----------------------------------------------

Building Android applications requires the installation of the [Android SDK].

### Install the Android SDK

1. Download the correct version of the [Android SDK] for your operating system from the Android web site.

2. Unzip the archive and place it in a location of your choosing. For example on Linux or Mac, you may want to place it in the root of your user directory. See the [Android Developers] web site for additional installation details.

3. Configure the `ANDROID_HOME` environment variable based on the location where you installed the Android SDK. Additionally, you should consider adding `ANDROID_HOME/tools`, `ANDROID_HOME/platform-tools`, and `ANDROID_HOME/build-tools` to your PATH.

	Mac OS X:

	```sh
	$ export ANDROID_HOME=/<installation location>/android-sdk-macosx
    $ export PATH=${PATH}:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
    ```
    
    Linux:
    
    ```sh
    $ export ANDROID_HOME=/<installation location>/android-sdk-linux
    $ export PATH=${PATH}:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
    ```
	    
    Windows:
    
    ```sh
    set ANDROID_HOME=C:\<installation location>\android-sdk-windows
    set PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools
    ```

4. Once the SDK is installed, we need to add the relevant [Platforms and Packages]. We are using Android 4.2.2 (API 17) in this guide.

### Install Android SDK Platforms and Packages

The Android SDK download does not include any specific Android platform SDKs. In order to run the sample code you need to download and install the latest SDK Platform. You accomplish this by using the Android SDK and AVD Manager that was installed from the previous step.

1. Open the Android SDK Manager window:

	```sh
	$ android
	```

	> Note: if this command does not open the Android SDK Manager, then your path is not configured correctly.
	
2. Select the checkbox for *Tools*

3. Select the checkbox for the latest Android SDK, "Android 4.2.2 (API 17)" as of this writing

4. Select the checkbox for the *Android Support Library* from the *Extras* folder

5. Click the **Install packages...** button to complete the download and installation.

	> Note: you may want to simply install all the available updates, but be aware it will take longer, as each SDK level is a sizable download.

### Create an Android Virtual Device

If you have never installed the Android SDK, then you will probably need to create a new Android Virtual Device (avd) in order to use the emulator later on.

1. Check if there are any avds

    android list avd
    
2. If there are no devices listed, then create one:

    android create avd -n gs -t 1
    
3. If you get prompted about multiple system images, then pick one based on your platform. For example, I have an x86 laptop so I picked x86.

    android create avd -n gs -t 1 --abi x86
    
4. Check if you're new avd is on the list:

    android list avd
    
5. Test out your new avd by launching the emulator.

    emulator -avd gs

Set up the project
------------------

{!include#build-system-intro}

In a project directory of your choosing, create the following subdirectory structure; for example, with `mkdir -p src/main/java/hello` on *nix systems:

    └── src
        └── main
            └── java
                └── org
                    └── hello


### Create a Maven POM

    {!include:complete/pom.xml}
    
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
    

Invoking REST services with the RestTemplate
--------------------------------------------

Spring provides a convenient template class called the `RestTemplate`. The `RestTemplate` makes interacting with most RESTful services a one-liner incantation. In the example below, we establish a few variables and then make a request of the Twitter search service. As mentioned earlier, we will use Jackson to marshal the JSON response data into our representation classes.

    {!include:complete/src/main/java/org/hello/HelloActivity.java}
    
Thus far, we've only used the HTTP verb `GET` to make calls, but we could just as easily have used `POST`, `PUT`, etc.


Building and Running the Client
-------------------------------

To build the code, first build it:

    mvn clean install
    
Next, to run the emulator, invoke:

    mvn android:emulator-start
    
After the emulator is up and running, you can deploy and run the Android code to see the results of the search:

    mvn android:deploy android:run
	
This will compile the Android app and then run it in the emulator.


Summary
-------

Congratulations! You have just developed a simple REST client using Spring.

There's more to building and working with REST APIs than is covered here.

[Android SDK]: http://developer.android.com/sdk/index.html
[Android Developers]:http://developer.android.com/sdk/installing/index.html
[Platforms and Packages]:http://developer.android.com/sdk/installing/adding-packages.html