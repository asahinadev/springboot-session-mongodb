package com.example.spring.task;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DbInitializeTask {

	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		log.info("The time is now {}", LocalDateTime.now());
	}
}
