//package zh.bookreader.services.bootstrap
//
//import org.slf4j.LoggerFactory
//import org.springframework.boot.CommandLineRunner
//import org.springframework.stereotype.Component
//import zh.bookreader.services.SearchService
//
//@Component
//class BootstrapSearchIndex(private val searchService: SearchService) : CommandLineRunner {
//    private val log = LoggerFactory.getLogger(this.javaClass)
//
//    override fun run(vararg args: String) {
//        log.info("Starting up the search service [{}]", searchService.javaClass)
//        searchService.start();
//        log.info("Search service [{}] has been started up", searchService.javaClass)
//    }
//}