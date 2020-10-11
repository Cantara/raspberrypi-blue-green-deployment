# raspberrypi-blue-green-deployment
Enable continous deployment of java services on a cheep hw.

## Getting started

```
1. mvn clean install
2. ./start-nodes.sh
3. ./blue-green-deployment.sh
```

## Blue/Green deployment of two versions
![Blue/Green deployment](./doc/blue-green-sequence.png)

### Current MasterStatus
```
curl -X GET http://localhost:5050/blueGreenService/masterstatus
```
Result:
```
{
  "masterstatus": ".."
}
```

### Request Primary
```
curl -X PUT http://localhost:5050/blueGreenService/masterstatus/requestPrimary
```
Result:
* 202 OK - the node will drain and assume masterstatus=fallback
* 102 You need to wait
* 412 

## Warmup
![Warmup](./doc/warmup.png)

## Development
```
mvn clean install
java -jar target/blueGreenService.jar
curl -X GET http://localhost:5050/blueGreenService/health
```
