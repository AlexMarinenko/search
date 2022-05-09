# Getting Started

## Configuration and usage:

### Let's create an entity `User`

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

### Create default `UserRepository`

```java
@Repository
public interface UserRepository extends JpaSpecificationExecutor<User> {
}
```

### Extend `SearchService<T, R extends JpaSpecificationExecutor<T>>` for `User` Entity.

```java
@Service
public class UserSearchService extends SearchService<User, UserRepository> {
    public UserSearchService(UserRepository repository) {
        super(repository);
    }
}
```

### Implement `SearchController<T>` for `User` to search for and autowire the `UserSearchService`.

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

### Request example:

```json
{
  "conditions": [
    {
      "logic": "AND",
      "type": "STRING",
      "field": "username",
      "operator": "LIKE",
      "value": "john%"
    },
    {
      "logic": "AND",
      "type": "LONG",
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

### Response example:

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
      "username": "sam",
      "password": "top secret"
    }
  ],
  "metadata": {
    "page": 0,
    "size": 2,
    "count": 3,
    "total": 3
  }
}
```
