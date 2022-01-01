import com.prongbang.consumer.TryKafkaConsumer
import io.github.cdimascio.dotenv.dotenv

fun main(args: Array<String>) {
    // Load Environment
    val dotenv = dotenv()

    val consumer = TryKafkaConsumer.newInstance(dotenv)
    consumer.run()
}