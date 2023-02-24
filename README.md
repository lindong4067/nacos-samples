# Nacos Example base v2.0.4

## Dynamic config
### 1. Send request
$ curl -X GET http://127.0.0.1:8080/config/version -s
v1.0
### 2. Add nacos-example.yml in nacos, content is
version: v2.0
### 3. Send request again
$ curl -X GET http://127.0.0.1:8080/config/version -s
v2.0

## Discovery service

## Nacos with RestTemplate

## Nacos with OpenFeign

