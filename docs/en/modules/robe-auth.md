# robe-auth
---
Authhentication and authorization bundle for dropwizard with TokenBasedAuthBundle implementation.
## Motivation
Creating an auth bundle which can provide following points. 
* easy to understand 
* configurable 
* ready to use (TokenBasedAuthBundle impl.)
* cache support

## Getting Started
You have to complete 4 steps in order to start using token based authentication.
* Add dependency (Maven sample)

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-auth</artifactId>
  <version>0.5.0</version>
</dependency>
```

* Decide the properties. and how to serve in yml.

```yml
auth:
  tokenKey: auth-token
  domain: 127.0.0.1
  path: /
  maxage: 60 #s
  secure: false
  poolSize: 4 # parallel with cpu cores
  algorithm: PBEWithMD5AndTripleDES
  serverPassword: auto # auto for uuid, if it is cluster use custom password
   ```
   
* Add bundle to the bootstrap at you Aplication

```java
TokenBasedAuthBundle<T> authBundle = new TokenBasedAuthBundle<T>();
bootstrap.addBundle(authBundle);
```
* Use `@Auth` annotation to make your resource secure

```java
@GET
@UnitOfWork
@Path("{id}")
public String get(@Auth Credentials credentials, @PathParam("id") String id) {
...
```
Now it is ready to control all request paths according to you provided stores. (Yes you will provide stores for user roles and permissions.)

## Details
Configuration, usage, default asset servlets will be explained below. 
### Configuration
`TokenBasedAuthBundle`will read configurations from YML.
```yml
auth:
  tokenKey: auth-token
  domain: 127.0.0.1
  path: /
  maxage: 60 #s
  secure: false
  poolSize: 4 # parallel with cpu cores
  algorithm: PBEWithMD5AndTripleDES
  serverPassword: auto # auto for uuid, if it is cluster use custom password
   ```
As you see we defined a token which will work with the given cookie properties and encrypted with a random password using `PBEWithMD5AndTripleDES` algorithm.
Visit [Wikipedia Http Cookie](http://en.wikipedia.org/wiki/HTTP_cookie) for more info about cookie properties.
Field details,
* `tokenKey` : Name of the token cookie. This will be used for token operations.
* `domain` : domain for the cookie. 
* `path` : path for the cookie
* `maxage` : max age for the cookie in seconds
* `secure` : secure parameter for cookie.
* `poolSize`: CPU core limit for crypto operations. [See](http://www.jasypt.org/)
* `algorithm` : Algorithm for crypto operations. Stronger it gets harder it takes. [See](http://www.jasypt.org/)
* `serverPassword`: Password for token operations. 
 * `auto`: An automatic parsword generation per server. All nodes will take different password.
 * `aaaabbbb`: Given password will be used for token generation. Same passwords will make nodes understand each other.
 
### Responses



`UNAUTHORIZED(401, "Unauthorized")`: If user is unauthhorized.

`FORBIDDEN(403, "Forbidden")`: If user has no permission for the given path.
  



