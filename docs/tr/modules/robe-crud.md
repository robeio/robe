# robe-crud
---
Robe projeleri için basit bir şekilde CRUD ve JavaScript ekranlarının oluşturulması.
## Motivasyon
CRUD, sunucu tarafında ve önyüzde hazırlanması uzun zaman alan CRUD ekranları çok kısa sürede bir uygulama arayüzünden oluşturulabilmeli. Ekranlarının oluşması için, CRUD işlemlerinin yapılacağı Java sınıflarının tanımlanması gerekmektedir. CRUD modülü, tanımlanan bu sınıflar vasıtasıyla, RESTful web servislerini ve önyüzdeki JavaScript dosyalarını belirtilen dizinler içine başarı ile oluşturur. Önyüzde şuan jQuery tabanlı KendoUI ‘a destek verilmektedir.

## Başlarken
robe-crud modülünü kullanmak için jar dosyalarını indirmeniz gerekmektedir.
[RobeCrudGUI]() ve [RobeHtmlCrudGUI](../../robe-crud/disk/robe-crud-html-gui.jar?raw=true) linklerinden jar dosyalarını indirebilirsiniz.

* Pojo sınıfları üzerinde `@Entity` olması.

CRUD ile en iyi sonuçları alma için diğer tavsiyeler;

* Pojo sınıfının her bir alanına annotation olarak `@Column` verilmesi.
* @Column içersine  `name` verilmesi.
* @Column içerisindeki `unique` değerinin verilmesi.
* @Column içerisindeki `length` değerinin verilmesi. 
* @Column içerisindeki `nullable` değerinin verilmesi.

## Detaylar
Yapılandırma, kullanım ve tavsiyeler yer alacaktır.

### RobeCrudGUI
RobeCrudGUI seçilen dosya yolunda bulunan Pojolardan RESTful web servislerini belirtilen dizin içerisine oluşturulmasını sağlar.

### RobeHtmlCrudGUI
RobeHtmlCrudGUI seçilen dosya yolunda bulunan Pojolardan önyüzdeki JavaScript dosyalarını Robe yapısına uygun DataSource,Model,Html ve View dosyalarının belirtilen dizin içerisine oluşturulmasını sağlar.


##Kullanımı
### RobeCrudGUI 
Robe-crud-java-gui jar dosyasına çift tıklıyınız ve aşağıdaki adımları takip ediniz:

* `Select Project` ile entity sınıflarınızın yolunu seçiniz.
* `Select Output` ile üretilecek olan dosyaların yolunu seçiniz.
* `Packege` alanına üretilecek dosyaların paket adını giriniz.
*  Listelenen sınfılar arasında `Dao`,`Resource`,`Inject` ve `Auth` ile nelerin üretileceğini belirtiniz.
	* `Dao` entity sınıfı için `robe-hibernate` yapısına uygun olarak doa üretilip üretilmeyeceğini belirtir.
	* `Resource` entity sınıfı için `robe-hibernate` yapısına uygun olarak resource üretilip üretilmeyeceğini belirtir.
	* `Inject` entity için üretilecek resource içerisinde dao desteğinin inject yada constructor olacağına karar verilir.
	* `Auth ` entity için üretilecek resource içerisinde default olarak `robe-auth` ile kimlik doğrulaması gerektirip gerektirmeyeceğini belirtir.
* `Generate` butonuna tıklayınız.

İşleminizi başarıyla gerçekleştirdiniz.Şimdi output için seçtiğiniz dosya yolunuza gidiniz ve `sizin verdiğiniz package` altında `dao` ve `resources` adında iki adet klasörü kontrol ediniz.

### RobeHtmlCrudGUI

Robe-crud-html-gui jar dosyasına çift tıklıyınız ve aşağıdaki adımları takip ediniz:

* `Select Project` ile entity sınıflarınızın yolunu seçiniz.
* `Select Output` ile üretilecek olan dosyaların yolunu seçiniz.
* `Generate` butonuna tıklayınız.

İşleminizi başarıyla gerçekleştirdiniz.Şimdi output için seçtiğiniz dosya yolunuza gidiniz ve her bir entity için üretilen `robe-admin-ui`yapısındaki modülleri inceleyiniz.
