# Linux Cluster Monitoring Agent
## Introduction
The Linux Cluster Monitoring Agent (LCMA) is a tool designed to monitor and collect data from host machines over a network using IPv4 addresses; this includes hardware specifications, activity, and performance of Linux machines within a cluster. This data is stored in a PostgreSQL (psql) Relational Database Management System (RDBMS) and uses Docker containers. Docker is used to keep consistency between nodes/ servers. Bash scripts are used to initialize the process by creating the psql container using docker. The other scripts are used to monitor data within the Linux cluster related to the host information and usage by the host. The data, such as, CPU number, number of caches, amount of free memory, hostname, etc. is collected in real time and inserted into the database. The LCMA runs every minute using crontab, to generate statistics about resources and help users plan around resources with data. The Monitoring Agent uses crontab to collect data and send this data to the database on a scheduled basis.

## Quick Start
- Start a psql instance using psql_docker.sh
````bash
./scripts/psql_docker.sh create [db_username] [db_password]
````

- Create tables using ddl.sql
````bash
psql -h psql_host -U db_username -d host_agent -f sql/ddl.sql
````

- Insert hardware specs data into the db using host_info.sh
````
./scripts/host_info.sh [psql host] [port] host_agent [db_username] [db_password]
````

- Insert hardware usage data into the db using host_usage.sh
````
./scripts/host_usage.sh psql_host psql_port host_agent db_username db_password
```` 

- Crontab setup
````
# edit cronjobs
crontab -e 

# add to crontab
* * * * * bash <your path>/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log

# list crontab jobs
crontab -l

#- Use `crontab -l` to display command, and then test the command from the terminal as it (remove `* * * * *`).
````

## Implemenation
Discuss how you implement the project.

## Architecture 
![Architecture of Project](assets/architecture.png)

### Scripts
Shell script descirption and usage (use markdown code block for script usage)
- psql_docker.sh
- host_info.sh
- host_usage.sh
- crontab
- queries.sql (describe what business problem you are trying to resolve)

### Database Modeling
Describe the schema of each table using markdown table syntax (do not put any sql code)
- `host_info`
- `host_usage`

## Test
How did you test your bash scripts and SQL queries? What was the result?

## Improvements
- Write additional queries to tackle more failures.
- Create backup system incase database fails. 
- Edit crontab to monitor more information from the servers.
