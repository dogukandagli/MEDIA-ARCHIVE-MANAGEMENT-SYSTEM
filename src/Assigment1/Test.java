package Assigment1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Test {

	public static void loadDataset(String filePath, HashTable hashTable) {
		try (BufferedReader br1 = new BufferedReader(new FileReader(filePath))) {
			String line1;

			while ((line1 = br1.readLine()) != null) {
				try {
					String[] values1 = line1.split(";");

					String url = values1[0];
					String title = values1[1];
					String type = values1[2];
					String genres = values1[3];
					String releaseYear = values1[4];
					String imdbId = values1[5];
					String imdbAverageRating = values1[6];
					String imdbNumVotes = values1[7];
					String platforms = values1[8];
					String availableCountries = values1[9];

					TableEntry entry = new TableEntry(title.trim(), url.trim(), type.trim(), imdbId.trim(),
							releaseYear.trim(), imdbAverageRating.trim(), imdbNumVotes.trim());

					// Platformları, türleri ve ülkeleri ekle

					for (String genre : genres.split(",")) {
						String trimmedGenre = genre.trim();
						if (!trimmedGenre.isEmpty()) { // Boş türleri atla
							entry.addGenre(trimmedGenre);
						}
					}

					// Ülkeleri ekleyin

					MyArrayList<String> countriesList = new MyArrayList<String>();

					for (String country : availableCountries.split(",")) {
						countriesList.add(country.trim());
					}

					PlatformCountry platformCountry = new PlatformCountry(platforms, countriesList);
					entry.addPlatformCountry(platformCountry);

					entry.addPlatformCountry1(platformCountry);

					// Platformları işleme
					hashTable.addDataSet(imdbId, entry);
					// HashTable'a ekle
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println("Error parsing line (ArrayIndexOutOfBounds): " + line1);
				} catch (Exception e) {
					System.err.println("Error processing line: " + line1);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + filePath);
			e.printStackTrace();
		}
	}

	public static void searchByImdbId(HashTable hashTable, String imdbId) {
		TableEntry entry = hashTable.get(imdbId);
		if (entry != null) {
			System.out.println(entry); // Print the media details
		} else {
			System.out.println("not found");
		}
	}

	public static void runSearchTest(String filePath, HashTable hashTable) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			int successfulSearches = 0;
			long startTime = System.currentTimeMillis();

			while ((line = br.readLine()) != null) {
				if (hashTable.get(line.trim()) != null) {
					successfulSearches++;
				}
			}

			long endTime = System.currentTimeMillis();
			System.out.println("1000 searches completed in: " + (endTime - startTime) + "ms");
			System.out.println("Successful searches: " + successfulSearches);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void collisionAndTimeTest(HashTable hashTable) {

		System.out.println("Collision count: " + hashTable.collision);

		System.out.println("Time" + hashTable.addTime);

	}

	public static void listMediaByCountry(HashTable hashTable, String country) {
		MyArrayList<TableEntry> entries = new MyArrayList<>();

		// HashTable üzerinde iterasyon yaparak tüm girişleri kontrol ediyoruz
		for (int i = 0; i < hashTable.getTable().length; i++) {
			TableEntry entry = hashTable.getTable()[i];
			if (entry != null) {
				// Eğer medya, belirtilen ülkeye sahip bir platformda yayında ise listeye
				// ekliyoruz
				for (String country1 : entry.getCountries()) {
					if (country1.equals(country)) { // Use equals to compare strings
						entries.add(entry);
						break;
					}
				}
			}
		}

		// Sonuçları yazdırıyoruz
		System.out.println("Media streams in " + country + ":");
		if (entries.isEmpty()) {
			System.out.println("No media found.");
		} else {
			for (TableEntry entry : entries) {
				System.out.println();
				System.out.println(entry.getTitle() + entry.getImdbId());
			}
		}
	}

	public static void listTop10ByVotes(HashTable hashTable) {
		MyArrayList<TableEntry> entries = new MyArrayList<>();

		// HashTable üzerinde iterasyon yaparak tüm girişleri alıyoruz
		for (int i = 0; i < hashTable.getTable().length; i++) {
			TableEntry entry = hashTable.getTable()[i];
			if (entry != null) {
				entries.add(entry);
			}
		}

		// Oy sayısına göre azalan sırada sıralama yapıyoruz
		entries.sort((a, b) -> {
			try {
				int votesA = parseVotes(a.getImdbNumVotes());
				int votesB = parseVotes(b.getImdbNumVotes());
				return Integer.compare(votesB, votesA); // Azalan sırada
			} catch (NumberFormatException e) {
				System.err.println("Invalid number format: " + e.getMessage());
				return 0; // Hata durumunda sıralamayı etkisiz hale getir
			}
		});

		// Top 10 medya öğesini yazdırıyoruz
		System.out.println("Top 10 Media by User Votes:");
		for (int i = 0; i < Math.min(10, entries.size()); i++) {
			System.out.println();
			System.out.println("===========================================");
			System.out.println(entries.get(i));
			System.out.println();
		}
	}

	// Oy sayısını güvenli bir şekilde ayrıştıran yardımcı fonksiyon
	private static int parseVotes(String votes) {
		if (votes == null || votes.trim().isEmpty()) {
			return 0; // Geçersiz ya da boş değer için varsayılan 0
		}
		return Integer.parseInt(votes.trim());
	}

	public static void listMediaOnAllPlatforms(HashTable hashTable) {
		MyArrayList<TableEntry> entries = new MyArrayList<>();

		// HashTable üzerinde iterasyon yaparak tüm girişleri kontrol ediyoruz
		for (int i = 0; i < hashTable.getTable().length; i++) {
			TableEntry entry = hashTable.getTable()[i];
			if (entry != null) {
				// Medyanın tüm platformlarda yayında olup olmadığını kontrol ediyoruz
				if (entry.getPlatforms().contains("Netflix") && entry.getPlatforms().contains("Amazon Prime")
						&& entry.getPlatforms().contains("Apple TV+") && entry.getPlatforms().contains("Hulu")
						&& entry.getPlatforms().contains("HBO Max")) {
					entries.add(entry);
				}
			}
		}

		System.out.println("Media items streaming on all 5 platforms:");
		if (entries.isEmpty()) {
			System.out.println("No media found.");
		} else {
			for (TableEntry entry : entries) {
				System.out.println(entry);
				System.out.println();
				System.out.println("==========================================");
				System.out.println();
			}
		}
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String filePath1 = "movies_dataset.csv";
		String filePath2 = "search.txt";
		HashTable hashTable = new HashTable(); // HashTable örneği
		boolean loop = true;
		while (loop) {
			System.out.println("1.	Load dataset\r\n" + "2.	Run 1000 search test\r\n"
					+ "3.	Search for a media item with the ImdbId.\r\n"
					+ "4.	List the top 10 media according to user votes\r\n"
					+ "5.	List all the media streams in a given country\r\n"
					+ "6.	List the media items that are streaming on all 5 platforms\r\n");

			System.out.print("Please enter a number:");
			int i = -1;
            while (true) {
                try {
                    i = Integer.parseInt(scan.nextLine()); // Read input as an integer
                    if (i >= 1 && i <= 7) {  // Ensure the input is within valid range
                        break;  // Exit loop if valid option is entered
                    } else {
                        System.out.println("Please enter a number between 1 and 7.");
                    	System.out.print("Please enter a number:");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                	System.out.print("Please enter a number:");
                }
            }
			switch (i) {

			case 1:
				loadDataset(filePath1, hashTable);
				break;

			case 2:
				runSearchTest(filePath2, hashTable);

				break;
			case 3:
				System.out.print("ENTER ID:");
				String id = scan.nextLine();
				searchByImdbId(hashTable, id);
				break;

			case 4:
				listTop10ByVotes(hashTable);
				break;

			case 5:
				System.out.println("Enter which country you want to list media streams in:");
				String cnt = scan.nextLine();
				listMediaByCountry(hashTable,  cnt);
				break;
			case 6:
				listMediaOnAllPlatforms(hashTable);
				break;
			case 7:
				collisionAndTimeTest(hashTable);
				break;
			}

			System.out.println();
		}
		scan.close();
	}

}
