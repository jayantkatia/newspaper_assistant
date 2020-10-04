package billHistoryDisplay;

import java.sql.Date;

public class UserBean {
	private String customerName;
	private String customerMobile;
	private Integer days;
	private Date doBilling;
	private Float amount;
	public UserBean() {
		
	}
	public UserBean(String customerName, String customerMobile, Integer days, Date doBilling, Float amount) {
		super();
		this.customerName = customerName;
		this.customerMobile = customerMobile;
		this.days = days;
		this.doBilling = doBilling;
		this.amount = amount;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerMobile() {
		return customerMobile;
	}
	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Date getDoBilling() {
		return doBilling;
	}
	public void setDoBilling(Date doBilling) {
		this.doBilling = doBilling;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
}
