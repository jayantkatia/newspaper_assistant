package customersdisplay;
public class UserBean {
	private String name;
	private String mobileNumber;
	private String address;
	private String papers;
	private String area;
	private String hawkerName;
	private String dos;
	
	public UserBean() {
		
	}
	
	public String getDos() {
		return dos;
	}

	public void setDos(String dos) {
		this.dos = dos;
	}

	public UserBean(String name, String mobileNumber, String address, String papers, String area, String hawkerName,
			String dos) {
		super();
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.address = address;
		this.papers = papers;
		this.area = area;
		this.hawkerName = hawkerName;
		this.dos = dos;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPapers() {
		return papers;
	}
	public void setPapers(String papers) {
		this.papers = papers;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getHawkerName() {
		return hawkerName;
	}
	public void setHawkerName(String hawkerName) {
		this.hawkerName = hawkerName;
	}

}
