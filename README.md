# Nacos Example base v2.0.4

## Dynamic config
### 0. create config in nacos
    nacos-example.yml >>
    version: v3.0
    user:
        name: Zhang San
        age: 18


### 1. Send request
$ curl -X GET http://127.0.0.1:8080/config/version -s
v1.0
### 2. Add nacos-example.yml in nacos, content is
version: v2.0
### 3. Send request again
$ curl -X GET http://127.0.0.1:8080/config/version -s
v2.0

## Discovery service
### 1. Add metadata to service 
    metadata:
        version: v1
        command-codes: ADD-ONU,ADD-PONVLAN,DEL-ONU,DEL-PONVLAN
### 2. Send request
$ curl -X GET http://127.0.0.1:8080/tl1/v1/ADD-ONU
v1/ADD-ONU
## Nacos with RestTemplate
    Route
    restTemplate.getForObject("http://" + serviceName + "/tl1/" + command, String.class);
## Nacos with OpenFeign

