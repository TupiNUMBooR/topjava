# REST docs

## meals

Get all meals  
`curl -i "http://localhost:8080/topjava/rest/meals"`

Get meal by id  
`curl -i "http://localhost:8080/topjava/rest/meals/100003"`

---
Get filtered meals

```sh
curl -i -G "http://localhost:8080/topjava/rest/meals/filter" \
  -d startDate=2020-01-30 \
  -d endDate=2020-01-30 \
  -d startTime=10:00 \
  -d endTime=13:01
```

---
Create new meal

```sh
curl -i -X POST \
  -H "Content-Type: application/json; charset=utf-8" \
  -d '{"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300}' \
  "http://localhost:8080/topjava/rest/meals"
```

---
Update meal

```sh
curl -i -X PUT \
  -H "Content-Type: application/json; charset=utf-8" \
  -d '{"id":100003,"dateTime":"2020-01-30T10:02:00","description":"Обновленный завтрак","calories":200}' \
  "http://localhost:8080/topjava/rest/meals/100003"
```

---
Delete meal

```sh
curl -i -X DELETE \
  "http://localhost:8080/topjava/rest/meals/100003""
```
