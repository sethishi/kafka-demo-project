

cheat sheet

Producer 
./kafka-console-producer --broker-list 0.0.0.0:9092 --topic input-topic

List of Topics
./kafka-topics --zookeeper localhost:2181 --list

Delete a topic 
./bin/kafka-topics --zookeeper localhost:2181 --delete --topic giorgos-.*

Describe a topic
./bin/kafka-topics --zookeeper localhost:2181 --describe --topic csc.in

Enable deletion on topics
./kafka-server-start config/server.properties --override delete.topic.enable=true 
