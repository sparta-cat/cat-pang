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
import com.catpang.address.domain.model.Address;
import com.catpang.address.domain.repository.AddressRepository;
import com.catpang.core.application.dto.AddressDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubService {

	private final HubRepository hubRepository;
	private final HubRepositoryHelper hubRepositoryHelper;
	private final AddressRepository addressRepository;

	@Transactional
	public HubDto.Result createHub(HubDto.Create createDto, UserDetails userDetails) {
		UUID ownerId = UUID.fromString(userDetails.getUsername());

		Address address = Address.builder()
			.city(createDto.address().city())
			.street(createDto.address().street())
			.zipcode(createDto.address().zipcode())
			.latitude(createDto.address().latitude())
			.longitude(createDto.address().longitude())
			.build();

		address = addressRepository.save(address);

		Hub hub = Hub.builder()
			.name(createDto.name())
			.address(address)
			.ownerId(ownerId)
			.build();

		hub = hubRepository.save(hub);

		return HubDto.Result.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(toAddressDto(hub.getAddress()))
			.ownerId(hub.getOwnerId())
			.build();
	}
	@Transactional
	public HubDto.Result updateHub(UUID hubId, HubDto.Put putDto) {
		Hub hub = hubRepositoryHelper.findOrThrowNotFound(hubId);
		Address address = addressRepository.findById(putDto.addressId())
			.orElseThrow(() -> new IllegalArgumentException("Address not found"));

		hub.setName(putDto.name());
		hub.setAddress(address);

		return HubDto.Result.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(toAddressDto(hub.getAddress()))  // AddressDto로 변환하여 반환
			.ownerId(hub.getOwnerId())  // Long 타입으로 반환
			.build();
	}

	public HubDto.Result getHub(UUID hubId) {
		Hub hub = hubRepositoryHelper.findOrThrowNotFound(hubId);
		return HubDto.Result.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(toAddressDto(hub.getAddress()))  // AddressDto로 변환하여 반환
			.ownerId(hub.getOwnerId())  // Long 타입으로 반환
			.build();
	}

	public Page<HubDto.Result> searchHubs(Pageable pageable, HubSearchCondition condition) {
		return hubRepository.search(condition, pageable)
			.map(hub -> HubDto.Result.builder()
				.id(hub.getId())
				.name(hub.getName())
				.address(toAddressDto(hub.getAddress()))  // AddressDto로 변환하여 반환
				.ownerId(hub.getOwnerId())  // Long 타입으로 반환
				.build());
	}

	@Transactional
	public void deleteHub(UUID hubId) {
		Hub hub = hubRepositoryHelper.findOrThrowNotFound(hubId);
		hub.softDelete();
		hubRepository.save(hub);
	}

	// Address를 AddressDto.Result로 변환하는 메서드
	private AddressDto.Result toAddressDto(Address address) {
		return AddressDto.Result.builder()
			.id(address.getId())
			.city(address.getCity())
			.street(address.getStreet())
			.zipcode(address.getZipcode())
			.latitude(address.getLatitude())
			.longitude(address.getLongitude())
			.build();
	}
}
