#/bin/bash

# Build all modules within this Maven project.
#
#   ocpp_schemas:
#     Build the POJO's from OCPP 2.0.1 JSON schemas
#     NB! org.jsonschema2pojo.exception.ClassAlreadyExistsException is considered normal execution.
#     Link: https://github.com/joelittlejohn/jsonschema2pojo/issues/1555
#
#   ocpp_impl:
#     Build library files (JAR files) for the OCPP 2.0.1 implementation.
mvn clean install -DskipTests