//package ospbusapp;
//
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
////Controller class for processing GET requests and performing appropriate action
//@RestController
//public class BusController {
//
//    @GetMapping("/route/{route}")
//    public String getRoute(@PathVariable String route) {
//        return "Getting data for route " + route + "...";
//    }
//
//    //Samples:
//    //Use the GetMapping annotation to specify the method should be called when a GET request is received at this URL
//    @GetMapping("/hello-world")
//    public String helloWorld() {
//        System.out.println("...HELLO WORLD....");
//        return "Hello World!";
//    }
//
//    @GetMapping("/title")
//    @Cacheable("title")
//    public String getTitle() {
//        BusService busService = new BusService();
//        System.out.println("BUS CONTROLLER...");
//        return busService.getTitle();
//    }
//
//    @GetMapping("/buses/{busId}")
//    public String getNextStopsForBus(@PathVariable String busId) {
//        BusService busService = new BusService();
//        System.out.println("...EXECUTING METHOD...");
//
//        return busSet;
//    }
//}
