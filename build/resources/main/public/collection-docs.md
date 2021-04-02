# Collection Documentation
Collection is a server-less embedded database ideal for small web applications. It's based on the open source project [Nitrite Database](https://www.dizitart.org/nitrite-database.html).

**It features:**
- Embedded key-value object store
- Single file store
- Very fast and lightweight MongoDB like API
- Indexing
- Full text search capability
- Observable store

*Requires Java Express version 0.5.0 and above!*

<details>
    <summary>Show documentation</summary>

## Table of content
- [Getting started](#getting-started)
- [CollectionOptions](#collectionoptions)
- [Import](#import)
- [Export](#export)
- [Drop](#drop)
    - [Important note!](#important-note)
- [Model](#model)
- [Collection methods](#collection-methods)
    - [Filters](#filters)
    - [FindOptions](#findoptions)
- [Examples](#examples)

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

import org.dizitart.no2.objects.Id;

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

## Examples

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