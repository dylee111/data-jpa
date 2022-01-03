package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean // 등록자, 수정자 처리를 위한 AuditorAware 스프링 빈 등록
	public AuditorAware<String> auditorProvider() {
		return new AuditorAware<String>() {
			@Override
			public Optional<String> getCurrentAuditor() {
				// 실무에서는 세션 정보나 스프링 시큐리티 로그인 정보에서 ID를 받아서 사용
				// 예제에서는 해당 정보를 받을 수 없기 때문에 UUID로 대체
				return Optional.of(UUID.randomUUID().toString());
			}
		};
	}

}
