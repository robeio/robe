# robe-auth
---
Dropwizard için kimlik doğrulama ve yetkilendirme paketidir.TokenBasedAuthBundle ile birlikte uygulanmıştır.

## Motivasyon
Yarattığınız bir auth bundle aşağıdaki özellikleri sağlar.

* Kolay kullanım
* Kolay yapılandırma
* Kullanıma hazır (TokenBasedAuthBundle uygulanması)
* Ön bellek desteği

## Başlarken

Robe-auth kimlik doğrulaması modülünü kullanmak için 4 adımı tamamlamanız gerekmektedir.

* Bağımlılığı ekleyin (Örnek:Maven)

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-auth</artifactId>
  <version>0.5.0</version>
</dependency>
```

* Özelliklerini kendinize göre yml içinde yapılandırın.

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
   
* Uygulamanız içerisinde bootstrap e yeni bir bundle olarak ekleyin.

```java
TokenBasedAuthBundle<T> authBundle = new TokenBasedAuthBundle<T>();
bootstrap.addBundle(authBundle);
```
* `@Auth` annotation ı ile servislerinizi güvenli hale getirin

```java
@GET
@UnitOfWork
@Path("{id}")
public String get(@Auth Credentials credentials, @PathParam("id") String id) {
...
```
Artık istekler için kimlik doğrulamasını uyguladınız. Kullanıcı yetkileri ve izinleri üzerinden kontrolünüzü sağlamaya hazır.

## Detaylar
Yapılandırma, kullanım, varsayılan ayarlar aşağıda açıklanacaktır.
### Yapılandırma
`TokenBasedAuthBundle` yapılandırma ayarlarını yml içinden okumaktadır.Örnek aşağıdaki gibidir 
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
Örnekte görüldüğü gibi token parametrelere göre çalışmakta ve rastgele `PBEWithMD5AndTripleDES` algoritmasına göre şifrelenmektedir. Çerezler hakkında daha fazla bilgi için [Wikipedia Http Cookie](http://en.wikipedia.org/wiki/HTTP_cookie) ziyaret ediniz.
Parametre detayları,
* `tokenKey` : Token için belirtilen çerez adı. Token işlemleri için kullanılacaktırç
* `domain` : Çerez etki adı
* `path` : Çerez yolu
* `maxage` : Çerezin maksimum geçerli olacağı saniye sayısı.
* `secure` : Çerez için güvenlik parametresi.
* `poolSize`: Şifreleme işlemleri için CPU çekirdek sınırı.. [Bakınız](http://www.jasypt.org/)
* `algorithm` : Kripto işlemleri için algoritma. Güçlü olması kırmayı zorlaştırır. [Bakınız](http://www.jasypt.org/)
* `serverPassword`: Token işlemleri için şifre. 
 * `auto`: Her sunucu için otomatik şifre üretme işlemi. An automatic parsword generation per server. Her düğüm için ayrı bir şifre alacaktır.
 * `aaaabbbb`: Verilen şifre token üretmek için kullanılacaktır. Aynı şifre düğümleri diğerlerini anlamlı hale getirecektir.
 
### Yanıtlar

`UNAUTHORIZED(401, "Unauthorized")`: Eğer kullanıcının yetkisi mevcut değilse.

`FORBIDDEN(403, "Forbidden")`: Gidilen path için yetkisi mevcut değilse.
  



