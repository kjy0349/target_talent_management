package com.ssafy.s10p31s102be;
import com.ssafy.s10p31s102be.common.service.ProfileInitService;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class S10p31s102beApplication {
	@PostConstruct
	void setTimeZoneToKST() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(S10p31s102beApplication.class, args);
	}
}
