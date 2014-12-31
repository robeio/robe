# robe-convert
---
Convert bundle for dropwizard.It manages file importing and exporting between formats(xls, xlsx, xml, csv, tsv, json) to POJOs with the help of annotations. Annotations prevents the developer to write extra code for validating the procedure.

## Motivation
Creating a basic system for different libraries. Helping user to do the most important importing and exporting operations with one annotation.
## Getting started
You have to complete 3 steps in order to start using robe-convert bundle.
* Add dependency (Maven sample)

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-convert</artifactId>
  <version>0.5.0</version>
</dependency>
```

* Use `@Convert` annotation to makes you . Sample:

```java
public class SamplePojo {

    @Convert(order = 0, unique = true, title = "User Id")
    private int id;

    @Convert(order = 0)
    private String name;
...
```
Now it is ready for import&export operations with all formats below.
* User importer or exporter  to complete the operation.

```java
CSVImporter<SamplePojo> importer = new CSVImporter<>(SamplePojo.class);
List<SamplePojo> list = importer.importStream(new FileInputStream(outputFile.getPath()));
```

**!!! You can look at the test classes for sample usage !!!**

## Details
Usage and details will be explained below.
### Libraries
* xls : [Apache POI](http://poi.apache.org/)
* xlsx: [Apache POI](http://poi.apache.org/) 
* json: [Jackson](https://github.com/FasterXML/jackson)
* xml : [Jackson](https://github.com/FasterXML/jackson) (Different configuration)
* csv : [SuperCSV](http://supercsv.sourceforge.net/)
* tsv : [SuperCSV](http://supercsv.sourceforge.net/)

### Usage
Please look at the test classes for sample usage. If you really want to see a quick demo ...
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