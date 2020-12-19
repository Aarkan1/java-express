#### Getting Started
```java
Express app = new Express();

app.get("/", (req, res) -> {
   res.send("Hello World");
});

app.listen(); // Will listen on port 80 which is set as default
```

## Installation

### Download
**Direct download as jar:** 
[Latest java-express-1.0.0.jar](https://github.com/Aarkan1/java-express/raw/master/releases/java-express-1.0.0.jar)

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
    <version>1.0.0</version>
</dependency>
```

### Gradle
> Add this to your build.gradle
```xml
repositories {
    maven { url "https://jitpack.io/" }
}

dependencies {
    implementation 'com.github.Aarkan1:java-express:1.0.0'
}
```
