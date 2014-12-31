# robe-assets
---
Dropwizard projeleri için gelişmiş ve kolay kullanım için asset desteği sağlamaktadır.
## Motivasyon
Yarattığınız bir asset bundle aşağıdaki özellikleri sağlar.

* Kolay anlaşılabilir 
* Kolay yapılandırma yapılabilir
* Ortak ihtiyaçları karşılayabilir (classpath,filesystem,http)
* Önbellek desteği sağlamaktadır

## Başlarken
Robe-assets ile servise başlamak için 3 adımı tamamlamanız gerekmektedir. 

* Bağımlılığı ekleyin (Örnek:Maven)

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-assets</artifactId>
  <version>0.5.0</version>
</dependency>
```

* Nerde ve nasıl servis hizmeti edeceğine yml içerisinde karar verin.

```yml
assets:
 - resourcePath: /SomewhereonyourFileSystem/Project/UI/src/
   uriPath:      /ui/
   indexFile:    index.html
   assetsName:   io.robe.sample.ui
   cached:       true # true/false
   type: filesystem # use "classpath" or "filesystem" or "http"
   ```
   
* Uygulamanız içerisinde bootstrap e yeni bir bundle olarak ekleyin.

```java
   bootstrap.addBundle(new AdvancedAssetBundle<T>());
```
Şimdi `/SomewhereonyourFileSystem/Project/UI/src/` klasörü uygulamanız içinde `sampleapp/ui` olarak hizmet vermeye hazır.

## Detaylar
Yapılandırma, kullanım, varsayılan asset servis aşağıda açıklanacaktır.
### Yapılandırma
`AdvancedAssetBundle` yapılandrmalarını YML içerisinden okumaktadır. Varsayılan olarak `robe-admin` altından servis edilmektedir. Birden fazla asset servis edebilirsiniz. Örnek yapılandırma aşağıdaki gibidir.
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
Örnek yapılandırmada görüldüğü gibi 3 farklı tip olarak ile asset servis edebilirsiniz.
Alanların detaylı açıklaması aşağıdaki gibidir:

* `resourcePath ` : Kaynakları okumak için kullanılan kaynak yoludur.
* `uriPath` :  Asset i servis etmek için kullanılan url yoludur.Değer uygulamanın `applicationContextPath` içine eklenir.
* `indexFile` : Asset i varsayılan olan servis eden dosyadır.Eğer varsayılan olarak servis edilen dosyanız yoksa (index.html/index.php...), bu isim ile servis edebilirsiniz.   
* `assetsName` : Assets adı. Sunucu uygulamasına bu isim ile kayıt edilir.
* `cached` : Ön belleği aktif yada pasif eder`true/false`.
* `type` : Asset servisi tipi. Varsayılan olarak 3 adet tip uygulanabilir.
  * `filesystem` : Asset leri dosya sisyeminden yükler. (jar hariç)
  * `classpath` : Asset leri uygulamanızın kaynak dosyasından yükler (jar dahil)
  * `http` : Asset leri harici http sunucusundan yükler.
  
  



