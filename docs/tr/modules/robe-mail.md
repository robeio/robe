# robe-mail
---
Bu modül mail şablonu oluşturma, mail kuyruk işlemleri ve mail ile kimlik doğrulama vb işlemleri geliştirici dostu bir yaklaşım ile ele alır.
## Motivasyon
Yarattığınız bir mail bundle aşağıdaki özellikleri sağlar.
 
* Kolay Kullanım
* Kolay Yapılandırma 
* Şablonlama çözümü (Freemarker)
* Kuyruk desteği

## Başlarken
Mail bundle kullanmanız için aşağıdaki 4 adımı tamamlamanız gerekmektedir.

* Bağımlılığı ekleyin

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-mail</artifactId>
  <version>0.5.0</version>
</dependency>
```
* YML içerisinde özelliklerine karar verin. Bu özellikler doğrudan Java Mail API ile eşleşmektedir.

```yml
mail:
 host: smtp.gmail.com
 port: 587
 auth: true
 username: username@gmail.com
 password: password
 tlsssl: true
```
* Uygulamanız içerisinde bootstrap e yeni bir bundle olarak ekleyin.

```java
bootstrap.addBundle(new MailBundle<T>());
```
* Mail göndermek istediğiniz yerde `MailSender` ile gönderme işleminizi gerçekleştirin.

```java
MailItem mailItem = new MailItem();
mailItem.setBody("Some mail content");
mailItem.setReceivers(entity.getUsername());
mailItem.setTitle("Robe.io Password Change Request");
MailManager.sendMail(mailItem);
```

* Yada mail içeriği için template yapısını kullanın. (Freemarker)

```java
Template template = null;
Map<String, Object> parameter = new HashMap<String, Object>();
Writer out = new StringWriter();
String body = mailTemplateOptional.get().getTemplate();
template = new Template("robeTemplate", body, cfg);
parameter.put("name", entity.getName());
parameter.put("surname", entity.getSurname());
template.process(parameter, out);
mailItem.setBody(out.toString());
```
Şimdi mail gönderme işleminizi başarıyla gerçekleştirdiniz.

## Detaylar

### Mail Gönderme İşlemleri
`MailManager.sendMail(mailItem);` çağrıldığında kuyruk yapısı ve kuyruğu kullanan iş parçacığında gerçekleşen işlemler aşağıdaki gibidir:
 
 
* Gönderici iş parçacığı her yeni eleman eklendiğinde uyanır ve kuyruk boşalanana kadar çalışmaya başlar.  
* Kuruktaki her öğe yanlızca bir kez tüketiliyor olacak. 
* Herhangi bir hata oluşması halinde, bu hatanın işlenmesi geliştiricinin sorumluluğundadır. {@link io.robe.mail.MailEvent}.