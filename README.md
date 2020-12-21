#### Getting Started
```java
Express app = new Express();

app.get("/", (req, res) -> {
   res.send("Hello World");
});

app.listen(); // Will listen on port 8080 which is set as default
```

## Installation

### Download
**Direct download as jar:** 
[Latest java-express-1.0.5.jar](https://github.com/Aarkan1/java-express/raw/main/releases/java-express-1.0.5.jar)

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
    <version>1.0.5</version>
</dependency>
```

### Gradle
> Add this to your build.gradle
```xml
repositories {
    maven { url "https://jitpack.io/" }
}

dependencies {
    implementation 'com.github.Aarkan1:java-express:1.0.5'
}
```

### Documentation
Will be added soon.

Java Express is made to be almost identical to Node Express.js. See [Express.js api documentation](https://expressjs.com/en/5x/api.html#app) for reference.

It's a thin layer on top of Javalin, and all core features of Javalin is accessible through `app.raw()`.
See [Javalin api documentation](https://javalin.io/documentation).