SELECT
    cpu_number,
    id AS host_id,
    total_mem
FROM
    PUBLIC.host_info
GROUP BY
    cpu_number, id
ORDER BY
    total_mem DESC;
CREATE
OR REPLACE FUNCTION round5(ts timestamp) RETURNS timestamp AS $$ BEGIN RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$ LANGUAGE PLPGSQL;

--verify round5 function
SELECT
    host_id,
    timestamp,
    round5(timestamp)
FROM
    host_usage;
-- host_id, host_name, timestamp, avg_used_mem_percentage
SELECT
    host_id,
    hi.hostname,
    round5(hu.timestamp) AS timestamp,
  AVG(
    (hi.total_mem - hu.memory_free)* 100 / total_mem
  ) AS avg_used_mem_percentage
FROM
    host_info hi
    inner join host_usage hu on hi.id = hu.host_id
GROUP BY
    round5(hu.timestamp),
    hostname,
    host_id;

SELECT
    host_id,
    round5(timestamp) AS ts,
    COUNT(*) AS num_data_points
FROM
    host_usage
GROUP BY
  ts,
  host_id
HAVING
        COUNT(*) < 3
ORDER BY
    host_id;
