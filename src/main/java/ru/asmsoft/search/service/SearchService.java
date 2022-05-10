package ru.asmsoft.search.service;

import lombok.RequiredArgsConstructor;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
abstract public class SearchService<T, R extends JpaSpecificationExecutor<T>> {

    private final R repository;

    public SearchResult<T> search(SearchQuery query) {

        List<Sort.Order> orders = query.getSort() == null ? Collections.emptyList() :
                query.getSort().stream()
                .map(r -> new Sort.Order(r.getDirection(), r.getField()))
                .collect(Collectors.toList());

        Sort sort = Sort.by(orders);

        Pager pager = query.getPager() == null ? new Pager(0, 10) : query.getPager();

        Pageable pageRequest = PageRequest.of(pager.getPage(), pager.getSize(), sort);



        Specification<T> specification = new SpecificationBuilder<T>().build(query);
        Page<T> page = repository.findAll(specification, pageRequest);

        return SearchResult.of(page, pager);

    }
}
