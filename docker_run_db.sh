#!/usr/bin/env bash

docker run --rm --name=search-engine-app \
 -e MYSQL_DATABASE=search_engine \
 -e MYSQL_ROOT_PASSWORD=123root123 \
 -p 3306:3306 \
  mysql:8
