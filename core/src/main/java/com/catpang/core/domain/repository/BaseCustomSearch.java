package com.catpang.core.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseCustomSearch<E, C> {
	/**
	 * 주어진 조건에 따라 페이징된 엔티티 목록을 검색합니다.
	 *
	 * @param pageable  페이징 정보
	 * @param condition 검색 조건
	 * @return 페이징된 엔티티 목록
	 */
	Page<E> search(C condition, Pageable pageable);
}
