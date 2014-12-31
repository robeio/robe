# robe-convert
---
Dropwizard projeleri için dönüştürme modülüdür. Dosyaların formatlar arasında(xls, xlsx, xml, csv, tsv, json) dönüşümlerini sağlamaktadır. Dönüşüm işlemlerini POJO'lar içerisindeki annotation'lar üzerinden yapmaktadır. Bu sayede geliştirilerin fazladan kod yazmasını önler.

## Motivasyon
Farklı kütüphaneler için temel bir sistem oluşturmak ve kullanıcılar için çok önemli olan dönüşüm işlemlerini tek bir annotation ile yapmak.
## Başlarken

Robe-convert kullanmak için 3 adımı tamamlamanız gerekmektedir.

* Bağımlılığı ekleyin (Örnek:Maven)

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-convert</artifactId>
  <version>0.5.0</version>
</dependency>
```

* `@Convert` annotation ile dönüştürme ayarlarını belirtin. Örnek:

```java
public class SamplePojo {

    @Convert(order = 0, unique = true, title = "User Id")
    private int id;

    @Convert(order = 0)
    private String name;
...
```
Bununla beraber artık tüm formatlarda dönüştürme işlemleri için POJO nuz hazır.

* İmport yada export olduğunu belirtin ve dönüştürme işlemlerinizi tamamlayın.Örnek:

```java
CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
List<SamplePojo> list = importer.importStream(new FileInputStream(outputFile.getPath()));
```

**!!! Test sınıflarını inceleyerek bütün formatlar için örnekleri inceleyebilirsiniz !!!**

## Detaylar
Kullanılan kütüphaneler ve kullanımı aşağıdadır.
### Kütüphaneler
* xls : [Apache POI](http://poi.apache.org/)
* xlsx: [Apache POI](http://poi.apache.org/) 
* json: [Jackson](https://github.com/FasterXML/jackson)
* xml : [Jackson](https://github.com/FasterXML/jackson) (Faklı yapılandırma)
* csv : [SuperCSV](http://supercsv.sourceforge.net/)
* tsv : [SuperCSV](http://supercsv.sourceforge.net/)

### Kullanımı
Eğer gerçekten hızlı bir demo görmek istiyorsanız,örnek kullanım için test sınıflarına bakınız. 

```java
CSVExporter<SamplePojo> exporter = new CSVExporter(SamplePojo.class);
exporter.exportStream(outputStream, TestData.getData().iterator());

CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
List<SamplePojo> list = importer.importStream(new FileInputStream(outputFile.getPath()));

XLSExporter<SamplePojo> xlsExporter = new XLSExporter(SamplePojo.class, false);
xlsExporter.exportStream(outputStream, TestData.getData().iterator());

XLSImporter<SamplePojo> xlsImporter = new XLSImporter(SamplePojo.class, false);
List<SamplePojo> list = xlsImporter.importStream(new FileInputStream(outputFile.getPath()));
```