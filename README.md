# btc-wallet

BTCWallet API is created to allow storing BTC transaction and fetching the wallet balances by the hour from a specific period of time.

## Development

Before running the project, run the command below to start Cassandra DB locally.
```
docker-compose up -d cassandra-db
```

To run the project locally:
- Go to `src/main/kotlin/com/btc/wallet/Boot.kt`
- Click Run on the `main` function in your IDE
- API will be exposed on localhost:8080

Run tests: `mvn clean test`

## Save Record & Get Wallet Balances

Postman: https://www.getpostman.com/collections/1320449d046fbd59e119

### Save Record

endpoint: `/wallet/save`\
Example request
```
{
    "datetime": "2022-10-05T14:45:11+07:00",
    "amount": 13.322
}
```

### Get Wallet Balances

endpoint: `/wallet/get`\
Example request
```
{
    "startDateTime": "2022-10-05T14:35:05+07:00",
    "endDateTime": "2022-10-05T15:58:05+07:00"
}
```