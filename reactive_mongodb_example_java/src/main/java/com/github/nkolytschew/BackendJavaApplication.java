package com.github.nkolytschew;

//import com.github.nkolytschew.data.repository.RatingDocumentRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendJavaApplication.class, args);
	}
}

/**
 * simple class command line runner impl.
 * <p>
 * will be executed on application startup and create 2 entries.
 * <p>
 * keep in mind, that this method will be executed on each app start and may pollute/spam your database;
 * you usually only want to use this with embedded databases.
 */
//@Component
//class SampleDataCLR implements CommandLineRunner {
//
//	private final ConfigurationDocumentRepository configRepository;
//	private final RatingDocumentRepository ratingRepository;
//
//	SampleDataCLR(ConfigurationDocumentRepository configRepository, RatingDocumentRepository ratingRepository) {
//		this.configRepository = configRepository;
//		this.ratingRepository = ratingRepository;
//	}
//
//	@Override
//	public void run(String... args) {
//
//		configRepository.deleteAll().subscribe();
//
//		final ConfigurationDocument cDoc = configRepository.save(
//
//				new ConfigurationDocument(
//						"3c97c54b-0ac1-4598-96b8-45c4a269e875",
//						" name",
//						"awesome ", "nik ko",
//						new Date(),
//						new Date(),
//						new Binary(new byte[123]),
//						new Binary(new byte[312])
//				)
//
//		).block();
//
//		ratingRepository.save(
//				new RatingDocument(
//						"1",
//						cDoc.getId(),
//						Arrays.asList(
//								new RatingComment("maxi@spam_me.de", "maxi", 7, "Lorem ipsum dolor sit amet", new Date()),
//								new RatingComment("muster@spam_me.de", "muster", 8, "consetetur sadipscing elitr", new Date()),
//								new RatingComment("maxi.muster@spam_me.de", "maxi muster", 9, "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat", new Date()),
//								new RatingComment("john@spam_me.de", "john", 7, "sed diam voluptua", new Date()),
//								new RatingComment("doe@spam_me.de", "doe", 9, "At vero eos et accusam et justo duo dolores et ea rebum", new Date()),
//								new RatingComment("john doe@spam_me.de", "john doe", 10, "Stet clita kasd gubergren", new Date())
//						)
//				)
//		).subscribe();
//
//		configRepository.saveAll(Arrays.asList(
//				new ConfigurationDocument(UUID.randomUUID().toString(), " name", "awesome ", "nik ko", new Date(), new Date(), new Binary(new byte[2]), new Binary(new byte[1])),
//				new ConfigurationDocument(UUID.randomUUID().toString(), "awesome ", " description", "nik ko", new Date(), new Date(), new Binary(new byte[1]), new Binary(new byte[2])),
//				new ConfigurationDocument(UUID.randomUUID().toString(), "awesome name", "awesome description", "nik ko", new Date(), new Date(), new Binary(new byte[2]), new Binary(new byte[2])),
//				new ConfigurationDocument(UUID.randomUUID().toString(), "none", "none", "nik ko", new Date(), new Date(), new Binary(new byte[1]), new Binary(new byte[1]))
//		)).subscribe();
//
//	}
//}
