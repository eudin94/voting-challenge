#!/bin/bash
set -e

psql -v --username postgres --dbname voting_challenge <<-EOSQL
    CREATE SCHEMA voting_schema  AUTHORIZATION postgres;
EOSQL