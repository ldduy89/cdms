package com.example.cdms.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductCategoryRequest (
		@NotBlank String code,
		@NotBlank String name
){}
