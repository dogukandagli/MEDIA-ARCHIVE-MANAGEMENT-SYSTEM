package Assigment1;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

class TableEntry {

	private String title;
	private String url;
	private String type;
	private String releaseYear;
	public String imdbId;
	private String imdbAverageRating;
	private String imdbNumVotes;
	List<String> platforms;
	List<String> genres;
	List<String> countries;
	List<String> platformCountryPairs;
	boolean isDeleted = false;
	private List<PlatformCountry> platformCountries;

	public boolean isIn() {
		return this.imdbId != null && !this.imdbId.isEmpty();
	}

	public boolean isDeleted() {
		return this.isDeleted;
	}

	public void markAsDeleted() {
		this.isDeleted = true; 
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public String getImdbAverageRating() {
		return imdbAverageRating;
	}

	public void setImdbAverageRating(String imdbAverageRating) {
		this.imdbAverageRating = imdbAverageRating;
	}

	public String getImdbNumVotes() {
		return imdbNumVotes;
	}

	public void setImdbNumVotes(String imdbNumVotes) {
		this.imdbNumVotes = imdbNumVotes;
	}

	public List<String> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(List<String> platforms) {
		this.platforms = platforms;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public TableEntry(String title, String url, String type, String imdbId, String releaseYear,
			String imdbAverageRating, String imdbNumVotes) {
		this.title = title;
		this.url = url;
		this.type = type;
		this.imdbId = imdbId;
		this.releaseYear = releaseYear;
		this.imdbAverageRating = imdbAverageRating;
		this.imdbNumVotes = imdbNumVotes;
		this.platforms = new ArrayList<>();
		this.genres = new ArrayList<>();
		this.countries = new ArrayList<>();
		this.platformCountries = new ArrayList<>();
	}

	public void addPlatformCountry(PlatformCountry platformCountry) {
		this.platformCountries.add(platformCountry);
	}

	public List<PlatformCountry> getPlatformCountries() {
		return platformCountries;
	}

	public void setPlatformCountries(List<PlatformCountry> platformCountries) {
		this.platformCountries = platformCountries;
	}

	public void addGenre(String genre) {
		if (!genres.contains(genre)) {
			genres.add(genre);
			Collections.sort(genres);
		}
	}

	public void addPlatformCountry1(PlatformCountry platformCountry) {
		
		platforms.add(platformCountry.getPlatform());
		countries.addAll((Collection<? extends String>) platformCountry.getCountries());
	}

	public void sortLists() {
		Collections.sort(platforms);
		Collections.sort(countries);
		Collections.sort(genres);
	}

	@Override
	public String toString() {
		// Türleri ve diğer bilgileri yazdır
		return "Type : " + type + "\nGenre : " + genres + "\nRelease year : " + releaseYear + "\nImdb ID : " + imdbId
				+ "\nRating : " + imdbAverageRating + "\nNumber of Votes : " + imdbNumVotes + "\n\n" + platforms.size()
				+ " platforms found for " + title + "\n\n" + platformCountries;
	}

}
