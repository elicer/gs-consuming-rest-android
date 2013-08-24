This Getting Started guide walks you through the process of building an application that uses Spring for Android's `RestTemplate` to consume a REST service.

What you'll build
-----------------

You'll fetch some publicly visible Facebook data using their REST API from Android.

What you'll need
----------------

 - About 15 minutes
 - A favorite text editor or IDE
 - [Android SDK][sdk]
 - [Maven 3.0][mvn] or later
 - An Android device or Emulator

[sdk]: http://developer.android.com/sdk/index.html
[mvn]: http://maven.apache.org/download.cgi


How to complete this guide
--------------------------

Like all Spring's [Getting Started guides](/guides/gs), you can start from scratch and complete each step, or you can bypass basic setup steps that are already familiar to you. Either way, you end up with working code.

To **start from scratch**, move on to [Set up the project](#scratch).

To **skip the basics**, do the following:

 - [Download][zip] and unzip the source repository for this guide, or clone it using [Git][u-git]:
`git clone https://github.com/spring-guides/gs-consuming-rest-android.git`
 - cd into `gs-consuming-rest-android/initial`.
 - Jump ahead to [Fetch a REST resource](#initial).

**When you're finished**, you can check your results against the code in `gs-consuming-rest-android/complete`.
[zip]: https://github.com/spring-guides/gs-consuming-rest-android/archive/master.zip
[u-git]: /understanding/Git


<a name="scratch"></a>
Set up the project
------------------

In this section you set up a basic build script and then create a simple application. 

> **Note:** If you are new to Android projects, before you proceed, refer to [Installing the Android Development Environment](/guides/gs/android/) to help you configure your development environment.

You can use any build system you like when building apps with Spring, but the code you need to work with [Gradle](http://gradle.org) and [Maven](https://maven.apache.org) is included here. If you're not familiar with either, refer to [Building Android Projects with Gradle](/guides/gs/gradle-android/) or [Building Android Projects with Maven](/guides/gs/maven-android/).
 

### Create the directory structure

In a project directory of your choosing, create the following subdirectory structure; for example, with the following command on Mac or Linux:

```sh
$ mkdir -p src/main/java/org/hello
```

    └── src
        └── main
            └── java
                └── org
                    └── hello

### Create a Maven POM

`pom.xml`
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.hello</groupId>
    <artifactId>gs-consuming-rest-android</artifactId>
    <version>0.1.0</version>
    <packaging>apk</packaging>
    <name>gs-consuming-rest-android</name>

    <dependencies>
        <dependency>
            <groupId>com.google.android</groupId>
            <artifactId>android</artifactId>
            <version>4.1.1.4</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.android</groupId>
            <artifactId>spring-android-rest-template</artifactId>
            <version>1.0.1.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.2.1</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>com.jayway.maven.plugins.android.generation2</groupId>
                <artifactId>android-maven-plugin</artifactId>
                <version>3.6.1</version>
                <configuration>
                    <sdk>
                        <platform>18</platform>
                    </sdk>
                    <deleteConflictingFiles>true</deleteConflictingFiles>
                    <undeployBeforeDeploy>true</undeployBeforeDeploy>
                </configuration>
                <extensions>true</extensions>
            </plugin>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>1.6</source>
                    <target>1.6</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```
    
### Create an Android manifest

The [Android Manifest] contains all the information required to run an Android application, and it cannot build without one.

[Android Manifest]: http://developer.android.com/guide/topics/manifest/manifest-intro.html

`AndroidManifest.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.hello"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk android:minSdkVersion="7" />

    <uses-permission android:name="android.permission.INTERNET" />

    <application android:label="@string/app_name" >
        <activity
            android:name=".HelloActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```


### Create a string resource
Add a text string. Text strings can be referenced from the application or from other resource files.

`res/values/strings.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Consuming REST</string>
</resources>
```

### Create a layout
Here you define the visual structure for the user interface of your application.

`res/layout/hello_layout.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" >

    <TextView
        android:id="@+id/name"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content" />

    <TextView
        android:id="@+id/about"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content" />

    <TextView
        android:id="@+id/phone"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content" />

    <TextView
        android:id="@+id/website"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content" />

</LinearLayout>
```


<a name="initial"></a>
Fetch a REST resource
------------------------
Before you create the REST request, consider the data that you want your application to consume. Suppose that you want to find out what Facebook knows about Pivotal. Knowing that Pivotal has a page on Facebook and that the ID is "gopivotal", query Facebook's Graph API via this URL:

    http://graph.facebook.com/gopivotal

If you request that URL through your web browser or curl, you'll receive a JSON document that looks something like this:

```javascript
{
   "id": "161112704050757",
   "about": "At Pivotal, our mission is to enable customers to build a new class of applications, leveraging big and fast data, and do all of this with the power of cloud independence. ",
   "app_id": "0",
   "can_post": false,
   "category": "Internet/software",
   "checkins": 0,
   "cover": {
      "cover_id": 163344023827625,
      "source": "http://sphotos-d.ak.fbcdn.net/hphotos-ak-frc1/s720x720/554668_163344023827625_839302172_n.png",
      "offset_y": 0,
      "offset_x": 0
   },
   "founded": "2013",
   "has_added_app": false,
   "is_community_page": false,
   "is_published": true,
   "likes": 126,
   "link": "https://www.facebook.com/gopivotal",
   "location": {
      "street": "1900 South Norfolk St.",
      "city": "San Mateo",
      "state": "CA",
      "country": "United States",
      "zip": "94403",
      "latitude": 37.552261,
      "longitude": -122.292152
   },
   "name": "Pivotal",
   "phone": "650-286-8012",
   "talking_about_count": 15,
   "username": "gopivotal",
   "website": "http://www.gopivotal.com",
   "were_here_count": 0
}
```

As you can see, Facebook returns quite a bit of information. This guide deals with a small part of it.

Create a representation class
-----------------------------

To model this JSON data, you create a representation class that defines a few of these fields. The following example uses Jackson annotations. Jackson is a powerful JSON processor for Java that you can use within Spring.

`src/main/java/org/hello/Page.java`
```java
package org.hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Page {

    private String name;
    private String about;
    private String phone;
    private String website;

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

}
```

As you can see, this is a simple Java class with a handful of properties and matching getter methods. It's annotated with `@JsonIgnoreProperties` from the Jackson JSON processing library to indicate that any properties not bound in this type should be ignored.


Invoke a REST service with the RestTemplate
---------------------------------------

Spring provides a template class called `RestTemplate`. `RestTemplate` makes interacting with most RESTful services a simple process. In the example below, you establish a few variables and then make a request of our simple REST service. As mentioned earlier, you use Jackson to marshal the JSON response data into our representation classes.

`src/main/java/org/hello/HelloActivity.java`
```java
package org.hello;

import org.hello.Page;
import org.hello.R;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
    }

    @Override
    public void onStart() {
        super.onStart();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Page page = restTemplate.getForObject("http://graph.facebook.com/gopivotal", Page.class);

        TextView textView = (TextView) this.findViewById(R.id.name);
        textView.setText(page.getName());

        textView = (TextView) this.findViewById(R.id.about);
        textView.setText(page.getAbout());

        textView = (TextView) this.findViewById(R.id.phone);
        textView.setText(page.getPhone());

        textView = (TextView) this.findViewById(R.id.website);
        textView.setText(page.getWebsite());
    }

}
```

So far, you have only used the HTTP verb `GET` to make calls, but you could just as easily have used `POST`, `PUT`, and so on.

Build and run the client
------------------------

With an attached device or emulator running, invoke the code and see the results of the REST request:

```sh
$ mvn clean package android:deploy android:run
```

The command builds the Android app and runs it in the emulator or attached device.


Summary
-------

Congratulations! You have developed a simple REST client using Spring for Android.
