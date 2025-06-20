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

Add the required dependencies to your project. Wasapi is built on top of Retrofit and OkHttp.

#### Gradle

```gradle
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```

#### Maven

```xml
    <!-- Wasapi -->
<dependency>
    <groupId>io.github.umutayb</groupId>
    <artifactId>wasapi</artifactId>
    <version>0.0.1</version>
</dependency>
```

> Note: Wasapi itself is a collection of Java utilities. You can clone or include the classes directly until published as a library.

## Usage

### 1. Define your API interface

```java
public interface MyApi {
    @GET("endpoint")
    Call<MyResponse> getData();
}
```

### 2. Create your service instance

```java
import wasapi.WasapiClient;

MyApi api = WasapiClient.createService(MyApi.class, "https://api.example.com/");
```

### 3. Make API calls using `wasapi.Caller`

```java
wasapi.Caller.call(api.getData(), new ApiCallback<MyResponse>() {
    @Override
    public void onSuccess(MyResponse response) {
        // handle success
    }

    @Override
    public void onError(Throwable t) {
        // handle error
    }
});
```

## Structure

* **`wasapi.WasapiClient`**: Handles Retrofit service instantiation and base URL setup.
* **`wasapi.Caller`**: Utility to execute API calls and process callbacks in a consistent way.
* **`wasapi.WasapiUtilities`**: A place for common utilities (e.g., logging, checking response success, error parsing, etc.)

## Contributing

Contributions are welcome! If you’d like to contribute, open an issue or submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
