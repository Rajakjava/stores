package com.shopping.storemanagement;

import com.shopping.storemanagement.controller.OrderDetailsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StoremanagementApplicationTests {
		@Autowired
		private OrderDetailsController controller;

		@Test
		void contextLoads() throws Exception {
			assertThat(controller).isNotNull();
		}
	}

