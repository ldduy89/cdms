package com.example.cdms.sync;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "true", havingValue = "true")
public class ProductSyncJob {

	private final ProductService productService;
	private final RestClient.Builder restClientBuilder;

	@Scheduled(cron = "0 0 2 * * *")
	public void run() {
		try {
			List<ProductRequest>  source = restClientBuilder.build().get().uri("").retrieve()
					.body(new ParameterizedTypeReference<List<ProductRequest>>() {
					});
			productService.create(source);
		} catch (RestClientException e) {
			return;
		}
	}

}