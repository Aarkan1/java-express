### Getting Started
```java
Express app = new Express();

app.get("/", (req, res) -> {
   res.send("Hello World");
});

app.listen(4000); // Will listen on port 4000
```

# Installation

### Download
**Direct download as jar:** 
[Latest java-express-1.0.12.jar](https://github.com/Aarkan1/java-express/raw/main/releases/java-express-1.0.12.jar)

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
    <version>1.0.12</version>
</dependency>
```

### Gradle
> Add this to your build.gradle
```golang
repositories {
    maven { url "https://jitpack.io/" }
}

dependencies {
    implementation 'com.github.Aarkan1:java-express:1.0.12'
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
    - Very simple static-website
    - CRUD with embedded Collection database
    - File upload
    - Send cookies
    - File download

## Express
This class represents the entire HTTP-Server.

<details>
    <summary><strong>Show documentation</strong></summary>

```java
app.raw()                                               // The Javalin app this Express is built upon
app.enableCollections()                                 // Enables the embedded document database
app.enableCollections(String dbPath)                    // Enables the database and creates db on target path
app.enableCollections(CollectionOptions... options)     // Enables the database with options
app.enableCollections(String dbPath, CollectionOptions... options) // Enables the database with options and creates db on target path
app.useStatic(Path path)                                // Serves static files from target directory
app.useStatic(String path, Location location)           // Serves static files from target directory in classpath (Location.CLASSPATH)
app.useStaticFallback(String url, Path filePath)        // Route 404's to target file, good for SPA's
app.useStaticFallback(String url, String filePath, Location location) // Route 404's to target file in classpath (Location.CLASSPATH)
app.cors()                                              // Enable cors for all origins
app.cors(String origin)                                 // Enable cors for specific origins
app.devLogging()                                        // Use extensive logging on handlers
app.get(String path, (req, res) -> { })                 // Add a GET request handler
app.post(String path, (req, res) -> { })                // Add a POST request handler
app.put(String path, (req, res) -> { })                 // Add a PUT request handler
app.patch(String path, (req, res) -> { })               // Add a PATCH request handler
app.delete(String path, (req, res) -> { })              // Add a DELETE request handler
app.use((req, res) -> { })                              // Add a middleware for all methods
app.use(String path, (req, res) -> { })                 // Add a middleware for all methods on specified path
app.all(String path, (req, res) -> { })                 // Add a handler for all methods
app.sse(String path, client -> { })                     // Add a handler for Server Side Events
app.ws(String path, ws -> { })                          // Add a handler for WebSockets
app.locals()                                            // Get environment variables as a Map
app.locals(String name)                                 // Get an environment variable
app.locals(String name, Object obj)                     // Set an environment variable
app.enable(String name)                                 // Set an environment variable to true
app.disable(String name)                                // Set an environment variable to false
app.enabled(String name)                                // Get if environment variable is enabled as a boolean
app.disabled(String name)                               // Get if environment variable is disabled as a boolean
app.get(String name)                                    // Get an environment variable (same as locals)
app.set(String name, Object obj)                        // Set an environment variable (same as locals)
app.listen()                                            // Start the async server on port 80
app.listen(int port)                                    // Start the async server on an specific port
app.listen(String hostname, int port)                   // Start the async server on an specific hostname and port
app.stop()                                              // Stop the server
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
req.ctx()                                    // The Javalin context object which this Request is built upon
req.baseUrl()                                // The URL path on which a router instance was mounted
req.body()                                   // Returns body as a Map (consumes underlying request body if not cached)
req.body(Class<T> klass)                     // Returns body as a class (consumes underlying request body if not cached)
req.bodyAsBytes()                            // Returns body as a bytes (consumes underlying request body if not cached)
req.cookie(String name)                      // Returns an cookie by its name
req.cookies()                                // Returns all cookies
req.host()                                   // Returns host url "example.com"
req.hostname()                               // Returns host url with port "example.com:4000"
req.ip()                                     // Contains the remote IP address of the request
req.method()                                 // Contains a string corresponding to the HTTP method of the request: GET, POST, PUT, and so on
req.originalUrl()                            // This property is much like req.url; however, it retains the original request URL "/search?q=something"
req.params()                                 // Returns all params
req.params(String key)                       // Returns the url parameter by name
req.params(String key, Class<T> klass)       // Returns the url parameter by name as class
req.path()                                   // Returns the request path
req.protocol()                               // Returns the connection protocol
req.query()                                  // Returns all querys
req.query(String key)                        // Returns all querys by name
req.query(String key, Class<T> klass)        // Returns all querys by name as class
req.formData()                               // Returns all form values
req.formData(String key)                     // Returns the form value by name
req.formData(String key, Class<T> klass)     // Returns the form value by name as class
req.formDataFile(String key)                 // Returns the form value file by name
req.formDataFiles(String key)                // Returns multiple form value files by name
req.secure()                                 // Returns true when the connection is over HTTPS, false otherwise
req.subdomains()                             // An array of subdomains in the domain name of the request "tobi.ferrets.example.com" -> ["ferrets", "tobi"]
req.xhr()                                    // Returns true if the 'X-Requested-With' header field is 'XMLHttpRequest'
req.accepts(String accept)                   // Checks if the specified content types are acceptable
req.get(String header)                       // Returns the specified HTTP request header field
req.is(String contentType)                   // Returns the matching content type if the incoming request’s “Content-Type”
req.session()                                // Returns session attributes as Map (server side attribute)
req.session(String key)                      // Returns session attribute         (server side attribute)
req.session(String key, Object value)        // Set session attribute             (server side attribute)
```

</details>

## Response
The `res` object represents the HTTP response that an Express app sends when it gets an HTTP request.

In this documentation and by convention, the object is always referred to as `res` (and the HTTP request is `req`) but its actual name is determined by the parameters to the lambda method in which you’re working.

See [Request example](#request).

<details>
    <summary><strong>Show documentation</strong></summary>

```java
res.ctx()                                          // The Javalin context object which this Response is built upon
res.send(String text)                              // Send a string as response (text/html)
res.send(Object obj)                               // Send an object as a json string response (text/plain)
res.send(InputStream stream)                       // Send inputStream as response
res.send(byte[] bytes)                             // Send bytes as response
res.json(Object json)                              // Send object as JSON response (application/json)
res.append(String name, String value)              // Appends the specified value to the HTTP response header field
res.attachment()                                   // Sets the HTTP response "Content-Disposition" header field to “attachment”
res.attachment(String path)                        // Sets "Content-Disposition" header field and sets the filename and Content-Type to target file
res.download(Path path)                            // Sets attachment and transfers file from the given path
res.sendFile(Path path)                            // Transfers the file from the given path
res.cookie(String name, String value)              // Add a cookie to the response
res.cookie(Cookie cookie)                          // Add a cookie to the response
res.clearCookie(String name, String path)          // Clears the cookie specified by name
res.get(String key)                                // Get the value from an header field via key
res.links()                                        // Returns the header field "Link"
res.links(String next, String last)                // Joins the links provided as properties of the parameter to populate the response’s Link HTTP header field
res.location()                                     // Gets the response Location HTTP header
res.location(String location)                      // Sets the response Location HTTP header to the specified path parameter
res.redirect(String location)                      // Redirect the request to another url
res.redirect(int httpStatusCode, String location)  // Redirect the request to another url with set status
res.render(String filePath, Map<String, Object> model) // Calls send(JavalinRenderer.render(filePath, model) (text/html)
res.sendStatus(int statusCode)                     // Set the response status and send an empty response
res.set(String name, String value)                 // Set a specific response header
res.status(int statusCode)                         // Set the response status
res.status()                                       // Returns the current status
res.type(String contentType)                       // Set the content type
res.end()                                          // Ends the response process
res.end(String message)                            // Ends the response process with message
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

User jane = collection("User").findById("lic4XCz2kxSOn4vr0D8BV");

jane.setAge(30);
// updates model with same UUID
collection("User").save(jane); 

// delete Jane
collection("User").deleteById("lic4XCz2kxSOn4vr0D8BV"); 

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
let ws = new WebSocket('ws://localhost:4000/watch-collections')
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
ctx.data()                   // Byte[] (Array<Byte>)
ctx.offset()                 // int (Int)
ctx.length()                 // int (Int)

// WsCloseContext
ctx.status()                 // int (Int)
ctx.reason()                 // String or null (String?)

// WsErrorContext
ctx.error()                  // Throwable or null (Throwable?)
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

- Very simple static-website
- CRUD with embedded Collection database
- File upload
- Send cookies
- File download

<details>
    <summary><strong>Show documentation</strong></summary>

### Very simple static-website
```java
// Create instance
Express app = new Express();

// will serve both the html/css/js files and the uploads folder in target directory
app.useStatic(Paths.get("src/www"));

app.listen(4000); // start server on port 4000
```

### CRUD with embedded Collection database
```java
// Create instance
Express app = new Express();
// Enable collections before handlers
app.enableCollections();

// Get all articles
app.get("/articles", (req, res) -> {
   List<Article> articles = collection("Article").find();
   res.json(articles);
});

// Get article with id param
app.get("/articles/:id", (req, res) -> {
   String articleId = req.params("id");
   Article article = collection("Article").findById(articleId);
   res.json(article);
});

// Create new article or update existing if existing 'id' is present,
// and return same article with generated 'id' if it wasn't present
app.post("/articles", (req, res) -> {
   Article article = req.body(Article.class);
   Article articleWithGeneratedId = collection("Article").save(article);
   res.json(articleWithGeneratedId);
});

// Delete article with id param
app.delete("/articles/:id", (req, res) -> {
   String articleId = req.params("id");
   int affectCount = collection("Article").deleteById(articleId);

   if(affectCount > 0) {
      res.send("Delete ok");
   } else {
      res.status(404).send("Could not delete article");
   }
});

app.listen(4000); // start server on port 4000


// Class tagged as a Model to be saved in the collection
import express.database.Model;
import org.dizitart.no2.objects.Id;

@Model // required
class Article {
   @Id // required
   private String id;
   private String title;
   private String content;

   // getters, setters, etc..
}
```

### File upload

Server:

```java
// Create instance
Express app = new Express();

// Define endpoint to send formData with files
app.post("/api/upload", (req, res) -> {
   // Define list that will contain upload urls to send back to client
   List<String> uploadNames = new ArrayList<>();
   // extract the files from the FormData
   List<UploadedFile> files = req.formDataFiles("files");

   // loop files
   for (FileItem file : files) {
      // get filename and concat with upload folder name
      String filename = "/uploads/" + file.getFilename();
      // add upload filename to list
      uploadNames.add(filename);
      // save file to static directory (creates dirs if necessary)
      FileUtil.streamToFile(file.getContent(), "src/www" + filename);
   }

   // return uploaded filenames to client
   res.json(uploadNames);
});
```

Client (JavaScript):

```js
async function uploadFiles(e) {
    e.preventDefault();

    // upload files with FormData
    let files = document.querySelector('input[type=file]').files;

    if(files.length) {
        let formData = new FormData();
        
        // add files to formData
        for(let file of files) {
            formData.append('files', file, file.name);
        }
        
        // upload selected files to server
        let uploadResult = await fetch('/api/upload', {
            method: 'POST',
            body: formData
        });
        
        // get the uploaded file urls from response
        let uploadNames = await uploadResult.json();
    }
}
```

### Send cookies
```java
// Create instance
Express app = new Express();

// Define route
app.get("/give-me-cookies", (req, res) -> {

   // Set an cookie (you can call res.cookie how often you want)
   res.cookie(new Cookie("my-cookie", "Hello World!"));
   
   // Send text
   res.send("Your cookie has been set!");
});

app.listen(4000); // start server on port 4000
```

### File download
```java
// Create instance
Express app = new Express();

// Your file
Path downloadFile = Paths.get("my-big-file");

// Create get-route where the file can be downloaded
app.get("/download-me", (req, res) -> res.download(downloadFile));

app.listen(4000); // start server on port 4000
```

    
</details>

