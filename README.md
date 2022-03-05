# demo

http://localhost:8080/demo/swagger-ui.html#/demo/depositsUsingPOST

curl -X POST "http://localhost:8080/demo/api/history" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"start_date_time\": \"2022-03-05T00:00:01+00:00\", \"end_date_time\": \"2022-03-05T02:00:01+00:00\"}"

curl -X POST "http://localhost:8080/demo/api/deposits" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"datetime\": \"2022-03-05T00:00:01+00:00\", \"amount\": \"1\"}"