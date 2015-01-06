# robe-mail
---
This module handles mail template creation, mail queue operations and mail authentication etc. These operations are handled in a developer friendly approach.
## Motivation
Creating a mail bundle which can provide following points. 
* easy to understand 
* configurable 
* templating solution included (Freemarker)
* queue support included.

## Getting Started
You have to complete 4 steps in order to start mail bundle
* Add dependency (Maven sample)

```xml
<dependency>
  <groupId>io.robe</groupId>
  <artifactId>robe-mail</artifactId>
  <version>0.5.0</version>
</dependency>
```
* Decide the properties. It is directly matching with java Mail API.

```yml
mail:
 host: smtp.gmail.com
 port: 587
 auth: true
 username: username@gmail.com
 password: password
 tlsssl: true
```
   
* Add bundle to the bootstrap at you Aplication

```java
bootstrap.addBundle(new MailBundle<T>());
```
* Use `MailSender` class whenever you want to send mail

```java
MailItem mailItem = new MailItem();
mailItem.setBody("Some mail content");
mailItem.setReceivers(entity.getUsername());
mailItem.setTitle("Robe.io Password Change Request");
MailManager.sendMail(mailItem);
```

* Or use mail templates to set mail body. (Freemarker)

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
Now it is ready to control all request paths according to you provided stores. (Yes you will provide stores for user roles and permissions.)

## Details

### Mail Sending Operation
Here what happens at Queue and consumer thread implementation when you call `MailManager.sendMail(mailItem);` 
* Sender thread wakes up on every item insert and works until queue goes empty.
* Every item in queue will be consumed only once. 
* If any error occurs it is the developers responsibility to handle it. {@link io.robe.mail.MailEvent}.