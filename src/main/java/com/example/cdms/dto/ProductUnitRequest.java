package com.example.cdms.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductUnitRequest (
		@NotBlank String code,
		@NotBlank String name
){}
