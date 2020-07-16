package com.team.tracking_management_system_backend;


import com.team.tracking_management_system_backend.interceptor.AdminInterceptor;
import com.team.tracking_management_system_backend.interceptor.EmployeeInterceptor;
import com.team.tracking_management_system_backend.interceptor.LoginInterceptor;
import com.team.tracking_management_system_backend.interceptor.ManagerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private AdminInterceptor adminInterceptor;
    @Autowired
    private ManagerInterceptor managerInterceptor;
    @Autowired
    private EmployeeInterceptor employeeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {//注入拦截器对象
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login");

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/api/admin/**");

        registry.addInterceptor(managerInterceptor)
                .addPathPatterns("/api/manager/**");
        registry.addInterceptor(employeeInterceptor)
                .addPathPatterns("/api/employee/**");
    }
}
