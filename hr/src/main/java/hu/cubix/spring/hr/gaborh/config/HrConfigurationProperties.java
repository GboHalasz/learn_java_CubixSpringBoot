package hu.cubix.spring.hr.gaborh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigurationProperties {

	private Raise raise;

	public Raise getRaise() {
		return raise;
	}

	public void setRaise(Raise raise) {
		this.raise = raise;
	}

	public static class Raise {

		private Smart smart;

		public Smart getSmart() {
			return smart;
		}

		public void setSmart(Smart special) {
			this.smart = special;
		}

		public static class Smart {

			private long monthsLimit1;

			private int percentForMonthsLimit1;

			private long monthsLimit2;

			private int percentForMonthsLimit2;

			private long monthsLimit3;

			private int percentForMonthsLimit3;

			public long getMonthsLimit1() {
				return monthsLimit1;
			}

			public void setMonthsLimit1(long monthsLimit1) {
				this.monthsLimit1 = monthsLimit1;
			}

			public int getPercentForMonthsLimit1() {
				return percentForMonthsLimit1;
			}

			public void setPercentForMonthsLimit1(int percentForMonthsLimit1) {
				this.percentForMonthsLimit1 = percentForMonthsLimit1;
			}

			public long getMonthsLimit2() {
				return monthsLimit2;
			}

			public void setMonthsLimit2(long monthsLimit2) {
				this.monthsLimit2 = monthsLimit2;
			}

			public int getPercentForMonthsLimit2() {
				return percentForMonthsLimit2;
			}

			public void setPercentForMonthsLimit2(int percentForMonthsLimit2) {
				this.percentForMonthsLimit2 = percentForMonthsLimit2;
			}

			public long getMonthsLimit3() {
				return monthsLimit3;
			}

			public void setMonthsLimit3(long monthsLimit3) {
				this.monthsLimit3 = monthsLimit3;
			}

			public int getPercentForMonthsLimit3() {
				return percentForMonthsLimit3;
			}

			public void setPercentForMonthsLimit3(int percentForMonthsLimit3) {
				this.percentForMonthsLimit3 = percentForMonthsLimit3;
			}

		}

	}
}
