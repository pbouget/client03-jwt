# Client03-jwt : Démo Spring Boot Security et JWT

Projet Gradle à importer dans Eclipse.
## EER correspondant

![EER](images/client03-jwt.png)

- *adresse* et *client* : 2 tables du TP client03 sur le mapping hibernate.
- *user* et *app_user_role_list* pour la gestion de l'authentification.

A la racine, vous avez un fichier *json* à importer dans Postman pour tester l'application.

## Modifications à faire dans *application.properties*

- Database à créer ou remplacer par une BD existante.
- Remplacez le *username* et le *password*
- Conservez ou enlevez le numéro de port *server.port=8088* dans le fichier *application.properties*
- Pensez à faire un refresh des dépendances du Gradle

```java
#===============================
# base de données MySQL
# ===============================
spring.datasource.url=jdbc:mysql://localhost:3306/jpa-client?useSSL=false&serverTimezone=CET
spring.datasource.username=test
spring.datasource.password=test
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

A vous de jouer !