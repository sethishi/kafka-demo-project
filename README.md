### Kafka demo to replace traditional database

#### Demo application to produce data

This application is a kafka producer and listener application.
The scope of the application is to consolidate the data from 3 different streams of data and send it out on a common topic. 

#### Kafka Stream to merge and produce the data

VDIStreams is the streaming application that is consolidating the data on arrival on 3 difffrent topics and then joining them to create a new consolidated stream.  


#### Start docker using kafka
git clone https://github.com/confluentinc/examples 

cd cp-all-in-one/

docker-compose up -d --build



