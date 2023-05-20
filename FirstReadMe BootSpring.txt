https://youtu.be/mPPhcU7oWDU
Spring Boot Microservice Project Full Course in 6 Hours
Programming Techie
https://github.com/SaiUpadhyayula/spring-boot-microservices

6:22 Product-service app
Maven Dependensy: 
Lombok
Spring Web
Spring Data MongoDB
7:36 Config applicattion.properties
spring.data.mongodb.uri=mongodb://localhost:27017/product-service
// Run MongoDB locally with command in CMD
mongod

8:04 Create product module
@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}

8:42 Create  repository interface
public interface ProductRepository extends MongoRepository<Product, String> {
}

9:14(15:40)(17:57) Create Product Controller
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequestDto){
        productService.createProduct(productRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }
}


10:36 Create DTO
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
}

11:32(18:40) Create Product Service
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequestDto){
        Product product = Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved.", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}

16:19 Create ProductResponse DTO
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}

23:39 Create test entigrated test
24:26 Install Testcontainers https://www.testcontainers.org/
24:56 Copy dependencyManagement to pom.xml  from testcontainers.org/
27:16 Inatall MongoDB Container from testcontainers.org/
28:48 Add Test framework integration JUnit5 from testcontainers.org/

29:37 Test

44:52 // Create Order-service app
Dependencies:
Lombok
Spring Web
Spring Data JPA
MySQL Driver

46:12 // Create project microservices-parent
47:27 // Create new packages:
order-service.controller
order-service.dto
order-service.model
order-service.repository
order-service.service
48:17 // Create model.order class
49:48 // Create OrderLineItems class
53:23 // Create order-service.controller.OrderController
55:56 // Crate service order-service.service.OrderService
58:53 // Create repository
1:01:39 // Add db properties to application.properties.

1:06:00 // Create project inventory-service
Dependencies:
Lombok
Spring Web
Spring Data JPA
MySQL Driver
1:08:28 // Create packages:
inventory-service.controller
inventory-service.dto
inventory-service.model
inventory-service.repository
inventory-service.service
1:08:34 // Create Inventory model
1:09:45 // Create Inventory Repository
1:10:44 // Create Inventory Controller
1:12:44 // Create Inventory Service 
1:14:07 // Add method findBySkuCode to InventoryReapository
1:16:47 // Create dumy DB
-------
1:19:45 // Create maven parent project
1:20:18 // Create new maven project
File->New->Project->New Project->Java-Maven
JDK17
name:microservices-new
- Create
1:21:37 Create modules in microservices-new
File->New->Module->New Module->Java-Maven
JDK17
name: product-service
-Create (create product-service, order-service, inventory-service)
1:22:52 // Delete mincroservices-new.src folder
1:22:58 // Migreate project src directory to module src directory (migrate product-service, order-service, inventory-service)
1:23:23 // Open microservices-parent.product-service.pom.xml and copy dependencies  and past to module pom.xml(microservices-new.product-service.pom.xml) after </properties> (repeat for order-service and inventory service)

1:25:28 //  Copy <dependencyManagement> from microservices-parent.product-service.pom.xml to microservices-new.pom.xml
1:26:35 //  Copy <parent > from microservices-parent.product-service.pom.xml to microservices-new.pom.xml 
1:28:03 // Copy <build>  from microservices-parent.product-service.pom.xml to microservices-new.pom.xml 
1:29:16 // Delete src directory from modules directory and past from projects
1:30:40 // Maven clean verify (for checking if all works) or run main() of each app
---------
1:35:04 // Inter Process Communication
1:38:23 // Call Inventory Service from Order Service
1:38:47 // Create new config package in Order-service









