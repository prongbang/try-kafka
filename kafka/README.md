# Kafka

## Install Kafka

https://github.com/lensesio/fast-data-dev

- Compose

```shell
docker compose up -d
```

- Script

```shell
docker run --rm -p 2181:2181 -p 3030:3030 -p 8081-8083:8081-8083 \
       -p 9581-9585:9581-9585 -p 9092:9092 -e ADV_HOST=127.0.0.1   \
       landoop/fast-data-dev:latest
```

### Web GUI

http://localhost:3030/

## Run Create Topic

```shell
make run_create_topic topic=user-messages
```

Output

```
Created topic "user-messages".
```

## Run Test Consume

```shell
make run_test_consume topic=user-messages
```

Listen

```
{"id":"1","message":"Hello World"}
```

## Run produce

```
cd ../produce
make run
```

Output

```
Delivered message to topic user-messages [1] at offset 0
2021/12/31 22:45:47 Produce message successfully
```