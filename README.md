# Search library

Search library for spring data jpa applications.

# Getting Started

## Configuration and usage:

### 1. Package

First of all, let's add the search dependency into the project.

For maven:
```xml
<dependency>
    <groupId>ru.asmsoft</groupId>
    <artifactId>search</artifactId>
    <version>1.1.4</version>
</dependency>
```

For gradle: 
```bash
implementation 'ru.asmsoft:search:1.1.2'
```

### 2. Entity

Let's create an entity `User` to play with.

```java
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    private long id;

    @Column
    private String username;

    @Column
    private String password;

}
```

### 3. Repository

Let's create `UserRepository` as JPA specification executor.

```java
@Repository
public interface UserRepository extends JpaSpecificationExecutor<User> {
}
```

### 4. Service

You can build default search service for `User` entity extending `SearchService<T, R extends JpaSpecificationExecutor<T>>`.
Use `UserRepository` created in the previous step to inject it with the constructor into service instance.

```java
@Service
public class UserSearchService extends SearchService<User, UserRepository> {
    public UserSearchService(UserRepository repository) {
        super(repository);
    }
}
```

### 5. Controller

Use the service either with a Controller or with a Component, Facade, etc.
For example, let's implement `SearchController<T>` for `User` and autowire the `UserSearchService` into it as shown below.

```java
@RestController
@RequiredArgsConstructor
public class UserSearchController implements SearchController<User> {
    
    private final UserSearchService userSearchService;

    @PostMapping("/api/v1/users/search")
    @Override
    public SearchResult<User> search(@RequestBody SearchQuery searchQuery) {
        return userSearchService.search(searchQuery);
    }
}

```

### 6. Request:

Request consists of 3 sections:
- `conditions` which contains logical sets to filter the data. 
- `pager` which defines the pagination parameters for the results. This section has `page=0` and `size=10` by default if not specified.
- `sort` is responsible for sorting parameters

#### Request example:

```json
{
  "conditions": [
    {
      "expression": "AND",
      "field": "username",
      "operator": "LIKE",
      "value": "john%"
    },
    {
      "expression": "AND",
      "field": "id",
      "operator": "IN",
      "values": [
        1, 2, 3
      ]
    }
  ],
  "pager": {
    "size": 10,
    "page": 0
  },
  "sort": [
    {
      "field": "username",
      "direction": "ASC"
    },
    {
      "field": "password",
      "direction": "DESC"
    }
  ]
}
```

#### Available field types:

- `BOOLEAN`,
- `BYTE`,
- `CHARACTER`,
- `SHORT`,
- `INTEGER`,
- `LONG`,
- `FLOAT`,
- `DOUBLE`,
- `BIGINTEGER`,
- `BIGDECIMAL`,
- `DATE`,
- `DATETIME`,
- `STRING`

#### Implemented operators:

```bash
+------------------+---------+---------+
| Operation        | Symbol  | Values  |
+------------------+---------+---------+
| Equal            |   =     | Single  |
| Not equal        |  !=     | Single  |
| Like             |  LIKE   | Single  |
| Greater          |   >     | Single  |
| Greater or equal |  >=     | Single  |
| Less             |   <     | Single  |
| Less or equal    |  <=     | Single  |
| In               |  IN     | Array   |
+------------------+---------+---------+
```


### 7. Response

Response has 2 sections:
- `items` which contains the results of the search in an array of DTOs
- `metadata` which contains the `pagination` parameters, `count` of elements in the resulting array and `total` elements available in the database according to the search conditions.

#### Response example:

```json
{
  "items": [
    {
      "id": 1,
      "username": "john",
      "password": "secret"
    },
    {
      "id": 2,
      "username": "johny",
      "password": "top secret"
    }
  ],
  "metadata": {
    "page": 0,
    "size": 10,
    "count": 2,
    "total": 2
  }
}
```
