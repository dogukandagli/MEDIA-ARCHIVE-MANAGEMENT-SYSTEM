
package Assigment1;


class HashTable {
	private TableEntry[] table;
	public int numberOfEntires;

	// Constructor
	public HashTable() {
		int size = 100;
		table = new TableEntry[size];
		numberOfEntires = 0;
	}

	int hash = 0;

	public int hash(String key) {

		int hashValue = simpleHash(key) % table.length;
		// int hashValue = polynomialHash(key) % table.length;
		if (hashValue < 0) {
			return hashValue + table.length;
		}

		return hashValue;
	}

	public static int simpleHash(String key) {
		int hash = 0;
		key = key.toLowerCase();
		for (int i = 0; i < key.length(); i++) {
			// Convert character to number (1-26 for case insensitive)
			int charValue = key.charAt(i);

			// Add to the hash value
			hash += charValue;
		}
		return hash;
	}

	public static int polynomialHash(String key) {
	    key = key.toLowerCase(); // Harfleri küçük yap
	    int hashValue = 0;
	    int x = Integer.MAX_VALUE; // `int` tipi için maksimum değer
	    
	    // İlk hesaplama: Taşma riski olmadan hesaplama
	    for (int i = 0; i < key.length(); i++) {
	        int charValue = Character.toLowerCase(key.charAt(i)) - 'a' + 1; // Harfler için 1-26
	        hashValue += charValue * Math.pow(33, key.length() - 1 - i);

	        // Eğer değer x sınırını aşarsa, taşma olduğunu kabul et
	        if (hashValue > x) {
	            // Horner kuralına geç
	            return computeHashWithHorner(key, 33, x);
	        }
	    }

	    return hashValue;
	}

	public static int computeHashWithHorner(String key, int z, int mod) {
	    int hashValue = 0;

	    // Horner kuralı ile güvenli hesaplama
	    for (int i = 0; i < key.length(); i++) {
	        int charValue = Character.toLowerCase(key.charAt(i)) - 'a' + 1; // Harfler için 1-26
	        hashValue = (hashValue * z + charValue) % mod; // Modüler aritmetik
	    }

	    return hashValue;
	}

	long addTime = 0;        

