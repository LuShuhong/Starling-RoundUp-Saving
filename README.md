# Starling Bank Technical Challenge

## Candidate Name
Shuhong Lu

## THANK YOU
I would like to say thanks for coming out with such an interesting task. It is the best and most interesting task I have done recently.

## The Challenge
We’d like you to develop a “round-up” feature for Starling customers using our public
developer API that is available to all customers and partners.

For a customer, take all the transactions in a given week and round them up to the nearest
pound. For example with spending of £4.35, £5.20 and £0.87, the round-up would be £1.58.
This amount should then be transferred into a savings goal, helping the customer save for
future adventures.

## Technology Requirements
* Java 17

## How to Run
* My access token is not added to GitHub. So please input your access token in application.properties for the 'customer.token.access' field after the word "Bearer " (Notes: There is a space between the word Bearer and your access token)

* Build all modules with:
```
mvn clean install
```
* To run an HTTP web server, go to the project directory and execute:
```
java -jar target/roundUpSaving-0.0.1-SNAPSHOT.jar
```
Alternatively, click the Run button of your favourite IDE.

* Send a PUT request to the port number of the machine running the application, e.g., ```http://localhost:8080```.
The endpoint is ```/starling/v1/save-my-weekly-round-up'``` 

* Sit back and relax, the application saves the round-up amounts from your main account since a week ago to a savings goal. If there is no savings goal, one will be created for you and the amounts saved to that space.


## Testing Locations
* Unit Testing: 
```Respective directories from src/test```
* Integration Testing: 
```src/test/java/com/starlingbank/roundUpSaving/RoundUpSavingApplicationTests.java```

## Development Process
1. Get familiar with Starling APIs.
2. Decide on platform and framework (Java Spring Boot).
3. Design the application based on layers (Controller, Services, Models, etc.)
4. Start with Account service to get the basic account information from the account holder, using /api/v2/accounts
5. Create a Get mapping in my own application to test and see it works
6. Repeat Step 4 and 5 for: 
* Transaction service to get transactions
* Savings Goals service to transfer to or create a saving goal if not already existed
* RoundUp service as the Main execution point

## Unexpected Challenge
There appears to be an issue with JDK version 11-18 which incorrectly adds encoding header to GET requests. The issue has been noticed in Starling Developers channel too. Switching to RestTemplate solves this issue for me, but that means I am not using the latest WebClient for http requests.

## Some refactoring I have done after main development
* Remove unused imports
* Use Java record when suited
* Improve error handling and create custom exceptions classes for better debugging process
* Improve logging using Slf4j
* Removed unused endpoints, e.g, /accounts, /transactions, and keep only one endpoint
* Use Builder Design Pattern to construct Url to follow DRY principles

## Further Improvement
* Allows currency switch.
* Allows saving round-up between any two dates 


