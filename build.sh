#!/bin/sh

./mvnw package spring-boot:build-image -DskipTests -Dspring.profiles.active=prod
