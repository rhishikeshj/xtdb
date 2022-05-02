# Helm chart to deploy xtdb with Kubernetes
This subdirectory contains the files and instructions needed to deploy [XTDB](https://xtdb.com) in a Kubernetes cluster along with it's dependencies.

Currently the Helm chart inside the `xtdb` directory, deploys and runs an [XTDB](https://xtdb.com) instance which uses Kafka as it's transaction store and Postgres as it's document store.

## Pre-requisites

1. A Kubernetes cluster, typically created with a tool like [minikube](https://minikube.sigs.k8s.io/docs/) or something more sophisticated.
2. [Helm](https://helm.sh/)
3. [Docker](https://www.docker.com/)

## Setup

### Build docker image

Let's start by building the Docker image for XTDB which is configured to use Kafka and Postgres.
The required files are
- `build-docker.sh`
- `Dockerfile`
- `deps.edn`
- `xtdb.edn`
- `logback.xml`

The `build-docker.sh` script builds the docker image with image name and image version taken from `env`.
So to build a version called `xtdb_kafka_jdbc:1.0.0` run the following command from the `build/kubernetes` dir.

```
IMAGE_NAME=xtdb_kafka_jdbc IMAGE_VERSION=0.1.0 sh build-docker.sh
```

**Note**

In order for the Kubernetes engine to find the local docker image, you might have to do some additional steps, for example with minikube, you have to run

```
eval $(minikube -p minikube docker-env)
```

Run `docker image ls` to make sure you can see the docker image with the correct tag in the list.

### Install Helm chart

Start of by making sure you have a separate namespace in which to install the Helm chart.

```
kubectl create ns juxt
```

Now, install the Helm chart

```
helm install extdb xtdb -n juxt
```

This will start Postgres, Kafka and XTDB. The first run might take some extra time, have patience :)


XTDB will use the Kafka topic `crux-tx-logs` and PG database `crux-document-store` for it's operations. Make sure to not use any of these for testing or debugging purposes.
If you want to customize these values, head over to `xtdb.edn`, update the values and start from Step 1 i.e *building the docker image*

### Access the service

To make sure we can access the [XTDB HTTP API](https://docs.xtdb.com/clients/1.20.0/http/) on our local machine, forward the correct port from the Kubernetes cluster

```
kubectl port-forward svc/extdb -n juxt 3000:3000
```

*If you want to access Kafka and Postgres for some debugging, check the `port-forwards.sh` file*

Now open [XTDB HTTP API](http://localhost:3000/_xtdb/query) in your browser and query away!
