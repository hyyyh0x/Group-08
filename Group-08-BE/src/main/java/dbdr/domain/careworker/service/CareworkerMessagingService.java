package dbdr.domain.careworker.service;

import dbdr.domain.careworker.entity.Careworker;
import dbdr.domain.careworker.repository.CareworkerRepository;
import dbdr.global.util.line.LineMessagingUtil;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareworkerMessagingService {

	private final CareworkerService careworkerService;
	private final CareworkerRepository careworkerRepository;
	private final LineMessagingUtil lineMessagingUtil;

	@Transactional
	public void handleCareworkerPhoneMessage(String userId, String phoneNumber) {
		Careworker careworker = careworkerService.findByPhone(phoneNumber);
		String userName = careworker.getName();
		careworker.updateLineUserId(userId);
		careworkerRepository.save(careworker);

		String welcomeMessage =
			" " + userName + " 요양보호사님, 안녕하세요! 🌸 \n" +
				" 최고의 요양원 서비스 돌봄다리입니다. 🤗\n" +
				" 저희와 함께 해주셔서 정말 감사합니다! 🙏\n" +
				" 기본적인 알림 시간은 매일 오후 5시로 설정되어있습니다. 😄\n" +
				" 알림을 받고 싶은 시간을 수정하고 싶으시다면 알려주세요! 💬\n" +
				" 예 : `오후 7시' 혹은 '오후 7시 30분'";

		log.info("Careworker {} has been registered with Line ID {}", userName, userId);
		log.info("Sending welcome message to CareworkerPhone {}", careworker.getPhone());

		lineMessagingUtil.sendMessageToUser(userId, welcomeMessage);
	}

	@Transactional
	public void updateCareworkerAlertTime(String userId, String ampm, String hour, String minute) {
		Careworker careworker = careworkerService.findByLineUserId(userId);
		int minuteValue = (minute != null) ? Integer.parseInt(minute) : 0;  // minute이 null이면 0으로 처리
		LocalTime alertTime = lineMessagingUtil.convertToLocalTime(ampm, Integer.parseInt(hour), minuteValue);
		careworker.updateAlertTime(alertTime);
		careworkerRepository.save(careworker);
	}
}
