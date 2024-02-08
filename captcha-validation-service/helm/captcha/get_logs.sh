#!/bin/sh
# pod name
kubectl -n kernel logs -f $1 | grep -v "/v1/captcha/actuator/health" | grep -v "/v1/captcha/actuator/prometheus"
