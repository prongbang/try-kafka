# make run_kafka
run_kafka:
	docker compose up -d

# make run_create_topic topic=user-messages
run_create_topic:
	kafka/bin/kafka-topics.sh --create --zookeeper localhost:2181 \
  --replication-factor 1 --partitions 3 --topic $(topic)

# make run_test_consume topic=user-messages
run_test_consume:
	kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
  --topic $(topic)
