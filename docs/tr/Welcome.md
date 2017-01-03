# Robe.io
#### *A brand new robe for dropwizard.*
----------------------------------------
## Robe Hakkında

Robe, günümüz teknolojilerine hızlı uyum sağlayarak, bir çok bilinmeyen ya da çekinilen teknolojiyi kurumsal ve bireysel projelerde kullanılabilecek basitliğe ve kararlılığa ulaştırmayı hedefleyen bir altyapıdır.

Temel anlamda yeni bir projeye başlamak için yaşanılan birçok sorunu aşmak ve verimi arttırmak amacı ile yeni dönem java teknolojilerini ve mimarilerini bir çatı altında toplamak , bunları işe yarar ve en önemlisi kullanılabilir kılmak amacı ile Robe projesinin temel hedifidir.

Projenin ana motivasyonu kolaylık ve anlaşılırlık ilkeleri ile belirlenmiş olup bunun yanında yazılım ektöründe uzun yıllardır standart olarak kabul edilmiş teknolojiler kullanılarak güncelliği ve güvenilirliği sağlamaktır.

Robe Admin Desteklediği Tarayıcılar: ([BrowserStack](http://www.browserstack.com)  üzerinde testleri yapılmıştır.)
                                  
- [x] Internet Exporer 8.0 +
- [x] Chrome 14.0 +
- [x] Firefox 4.0 +
- [x] Safari 4.0 +
- [x] Opera 15.0 +

##Başlangıç
Robe Temel Fonksiyonları:

* **Robe.Auth :**  Bütün kısıtlamalar ve yetkilendirmeler servis bazında sağlanmakta olup, kullanıcıya ekran kısıtlamanın yanında seçilen servisin yetkisiz kullanılamaması gibi güvenlik opsiyonları getirilmiştir.
* **Robe.Quartz :** Uygulama üzerindeki bütün zamanlanmış görevlerin yerine getirilmesi için quartz hJobInfo kütüphanesi eklenmiştir. Zamanlanmış görevler bu yapı sayesinde tam olarak platform bağımsız çalışmaktadır. Görev planlanması Java Annotaions kullanılarak yapılmaktadır. Tek bir annotation ile belirtilen sınıfa, görev ve zaman ataması yapılabilir.
* **Robe.Common :** Bu modül, Robe içerisindeki diğer tüm modüllerin ortak kullanım alanı olarak tanımlanabilir. Özel exception işleme, veri transfer nesneleri, sunucu metrik bilgileri ve özel dosya işlemleri gibi bir çok sınıfı barındırır.
* **Robe.Convert :** Convert kütüphanesi tam anlamıyla bir haritalama metodolojisi üzerine kurgulanmıştır. İçeriği hakkında bilgi sahibi olunmayan fakat bir Java Sınıfı ile tanımlanabilen bir dosya(excel, xml, csv, tsv, json vs.) okunarak, kod geliştiricinin yine bir Java sınıfı ile tanımladığı istediği formatta dosya biçimine dönüştürülebilir. Veri kontrolleri Java Annotations kullanılarak yapılır, bu sayede kontrol işlemleri için harici kod yazmaya gerek kalmamaktadır. Convert kütüphanesi başarılı ve anlaşılır exception işleme yöntemleri ile dosya dönüşümlerinde hata oluşursa, hatanın nereden geldiğini başarılı bir biçimde göstermektedir.
* **Robe.Crud :** Crud kütüphanesi Robe uygulama çatısının en önemli parçalarıdır. Özellikle sunucu tarafında ve önyüzde hazırlanması uzun zaman alan CRUD ekranları bu modül sayesinde çok kısa sürede bir uygulama arayüzünden oluşturulabilmektedir. Ekranlarının oluşması için, CRUD işlemlerinin yapılacağı Java sınıflarının tanımlanması gerekmektedir. CRUD modülü, tanımlanan bu sınıflar vasıtasıyla, RESTful web servislerini ve önyüzdeki JavaScript dosyalarını belirtilen dizinler içine başarı ile oluşturur. Önyüzde şuan jQuery tabanlı KendoUI ‘a destek verilmektedir.
* **Robe.Guice :** Robe.Guice nesne bağımlılıklarının yönetimi için kullanmış olduğumuz yapıdır. Kolay yönetilebilir bir uygulama arayüzü sağlamaktadır.
* **Robe.Hibernate :** Robe 'un Hibernate kütüphanesi, veritabanı işlemlerini(veri ekleme, silme, güncelleme, silme) kolaylaştırmak ve DBMS bağımsız çalışmasını sağlayan kütüphanedir. Veritabanında, tabloları belirtecek olan Java sınıflar Robe.Hibernate sınıfının sağlamış olduğu Base sınıflardan miras alarak tasarlanır. Bu sayede sunucu çalışmaya başladığında Robe.Hibernate gelişticinin fazladan emek sarf etmesini önleyerek, tanımladığı Java sınıflarını veritabanı tablolarına dönüştürür. Ayrıca veri erişim nesnerleri sayesinde, veriler en az maaliyet ile yönetilebilmektedir. Örneğin, sadece bir kaç satır kod ile CRUD ekranları yönetilebilmektedir. 
* **Robe.Mail :** Bu modül, ön tanımlı ayarlar ile elektronik posta gönderim işlemlerini kolaylaştırmak üzerine kurgulanmıştır. Temelde kullanıcı işlemlerinde(yeni kullanıcı oluşturma, şifre sıfırlama ve ilk oluşturma vs…) ve düzenli gönderilen elektronik postalarda kullanılmaktadır. Gönderilecek olan e-posta şemaları da veritabanında tutulmakta ve yönetim panelinden düzenlenebilmektedir.
* **Robe.MQ :** Asenkron ve senkron özelliklerine sahip  iç ve dış olmak üzere queue entegrasyonlarında kolaylık sağlamaktadır.
* **Robe.AdminPanel** : Robe tabanlı uygulamalarda, sunucu tarafında uygulama geliştirme sürecini hızlandıracak olan çözümler olduğu gibi sunucu tarafınının önyüzden yönetimi  kolaylaştırmak amacıyla bir kullanıcı arayüzü de sunulmaktadır. Bu arayüz, kullanıcı, rol, izin, mail ve zamanlanmış görevlerin yönetimi sağlamaktadır. Robe.AdminPanel şuanda jQuery tabanlı Kendo UI kullanılarak özgün bir arayüz ile geliştirilmiş olup, sunucu metrik bilgilerini de bu arayüzden takip edebilmekteyiz. Ayrıca bu arayüz sadece Robe ‘un yönetiminin sağlanmasının yanı sıra, geliştirici eğer isterse kendi uygulamasının menülerini de buraya yerleştirerek uygulama geliştirmeye devam edebilir. Bu sayede içerik yönetimi tek bir panelden yapılabilmektedir. Eğer uygulamaya başka bir arayüzden devam etmek istenilirse, servis ayarlarında bu belirtilerek geliştirme yapılabilir.