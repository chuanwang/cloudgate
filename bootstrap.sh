#!/bin/bash
#################################################################
# Script to bootstrap the postgresql database                   #
#################################################################

# directory where postgresql is installed. Usually it is /usr/bin
POSTGRESQL_DIR=$1

# database cluster data directory
DATA_DIR=/home/pgsql/data
SQL_SCRIPT_DIR=/home/pgsql/init_db.sql

# database log file
LOGFILE=/home/pgsql/logfile

CURRENT_DIR=`pwd`

if [ "${POSTGRESQL_DIR}" == "" ]; then
    echo "Usage sudo $0 postgresql_install_dir(usually /usr/bin)" 
    echo ""
    exit 1;
fi;

pass=$(perl -e 'print crypt("verycloud", "salt")')

# Create cloudgate user with password verycloud
useradd -p $pass cloudgate

mkdir -p ${DATA_DIR}

chown -R cloudgate ${DATA_DIR}/../

# init db file directory
su - cloudgate -c "${POSTGRESQL_DIR}/initdb -D ${DATA_DIR}"

# create log file
su - cloudgate -c "touch ${LOGFILE}"

# start postgresql server
su - cloudgate -c "${POSTGRESQL_DIR}/pg_ctl -D ${DATA_DIR} -l ${LOGFILE} start"

# wait for server start
sleep 5

# create verycloud database
su - cloudgate -c "${POSTGRESQL_DIR}/createdb verycloud"

# move sql script to pgsql directory so that user cloudgate can read it.
cp ./init_db.sql ${SQL_SCRIPT_DIR}
chown cloudgate ${SQL_SCRIPT_DIR}

# run sql script to populate tables into verycloud database
su - cloudgate -c "${POSTGRESQL_DIR}/psql verycloud < ${SQL_SCRIPT_DIR}"

########################################################
# End of script                                        #
########################################################

