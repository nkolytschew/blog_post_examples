package com.github.nkolytschew


import com.github.nkolytschew.data.document.ConfigurationDocument
import com.github.nkolytschew.data.document.RatingComment
import com.github.nkolytschew.data.document.RatingDocument
import com.github.nkolytschew.data.repository.ConfigurationDocumentRepository
import com.github.nkolytschew.data.repository.RatingDocumentRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.*

@SpringBootApplication
class BackendKotlinApplication

/**
 * bootstrap spring
 */
fun main(args: Array<String>) {
  SpringApplication.run(BackendKotlinApplication::class.java, *args)
}

/**
 * simple class command line runner impl.
 *
 * will be executed on application startup and create some entries.
 *
 * keep in mind, that this method will be executed on each app start and may pollute/spam your database;
 * you usually only want to use this with embedded databases.
 */
//@Component
//class SampleDataCLR(val configRepository: ConfigurationDocumentRepository,
//                    val ratingRepository: RatingDocumentRepository) : CommandLineRunner {
//  override fun run(vararg args: String?) {
//    configRepository.deleteAll().subscribe()
//
//    val configurationWithRatings = configRepository.getAggregatedConfigurationWithRatingsById("3c97c54b-0ac1-4598-96b8-45c4a269e875")
//
//    //  blocking
//    val cDoc = configRepository.save(
//        ConfigurationDocument(
//            " name",
//            "nik ko",
//            "awesome ",
//            ByteArray(DEFAULT_BUFFER_SIZE, { 2 }).toString(),
//            ByteArray(DEFAULT_BUFFER_SIZE, { 1 }).toString())
//    ).block()
//
//    // create after configuration; not necessary to block
//    ratingRepository.save(
//        RatingDocument(
//            UUID.randomUUID().toString(),
//            cDoc.id,
//            arrayListOf(
//                RatingComment("maxi@spam_me.de", "maxi", 7, "Lorem ipsum dolor sit amet"),
//                RatingComment("muster@spam_me.de", "muster", 8, "consetetur sadipscing elitr"),
//                RatingComment("maxi.muster@spam_me.de", "maxi muster", 9, "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat"),
//                RatingComment("john@spam_me.de", "john", 7, "sed diam voluptua"),
//                RatingComment("doe@spam_me.de", "doe", 9, "At vero eos et accusam et justo duo dolores et ea rebum"),
//                RatingComment("john doe@spam_me.de", "john doe", 10, "Stet clita kasd gubergren")
//            ))
//    ).subscribe()
//
//    // not necessary to block
//    configRepository.saveAll(Flux.just(
//        ConfigurationDocument(" name", "nik ko", "awesome ", ByteArray(DEFAULT_BUFFER_SIZE, { 2 }).toString(), ByteArray(DEFAULT_BUFFER_SIZE, { 1 }).toString()),
//        ConfigurationDocument("awesome ", "nik ko", " description", ByteArray(DEFAULT_BUFFER_SIZE, { 1 }).toString(), ByteArray(DEFAULT_BUFFER_SIZE, { 2 }).toString()),
//        ConfigurationDocument("awesome name", "nik ko", "awesome description", ByteArray(DEFAULT_BUFFER_SIZE, { 2 }).toString(), ByteArray(DEFAULT_BUFFER_SIZE, { 2 }).toString()),
//        ConfigurationDocument("none", "nik ko", "none", ByteArray(DEFAULT_BUFFER_SIZE, { 1 }).toString(), ByteArray(DEFAULT_BUFFER_SIZE, { 1 }).toString())
//    )).subscribe()
//  }
//}

