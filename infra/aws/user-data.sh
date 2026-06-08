#!/bin/bash
# TravelSphere Platform - EC2 Bootstrap Script
# This is the only shell script in the project (required for cloud-init)

set -e

echo "=== TravelSphere EC2 Bootstrap ==="

# Update system
yum update -y

# Install Docker
yum install -y docker git
systemctl enable docker
systemctl start docker

# Install Docker Compose
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# Clone repository
cd /opt
git clone https://github.com/YOUR_USERNAME/travelsphere-platform.git
cd travelsphere-platform

# Create .env file from secrets
cat > .env << EOF
POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
REDIS_PASSWORD=${REDIS_PASSWORD}
JWT_SECRET=${JWT_SECRET}
AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
AWS_REGION=${AWS_REGION}
SPRING_PROFILES_ACTIVE=prod
EOF

# Start all services
docker-compose up -d

# Wait for health checks
sleep 60

# Verify gateway is healthy
curl -f http://localhost:8080/actuator/health || echo "Warning: Gateway health check failed"

echo "=== TravelSphere deployment complete ==="
