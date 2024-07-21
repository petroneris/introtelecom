#!/bin/bash
## This script creates test database and populates it with data needed to run various functional & integration tests.
## Must be run as user 'postgres'.

exec_dir=$(dirname $0)

psql -a -f ${exec_dir}/test_intro_telecom_01_database.sql

## Now that the 'test' user has been created, we create and populate tables with that user
psql -U test -d test_intro_telecom -a -f ${exec_dir}/test_intro_telecom_02_tables.sql
psql -U test -d test_intro_telecom -a -f ${exec_dir}/test_intro_telecom_03_basic_data.sql
psql -U test -d test_intro_telecom -a -f ${exec_dir}/test_intro_telecom_04_test_data.sql
