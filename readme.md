# ProjectT
> link to [front](https://github.com/marinerbob/s-project)

Базовая серверная часть для портала по заказу суши

## Installing / Getting started

Add these VM options params to run config to specify database connection
```
-Dspring.datasource.url={url}
-Dspring.datasource.username={db_login}
-Dspring.datasource.password={db_pass}
```
To generate JOOQ data with maven run 
```
jooq-codegen:generate
```