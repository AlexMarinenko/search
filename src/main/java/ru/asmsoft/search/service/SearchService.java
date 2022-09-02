package ru.asmsoft.search.service;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.asmsoft.search.model.Pager;
import ru.asmsoft.search.model.SearchQuery;
import ru.asmsoft.search.model.SearchResult;
import ru.asmsoft.search.specification.SpecificationBuilder;

/**
 * Search service.
 */
public abstract class SearchService<T, R extends JpaSpecificationExecutor<T>> {

  private final R repository;
  private final Class<T> entityClass;

  /**
   * Search service constructor.
   *
   * @param repository repository to use for search
   */
  protected SearchService(R repository) {
    this.repository = repository;
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

    Specification<T> specification = new SpecificationBuilder<>(entityClass)
            .build(query);

    Page<T> page = repository.findAll(specification, pageRequest);

    return SearchResult.of(page, pager);
  }
}
