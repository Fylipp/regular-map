# Regular Map
This project aims to make mapping [RegEx](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html)
matches to objects or function calls.

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

## License
See the `LICENSE` file in the root directory of the repository.
