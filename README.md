# Application de démo Beryl, module Batch

## Documentations 
Les documents relatifs à la mise en place de Béryl (dmex, bdd, ...) sont présent [ici](https://harmoniemutuelle.sharepoint.com/:f:/s/EAND/EuvYQWdtun9Pkb6YKmhzALwBas-FXuoViA64_yPNOaKLqA?e=ZPL91o).

## Stack utilisée
La stack utilisée est la dernière basée sur le seed [2.0.x](https://github.com/hm-it/seed-springboot-back/releases) et la version [2.0.x](https://github.com/hm-it/fwk-spring-hm-batch/releases) du Framework Batch.

## Sécurisation
Le contexte de sécurité (`security.fwkHm.contextPath`) __doit être identique__ au contexte batch (`hm.batch.http.endpoint`)

```properties
# Context-path securisé
security.fwkHm.contextPath=/batch
# Déclenchement par HTTP
hm.batch.http.endpoint=/batch
```

L'authentification des endpoints de lancement du batch est validée par un token JWT.

L'authentification est faite par l'appelant (beryl-api) avec un secret commun (`security.authentication.jwt.secret`) entre l'appelant (_beryl-api_) et le batch (_beryl-batch_).

D'un point de vue architecture, l'authentification __NE DOIT PAS ETRE__ faite en direct sur le batch (_beryl-batch_).

## Documentations
* [Migration vers Spring Boot 2.x](https://github.com/hm-it/seed-springboot-back/blob/master/doc/migration.md)