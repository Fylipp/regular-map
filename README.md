# Regular Map
This project aims to make mapping [RegEx](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html)
matches to objects or function calls a trivial task.

## Example (see the [wiki](https://github.com/Fylipp/regular-map/wiki) for more information)
The following example will illustrate how a match gets mapped to an object.
```kotlin
// A simple data class
data class Person(@NamedGroup("firstname") val firstname: String, @NamedGroup("lastname") val lastname: String)

// The logic
val pattern = Pattern.compile("(?<firstname>[a-z]+)[ ](?<lastname>[a-z]+)", Pattern.CASE_INSENSITIVE)
val person = pattern.mapObject<Person>("Philipp Ploder")

// The variable <person> now contains the object Person("Philipp", "Ploder")
```

## Installation
Either download the latest release from the `Releases` tab or use Maven with a custom repository.
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependency>
    <groupId>com.github.Fylipp</groupId>
    <artifactId>regular-map</artifactId>
    <version>v1.0.0</version>
</dependency>
```

## License
See the `LICENSE` file in the root directory of the repository.
