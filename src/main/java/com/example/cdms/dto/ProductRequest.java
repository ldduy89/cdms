package com.example.cdms.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest (
		@NotBlank String productName
){}
