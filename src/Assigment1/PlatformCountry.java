package Assigment1;


public class PlatformCountry {
	private String platform;
	private MyArrayList<String> countries;

	// Constructor
	public PlatformCountry(String platform, MyArrayList<String> countriesList) {
		this.platform = platform;
		this.countries = countriesList;
	}

	// Getters
	public String getPlatform() {
		return platform;
	}

	public MyArrayList<String> getCountries() {
		return countries;
	}

	// Setters
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public void setCountries(MyArrayList<String> countries) {
		this.countries = countries;
	}

	@Override
	public String toString() {
		return platform + " - " + countries ;
	}

}
