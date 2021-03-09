### Getting Started
```java
Express app = new Express();

app.get("/", (req, res) -> {
   res.send("Hello World");
});

app.listen(3000); // Will listen on port 3000
```

# Installation

### Download
**Direct download as jar:** 
[Latest java-express-1.0.7.jar](https://github.com/Aarkan1/java-express/raw/main/releases/java-express-1.0.7.jar)

**Old version:**
[Older versions](https://github.com/Aarkan1/java-express/tree/master/releases)

### Maven
> Add repository:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

> Add dependency:
```xml
<dependency>
    <groupId>com.github.Aarkan1</groupId>
    <artifactId>java-express</artifactId>
    <version>1.0.7</version>
</dependency>
```

### Gradle
> Add this to your build.gradle
```xml
repositories {
    maven { url "https://jitpack.io/" }
}

dependencies {
    implementation 'com.github.Aarkan1:java-express:1.0.7'
}
```

# Documentation
Java Express is made to be almost identical to Node Express.js. See [Express.js api documentation](https://expressjs.com/en/5x/api.html#app) for reference.

It's a thin layer on top of Javalin, and all core features of Javalin is accessible through `app.raw()`.
See [Javalin api documentation](https://javalin.io/documentation).

## Table of content
- [Express](#express)
- [Request](#request)
- [Response](#response)
- [Collection](#collection)
- [Static Files](#static-files)
- [Static fallback mode](#static-fallback-mode)
- [Uploads](#uploads)
- [WebSockets](#websockets)
- [Server-sent Events](#server-sent-events)
- [Configuration](#configuration)
- [Examples](#examples)

## Express
This class represents the entire HTTP-Server.

<details>
    <summary><strong>Show documentation</strong></summary>

```java
app.raw()                                               // 
app.enableCollections()                                 // 
app.enableCollections(String dbPath)                    // 
app.enableCollections(CollectionOptions... options)     // 
app.enableCollections(String dbPath, CollectionOptions... options)   //
app.useStatic(Path path)                                // 
app.useStatic(String path, Location location)           // 
app.useStaticFallback(String url, Path filePath)        // 
app.useStaticFallback(String url, String filePath, Location location) //
app.cors()                                              // 
app.cors(String origin)                                 // 
app.devLogging()                                        // 
app.put(String path, (req, res) -> { })                 // 
app.get(String path, (req, res) -> { })                 // 
app.post(String path, (req, res) -> { })                // 
app.patch(String path, (req, res) -> { })               // 
app.delete(String path, (req, res) -> { })              // 
app.use((req, res) -> { })                              // 
app.use(String path, (req, res) -> { })                 // 
app.all(String path, (req, res) -> { })                 // 
app.sse(String path, client -> { })                     // 
app.ws(String path, ws -> { })                          // 
app.locals()                                            // 
app.locals(String name)                                 // 
app.locals(String name, Object obj)                     // 
app.enable(String name)                                 // 
app.disable(String name)                                // 
app.enabled(String name)                                // 
app.disabled(String name)                               // 
app.set(String name, Object obj)                        // 
app.get(String name)                                    // 
app.listen()                                            // 
app.listen(int port)                                    // 
app.listen(String hostname, int port)                   // 
app.stop()                                              // 
```

</details>


## Request
The `req` object represents the HTTP request and has properties for the request query string, parameters, body, HTTP headers, and so on. In this documentation and by convention, the object is always referred to as `req` (and the HTTP response is `res`) but its actual name is determined by the parameters to the lambda method in which you’re working.

For example:

```java
app.get('/user/:id', (req, res) -> {
  res.send('user ' + req.params("id"));
});
```

But you could just as well have:

```java
app.get('/user/:id', (request, response) -> {
  response.send('user ' + request.params("id"));
});
```

<details>
    <summary><strong>Show documentation</strong></summary>

```java
req.ctx()                                // 
req.baseUrl()                            // 
req.body()                               // 
req.body(Class<T> klass)                 // 
req.bodyAsBytes()                        // 
req.cookie(String name)                  // 
req.cookies()                            // 
req.host()                               // 
req.hostname()                           // 
req.ip()                                 // 
req.method()                             // 
req.originalUrl()                        // 
req.params()                             // 
req.params(String key)                   // 
req.params(String key, Class<T> klass)   // 
req.path()                               // 
req.protocol()                           // 
req.query()                              // 
req.query(String key)                    // 
req.query(String key, Class<T> klass)    // 
req.formData()                           // 
req.formData(String key)                 // 
req.formData(String key, Class<T> klass) // 
req.formDataFile(String key)             // 
req.formDataFiles(String key)            // 
req.secure()                             // 
req.subdomains()                         // 
req.xhr()                                // 
req.accepts(String accept)               // 
req.acceptsCharsets()                    // 
req.acceptsEncodings()                   // 
req.acceptsLanguages()                   // 
req.get(String header)                   // 
req.header(String field)                 // 
req.is(String contentType)               // 
req.session()                            // 
req.session(String key)                  // 
req.session(String key, Object value)    // 
```

</details>

## Response
The `res` object represents the HTTP response that an Express app sends when it gets an HTTP request.

In this documentation and by convention, the object is always referred to as `res` (and the HTTP request is `req`) but its actual name is determined by the parameters to the lambda method in which you’re working.

See [Request example](#request).

<details>
    <summary><strong>Show documentation</strong></summary>

```java
res.ctx()                                          // 
res.send(String text)                              // 
res.send(Object obj)                               // 
res.send(InputStream stream)                       // 
res.send(byte[] bytes)                             // 
res.json(Object json)                              // 
res.append(String name, String value)              // 
res.attachment()                                   // 
res.attachment(String path)                        // 
res.download(Path path)                            // 
res.sendFile(Path path)                            // 
res.cookie(String name, String value)              // 
res.cookie(Cookie cookie)                          // 
res.clearCookie(String name, String path)          // 
res.end()                                          // 
res.get()                                          // 
res.links()                                        // 
res.links(String next, String last)                // 
res.location()                                     // 
res.location(String location)                      // 
res.redirect(String location)                      // 
res.redirect(String location, int httpStatusCode)  // 
res.render(String filePath, Map<String, Object> model) // 
res.sendStatus(int statusCode)                     // 
res.set(String name, String value)                 // 
res.status(int statusCode)                         // 
res.status()                                       // 
res.type(String contentType)                       // 
res.stop()                                         // 
res.stop(String text)                              // 
```

</details>

## Collection
Collection is a server-less embedded database ideal for small web applications. It's based on the open source project [Nitrite Database](https://www.dizitart.org/nitrite-database.html).

**It features:**
- Embedded key-value object store
- Single file store
- Very fast and lightweight MongoDB like API
- Indexing
- Full text search capability
- Observable store

*Requires Java Express version 0.5.0 and above!*

```java
import static express.database.Database.collection;

Express app = new Express();

app.enableCollections();  // initialize database

User john = new User("John").
collection("User").save(john);  // create or update user

List<User> users = collection("User").find();  // get all users
```

<details>
    <summary><strong>Show documentation</strong></summary>

## Table of content
- [Getting started]()
- [CollectionOptions](#collectionoptions)
- [Import](#import)
- [Export](#export)
- [Drop](#drop)
    - [Important note!](#important-note)
- [Model](#model)
- [Collection methods](#collection-methods)
    - [Filters](#filters)
    - [FindOptions](#findoptions)
- [Collection Examples](#collection-examples)

## Getting started
The Express app has an embedded nosql database, ready to be used if you enable it by adding `app.enableCollections()` right after app is instantiated. 
This will create a database-file in your project. Easy to deploy or share.
When collections are enabled you can use the static `collection()`-method to manipulate the database. 
**collection()** takes either a String with the classname, case sensitive, or the Class itself. 

```java
import static express.database.Database.collection;

Express app = new Express();
// creates a database-file in /db-folder called 'embedded.db'
app.enableCollections(); 
// creates the file at target path
app.enableCollections(String dbPath); 


User john = new User("John").
// generates an UUID
collection("User").save(john); 

User jane = collection("User").findById("xxxxxxxx-xxxx-4xxx-8xxx-xxxxxxxxxxxx");

jane.setAge(30);
// updates model with same UUID
collection("User").save(jane); 

// delete Jane
collection("User").deleteById("xxxxxxxx-xxxx-4xxx-8xxx-xxxxxxxxxxxx"); 

List<User> users = collection("User").find();
List<User> users = collection(User.class).find();

List<User> usersNamedJohn = collection("User").find(eq("name", "John"));
```

Watch a collection on changes
```java
// watchData has 2 fields. 
// getEvent() is the event triggered - 'insert', 'update' or 'delete'
// getData() is a list with effected models
collection("User").watch(watchData -> {
    List<User> effectedUsers = watchData.getData();

    switch(watchData.getEvent()) {
        case "insert": // on created model
        break;

        case "update": // on updated model
        break;

        case "delete": // on deleted model
        break;
    }
});
```

### CollectionOptions
CollectionOptions can be passed when enabling collections to set certain options.
Options available are:
- *CollectionOptions.ENABLE_WATCHER* - Enables WebSocket listener on collection changes
- *CollectionOptions.DISABLE_BROWSER* - Disables collection browser (good when deploying)

You can pass one or multiple options when enabling collections:
```java
Express app = new Express();
app.enableCollections(CollectionOptions.ENABLE_WATCHER, CollectionOptions.DISABLE_BROWSER);
```

**ENABLE_WATCHER**

This starts an `WebSocket` endpoint in the database that will send *watchData* when a change happens.

**WatchData** is an object containing *model*, *event*, *data*.
- *model*: The collection that were triggered
- *event*: The event that was triggered, 'insert', 'update' or 'delete'
- *data*: List of items that are related to the change

To listen to these events on the client you have to create a connection to `'ws://<hostname>:<port>/watch-collections'` with `WebSocket`.

```js
let ws = new WebSocket('ws://localhost:3000/watch-collections')
```

With the webSocket you can listen to messages from the collection channel.

```js
ws.onmessage = messageEvent => {
    const watchData = JSON.parse(messageEvent.data);

    // deconstruct model, event and data from watchData
    const { model, event, data } = watchData;

    if(model == 'BlogPost') {
        // do something with BlogPost
    } 
    else if(model == 'Message') {
        // do something with Message
    }
};
```

#### Example

Java:
```java
Express app = new Express();
app.enableCollections(CollectionOptions.ENABLE_WATCHER);
```

JavaScript:
```js
ws.onmessage = messageEvent => {
    const watchData = JSON.parse(messageEvent.data);

    // deconstruct model, event and data from watchData
    const { model, event, data } = watchData;

    switch(event) {
        case 'insert':
            // add post to list
            model == 'BlogPost' && posts.push(data[0]);
            model == 'Message' && // add message to list
        break;
        case 'update':
        break;
        case 'delete':
            // remove post from list
            model == 'BlogPost' && (posts = posts.filter(post => post.id !== data[0].id));
        break;
    };

    // update 
    renderPosts();
};
```

**DISABLE_BROWSER**

This will simply disable the collection browser. This might be a good idea to save CPU and RAM when deploying. 

```java
Express app = new Express();
app.enableCollections(CollectionOptions.DISABLE_BROWSER);
```

## Import
The collection supports mocking data as JSON, from example [mockaroo.com](https://www.mockaroo.com/).
**Note**: Format must be a JSON-array. 

Simply select a .json-file and click `import`. This will append the data to the collection. 
It's important that the fields match the **model** in the collection.

## Export
Export will download the current collection as a .json-file.

This file can easily be used to import into the collection, and can serve as a backup.

The .json-file is also created in the db-directory with the name of the model. 

## Drop
Will delete all data in the collection.

### Important note!
After a model is saved to the collection, the class with **@Model** annotation **CANNOT** be moved to another package or renamed. This will corrupt the database-file, and will have to be removed. 
Keep backups!

Changing the name of a field will not corrupt the database, but will remove the value from all models.

## Model
For the collections to work the following two annotations must be present in at least one class.

### @Model Annotation
Marks a class to be used with a collection. Is required if an object is going to be saved to the collection.

### @Id Annotation
Each object in a Collection must be uniquely identified by a field marked with **@Id** annotation. The collection maintains an unique index on that field to identify the objects.
If no id is manually set, the Collection will generate an UUID to that field when inserted or saved. 

```java
import express.database.Model;
import org.dizitart.no2.objects.Id;

@Model
public class MyType {

    @Id
    private String id;
    private String name;
}
```

The collection provides a set of annotations for model objects while using it in a collection. The annotations are to let the collection know about various information about the **model** while constructing it. It also helps to reduce some boilerplate code.

**@Index** is required to do **text()** or **regex()** filtering on a field. It can only be used within a **@Indices** annotation.
**Index types are:**
- IndexType.Unique - used with unique fields
- IndexType.NonUnique - used with single value duplicate fields
- IndexType.FullText - used with multiple word fields, NonUnique

Example
```java
// Employee class
@Indices({
        @Index(value = "joinDate", type = IndexType.NonUnique),
        @Index(value = "name", type = IndexType.Unique)
})
@Model
public class Employee {
    @Id
    private String id;
    private Date joinDate;
    private String name;
    private String address;

    // ... public getters and setters
}
```

## Collection methods

To use the collection you need to add which model to query for in the collection parameter, ex `collection("User")` will only query for Users. 

**Table 1. Collection methods**

| Operation | Method | Description |
| --- | --- | --- |
| Get all models | find(Filter, SortOptions) | Returns a list with objects. If no filter is used find() will return ALL models. |
| Get one model | findOne(Filter) | Returns first found model. |
| Get model with id | findById(String) | Returns the object with mathing id. |
| Create new model | insert(Object) | Creates a new model in the collection. Generates an UUID if no id is present. Can insert an array of models. |
| Create or Update a model | save(Object) | Creates a new model in the collection if no id is present. If theres an id save() will update the existing model in the collection. Can save an array of models. |
| Update models | update(Filter, Object) | Update all models matching the filter. |
| Update a model with id | updateById(String) | Updates the model with matching id. |
| Delete models | delete(Filter) | Deletes all models matching the filter. |
| Delete a model with id | deleteById(String) | Deletes the model with matching id. |
| Watch a collection | watch(lambda) | Register a watcher that triggers on changes in the collection. |


### Filters

Filters are the selectors in the collection’s find operation. It matches models in the collection depending on the criteria provided and returns a list of objects.

Make sure you import the static method **ObjectFilters**.

```java
import static org.dizitart.no2.objects.filters.ObjectFilters.*;
```

**Table 2. Comparison Filter**

| Filter | Method | Description |
| --- | --- | --- |
| Equals | eq(String, Object) | Matches values that are equal to a specified value. |
| Greater | gt(String, Object) | Matches values that are greater than a specified value. |
| GreaterEquals | gte(String, Object) | Matches values that are greater than or equal to a specified value. |
| Lesser | lt(String, Object) | Matches values that are less than a specified value. |
| LesserEquals | lte(String, Object) | Matches values that are less than or equal to a specified value. |
| In | in(String, Object[]) | Matches any of the values specified in an array. |
| NotIn | notIn(String, Object[]) | Matches none of the values specified in an array. |

**Table 3. Logical Filters**

| Filter | Method | Description |
| --- | --- | --- |
| Not | not(Filter) | Inverts the effect of a filter and returns results that do not match the filter. |
| Or | or(Filter[]) | Joins filters with a logical OR returns all ids of the models that match the conditions of either filter. |
| And | and(Filter[]) | Joins filters with a logical AND returns all ids of the models that match the conditions of both filters. |

**Table 4. Array Filter**

| Filter | Method | Description |
| --- | --- | --- |
| Element Match | elemMatch(String, Filter) | Matches models that contain an array field with at least one element that matches the specified filter. |

**Table 5. Text Filters**
*Note*: For these filters to work the field must be indexed. See [Annotations](#annotations)

| Filter | Method | Description |
| --- | --- | --- |
| Text | text(String, String) | Performs full-text search. |
| Regex | regex(String, String) | Selects models where values match a specified regular expression. |

### FindOptions

A FindOptions is used to specify search options. It provides pagination as well as sorting mechanism.

```java
import static org.dizitart.no2.FindOptions.*;
```

Example
```java
// sorts all models by age in ascending order then take first 10 models and return as a List
List<User> users = collection("User").find(sort("age", SortOrder.Ascending).thenLimit(0, 10));
```
```java
// sorts the models by age in ascending order
List<User> users = collection("User").find(sort("age", SortOrder.Ascending));
```
```java
// sorts the models by name in ascending order with custom collator
List<User> users = collection("User").find(sort("name", SortOrder.Ascending, Collator.getInstance(Locale.FRANCE)));
```
```java
// fetch 10 models starting from offset = 2
List<User> users = collection("User").find(limit(2, 10));
```

## Collection Examples

**and()**
```java
// matches all models where 'age' field has value as 30 and
// 'name' field has value as John Doe
collection("User").find(and(eq("age", 30), eq("name", "John Doe")));
```

**or()**
```java
// matches all models where 'age' field has value as 30 or
// 'name' field has value as John Doe
collection("User").find(or(eq("age", 30), eq("name", "John Doe")));
```

**not()**
```java
// matches all models where 'age' field has value not equals to 30
collection("User").find(not(eq("age", 30)));
```

**eq()**
```java
// matches all models where 'age' field has value as 30
collection("User").find(eq("age", 30));
```

**gt()**
```java
// matches all models where 'age' field has value greater than 30
collection("User").find(gt("age", 30));
```

**gte()**
```java
// matches all models where 'age' field has value greater than or equal to 30
collection("User").find(gte("age", 30));
```

**lt()**
```java
// matches all models where 'age' field has value less than 30
collection("User").find(lt("age", 30));
```

**lte()**
```java
// matches all models where 'age' field has value lesser than or equal to 30
collection("User").find(lte("age", 30));
```

**in()**
```java
// matches all models where 'age' field has value in [20, 30, 40]
collection("User").find(in("age", 20, 30, 40));
```

**notIn()**
```java
// matches all models where 'age' field does not have value in [20, 30, 40]
collection("User").find(notIn("age", 20, 30, 40));
```

**elemMatch()**
```java
// matches all models which has an array field - 'color' and the array
// contains a value - 'red'.
collection("User").find(elemMatch("color", eq("$", "red"));
```

**text()**
```java
// matches all models where 'address' field has a word 'roads'.
collection("User").find(text("address", "roads"));

// matches all models where 'address' field has word that starts with '11A'.
collection("User").find(text("address", "11a*"));

// matches all models where 'address' field has a word that ends with 'Road'.
collection("User").find(text("address", "*road"));

// matches all models where 'address' field has a word that contains a text 'oa'.
collection("User").find(text("address", "*oa*"));

// matches all models where 'address' field has words like '11a' and 'road'.
collection("User").find(text("address", "11a road"));

// matches all models where 'address' field has word 'road' and another word that start with '11a'.
collection("User").find(text("address", "11a* road"));
```

**regex()**
```java
// matches all models where 'name' value starts with 'jim' or 'joe'.
collection("User").find(regex("name", "^(jim|joe).*"));
```

</details>

## Static Files
_src: [Javalin docs](https://javalin.io/documentation#static-files)_

You can enabled static file serving by doing `app.useStatic(Paths.get("src/folder"))`, and/or `app.useStatic("/classpath-folder"), Location.INTERNAL)`. Static resource handling is done after endpoint matching, meaning your self-defined endpoints have higher priority. 

If you do `app.useStatic(Paths.get("src/folder"))`. Your index.html file at /src/folder/index.html will be available at `http://{host}:{port}/index.html` and `http://{host}:{port}/`.

You can call `useStatic` multiple times to set up multiple handlers.

## Static fallback mode
_src: [Javalin docs](https://javalin.io/documentation#single-page-mode)_

Static fallback mode is similar to static file handling. It runs after endpoint matching and after static file handling. It’s basically a very fancy 404 mapper, which converts any 404’s into a specified page. You can define multiple fallback page handlers for your application by specifying different root paths.

You can enabled single page mode by doing `app.useStaticFallback("/", Paths.get("src/folder/index.html"))`, and/or `app.useStaticFallback("/", "/classpath/to/index.html", Location.INTERNAL)`.

## Uploads
Uploaded files are easily accessible via `req.formDataFile()`:

<details>
    <summary><strong>Show documentation</strong></summary>

Java endpoint:

```java
app.post("/api/upload", (req, res) -> {
    List<UploadedFile> files = req.formDataFiles("files");  // get files as list
    UploadedFile file = req.formDataFile("files");          // get a single file

    // with FileOutputStream
    Path path = Paths.get("src/uploads/" + file.getFilename());
    try (FileOutputStream os = new FileOutputStream(path.toString())) {
        os.write(file.getContent().readAllBytes()); // write to file
    }

    // with FileUtil (creates dirs if necessary)
    FileUtil.streamToFile(file.getContent(), "src/uploads/" + file.getFilename());
});
```

JavaScript:

```js
let files = document.querySelector('input[type=file]').files;
let formData = new FormData();

for(let file of files) {
   formData.append('files', file, file.name);
}
   
fetch('/api/upload', {
   method: 'POST',
   body: formData
});
```

or with HTML:

```html
<form method="post" action="/api/upload" enctype="multipart/form-data">
    <input type="file" name="files" multiple>
    <button>Submit</button>
</form>
```
    
</details>

## WebSockets
_src: [Javalin docs](https://javalin.io/documentation#websockets)_

Express has a very intuitive way of handling WebSockets. You declare an endpoint with a path and configure the different event handlers in a lambda:

```java
app.ws("/websocket/:path", ws -> {
    ws.onConnect(ctx -> System.out.println("Connected"));
});
```

There are a total of five events supported:

```java
ws.onConnect(WsConnectContext);
ws.onError(WsErrorContext);
ws.onClose(WsCloseContext);
ws.onMessage(WsMessageContext);
ws.onBinaryMessage(WsBinaryMessageContext);
```

<details>
    <summary><strong>Show documentation</strong></summary>
    
A WebSocket endpoint is declared with app.ws(path, handler). WebSocket handlers require unique paths.

```java
app.ws("/websocket/:path", ws -> {
    ws.onConnect(ctx -> System.out.println("Connected"));
    ws.onMessage(ctx -> {
        User user = ctx.message(User.class); // convert from json
        ctx.send(user); // convert to json and send back
    });
    ws.onBinaryMessage(ctx -> System.out.println("Message"))
    ws.onClose(ctx -> System.out.println("Closed"));
    ws.onError(ctx -> System.out.println("Errored"));
});
```

### WsContext (ctx)

The Context object provides you with everything you need to handle a websocket-request. It contains the underlying websocket session and servlet-request, and convenience methods for sending messages to the client.

```java
// WsMessageContext
ctx.message()                // get the message as String
ctx.message(MyObject.class)  // get message as target class (T)

// WsContext
ctx.matchedPath()            // get the path used to match this request, ex "/path/:param"

ctx.send(object)             // send an object as JSON (string message)
ctx.send(string)             // send a string message
ctx.send(byteBuffer)         // send a bytebuffer message

ctx.queryString()            // get the query string
ctx.queryParamMap()          // get a map of the query parameters
ctx.queryParams(key)         // get query parameters by key
ctx.queryParam(key)          // get query parameter by key
ctx.queryParam(key, default) // get query parameter (or default value)
ctx.queryParam(key, class)   // get query parameter as class

ctx.pathParamMap()           // get path parameter map
ctx.pathParam(key)           // get path parameter
ctx.pathParam(key, class)    // get path parameter as class

ctx.host()                   // get the host

ctx.header(key)              // get request header
ctx.headerMap()              // get a map of the request headers

ctx.cookie(key)              // get request cookie
ctx.cookieMap()              // get a map of all request cookies

ctx.attribute(key, value)    // set request attribute
ctx.attribute(key)           // get request attribute
ctx.attributeMap()           // get a map of request attributes

ctx.sessionAttribute(key)    // get request session attribute (from when WebSocket upgrade was performed)
ctx.sessionAttributeMap()    // get a map of session attributes (from when WebSocket upgrade was performed)

// WsBinaryMessageContext
ctx.data() // Byte[] (Array<Byte>)
ctx.offset() // int (Int)
ctx.length() // int (Int)

// WsCloseContext
ctx.status() // int (Int)
ctx.reason() // String or null (String?)

// WsErrorContext
ctx.error() // Throwable or null (Throwable?)
```

</details>

## Server-sent Events
_src: [Javalin docs](https://javalin.io/documentation#server-sent-events)_

Server-sent events (often also called event source) are very simple in Javalin. You call `app.sse()`, which gives you access to the connected `SseClient`:

<details>
    <summary><strong>Show documentation</strong></summary>

```java
app.sse("/sse", client ->
    client.sendEvent("connected", "Hello, SSE");
    client.onClose(() -> System.out.println("Client disconnected"));
});
```

The `SseClient` has access to three things:

```java
client.sendEvent() // method(s) for sending events to client
client.onClose(runnable) // callback which runs when a client closes its connection
client.ctx // the Context for when the client connected (to fetch query-params, etc)
```
    
</details>

## Configuration
_src: [Javalin docs](https://javalin.io/documentation#configuration)_

You can pass a config object when creating a new instance of Express. The below snippets shows all the available config options:

<details>
    <summary><strong>Show documentation</strong></summary>

```java
Express app = new Express(config -> {

    // JavalinServlet
    config.addSinglePageRoot(root, file)            // ex ("/", "/index.html")
    config.addSinglePageRoot(root, file, location)  // ex ("/", "src/file.html", Location.EXTERNAL)
    config.addStaticFiles(directory)                // ex ("/public")
    config.addStaticFiles(directory, location)      // ex ("src/folder", Location.EXTERNAL)
    config.addStaticFiles(prefix, dir, location)    // ex ("/assets", "src/folder", Location.EXTERNAL)
    config.aliasCheckForStaticFiles = AliasCheck    // symlink config, ex new ContextHandler.ApproveAliases();
    config.asyncRequestTimeout = timeoutInMs        // timeout for async requests (default is 0, no timeout)
    config.autogenerateEtags = true/false           // auto generate etags (default is false)
    config.compressionStrategy(Brotli(4), Gzip(6))  // set the compression strategy and levels - since 3.2.0
    config.contextPath = contextPath                // context path for the http servlet (default is "/")
    config.defaultContentType = contentType         // content type to use if no content type is set (default is "text/plain")
    config.dynamicGzip = true/false                 // dynamically gzip http responses (default is true)
    config.enableCorsForAllOrigins()                // enable cors for all origins
    config.enableCorsForOrigin(origins)             // enable cors for specific origins
    config.enableDevLogging()                       // enable extensive development logging for http and websocket
    config.enableWebjars()                          // enable webjars (static files)
    config.enforceSsl = true/false                  // redirect http traffic to https (default is false)
    config.ignoreTrailingSlashes = true/false       // default is true
    config.logIfServerNotStarted = true/false       // log a warning if user doesn't start javalin instance (default is true)
    config.precompressStaticFiles = true/false      // store compressed files in memory (avoid recompression and ensure content-length is set)
    config.prefer405over404 = true/false            // send a 405 if handlers exist for different verb on the same path (default is false)
    config.requestCacheSize = sizeInBytes           // set the request cache size, used for reading request body multiple times (default is 4kb)
    config.requestLogger { ... }                    // set a request logger
    config.sessionHandler { ... }                   // set a SessionHandler

    // WsServlet
    config.wsContextPath = contextPath              // context path for the websocket servlet (default is "/")
    config.wsFactoryConfig { ... }                  // set a websocket factory config
    config.wsLogger { ... }                         // set a websocket logger

    // Server
    config.server { ... }                           // set a Jetty server for Javalin to run on

    // Misc
    config.accessManager { ... }                    // set an access manager (affects both http and websockets)
});
```

</details>

## Examples

<details>
    <summary><strong>Show documentation</strong></summary>
    
</details>

