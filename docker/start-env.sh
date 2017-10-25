#! /bin/bash

set -e

echo "creating edvorg network"
docker network create edvorg || echo "edvorg network is already created"

echo "creating directory structure"
sudo mkdir -m 777 -p /var/edvorg/redis
sudo mkdir -m 777 -p /var/edvorg/elastic

./build "$@"
./run "$@"
