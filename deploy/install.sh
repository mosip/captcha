#!/bin/bash
# Installs captcha-service helm charts
## Usage: ./install.sh [kubeconfig]

if [ $# -ge 1 ] ; then
  export KUBECONFIG=$1
fi

NS=captcha
CHART_VERSION=12.0.2

echo Create $NS namespace
kubectl create ns $NS

function installing_captcha() {
  echo Istio label

  kubectl label ns $NS istio-injection=disabled --overwrite
  helm repo update

  echo Copy configmaps
  sed -i 's/\r$//' copy_cm.sh
  ./copy_cm.sh

  echo Installing captcha
  helm -n $NS install captcha mosip/captcha --version $CHART_VERSION

  echo Installed captcha service
  return 0
}

# set commands for error handling.
set -e
set -o errexit   ## set -e : exit the script if any statement returns a non-true return value
set -o nounset   ## set -u : exit the script if you try to use an uninitialised variable
set -o errtrace  # trace ERR through 'time command' and other functions
set -o pipefail  # trace ERR through pipes
installing_captcha   # calling function
