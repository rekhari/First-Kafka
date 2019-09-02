import com.kafka.Consumer;
import org.apache.kafka.common.serialization.Serdes;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCountLambdaExample {
    static final String inputTopic = "streams-plaintext-input";
    static final String outputTopic = "streams-wordcount-output";
    final static Logger logger = LoggerFactory.getLogger(WordCountLambdaExample.class);

    public static void main(String[] args) {
        final String bootstrapServers = args.length > 0 ? args[0] : "pr:9092";
        final Properties streamsConfiguration = getStreamsConfiguration(bootstrapServers);
        final StreamsBuilder builder = new StreamsBuilder();
        createWordCountStream(builder);
        final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);
        //streams.cleanUp();
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    static Properties getStreamsConfiguration(final String bootstrapServers) {
        final Properties streamsConfiguration = new Properties();
        // Give the Streams application a unique name.  The name must be unique in the Kafka cluster
        // against which the application is run.
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-lambda-example1");
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "wordcount-lambda-example-client1");
        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Specify default (de)serializers for record keys and for record values.
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        // Records should be flushed every 10 seconds. This is less than the default
        // in order to keep this example interactive.
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        // For illustrative purposes we disable record caches.
        streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        // Use a temporary directory for storing state, which will be automatically removed after the test.
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp");
        return streamsConfiguration;
    }

    /**
     *
     * @param builder
     */
    static void createWordCountStream(final StreamsBuilder builder) {
        final KStream<String, String> textLines = builder.stream(inputTopic);
        final Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);
        final KTable<String, Long> wordCounts = textLines.flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase()))).groupBy((keyIgnored, word)->word).count();
        wordCounts.toStream().to(outputTopic, Produced.with(Serdes.String(), Serdes.Long()));
        logger.info("Stream built");
    }
}
