package com.team.tracking_management_system_backend;

import com.team.tracking_management_system_backend.entity.Manager;
import com.team.tracking_management_system_backend.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TrackingManagementSystemBackEndApplicationTests {
	@Autowired
	AdminService adminService;
	@Test
	public void testAdminService(){

	}

}
