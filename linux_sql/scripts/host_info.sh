#!/bin/bash

#setup arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
export PGPASSWORD=$5

#validate arguments
if [ $# -ne 5 ]; then
	echo "illegal number of parameters "
	exit 1
fi

#parse hardware specification
hostname=$(hostname -f)
lscpu_out=$(lscpu)
proc_mem_out=$(cat /proc/meminfo)

cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | head -n1 | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "Model [a-z]+:" | cut -d ":" -f 2 | xargs)
cpu_mhz=$(echo "$lscpu_out" | egrep "CPU [A-Z]+[a-z]:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out" | egrep "L2 cache:" | awk '{print substr($3, 1, length($3)-1)}' | xargs)
total_mem=$(echo "$proc_mem_out" | egrep "MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(date "+%Y-%m-%d %T")

#Building the insert statement
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, L2_cache, total_mem, timestamp)
VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, $total_mem, '$timestamp')"

psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?