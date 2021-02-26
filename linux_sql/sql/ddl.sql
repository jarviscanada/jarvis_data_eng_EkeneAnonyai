\c host_agent;
CREATE TABLE IF NOT EXISTS PUBLIC.host_info (
                                                id SERIAL PRIMARY KEY NOT NULL, hostname VARCHAR UNIQUE gNOT NULL,
                                                cpu_number INTEGER NOT NULL, cpu_architecture VARCHAR NOT NULL,
                                                cpu_model VARCHAR NOT NULL, cpu_mhz FLOAT NOT NULL,
                                                L2_cache INTEGER NOT NULL, total_mem INTEGER NOT NULL,
                                                "timestamp" TIMESTAMP NOT NULL
);
CREATE TABLE IF NOT EXISTS PUBLIC.host_usage (
                                                 "timestamp" TIMESTAMP NOT NULL,
                                                 host_id SERIAL NOT NULL,
                                                 memory_free INTEGER NOT NULL,
                                                 cpu_idle FLOAT NOT NULL,
                                                 cpu_kernel FLOAT NOT NULL,
                                                 disk_io INTEGER NOT NULL,
                                                 disk_available INTEGER NOT NULL,
                                                 CONSTRAINT machine_id FOREIGN KEY(host_id) REFERENCES host_info(id)
    );
