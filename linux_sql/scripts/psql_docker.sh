#!/bin/bash

#Setup arguments
command=$1
db_username=$2
db_password=$3

#check if docker is running. Else, start up docker
sudo systemctl status docker || systemctl start docker

#validate arguments
case $command in
  create)

    if [ $(docker container ls -a -f name=jrvs-psql | wc -l) -eq 2 ]; then
      echo "Container is already created."
      exit 1
    fi

    #check db_username or db_password is not passed through CLI arguments
    if [ -z "$2" ] || [ -z "$3" ]; then
      echo "Enter username or password with create command"
      exit 1
    fi

    #create pgdata volume
    docker volume create pgdata
    #create psql container
    docker run --name jrvs-psql -e POSTGRES_PASSWORD=${db_password} -e POSTGRES_USER=${db_username} -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres

    exit $?

    if [ $(docker container ls -a -f name=jrvs-psql | wc -l) -eq 1 ]; then
      echo "container was not created"
      exit 1
    fi

  ;;
  start)
    #start docker container
    docker container start jrvs-psql
    exit $?

  ;;
  stop)
    #stop docker container
    docker container stop jrvis-psql
    exit $?
  ;;
  *)
    echo "invalid command"
    exit 1
  ;;
esac

exit 0