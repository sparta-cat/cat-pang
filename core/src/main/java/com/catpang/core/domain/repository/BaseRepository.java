package com.catpang.core.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 기본 레포지토리 인터페이스.
 *
 * 이 인터페이스는 엔티티의 CRUD 연산을 수행하며, 추가적인 검색 조건을 지원합니다.
 *
 * @param <E>  엔티티 타입
 * @param <ID> 엔티티의 기본 키 타입
 * @param <C>  검색 조건을 위한 컨디션 클래스
 */
@NoRepositoryBean
public interface BaseRepository<E, ID, C> extends JpaRepository<E, ID> {

	/**
	 * 주어진 조건에 따라 페이징된 엔티티 목록을 검색합니다.
	 *
	 * @param pageable  페이징 정보
	 * @param condition 검색 조건
	 * @return 페이징된 엔티티 목록
	 */
	Page<E> search(C condition, Pageable pageable);

	/**
	 * 삭제되지 않은 특정 엔티티를 ID로 찾습니다.
	 *
	 * @param id 엔티티의 ID
	 * @return 삭제되지 않은 엔티티의 Optional
	 */
	// Optional<E> findByIdAndIsDeletedFalse();
}