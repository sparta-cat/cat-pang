package com.catpang.core.infrastructure.util;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.querydsl.jpa.JPQLQuery;

@Component
public class PagingUtil {

	public static <T> List<T> getPage(List<T> entity, Pageable pageable) {
		int maxStartIndex = entity.size() - (int)pageable.getOffset();
		int startIndex = (int)Math.min(pageable.getOffset(), entity.size());
		int lastIndex = (int)pageable.getOffset() + pageable.getPageSize();
		int maxLastIndex = Math.max(startIndex, maxStartIndex);
		lastIndex = Math.min(lastIndex, maxLastIndex);
		return entity.subList(startIndex, lastIndex);
	}

	public <T> PageImpl<T> getPageImpl(Pageable pageable, JPQLQuery<T> query) {
		long totalCount = query.fetchCount(); // 전체 개수를 fetch
		List<T> results = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch(); // 페이징 처리된 결과를 fetch
		return new PageImpl<>(results, pageable, totalCount);
	}
}