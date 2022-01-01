# Elastic Search

- Run Compose

```shell
make run_elastic
```

- Run Script

```shell
$ docker run -p 9200:9200 -p 9300:9300 \
  -e "discovery.type=single-node" \
  docker.elastic.co/elasticsearch/elasticsearch:6.3.0
```