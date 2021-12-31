package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"

	_ "github.com/prongbang/goenv"
	"github.com/prongbang/produce/producer"
)

func greet(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Hello World! %s", time.Now())
}

func main() {
	// Initialize kafka producer
	err := producer.InitKafka()
	if err != nil {
		log.Fatal("Kafka producer ERROR: ", err)
	}

	topics := "user-messages"

	messageJson, _ := json.Marshal(&producer.Message{
		Id:      "1",
		Message: "Hello World",
	})

	producerErr := producer.Produce(topics, string(messageJson))
	if producerErr != nil {
		log.Print(err)
	} else {
		log.Print("Produce message successfully")
	}
}
