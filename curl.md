`curl -i "http://localhost:8080/topjava/rest/meals"`  
`curl -i "http://localhost:8080/topjava/rest/meals/100003"`  
`curl -i "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=10:00&endTime=13:01"`  

```sh
curl -i -X POST \
  -H "Content-Type: application/json; charset=utf-8" \
  -d '{"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300}' \
  "http://localhost:8080/topjava/rest/meals"
```

```sh
curl -i -X PUT \
  -H "Content-Type: application/json; charset=utf-8" \
  -d '{"id":100003,"dateTime":"2020-01-30T10:02:00","description":"Обновленный завтрак","calories":200}' \
  "http://localhost:8080/topjava/rest/meals/100003"
```

```sh
curl -i -X DELETE \
  "http://localhost:8080/topjava/rest/meals/100003""
```
