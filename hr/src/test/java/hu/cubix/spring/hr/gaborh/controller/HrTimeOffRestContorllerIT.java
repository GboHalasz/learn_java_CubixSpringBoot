package hu.cubix.spring.hr.gaborh.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.cubix.spring.hr.gaborh.dto.LoginDto;
import hu.cubix.spring.hr.gaborh.dto.TimeOffRequestDto;
import hu.cubix.spring.hr.gaborh.dto.TimeOffRequestSearchDto;
import hu.cubix.spring.hr.gaborh.mapper.TimeOffRequestMapper;
import hu.cubix.spring.hr.gaborh.model.Company;
import hu.cubix.spring.hr.gaborh.model.CompanyForm;
import hu.cubix.spring.hr.gaborh.model.Employee;
import hu.cubix.spring.hr.gaborh.model.Position;
import hu.cubix.spring.hr.gaborh.model.Qualification;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequest;
import hu.cubix.spring.hr.gaborh.model.TimeOffRequestStatus;
import hu.cubix.spring.hr.gaborh.repository.CompanyFormRepository;
import hu.cubix.spring.hr.gaborh.repository.CompanyRepository;
import hu.cubix.spring.hr.gaborh.repository.EmployeeRepository;
import hu.cubix.spring.hr.gaborh.repository.PositionDetailsByCompanyRepository;
import hu.cubix.spring.hr.gaborh.repository.PositionRepository;
import hu.cubix.spring.hr.gaborh.repository.TimeOffRequestRepository;
import hu.cubix.spring.hr.gaborh.service.CompanyService;
import hu.cubix.spring.hr.gaborh.service.EmployeeService;
import hu.cubix.spring.hr.gaborh.service.TimeOffRequestService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class HrTimeOffRestContorllerIT {

	private static final String API_TIMEOFFS = "/api/timeoffs";

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	CompanyService companyService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyFormRepository companyFormRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;

	@Autowired
	TimeOffRequestRepository timeOffRequestRepository;

	@Autowired
	TimeOffRequestService timeOffRequestService;

	@Autowired
	TimeOffRequestMapper timeOffRequestMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	private CompanyForm savedCompanyForm;
	private Company savedCompany;
	private Position savedPosition;
	private Employee savedEmployee1;
	private Employee savedEmployee2;
	private Employee savedEmployee3;
	private String jwt;

	@BeforeEach
	public void init() {
		timeOffRequestRepository.deleteAllInBatch();
		employeeRepository.deleteAllInBatch();
		positionDetailsByCompanyRepository.deleteAllInBatch();
		companyRepository.deleteAllInBatch();
		companyFormRepository.deleteAllInBatch();
		positionRepository.deleteAllInBatch();

		savedCompanyForm = companyFormRepository.save(new CompanyForm("LLC"));
		savedCompany = companyService
				.create(new Company(01L, 1L, "SmallCompany", "1100 Budapest, Vágóhíd u. 12.", savedCompanyForm, null));

		savedPosition = positionRepository.save(new Position("developer", Qualification.NONE));
		savedEmployee3 = employeeRepository.save(new Employee("Juno Name3", savedPosition, 10000,
				LocalDateTime.of(2010, 01, 12, 8, 00), savedCompany, null));
		savedEmployee1 = employeeRepository.save(new Employee("Boss name1", savedPosition, 10000,
				LocalDateTime.of(1990, 01, 12, 8, 00), savedCompany, savedEmployee3));
		savedEmployee2 = employeeRepository.save(new Employee("Junior Name2", savedPosition, 10000,
				LocalDateTime.of(2015, 01, 12, 8, 00), savedCompany, savedEmployee1));

		savedEmployee1.setUsername("user1");
		savedEmployee1.setPassword(passwordEncoder.encode("pass"));
		employeeService.save(savedEmployee1);
		savedEmployee2.setUsername("user2");
		savedEmployee2.setPassword(passwordEncoder.encode("pass"));
		employeeService.save(savedEmployee2);
		LoginDto testLogin = new LoginDto("user2", "pass");
		jwt = webTestClient.post().uri("/api/login").header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).bodyValue(testLogin).exchange()
				.expectStatus().isOk().expectBody(String.class).returnResult().getResponseBody();
	}

	@Test
	void testThatCreatedTimeOffRequestIsStored() {
		// ARRANGE
		List<TimeOffRequest> requestsBefore = timeOffRequestRepository.findAll();

		TimeOffRequestDto testRequest = new TimeOffRequestDto(LocalDateTime.now(), LocalDate.of(2024, 01, 01),
				LocalDate.of(2024, 01, 10), savedEmployee2.getId(), savedEmployee1.getId(), null);

		// ACT
		webTestClient.post().uri(API_TIMEOFFS).header("X-CSRF-TOKEN", "my-secret")
				.header("Authorization", "Bearer " + jwt).cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret"))
				.bodyValue(testRequest).exchange().expectStatus().isOk();

		List<TimeOffRequest> requestsAfter = timeOffRequestRepository.findAll();
		TimeOffRequest storedRequest = requestsAfter.get(requestsAfter.size() - 1);

		// ASSERT
		assertThat(requestsAfter.size()).isEqualTo(requestsBefore.size() + 1);
		assertThat(requestsAfter.subList(0, requestsBefore.size())).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(requestsBefore);

		assertThat(storedRequest.getCreatedAt()).isAfterOrEqualTo(testRequest.getCreatedAt());
		assertThat(storedRequest.getStartDate()).isEqualTo(testRequest.getStartDate());
		assertThat(storedRequest.getEndDate()).isEqualTo(testRequest.getEndDate());
		assertThat(storedRequest.getSubmitter().getId()).isEqualTo(testRequest.getSubmitterId());
		assertThat(storedRequest.getApprover()).isNull();
		assertThat(storedRequest.getStatus()).isEqualTo(TimeOffRequestStatus.NOT_JUDGED);
	}

	@Test
	void testThatUpdatedTimeOffRequestIsListedAt() {
		// ARRANGE
		TimeOffRequest savedRequest1 = timeOffRequestRepository.save(
				new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00).plusMonths(2), LocalDate.of(2024, 03, 01),
						LocalDate.of(2024, 03, 10), savedEmployee2, null, TimeOffRequestStatus.NOT_JUDGED));

		List<TimeOffRequest> requestsBefore = timeOffRequestRepository.findAll();

		// ACT
		TimeOffRequestDto testRequestDto = new TimeOffRequestDto(null, LocalDate.of(2024, 02, 01),
				LocalDate.of(2024, 02, 10), savedEmployee3.getId(), savedEmployee1.getId(),
				TimeOffRequestStatus.APPROVED);

		webTestClient.put().uri(API_TIMEOFFS + "/" + savedRequest1.getId()).header("Authorization", "Bearer " + jwt)
				.header("X-CSRF-TOKEN", "my-secret").cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret"))
				.bodyValue(testRequestDto).exchange().expectStatus().isOk();

		List<TimeOffRequest> requestsAfter = timeOffRequestRepository.findAll();
		TimeOffRequest storedRequest = requestsAfter.get(requestsAfter.size() - 1);

		// ASSERT
		assertThat(requestsAfter.size()).isEqualTo(requestsBefore.size());

		assertThat(storedRequest.getId()).isEqualTo(savedRequest1.getId());
		assertThat(storedRequest.getCreatedAt()).isEqualTo(savedRequest1.getCreatedAt());
		assertThat(storedRequest.getStartDate()).isNotEqualTo(savedRequest1.getStartDate());
		assertThat(storedRequest.getEndDate()).isNotEqualTo(savedRequest1.getEndDate());
		assertThat(storedRequest.getStartDate()).isEqualTo(testRequestDto.getStartDate());
		assertThat(storedRequest.getEndDate()).isEqualTo(testRequestDto.getEndDate());
		assertThat(storedRequest.getSubmitter()).isEqualTo(savedRequest1.getSubmitter());
		assertThat(storedRequest.getApprover()).isNull();
		assertThat(storedRequest.getStatus()).isEqualTo(TimeOffRequestStatus.NOT_JUDGED);
	}

	@Test
	void testThatApprovedTimeOffRequestGetApprovedStatus() {
		// ARRANGE
		TimeOffRequest savedRequest1 = timeOffRequestRepository.save(
				new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00).plusMonths(2), LocalDate.of(2024, 03, 01),
						LocalDate.of(2024, 03, 10), savedEmployee2, null, TimeOffRequestStatus.NOT_JUDGED));
		
		LoginDto testLogin = new LoginDto("user1", "pass");
		jwt = webTestClient.post().uri("/api/login").header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).bodyValue(testLogin).exchange()
				.expectStatus().isOk().expectBody(String.class).returnResult().getResponseBody();
		// ACT
		webTestClient.get().uri(API_TIMEOFFS + "/" + savedRequest1.getId() + "/approve")
				.header("Authorization", "Bearer " + jwt).header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).exchange().expectStatus().isOk();

		TimeOffRequest storedRequest = timeOffRequestRepository.findById(savedRequest1.getId()).get();

		// ASSERT
		assertThat(storedRequest.getId()).isEqualTo(savedRequest1.getId());
		assertThat(storedRequest.getCreatedAt()).isEqualTo(savedRequest1.getCreatedAt());
		assertThat(storedRequest.getStartDate()).isEqualTo(savedRequest1.getStartDate());
		assertThat(storedRequest.getEndDate()).isEqualTo(savedRequest1.getEndDate());
		assertThat(storedRequest.getSubmitter()).isEqualTo(savedRequest1.getSubmitter());
		assertThat(storedRequest.getApprover()).isEqualTo(savedEmployee1);
		assertThat(storedRequest.getStatus()).isEqualTo(TimeOffRequestStatus.APPROVED);
	}

	@Test
	void testThatRejectedTimeOffRequestGetRejectedStatus() {
		// ARRANGE
		TimeOffRequest savedRequest1 = timeOffRequestRepository.save(
				new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00).plusMonths(2), LocalDate.of(2024, 03, 01),
						LocalDate.of(2024, 03, 10), savedEmployee2, null, TimeOffRequestStatus.NOT_JUDGED));
		
		LoginDto testLogin = new LoginDto("user1", "pass");
		jwt = webTestClient.post().uri("/api/login").header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).bodyValue(testLogin).exchange()
				.expectStatus().isOk().expectBody(String.class).returnResult().getResponseBody();
		// ACT
		webTestClient.get().uri(API_TIMEOFFS + "/" + savedRequest1.getId() + "/reject")
				.header("Authorization", "Bearer " + jwt).header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).exchange().expectStatus().isOk();

		TimeOffRequest storedRequest = timeOffRequestRepository.findById(savedRequest1.getId()).get();

		// ASSERT
		assertThat(storedRequest.getId()).isEqualTo(savedRequest1.getId());
		assertThat(storedRequest.getCreatedAt()).isEqualTo(savedRequest1.getCreatedAt());
		assertThat(storedRequest.getStartDate()).isEqualTo(savedRequest1.getStartDate());
		assertThat(storedRequest.getEndDate()).isEqualTo(savedRequest1.getEndDate());
		assertThat(storedRequest.getSubmitter()).isEqualTo(savedRequest1.getSubmitter());
		assertThat(storedRequest.getApprover()).isEqualTo(savedEmployee1);
		assertThat(storedRequest.getStatus()).isEqualTo(TimeOffRequestStatus.REJECTED);
	}

	@Test
	void testThatAddedTimeOffRequestCanBeFoundById() {
		// ARRANGE
		TimeOffRequest savedRequest = timeOffRequestRepository
				.save(new TimeOffRequest(LocalDateTime.now(), LocalDate.of(2024, 01, 01), LocalDate.of(2024, 01, 10),
						savedEmployee2, savedEmployee1, TimeOffRequestStatus.APPROVED));

		// ACT
		TimeOffRequestDto resultRequest = webTestClient.get().uri(API_TIMEOFFS + "/" + savedRequest.getId())
				.header("Authorization", "Bearer " + jwt).header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).exchange().expectStatus().isOk()
				.expectBody(TimeOffRequestDto.class).returnResult().getResponseBody();

		// ASSERT
		assertThat(resultRequest).isNotNull();
		assertThat(resultRequest.getId()).isEqualTo(savedRequest.getId());
		assertThat(resultRequest.getCreatedAt()).isCloseTo(savedRequest.getCreatedAt(), within(1, ChronoUnit.MICROS));
		assertThat(resultRequest.getEndDate()).isEqualTo(savedRequest.getEndDate());
		assertThat(resultRequest.getSubmitterId()).isEqualTo(savedRequest.getSubmitter().getId());
		assertThat(resultRequest.getApproverId()).isEqualTo(savedRequest.getApprover().getId());
		assertThat(resultRequest.getStatus()).isEqualTo(TimeOffRequestStatus.APPROVED);
	}

	@Test
	void testThatDeletedTimeOffRequestIsNotListed() {
		// ARRANGE
		List<TimeOffRequest> requestsBefore = timeOffRequestRepository.findAll();

		TimeOffRequest savedRequest = timeOffRequestRepository
				.save(new TimeOffRequest(LocalDateTime.now(), LocalDate.of(2024, 01, 01), LocalDate.of(2024, 01, 10),
						savedEmployee2, null, TimeOffRequestStatus.NOT_JUDGED));

		List<TimeOffRequest> requestsAfterAdd = timeOffRequestRepository.findAll();

		// ACT
		webTestClient.delete().uri(API_TIMEOFFS + "/" + savedRequest.getId()).header("Authorization", "Bearer " + jwt)
				.header("X-CSRF-TOKEN", "my-secret").cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret"))
				.exchange().expectStatus().isOk();

		List<TimeOffRequest> requestsAfterDelete = timeOffRequestRepository.findAll();

		// ASSERT
		assertThat(requestsAfterAdd.size()).isEqualTo(requestsBefore.size() + 1);
		assertThat(requestsAfterDelete.size()).isEqualTo(requestsAfterAdd.size() - 1);

		assertThat(requestsAfterDelete).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(requestsBefore);
	}

	@Test
	void testThatFilteringIsWorkingProperly() {
		// ARRANGE
		TimeOffRequest savedRequest1 = timeOffRequestRepository
				.save(new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00), LocalDate.of(2024, 01, 01),
						LocalDate.of(2024, 01, 10), savedEmployee2, savedEmployee1, TimeOffRequestStatus.APPROVED));
		TimeOffRequest savedRequest2 = timeOffRequestRepository.save(
				new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00).plusMonths(1), LocalDate.of(2024, 02, 01),
						LocalDate.of(2024, 02, 10), savedEmployee2, savedEmployee1, TimeOffRequestStatus.REJECTED));
		TimeOffRequest savedRequest3 = timeOffRequestRepository.save(
				new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00).plusMonths(2), LocalDate.of(2024, 03, 01),
						LocalDate.of(2024, 03, 10), savedEmployee2, null, TimeOffRequestStatus.NOT_JUDGED));
		TimeOffRequest savedRequest4 = timeOffRequestRepository.save(
				new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00).plusMonths(3), LocalDate.of(2024, 04, 01),
						LocalDate.of(2024, 04, 10), savedEmployee2, savedEmployee1, TimeOffRequestStatus.APPROVED));
		TimeOffRequest savedRequest5 = timeOffRequestRepository
				.save(new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00), LocalDate.of(2024, 01, 01),
						LocalDate.of(2024, 01, 10), savedEmployee3, savedEmployee1, TimeOffRequestStatus.APPROVED));
		TimeOffRequest savedRequest6 = timeOffRequestRepository.save(
				new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00).plusMonths(1), LocalDate.of(2024, 02, 01),
						LocalDate.of(2024, 02, 10), savedEmployee3, savedEmployee1, TimeOffRequestStatus.REJECTED));
		TimeOffRequest savedRequest7 = timeOffRequestRepository.save(
				new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00).plusMonths(2), LocalDate.of(2024, 03, 01),
						LocalDate.of(2024, 03, 10), savedEmployee3, null, TimeOffRequestStatus.NOT_JUDGED));
		TimeOffRequest savedRequest8 = timeOffRequestRepository.save(
				new TimeOffRequest(LocalDateTime.of(2024, 01, 01, 13, 00, 00).plusMonths(2), LocalDate.of(2024, 04, 01),
						LocalDate.of(2024, 04, 10), savedEmployee3, null, TimeOffRequestStatus.NOT_JUDGED));

		// ACT
		TimeOffRequestSearchDto filterByStatus = new TimeOffRequestSearchDto(1L, null, null, null, null, null, null,
				null, TimeOffRequestStatus.REJECTED);
		List<TimeOffRequestDto> resultFilterByStatus = webTestClient.post().uri(API_TIMEOFFS + "/search")
				.header("Authorization", "Bearer " + jwt).header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).bodyValue(filterByStatus).exchange()
				.expectStatus().isOk().expectBodyList(TimeOffRequestDto.class).returnResult().getResponseBody();

		TimeOffRequestSearchDto filterBySubmitter = new TimeOffRequestSearchDto(2L, null, null, null, null, null,
				"Juni", null, null);
		List<TimeOffRequestDto> resultFilterBySubmitter = webTestClient.post().uri(API_TIMEOFFS + "/search")
				.header("Authorization", "Bearer " + jwt).header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).bodyValue(filterBySubmitter).exchange()
				.expectStatus().isOk().expectBodyList(TimeOffRequestDto.class).returnResult().getResponseBody();

		TimeOffRequestSearchDto filterByApprover = new TimeOffRequestSearchDto(3L, null, null, null, null, null, null,
				"Bo", null);
		List<TimeOffRequestDto> resultFilterByApprover = webTestClient.post().uri(API_TIMEOFFS + "/search")
				.header("Authorization", "Bearer " + jwt).header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).bodyValue(filterByApprover).exchange()
				.expectStatus().isOk().expectBodyList(TimeOffRequestDto.class).returnResult().getResponseBody();

		TimeOffRequestSearchDto filterByCreationInterval = new TimeOffRequestSearchDto(4L, null,
				LocalDate.of(2024, 01, 01), LocalDate.of(2024, 02, 28), null, null, null, null, null);
		List<TimeOffRequestDto> resultFilterByCreationInterval = webTestClient.post().uri(API_TIMEOFFS + "/search")
				.header("Authorization", "Bearer " + jwt).header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).bodyValue(filterByCreationInterval)
				.exchange().expectStatus().isOk().expectBodyList(TimeOffRequestDto.class).returnResult()
				.getResponseBody();

		TimeOffRequestSearchDto filterByDurationInterval = new TimeOffRequestSearchDto(4L, null, null, null,
				LocalDate.of(2024, 01, 10), LocalDate.of(2024, 03, 05), null, null, null);
		List<TimeOffRequestDto> resultFilterByDurationInterval = webTestClient.post().uri(API_TIMEOFFS + "/search")
				.header("Authorization", "Bearer " + jwt).header("X-CSRF-TOKEN", "my-secret")
				.cookies(cookies -> cookies.add("CSRF-TOKEN", "my-secret")).bodyValue(filterByDurationInterval)
				.exchange().expectStatus().isOk().expectBodyList(TimeOffRequestDto.class).returnResult()
				.getResponseBody();

		// ASSERT
		assertThat(resultFilterByStatus).isNotNull();
		assertThat(resultFilterByStatus.size()).isEqualTo(2);
		for (TimeOffRequestDto tor : resultFilterByStatus) {
			assertThat(tor.getStatus()).isEqualTo(filterByStatus.getStatus());
		}

		assertThat(resultFilterBySubmitter).isNotNull();
		assertThat(resultFilterBySubmitter.size()).isEqualTo(4);
		for (TimeOffRequestDto tor : resultFilterBySubmitter) {
			assertThat(tor.getSubmitterId()).isEqualTo(savedEmployee2.getId());
		}

		assertThat(resultFilterByApprover).isNotNull();
		assertThat(resultFilterByApprover.size()).isEqualTo(5);
		for (TimeOffRequestDto tor : resultFilterByApprover) {
			assertThat(tor.getApproverId()).isEqualTo(savedEmployee1.getId());
		}

		assertThat(resultFilterByCreationInterval).isNotNull();
		assertThat(resultFilterByCreationInterval.size()).isEqualTo(4);
		for (TimeOffRequestDto tor : resultFilterByCreationInterval) {
			assertThat(tor.getCreatedAt()).isBetween(
					filterByCreationInterval.getCreatedAtIntervalStart().atStartOfDay(),
					filterByCreationInterval.getCreatedAtIntervalEnd().plusDays(1).atStartOfDay());
		}

		assertThat(resultFilterByDurationInterval).isNotNull();
		assertThat(resultFilterByDurationInterval.size()).isEqualTo(6);
		for (TimeOffRequestDto tor : resultFilterByDurationInterval) {
			assertTrue((tor.getStartDate().isBefore(filterByDurationInterval.getEndDate())
					|| tor.getStartDate().isEqual(filterByDurationInterval.getEndDate()))
					&& (tor.getEndDate().isAfter(filterByDurationInterval.getStartDate())
							|| tor.getEndDate().isEqual(filterByDurationInterval.getStartDate())));
		}

	}

}
