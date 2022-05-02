#!/usr/bin/env bash
set -x
kubectl port-forward svc/postgres -n juxt 5432:5432 &
kubectl port-forward svc/extdb -n juxt 3000:3000 &
kubectl port-forward svc/kafka -n juxt 9094:9094 &
