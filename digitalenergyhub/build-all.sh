#/bin/bash

# Build all modules within this Maven project.
#
#   Module 'ocpp_schemas':
#     Builds the POJO's from the OCPP 2.0.1 JSON schemas
#     NB! org.jsonschema2pojo.exception.ClassAlreadyExistsException is considered normal execution.
#     Link: https://github.com/joelittlejohn/jsonschema2pojo/issues/1555
#
#   Module 'ocpp_impl':
#     Builds framework library files (JAR files).
mvn clean install -DskipTests