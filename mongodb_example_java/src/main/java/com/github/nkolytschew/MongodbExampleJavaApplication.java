package com.github.nkolytschew;

import org.bson.types.ObjectId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class MongodbExampleJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongodbExampleJavaApplication.class, args);
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
//	private ConfigurationDocumentRepository repository;
//
//	SampleDataCLR(final ConfigurationDocumentRepository repository) {
//		this.repository = repository;
//	}
//
//	@Override
//	public void run(String... args) {
//		repository.insert(Arrays.asList(
//				new ConfigurationDocument(ObjectId.get(), "this"),
//				new ConfigurationDocument(ObjectId.get(), "is"),
//				new ConfigurationDocument(ObjectId.get(), "wonderful")
//		));
//	}
//}

/**
 * simple controller.
 * <p>
 * returns hello universe on request path "/hello".
 * used to check if our app works and responds.
 */
@RestController
class HelloUniverseController {
	@GetMapping("/")
	public String getHelloUniverse() {
		return "Hello Universe";
	}
}

/**
 * pojo.
 * <p>
 * defines the structure for our document entries.
 */
@Document(collection = "test")
class ConfigurationDocument {
	@Id
	private ObjectId id;
	@Field
	private String name;

	// for jpa
	public ConfigurationDocument() {
	}

	public ConfigurationDocument(final ObjectId id, final String name) {
		this.id = id;
		this.name = name;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

/**
 * simple mongo repository.
 * <p>
 * provides the main CRUD operations.
 */
@RepositoryRestResource(path = "docs")
interface ConfigurationDocumentRepository extends MongoRepository<ConfigurationDocument, ObjectId> {
	List<ConfigurationDocument> findAllByName(@Param("name") String name);
}