	public void addDataSet(String key, TableEntry entry) {
		long startTime = System.currentTimeMillis();
		// Eğer anahtar ya da giriş null ise hata fırlat
		if (key == null || entry == null) {
			throw new IllegalArgumentException("Cannot add null to a dictionary");
		}

		// Hash işlemi ve probe

		int index = hash(key);
		index = probe(index, key);
		if (index < 0 || index >= table.length) {
			throw new ArrayIndexOutOfBoundsException("Invalid index calculated: " + index);
		}

		// Tablo boyutunu kontrol et ve gerekirse yeniden boyutlandır
		assert (index >= 0) && (index < table.length);
		if (isHashTableTooFull(index, key)) {
			enlargeHashTable();
			index = hash(key); // Yeniden hash hesapla
			index = probe(index, key);
		}

		// Eğer index boşsa ya da silinmişse, yeni veriyi ekle
		if (table[index] == null || (table[index] != null && table[index].isDeleted())) {
			table[index] = entry;
			numberOfEntires++;
		} else {
			// Eğer zaten veri varsa, platformları ve ülkeleri güncelle
			TableEntry existingEntry = table[index];

			// Yeni platformları ekle
			for (String platform : entry.getPlatforms()) {
				if (!existingEntry.getPlatforms().contains(platform.trim())) {
					existingEntry.getPlatforms().add(platform.trim());
				}
			}

			// Yeni ülkeleri ekle
			for (String country : entry.getCountries()) {
				if (!existingEntry.getCountries().contains(country.trim())) {
					existingEntry.getCountries().add(country.trim());
				}
			}

			// Platform ve ülke verilerini ekleyeceğiz
			for (PlatformCountry platformCountry : entry.getPlatformCountries()) {
				boolean exists = false;

				// Eğer aynı platform ve ülkeye sahip bir entry varsa, var olanı güncelle
				for (PlatformCountry existingPlatformCountry : existingEntry.getPlatformCountries()) {
					if (existingPlatformCountry.getPlatform().equals(platformCountry.getPlatform())
							&& existingPlatformCountry.getCountries().equals(platformCountry.getCountries())) {
						exists = true;
						break;
					}
				}

				// Eğer yeni platform ve ülke varsa, listeye ekle
				if (!exists) {
					existingEntry.addPlatformCountry(platformCountry);
				}
			}

		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		addTime += time;
	}

	private boolean isHashTableTooFull(int index, String key) {
		double loadFactor = (double) numberOfEntires / table.length;
		if (loadFactor >= 0.8) {
			return true; // Yeni index'i bul
		}
		return false;
	}

	private void enlargeHashTable() {

		TableEntry[] oldtable = table;
		int oldsize = table.length;
		int newSize = oldsize * 2;

		TableEntry[] temp = (TableEntry[]) new TableEntry[newSize];
		table = temp;
		numberOfEntires = 0;

		for (int index = 0; index < oldsize; index++) {
			if (oldtable[index] != null && oldtable[index].isIn()) {
				addDataSet(oldtable[index].getImdbId(), oldtable[index]);
			}
		}

	}

	public TableEntry[] getTable() {
		return table;
	}

	private int dbSecondaryHash(String key) {

		//int prime = getPrimeLessThan(table.length); // Tablo boyutundan küçük asal sayıyı al
		// return prime - (simpleHash(key) % prime);
		return 17 - simpleHash(key) % 17;
		// return 29- polynomialHash(key) % 29;
	}

	
	long collision;

	private int probe(int index, String key) {
		int stepSize = dbSecondaryHash(key);
		// double hashing
		boolean found = false;
		int removedStateIndex = -1;

		while (!found && (table[index] != null)) {
			if (table[index].isIn()) {
				if (key.equals(table[index].getImdbId())) {
					found = true; // Aranan anahtar bulundu.
				} else {
					index = (index + stepSize) % table.length; // double hashing
					//index = (index + 1) % table.length;
					collision++; // Çakışma sayısını artır.
				}
			} else {
				if (removedStateIndex == -1) {
					removedStateIndex = index; // İlk silinmiş durumu kaydet.
				}
				//index = (index + 1) % table.length;
				index = (index + stepSize) % table.length; //double hashing
			}
		}

		if (found || (removedStateIndex == -1)) {
			return index;
		} else {
			return removedStateIndex;
		}
	}

	public void resetCollisionCount() {
		collision = 0;
	}

	public TableEntry get(String key) {

		int index = hash(key);
		index = locate(index, key);
		if (index != -1) {
			if (table[index] != null && table[index].getImdbId().equals(key)) {
				return table[index]; 
			}
		}
		return null; 
	}

	public boolean remove(String key) {
		int index = hash(key); // Anahtar için hash değeri
		index = locate(index, key); // Anahtarın tablodaki yerini bul

		if (index == -1 || table[index] == null) {
			// bulmazsa bull
			System.out.println("Media with id " + key + " not found.");
			return false;
		}

		// Öğeyi bulduysanız, silme işlemi yapın
		if (table[index].getImdbId().equals(key)) {
			table[index]=null;
			table[index].markAsDeleted(); // Öğeyi silinmiş olarak işaretle
			System.out.println("Media with id " + key + " has been removed.");
			numberOfEntires--;
			return true;
		}

		return false; // Öğe bulunamadıysa
	}

	private int locate(int index, String key) {
		int stepSize = dbSecondaryHash(key);
		boolean found = false;
		while (!found && (table[index] != null)) {
			if (table[index].isIn() && key.equals(table[index].getImdbId())) {
				found = true;
			} else {

				
			  // index = (index + 1) % table.length;
				index = (index + stepSize) % table.length; // double hashing
			}
		}
		int result = -1;
		if (found) {
			result = index;
		}
		return result;
	}

	public MyArrayList<TableEntry> getAllEntries() {
		MyArrayList<TableEntry> entries = new MyArrayList<>();
		for (TableEntry entry : table) {
			if (entry != null && !entry.isDeleted()) {
				entries.add(entry);
			}
		}
		return entries;
	}

	
	
}
