package com.example.AppBancariaV2;

import com.example.AppBancariaV2.Services.ServiceCB;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppBancariaV2Application {
	public static void main(String[] args) {
		ServiceCB sv = new ServiceCB();
		sv.menuCreador();
	}
}
