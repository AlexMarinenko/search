package ru.asmsoft.search.service;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import ru.asmsoft.search.model.Pager;
import ru.asmsoft.search.model.SearchQuery;
import ru.asmsoft.search.model.SearchResult;
import ru.asmsoft.search.specification.SpecificationBuilder;

/**
 * Search service.
 */
public abstract class SearchService<T> {

  private final JdbcAggregateTemplate jdbcAggregateTemplate;
  private final Class<T> entityClass;

  /**
   * Search service constructor.
   *
   * @param jdbcAggregateTemplate aggregate template to use for search
   */
  protected SearchService(JdbcAggregateTemplate jdbcAggregateTemplate) {
    this.jdbcAggregateTemplate = jdbcAggregateTemplate;
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

    Pageable pageRequest = PageRequest.of(
            pager.getPage(),
            pager.getSize(),
            sort
    );

    CriteriaDefinition criteriaDefinition = new SpecificationBuilder<>(entityClass)
            .build(query);

    Query querySpecification = Query.query(criteriaDefinition)
        .sort(sort)
        .limit(pager.getSize())
        .offset((long) pager.getPage() * pager.getSize());

    List<T> items =
        StreamSupport.stream(
                jdbcAggregateTemplate.findAll(querySpecification, entityClass).spliterator(),
                false)
            .toList();

    long total = jdbcAggregateTemplate.count(Query.query(criteriaDefinition), entityClass);

    Page<T> page = new PageImpl<>(items, pageRequest, total);

    return SearchResult.of(page, pager);
  }
}
