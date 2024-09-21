# Hoplite Example

## Running demo:

```shell
./gradlew :demo:run --args=prod
```

**What it should show:**

```
          defaults : classpath:/conf/defaults.yaml
external overrides : <user.dir>/hoplite-sample/conf/application-prod.yaml
╭───────────────────────────┬───────────────────────────╮
│          Setting          │     Value                  │
├───────────────────────────┼────────────────────────────┤
│        elasticsearch.host │ prd-elasticsearch.scv      │
├───────────────────────────┼────────────────────────────┤
│        elasticsearch.port │ 8200                       │
├───────────────────────────┼────────────────────────────┤
│ elasticsearch.clusterName │ product-search             │
╰───────────────────────────┴───────────────────────────╯
```

**_Verify by running_**:

```shell
./gradlew check
```

