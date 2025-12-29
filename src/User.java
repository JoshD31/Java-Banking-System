import java.util.Date;

public class User {
    private String firstName;
    private String lastName;
    private Date dob;
    private String ssn;
    private String address;
    private String email;
    private String tel;

    public User(String firstName, String lastName, Date dob, String ssn, String address, String email, String tel) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.ssn = ssn;
        this.address = address;
        this.email = email;
        this.tel = tel;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }
}




/*import java.util.Date;

public class User {
	private String firstName;
	private String lastName;
	private Date dob;
	private String ssn;
	private String address;
	private String email;
	private String phone;
	
	public User() {
		firstName = null;
		lastName = null;
		dob = null;
		ssn = null;
		address = null;
		email = null;
		phone = null;
	} 
	
	public User(String firstName, String lastName, Date dob, String ssn, String address, String email, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.ssn = ssn;
		this.address = address;
		this.email = email;
		this.phone = phone;
	}
	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public boolean equals(Object right) {
		//if parameter is not of user class
		if(! (right instanceof User)) {
			return false;
		}
		User left = this;
		// if 2 users are same object
		if(left == right) {
			return true;
		}
		//downcasting object to be interpreted as user
		User x = (User) right;
		//if all data is exactly the same
		if(left.firstName.equals(x.firstName)
		&& left.lastName.equals(x.lastName)
		&& left.dob.equals(x.dob)
		&& left.ssn.equals(x.ssn)
		&& left.email.equals(x.email)
		&& left.address.equals(x.phone)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("User: ");
		result.append(firstName + ", ");
		result.append( lastName + ", ");
		result.append("DOB: " + dob + ", ");
		result.append("SSN: " + ssn + ", ");
		result.append("Address: " + address + ", ");
		result.append("Email: " + email + ", ");
		result.append("Phone: " + phone + "\n");
		return result.toString();
	}
	
	
	
	
	
	
	
}
*/