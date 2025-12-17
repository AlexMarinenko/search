package ru.asmsoft.search.service;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.util.Streamable;
import org.springframework.data.domain.Sort;
import ru.asmsoft.search.model.Pager;
import ru.asmsoft.search.model.SearchQuery;
import ru.asmsoft.search.model.SearchResult;
import ru.asmsoft.search.specification.SpecificationBuilder;

/**
 * Search service.
 */
public abstract class SearchService<T> {

  private final JdbcAggregateOperations operations;
  private final Class<T> entityClass;

  /**
   * Search service constructor.
   *
   * @param operations JDBC aggregate operations to use for search
   */
  protected SearchService(JdbcAggregateOperations operations) {
    this.operations = operations;
    this.entityClass =
        (Class<T>)
            ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  /**
   * Search method for SearchQuery with converter.
   *
   * @param query the search query
   * @param converter results converting function
   * @param <E> type for converter
   * @return SearchResult
   */
  public <E> SearchResult<E> search(SearchQuery query, Function<T, E> converter) {
    SearchResult<T> result = search(query);
    List<E> convertedResults =
        result.getItems().stream().map(converter).toList();
    return new SearchResult<>(convertedResults, result.getMetadata());
  }

  /**
   * Search method for SearchQuery.
   *
   * @param query the search query
   * @return SearchResult
   */
  public SearchResult<T> search(SearchQuery query) {

    List<Sort.Order> orders =
        query.getSort() == null
            ? Collections.emptyList()
            : query.getSort().stream()
                .map(r -> new Sort.Order(r.getDirection(), r.getField()))
                .collect(Collectors.toList());

    Sort sort = Sort.by(orders);

    Pager pager = query.getPager() == null
            ? new Pager(0, 10)
            : query.getPager();

    Criteria criteria = new SpecificationBuilder<>(entityClass)
            .build(query);

    Query findQuery = Query.query(criteria).sort(sort)
            .limit(pager.getSize())
            .offset((long) pager.getPage() * pager.getSize());

    List<T> items = Streamable.of(operations.findAll(findQuery, entityClass)).toList();

    long total = Streamable.of(operations.findAll(Query.query(criteria).sort(sort), entityClass))
            .toList()
            .size();

    return SearchResult.of(items, pager, total);
  }
}
