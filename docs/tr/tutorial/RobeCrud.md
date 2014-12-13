# Robe.Crud

CRUD, sunucu tarafında ve önyüzde hazırlanması uzun zaman alan CRUD ekranları bu modül sayesinde çok kısa sürede bir uygulama arayüzünden oluşturulabilmektedir. Ekranlarının oluşması için, CRUD işlemlerinin yapılacağı Java sınıflarının tanımlanması gerekmektedir. CRUD modülü, tanımlanan bu sınıflar vasıtasıyla, RESTful web servislerini ve önyüzdeki JavaScript dosyalarını belirtilen dizinler içine başarı ile oluşturur. Önyüzde şuan jQuery tabanlı KendoUI ‘a destek verilmektedir.

####1-RobeCrudGUI
RobeCrudGUI seçilen dosya yolunda bulunan Pojolardan **RESTful** web servislerini belirtilen dizin içerisine oluşturulmasını sağlar.

####2-RobeHtmlCrudGUI
RobeHtmlCrudGUI seçilen dosya yolunda bulunan Pojolardan önyüzdeki **JavaScript** dosyalarını Robe yapısına uygun DataSource,Model,Html ve View dosyalarının belirtilen dizin içerisine oluşturulmasını sağlar.

CRUD yapılacak Pojolar için dikkat edilmesi gerekenler;

* Pojo sınıfları üzerinde `@Entity` olması aksi taktirde CRUD bu pojoyu dikkate almaz.
* Pojo sınıfının her bir field’ı üzerinde `@Column` verilmesi.
* @Column içersine  `name` verilmesi.

CRUD ile en iyi sonuçları alma için diğer tavsiyeler;

* @Column içerisindeki `unique` değerinin verilmesi.
* @Column içerisindeki `length` değerinin verilmesi. 
* @Column içerisindeki `nullable` değerinin verilmesi.

Bunlar ne anlama geliyor;
* `unique` değeri true verildiğinde CRUD create servislerinde bu değerin unique olmasını kontrol edecek.Unique olmaması halinde hata mesajı verecek kodu ve mesajı eklemektedir.
* `length` alanı JavaScript model içerindeki validation için gereklidir. Default olarak 255 verilmektedir.
* `nullable` javaScript model içerisindeki validation için gereklidir. false olması halinde zorun alan olacak ve boş geçilmesi engellenecektir.

#####RobeCrudGUI Nasıl Kullanılır ?
* Öncelikle Entity lerinizin bulunduğu projeyi seçiniz


![Crud](http://goo.gl/lmIMrQ)

* Proje seçildikten sonra path altındaki entity ler listelenecektir.


![Crud](http://goo.gl/juI9iY)

* Resource ve dao çıktılarının olacağı yeri seçiniz ve package giriniz.Çıktı sizin verdiğiniz package.dao ve package.resource şeklinde olacaktır.Oluşturmak istemediğiniz entity için seçimi kaldırabilirsiniz.

![Crud](http://goo.gl/m42x0f)

* `Generate` butonuna basınız.
Başarılı bir şekilde generate edildikten sonra output klasörüne baktığımızda dao ve resource adında iki klasör oluşturulmaktadır. Herhangi bir idea ile açtığınızda bunlar package olarak görünecektir.

![Crud](http://goo.gl/J9L3NN)

Dao klasörünün altında generate ettiğiniz entity lere ait daolar listenelecektir.

![Crud](http://goo.gl/eYOk3Y)

*TestPojoDao çıktısı ;*

![Crud](http://goo.gl/KQtyUT)


#####RobeHtmlCrudGUI Nasıl Kullanılır ?

* Öncelikle Entity lerinizin bulunduğu projeyi seçiniz. Seçim yaptıktan sonra path altındaki entity ler listelenecektir. Oluşturmak istemediğiniz entity için seçimi kaldırabilirsiniz.

![Crud](http://goo.gl/juI9iY)

* Output için klasör seçin ve proje adını giriniz. Proje adınızı girmenizin nedeni robe hiyerarşisine uygun klasör yapısı oluşturmak ve oluşturulacak dosyalarda importları düzgün tanımlayabilmektir.

![Crud](http://goo.gl/SkE929)

* `Generate` butonuna basınız.
Başarılı bir şekilde generate edildikten sonra output klasörüne baktığımızda html ve js adında iki klasör oluşturulmaktadır.

![Crud](http://goo.gl/Hkx8UI)

Html klasörü içerisinde herbir ekran için view sayfalarına uygun html sayfaları oluşturulmaktadır.

![Crud](http://goo.gl/szWDfQ)

*Örnek TestPojoManagement.html dosyası ;*

![Crud](http://goo.gl/S62WGX)

js ->*Proje adınız* altında data,Model ve view dosyaları mevcuttur.

![Crud](http://goo.gl/ZNpd3V)

data klasörü altında Datasources mevcuttur. *Örnek TestPoojoDataSource :*

![Crud](http://goo.gl/K8gFPK)

Model klasörü altında modelleriniz mevcuttur. *Örnek TestPojoModel ;*

![Crud](http://goo.gl/unRKTJ)

view klasörü altında view *javaScritp* dosyaları mevcuttur.

![Crud](http://goo.gl/7kt80F)

Örnek bir *TestPojoManagementView* içindeki kendoGrid;

![Crud](http://goo.gl/AD1C0n)


