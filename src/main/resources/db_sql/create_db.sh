#!/bin/bash
## This script creates database and populates it with data needed to run the application.
## Must be run as user 'postgres'.

exec_dir=$(dirname $0)

psql -a -f ${exec_dir}/intro_telecom_01_database.sql

## Now that the 'telecom' user has been created, we create and populate tables with that user
psql -U telecom -d intro_telecom -a -f ${exec_dir}/intro_telecom_02_tables.sql
psql -U telecom -d intro_telecom -a -f ${exec_dir}/intro_telecom_03_tables_initial_population.sql
