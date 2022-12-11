# Questions

This is the questions section for the assignment.

## Vehicles defined as classes

Is there a reason we need Classes for vehicle definition? As if we do not need to have any information
except the vehicle name, might be more simple just to have them defined as Enums.

Another option if we need them to be more dynamic for different regions, we could define them in a 
database or a configuration file.


## HTTP Access

I have added an API endpoint where you can call the calculation with different inputs at /congestiontax/get

The API has a Swagger interactive documentation which you can find at /swagger-ui/index.html

### Heroku deployment

This implementation is deployed on Heroku at https://infinite-meadow-14547.herokuapp.com/

Swagger URL: https://infinite-meadow-14547.herokuapp.com/swagger-ui/index.html

API Endpoint: https://infinite-meadow-14547.herokuapp.com/congestiontax/get

Calling the API from Bash:

```bash
curl -X 'POST' \
'https://infinite-meadow-14547.herokuapp.com/congestiontax/get' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"vehicleType": "Car",
"entryDates": [
"2013-01-14T21:00:00",
"2013-01-15T21:00:00",
"2013-02-07T06:23:27",
"2013-02-07T15:27:00",
"2013-02-08T06:27:00",
"2013-02-08T06:20:27",
"2013-02-08T14:35:00",
"2013-02-08T15:29:00",
"2013-02-08T15:47:00",
"2013-02-08T16:01:00",
"2013-02-08T16:48:00",
"2013-02-08T17:49:00",
"2013-02-08T18:29:00",
"2013-02-08T18:35:00",
"2013-03-26T14:25:00",
"2013-03-28T14:07:27"
]
}'
```


## Tax Value Definition

At the moment the Tax Value is defined as *int*. This should suffice for Swedish Krona, but if Sweden
will switch one day to Euro, or we would plan to have the service to be used in a country with
different currency, it would be advisable to change the data type in which the tax is store and
calculated.

## Dynamically modify different tax rules

Based on the manager requirements from the Bonus scenario, there are a quite a few ways to achieve this.

First, if we would need to support different cities at once, we would need to add a parameter to the
API calls to specify the location.

Solutions:
1. Storing in a configuration file. In this way, we would need direct access to the location where
   the application is running.
2. Storing in an embedded database. In this case we would need access to the embedded database.
3. Storing in an external database. This solution is similar to the 2nd solution except that we will 
   need to configure the connection to the database. In the case that something happens to that database
   or the connection to our application our service could stop working as expected.

- For solutions 1 and 2 they are very easy to setup on any machine. Solution 3 requires an extra external
  database that we need to take in consideration.
- For solution 1, we could keep the configuration in the source control and change the configuration
  with a new deployment, in which case we will have the history in the version control.
- Solution 2, is similar to 1, except that we do not necessarily need access to the underlying server,
  as we could connect to the database.
- Solution 3, is similar to 2, except that the database does not need to be on the same machine. On
  the other hand we increase the setup overhead and dependency on an external source.

### My take on this solutions

Solution 1 can be a good option in case we want to have a clear traceability from the source code, and if
we do not care about downtime which can be mitigated by having multiple instances of the service and
upgrading them one by one. One big drawback is that it is prone to typos errors due to human
interaction.

Solution 2 can be good if we want to keep the datastore tightly coupled with the service.

Solution 3 can be a good option if we already have a database which is used to keep other configurations
and we do not want to add a new one. This can be in the case we change configurations directly on the
database.

Regardless of the solution some of the drawbacks can be mitigated by adding a new api endpoint for
modifying the configuration. This would be the best option from my point of view, as the service will
be the sole responsible of altering the configuration. This adds the possibility to add some safeguards.

