package com.catpang.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 기본 레포지토리 인터페이스.
 * <p>
 * 이 인터페이스는 엔티티의 CRUD 연산을 수행하며, 추가적인 검색 조건을 지원합니다.
 *
 * @param <E>  엔티티 타입
 * @param <ID> 엔티티의 기본 키 타입
 * @param <C>  검색 조건을 위한 컨디션 클래스
 */
@NoRepositoryBean
public interface BaseRepository<E, ID, C> extends JpaRepository<E, ID>, BaseCustomSearch<E, C> {

}