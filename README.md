# Wasapi
[![Maven Central](https://img.shields.io/maven-central/v/io.github.umutayb/wasapi?color=brightgreen&label=Wasapi)](https://mvnrepository.com/artifact/io.github.umutayb/wasapi/latest)

**Wasapi** is a lightweight, Java-based API utility library that simplifies HTTP service generation and API calls using Retrofit. It abstracts the boilerplate needed to set up and use APIs, making it easier to integrate network calls in your Java applications.

## Features

* Simplified API service generation
* Centralized HTTP configuration
* Easy-to-use `wasapi.Caller` utility for handling requests
* Designed with modularity and readability in mind

## Getting Started

### Installation

Add the required dependency to your project. 

#### Maven

```xml
<!-- Wasapi -->
<dependency>
    <groupId>io.github.umutayb</groupId>
    <artifactId>wasapi</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Usage

### 1. Define your API interface

```java
public interface MyApiServices {
    @GET("endpoint")
    Call<MyResponse> getData();
}
```

### 2. Create your service instance

```java
import wasapi.WasapiClient;

MyApiServices api = new WasapiClient.Builder()
        .baseUrl("https://api.example.com/")
        .build(MyApiServices.class);
```

### 3. Make API calls using `wasapi.Caller`

```java
import retrofit2.Call;
import wasapi.WasapiUtilities;

class MyApi extends WasapiUtilities {

    MyApiServices api = new WasapiClient.Builder()
            .baseUrl("https://api.example.com/")
            .build(MyApiServices.class);

    public static void main(String[] args) {
        Call apicall = api.getData();
        perform(apicall);
    }
}
```

## Structure

* **`wasapi.WasapiClient`**: Handles Retrofit service instantiation and base URL setup.
* **`wasapi.Caller`**: Utility to execute API calls and process callbacks in a consistent way.
* **`wasapi.WasapiUtilities`**: A utility class that provides utility methods for building multipart requests from files, monitoring HTTP response codes and validating fields in API responses.

## Contributing

Contributions are welcome! If you’d like to contribute, open an issue or submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
