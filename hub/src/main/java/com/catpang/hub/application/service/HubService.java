package com.catpang.hub.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catpang.core.application.service.EntityMapper;
import com.catpang.hub.application.dto.HubDto;
import com.catpang.hub.domain.model.Hub;
import com.catpang.hub.domain.repository.HubRepository;
import com.catpang.hub.domain.repository.HubRepositoryHelper;
import com.catpang.hub.domain.repository.HubSearchCondition;

import lombok.RequiredArgsConstructor;

/**
 * 허브와 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubService {

	private final HubRepository hubRepository;
	private final HubRepositoryHelper hubRepositoryHelper;

	/**
	 * 모든 허브를 조회합니다.
	 * @return 허브 리스트
	 */
	public List<Hub> getAllHubs() {
		return hubRepository.findAll();
	}

	/**
	 * 새로운 허브를 생성합니다.
	 * @param createDto 허브 생성에 필요한 정보가 담긴 DTO
	 * @param userDetails 인증된 사용자 정보
	 * @return 생성된 허브의 정보가 담긴 DTO
	 */
	@Transactional
	public HubDto.Result createHub(HubDto.Create createDto, UserDetails userDetails) {
		// 인증된 사용자로부터 사용자 ID를 추출합니다.
		Long ownerId = EntityMapper.getUserId(userDetails.getUsername());

		// 새로운 허브 엔티티를 생성합니다.
		Hub hub = Hub.builder()
			.name(createDto.name())
			.address(createDto.address())
			.ownerId(ownerId)
			.build();

		// 허브를 저장소에 저장합니다.
		hub = hubRepository.save(hub);

		// 결과 DTO를 생성하여 반환합니다.
		return HubDto.Result.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(hub.getAddress())
			.ownerId(hub.getOwnerId())
			.build();
	}

	/**
	 * 기존 허브의 정보를 수정합니다.
	 * @param hubId 수정할 허브의 ID
	 * @param putDto 수정할 내용이 담긴 DTO
	 * @return 수정된 허브의 정보가 담긴 DTO
	 */
	@Transactional
	public HubDto.Result updateHub(UUID hubId, HubDto.Put putDto) {
		// 허브를 조회합니다.
		Hub hub = hubRepositoryHelper.findOrThrowNotFound(hubId);

		// 허브의 정보를 수정합니다.
		hub.setName(putDto.name());
		hub.setAddress(putDto.address());

		// 결과 DTO를 생성하여 반환합니다.
		return HubDto.Result.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(hub.getAddress())
			.ownerId(hub.getOwnerId())
			.build();
	}

	/**
	 * 허브를 삭제합니다. 실제로는 소프트 삭제를 수행합니다.
	 * @param hubId 삭제할 허브의 ID
	 */
	@Transactional
	public void deleteHub(UUID hubId) {
		// 허브를 조회합니다.
		Hub hub = hubRepositoryHelper.findOrThrowNotFound(hubId);

		// 허브를 소프트 삭제합니다.
		hub.softDelete();

		// 변경 사항을 저장합니다.
		hubRepository.save(hub);
	}

	/**
	 * 특정 허브의 정보를 조회합니다.
	 * @param hubId 조회할 허브의 ID
	 * @return 허브의 정보가 담긴 DTO
	 */
	public HubDto.Result getHub(UUID hubId) {
		// 허브를 조회합니다.
		Hub hub = hubRepositoryHelper.findOrThrowNotFound(hubId);

		// 결과 DTO를 생성하여 반환합니다.
		return HubDto.Result.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(hub.getAddress())
			.ownerId(hub.getOwnerId())
			.build();
	}

	/**
	 * 허브를 검색 조건에 따라 조회합니다.
	 * @param pageable 페이징 정보
	 * @param condition 검색 조건
	 * @return 허브의 페이징된 결과 DTO
	 */
	public Page<HubDto.Result> searchHubs(Pageable pageable, HubSearchCondition condition) {
		// 허브를 검색합니다.
		return hubRepository.search(condition, pageable).map(hub -> HubDto.Result.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(hub.getAddress())
			.ownerId(hub.getOwnerId())
			.build());
	}
}
