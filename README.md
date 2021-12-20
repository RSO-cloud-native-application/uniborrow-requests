# RSO: Requests microservice

## Prerequisites

```bash
docker run -d --name pg-requests -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=requests -p 5432:5432 postgres:13
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar requests-api-1.0.0-SNAPSHOT.jar
```
Available at: localhost:8080/v1/requests

## Docker commands
```bash
docker build -t requests .   
docker requests
docker run requests  
docker tag requestsmp6079/requests   
docker push mp6079/requests
```

## Docker and environmental variables 
```bash
docker run --help
docker run -e MY_VAR=123
docker ps
docker build -t rso-dn
docker network ls
docker network create rso
docker network rm rso
docker rm -f pg-requests
docker run -d --name pg-requests -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=image-metadata -p 5432:5432 --network rso postgres:13
docker inspect pg-requests
docker run -p 8080:8080 --network rso -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-requests:5432/requests rso-dn
```

## Consul
```bash
consul agent -dev
```
Available at: localhost:8500


## Kubernetes
```bash
kubectl version
kubectl --help
kubectl get nodes
kubectl create -f requests-deployment.yaml 
kubectl apply -f requests-deployment.yaml 
kubectl get services 
kubectl get deployments
kubectl get pods
kubectl logs requests-deployment-6f59c5d96c-rjz46
kubectl delete pod requests-deployment-6f59c5d96c-rjz46
```
