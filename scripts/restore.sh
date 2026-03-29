#!/bin/bash

# Database restore script
if [ -z "$1" ]; then
    echo "Usage: ./restore.sh <backup_file>"
    exit 1
fi

BACKUP_FILE=$1

# Check if file exists
if [ ! -f "$BACKUP_FILE" ]; then
    echo "Backup file not found: $BACKUP_FILE"
    exit 1
fi

# Restore database
docker exec -i staysearch_postgres psql -U postgres -d staysearch_db < $BACKUP_FILE

echo "Restore completed from: $BACKUP_FILE"