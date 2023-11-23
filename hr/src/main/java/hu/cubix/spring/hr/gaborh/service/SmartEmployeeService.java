package hu.cubix.spring.hr.gaborh.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.spring.hr.gaborh.config.HrConfigurationProperties;
import hu.cubix.spring.hr.gaborh.config.HrConfigurationProperties.Raise.Smart;
import hu.cubix.spring.hr.gaborh.model.Employee;

@Service
public class SmartEmployeeService implements EmployeeService {

//	@Value("${hr.raise.smart.monthsLimit1}")
//	private long monthsLimit1;
//	@Value("${hr.raise.smart.percentForMonthsLimit1}")
//	private int percentForMonthsLimit1;
//	@Value("${hr.raise.smart.monthsLimit2}")
//	private long monthsLimit2;
//	@Value("${hr.raise.smart.percentForMonthsLimit2}")
//	private int percentForMonthsLimit2;
//	@Value("${hr.raise.smart.monthsLimit3}")
//	private long monthsLimit3;
//	@Value("${hr.raise.smart.percentForMonthsLimit3}")
//	private int percentForMonthsLimit3;

	@Autowired
	private HrConfigurationProperties config;

		
	@Override
	public int getPayRaisePercent(Employee employee) {
		Smart smartConfig = config.getRaise().getSmart();

		long passedMonth = employee.getStartDate().until(LocalDateTime.now(), ChronoUnit.MONTHS);
				
		Map<Long, Integer> unsortedLimits = Map.of(smartConfig.getMonthsLimit1(),
				smartConfig.getPercentForMonthsLimit1(), smartConfig.getMonthsLimit2(),
				smartConfig.getPercentForMonthsLimit2(), smartConfig.getMonthsLimit3(),
				smartConfig.getPercentForMonthsLimit3());
		// Map<Long, Integer> sortedLimits = new TreeMap<>(unsortedLimits);

		int payRaisePercent = 0;		
		
		for (Map.Entry<Long, Integer> entry : unsortedLimits.entrySet()) {
			if (passedMonth >= entry.getKey() && payRaisePercent < entry.getValue()) {
				payRaisePercent = entry.getValue();
			}
		}
		
		return payRaisePercent;
	}

}
