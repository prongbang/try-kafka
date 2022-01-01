package com.prongbang.consumer

import java.util.Properties
import org.apache.http.HttpHost

import com.fasterxml.jackson.databind.JsonNode
import io.github.cdimascio.dotenv.Dotenv
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.connect.json.JsonDeserializer
import org.elasticsearch.ElasticsearchException

import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.RestClient

class TryKafkaConsumer(
    private val dotenv: Dotenv
) {

    companion object {
        fun newInstance(dotenv: Dotenv): TryKafkaConsumer = TryKafkaConsumer(dotenv)
    }

    fun run() {
        val bootstrapServersConfig = dotenv["BOOTSTRAP_SERVERS_CONFIG"]
        val groupIdConfig = dotenv["GROUP_ID_CONFIG"]
        val topic = "user-messages"

        val props = Properties()
        with(props) {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig)
            put(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer().javaClass.name)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer().javaClass.name)
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
            put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
        }

        val esHost = dotenv["ES_HOST"]
        val esPort = dotenv["ES_PORT"].toInt()

        val client = RestHighLevelClient(RestClient.builder(HttpHost(esHost, esPort, "http")))

        val consumer = KafkaConsumer<String, JsonNode>(props)
        consumer.subscribe(listOf(topic))

        while (true) {
            val consumerRecords = consumer.poll(1000)

            if (consumerRecords.count() == 0) {
                continue
            }

            val bulkRequest = BulkRequest()
            val requestOptions = RequestOptions.DEFAULT

            consumerRecords.forEach {
                val id = it.value().get("id").asText()
                val message = it.value().get("message").asText()
                val jsonMap = mapOf("id" to id, "message" to message)

                // Your additional business logic

                bulkRequest.add(IndexRequest("user-messages", "doc", id).source(jsonMap))
            }

            try {
                client.bulk(bulkRequest, requestOptions)
                consumer.commitAsync()
                println("Indexed successfully")
            } catch (e: ElasticsearchException) {
                println(e)
            }
        }
        consumer.close()
    }
}