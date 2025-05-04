#!/bin/bash

# Display title
echo "====================================="
echo "   Microservices Deployment with Helm"
echo "====================================="

# Check if Minikube is running
echo "Checking Minikube status..."
if ! minikube status | grep -q "Running"; then
  echo "Minikube is not running. Starting Minikube..."
  minikube start
else
  echo "Minikube is already running."
fi

# Set namespace
NAMESPACE="ticket-booking"
minikube kubectl -- create namespace "$NAMESPACE" 2>/dev/null || echo "Namespace $NAMESPACE already exists"

# Deploy Helm chart
echo "====================================="
echo "Deploying ticket-booking application..."
echo "====================================="
helm install ticket-booking ./helm/ticket-booking --namespace "$NAMESPACE" --create-namespace

# Check deployment status
echo "====================================="
echo "     Checking Pods status          "
echo "====================================="
minikube kubectl -- get pods -n "$NAMESPACE"

echo "====================================="
echo "    Checking Services status       "
echo "====================================="
minikube kubectl -- get services -n "$NAMESPACE"

echo "====================================="
echo "     Deployment completed!         "
echo "====================================="