# robe-assets
---
Advanced and easy to use asset support for dropwizard projects.
## Motivation
Creating an asset bundle which can provide following points. 
* easy to understand 
* configurable 
* enough to serve common needs (classpath,filesystem,http)
* cache support

## Getting Started
You have to complete 3 steps in order to start serving you assets via robe-assets.
* Add dependency (Maven sample)

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-assets</artifactId>
  <version>0.5.0</version>
</dependency>
```

* Decide what to serve and how to serve in yml.

```yml
assets:
 - resourcePath: /SomewhereonyourFileSystem/Project/UI/src/
   uriPath:      /ui/
   indexFile:    index.html
   assetsName:   io.robe.sample.ui
   cached:       true # true/false
   type: filesystem # use "classpath" or "filesystem" or "http"
   ```
   
* Add bundle to the bootstrap at you Aplication

```java
   bootstrap.addBundle(new AdvancedAssetBundle<T>());
```
Now it is ready to serve `/SomewhereonyourFileSystem/Project/UI/src/` folder from the application url  
`sampleapp/ui`

## Details
Configuration, usage, default asset servlets will be explained below. 
### Configuration
`AdvancedAssetBundle`will read configurations from YML. As default `robe-admin` we get it under assets. You can define multiple asset servlets. Sample is below.
```yml
assets:
 - resourcePath: /SomewhereonyourFileSystem/Project/UI/src/
   uriPath:      /file/
   indexFile:    index.html
   assetsName:   io.robe.sample.ui
   cached:       true # true/false
   type: filesystem 
 - resourcePath: ui/src
   uriPath:      /resource/
   indexFile:    index.html
   assetsName:   io.robe.sample.ui
   cached:       true # true/false
   type: classpath 
 - resourcePath: http://www.robe.io
   uriPath:      /ext/
   indexFile:    index.html
   assetsName:   io.robe.sample.ui
   cached:       true # true/false
   type: http 
   ```
As you see we defined 3 different type of asset servlets. All with different asset resource and serve uri.
Now details of the fields
* `resourcePath ` : base  resource path to read resources. Base for all assets
* `uriPath` : path to serve assets. value will be appended to application `applicationContextPath` 
* `indexFile` : default asset to serve if the requested path doesn't have any asset name.
* `assetsName` : Assets name. Servlet will be registered with that name.
* `cached` : Caching will be activated or not `true/false`
* `type` : Asset servlet type. 3 asset servlet implemented from default.
  * `filesystem` : loads assets from the file system (outside of jar)
  * `classpath` : loads assets from the resources folder (inside of jar)
  * `http` : loads assets from an external http server.
  
  



