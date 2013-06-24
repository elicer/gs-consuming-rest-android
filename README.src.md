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
Fetch a REST resource
------------------------

With the Android project configured, it's time to create our REST request. Before we can do that though, we need to consider the data we are wanting to consume.

Suppose that you want to find out what Facebook knows about Pivotal. Knowing that Pivotal has a page on Facebook and that the ID is "gopivotal", you should be able to query Facebook's Graph API via this URL:

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

As you can see, Facebook returns quite a bit of information. Do not worry if some of this data appears unfamiliar. For the purposes of this guide, we are only going to concern ourselves with a few parts of it.

To model this JSON data, we’ll create a representation class which defines a few of these fields. In this example, we are making use of Jackson annotations. Jackson is a powerful JSON processor for Java, and can be utilized within Spring.

    {!include:complete/src/main/java/org/hello/Page.java}

As you can see, this is a simple Java class with a handful of properties and matching getter methods. It's annotated with `@JsonIgnoreProperties` from the Jackson JSON processing library to indicate that any properties not bound in this type should be ignored.


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
