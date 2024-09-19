package com.catpang.product.presentation;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.catpang.product.application.dto.ProductDto;
import com.catpang.product.application.service.ProductService;
import com.catpang.product.domain.repository.ProductSearchCondition;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

	private final ProductService productService;

	// @PreAuthorize 어노테이션을 통해 해당 메서드 접근을 특정 권한으로 제한할 수 있습니다.

	@PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
	@PostMapping
	public ProductDto.Result createProduct(@Valid @RequestBody ProductDto.Create createDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		return productService.createProduct(createDto, userDetails);
	}

	@PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
	@GetMapping("/{productId}")
	public ProductDto.Result getProduct(@PathVariable UUID productId) {
		return productService.getProduct(productId);
	}

	@PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
	@PutMapping("/{productId}")
	public ProductDto.Result updateProduct(@PathVariable UUID productId,
		@Valid @RequestBody ProductDto.Update updateDto) {
		return productService.updateProduct(productId, updateDto);
	}

	@PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
	@GetMapping
	public Page<ProductDto.Result> searchProducts(Pageable pageable, ProductSearchCondition condition) {
		return productService.searchProducts(pageable, condition);
	}

	@PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
	@DeleteMapping("/{productId}")
	public void deleteProduct(@PathVariable UUID productId) {
		productService.deleteProduct(productId);
	}
}
