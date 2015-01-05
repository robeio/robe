# Robe.io

### A brand new robe for Dropwizard.
Motivation
-------------------------------
Robe is a perspective which eliminates the complications and problems occurred in a software development chain by following the approach of Dropwizard.
Robe solves these complication by including various production ready libraries along with Dropwizard.

**Robe.Auth :** This module includes user role and permission mechanisms. We tried to implement ESAPI interfaces to handle all difficult tasks at enterprise authentications. By getting inspired with Dropwizard we implemented a token based authentication system. 

**Robe.Quartz :** This module includes all the solutions of job related issues with the help of quartz library. We tried to make it easier with new annotations and a new management screen. You can manage all your quartz jobs with a management UI and persistent DB store.

**Robe.Common :** This module is the common library for all the robe modules. It includes a problem specific exception handler, common data transfer objects and file operations.

**Robe.Convert :** This module is designed for mapping methodology. It manages file importing and exporting between formats(xls, xlsx, xml, csv, tsv, json) to POJOs with the help of annotations. Annotations prevents the developer to write extra code for validating the procedure. 

**Robe.Crud :** Crud analyses the package given by the user and finds the entity classes. Crud module creates the RESTful web services and JavaScript files accordingly with the previously given entity classes. For the time being in the front-end part, Crud is backed by KendoUI, based on JQuery. Support for different JavaScript libraries is still on development.

**Robe.Guice :** Guice is the open source Google Java library used for dependency injection. This package also includes all the auto scanning for resources, entities, tasks, managed commands, providers, injectable providers and health checks.

**Robe.Hibernate :**  This module reads the user's configuration file and maps the entity classes within the given packages and specifically stated class names to Dropwizard's hibernate module.[Details](./docs/en/modules/robe-hibernate.md)

**Robe.Mail :** This module handles mail template creation, mail queue operations and mail authentication etc. These operations are handled in a developer friendly approach.(Not fully implemented yet)
 

**Robe.AdminPanel :** This module handles admin operations which are assigned to the administrators. These operations allows controlling UI, users, roles, permissions, mails and scheduled tasks. AdminPanel is developed with KendoUI and allows the user to observe server metric information. In case of a need in changing the UI, the developer can place their application's menus in this panel. 

To be continued...
-------------------------------


Robe, supported by **Atlassian**, **IntelliJ** and **BrowserStack**.
