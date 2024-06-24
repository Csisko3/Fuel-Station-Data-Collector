# Fuel-Station-Data-Collector

## UML

![image](https://github.com/Csisko3/Fuel-Station-Data-Collector/assets/131276424/7471d3aa-6cc8-4908-99d8-cbb12f1a94af)
___

## Responsibilities

Csiszer Roland:

- Spring Boot App
- Data Collection Dispatcher
- Unit Tests

Hassan Taqi:

- Station Data Collector
- PDF Generator & File Storage
- Documentation

Diab Basel:

- Data Collection Receiver
- Java FX
- UML

## Setup/installation- and user-guide

You work in a company, that manages charging stations for electric cars. Every charging station keeps track of the customer that used the charger and the amount of kWh the customer used.

To generate an invoice PDF from a specific user, you have to input the user id in the given field and click on “Generate Invoice”. You have a *docker-compose* project that sets up five databases and a message queue (RabbitMQ). To start the databases through docker, a terminal input with docker-compose up must be created. To access RabbitMQ, make sure to log in with username: guest and password: guest.

In addition, following directories of the project must be started: SpringBootApp, DataCollectionDispatcher, StationDataCollector, DataCollectionReceiver and the GeneratePDF.

## Lessons learned

In this project we faced a lot of difficulties, to be honest mainly because of time mismanagement as we started it rather late. Nevertheless, we managed to finish it in time relatively comfortably as we showed discipline and dedication in the few days.  Looking back, we should have started earlier despite other subjects taking up time, as it would result in less stress and pressure.

When it comes to the use case, it was a perfect representation of distributed systems and although the difficulty levels it remained very interesting. The team members had the chance to find out for themselves if it suits them.

## Description of unit testing

We decided to test the repository in the service _StationDataCollector_. Since the this half way of our service it is important that the data flow is correct so we could narrow down mistkase easier if they happen.
The functions we tested are _GetInvoiceTest_ and _StationDataCollectorTests_.
_GetInvoiceTest_ simulates an HTTP GET request, checks the status code and the response data, ensures the file is created, and then deletes the file afterward. 
_StationDataCollectorTests_ sets up a mock environment for the InvoiceController, initializes the three stations (station_db 1-3) with test data, and verifies successful data retrieval for a specific customer ID from each database.

## **Tracked time**

~70 Stunden (von allen 3 zusammengerechnet)

## **Link for GIT**

[https://github.com/Csisko3/Fuel-Station-Data-Collector.git](https://github.com/Csisko3/Fuel-Station-Data-Collector.git)




