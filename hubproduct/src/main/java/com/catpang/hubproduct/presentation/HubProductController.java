package com.catpang.hubproduct.presentation;

import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.catpang.core.application.dto.HubProductDto;
import com.catpang.hubproduct.application.service.HubProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hub-products")
@RequiredArgsConstructor
@Validated
public class HubProductController {

	private final HubProductService hubProductService;

	@PostMapping
	public HubProductDto.Result createHubProduct(@Valid @RequestBody HubProductDto.Create createDto) {
		return hubProductService.createHubProduct(createDto);
	}

	@GetMapping("/{hubProductId}")
	public HubProductDto.Result getHubProduct(@PathVariable UUID hubProductId) {
		return hubProductService.getHubProduct(hubProductId);
	}
}
