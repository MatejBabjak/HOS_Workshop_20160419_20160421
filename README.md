# HOS_Workshop_20160419_20160421
* Prerequisites:
* * Existing PostgreSQL instance
* * * Existing DB user kv_user
* * * Existing schema rules
* * Existing Wildfly 8.2 or Wildfly 10 instance
* * * datasource java:jboss/datasources/SANDBOX

Setup:
* \ddl\create_tables.sql
* \dml\insert_test_rules.sql

TODO:
* DB Unit + in-memory H2 DB to avoid the prerequisites
* Refactoring & claen-up