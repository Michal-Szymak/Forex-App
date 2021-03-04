# Forex-App
Project created as a homework during infoShare Academy Java + Spring Bootcamp between 12/2020 and 02/2021.\
It loads Forex Data from file into database and provides user with basic analysis tools.

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Available endpoints](#endpoints)

## General info
Application supports data stored in CSV format according to the formula:\
YYYY.MM.DD,HH:MM,OPEN,HIGH,LOW,CLOSE\
OPEN, HIGH, LOW and CLOSE are information about exchange rate for specified period of time. Data loaded from file is grouped by a minute, but application allows user to check these values for wider period of time.\
Application also calculates VOLATILITY for specified period of time (which is a difference between highest and lowest value).
Currently it is assumed that data loaded from file contains EUR-USD exchange rate data.

*Even though bootcamp has finished, application is still being developed*

## Technologies
Project is created with:
- Java SE 11
- Spring framework
- MySQL database
- Maven

## Available endpoints
Below you can find a list of currently available endpoints:
- **/EURUSD** - displays basic data (OPEN, HIGH, LOW, CLOSE, VOLATILITY) for specified period of time. If data is not available in database, 404 is returned.
  * **/year** - for example: /EURUSD/2020
  * **/year/month** - for example: /EURUSD/2020/11
  * **/year/month/day** - for example: /EURUSD/2020/11/01
  * **/year/month/day/hour** - for example: /EURUSD/2020/11/01/17
  * **/year/month/day/hour/minute** - for example: /EURUSD/2020/11/01/17/55
- **/average_daily_volatility** - application calculates volatility for every day included in database and returns average value
- **/average_hourly_volatility** - as above, but calculated by hour
- **/average_minutely_volatility** - as above, but calculated by minute
- **/most_volatile_day** - displays date when the highest volatility occurred along with the value itself 
- **/most_volatile_hour** - as above, but with date and hour
