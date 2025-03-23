#!/bin/bash

# Display title
echo "====================================="
echo "   Microservices Deployment Script   "
echo "====================================="

# Check if Minikube is running
echo "Checking Minikube status..."
if ! minikube status | grep -q "Running"; then
  echo "Minikube is not running. Starting Minikube..."
  minikube start
else
  echo "Minikube is already running."
fi

# Apply files in a logical order
deploy_component() {
  local component=$1
  echo "====================================="
  echo "Deploying $component..."
  echo "====================================="

  # Deploy resources in order: database/messaging > discovery > gateway > services
  if [ -d "$component" ]; then
    # Apply deployment files first to create Pods
    find "$component" -name "deployment.yaml" -type f -print0 | sort -z | while IFS= read -r -d '' file; do
      echo "Applying deployment: $file"
      minikube kubectl -- apply -f "$file"
      # Wait a bit for the Pod to be created
      sleep 2
    done

    # Then apply service files to expose Pods
    find "$component" -name "service.yaml" -type f -print0 | sort -z | while IFS= read -r -d '' file; do
      echo "Applying service: $file"
      minikube kubectl -- apply -f "$file"
    done
  fi
}

# Root directory for Kubernetes configurations
K8S_ROOT="kubernetes"
cd $K8S_ROOT || { echo "Directory $K8S_ROOT not found"; exit 1; }

# Deploy in order: databases > messaging > discovery > gateway > services
echo "Deploying microservices in order..."

# 1. Databases first (MySQL, Redis)
deploy_component "databases"

# 2. Messaging (Kafka, Zookeeper)
deploy_component "messaging"

# 3. Discovery service
deploy_component "discovery"

# 4. API Gateway
deploy_component "gateway"

# 5. Microservices
deploy_component "services"

# 6. Apply any remaining services (if any)
for dir in */; do
  if [[ "$dir" != "databases/" && "$dir" != "messaging/" && "$dir" != "discovery/" && "$dir" != "gateway/" && "$dir" != "services/" ]]; then
    deploy_component "${dir%/}"
  fi
done

# Check deployment status
echo "====================================="
echo "     Checking Pods status          "
echo "====================================="
minikube kubectl -- get pods

echo "====================================="
echo "    Checking Services status       "
echo "====================================="
minikube kubectl -- get services

echo "====================================="
echo "     Deployment completed!         "
echo "====================================="