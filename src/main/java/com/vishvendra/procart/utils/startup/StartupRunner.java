package com.vishvendra.procart.utils.startup;

import com.vishvendra.procart.entities.ProductCurrency;
import com.vishvendra.procart.model.AdminDTO;
import com.vishvendra.procart.model.InventoryDTO;
import com.vishvendra.procart.model.ProductDTO;
import com.vishvendra.procart.model.ProfileDetailsDTO;
import com.vishvendra.procart.repository.CurrencyRepository;
import com.vishvendra.procart.service.AdminService;
import com.vishvendra.procart.service.inventory.InventoryService;
import com.vishvendra.procart.service.product.ProductService;
import com.vishvendra.procart.service.user.UserService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupRunner implements CommandLineRunner {

  private final AdminService adminService;
  private final ProductService productService;
  private final CurrencyRepository currencyRepository;
  private final UserService userService;
  private final InventoryService inventoryService;
  @Value("${spring.datasource.url}")
  private String dbUrl;
  @Value("${spring.datasource.username}")
  private String username;
  @Value("${spring.datasource.password}")
  private String password;
  @Value("${spring.datasource.driver-class-name}")
  private String className;
  @Value("${JWT_SECRET_KEY:}")
  private String jwtSecret;
  @Value("${JWT_EXPIRATION_IN_MILLIS:3600000}")
  private Long jwtSecretExpirationInMillis;
  @Value("${CREATE_MOCK_UP_DATA:false}")
  private Boolean createMockupData;

  public StartupRunner(AdminService adminService, ProductService productService,
      CurrencyRepository currencyRepository, UserService userService,
      InventoryService inventoryService) {
    this.adminService = adminService;
    this.productService = productService;
    this.currencyRepository = currencyRepository;
    this.userService = userService;
    this.inventoryService = inventoryService;
  }

  @Override
  public void run(String... args) throws Exception {
    if (Objects.isNull(jwtSecret) || Objects.isNull(jwtSecretExpirationInMillis)) {
      throw new Exception("Please set JWT_SECRET_KEY and JWT_EXPIRATION_IN_MILLIS");
    }
    boolean isUserTableEmpty = false;
    boolean dbExists = false;
    String dbName = "procart";
    Connection connection = null;
    ResultSet resultSet = null;
    try {
      Class.forName(className);
      connection = DriverManager.getConnection(dbUrl, username, password);
      resultSet = connection.getMetaData().getCatalogs();

      while (resultSet.next()) {
        String catalogs = resultSet.getString(1);
        if (dbName.equals(catalogs)) {
          dbExists = true;
          break;
        }
      }
      isUserTableEmpty = isUserTableEmpty(connection);
      if (dbExists && isUserTableEmpty) {
        log.info("Admin table is empty, creating an admin");
        this.adminService.createAdmin(createDumbAdmin());
        if (createMockupData) {
          ProductCurrency productCurrency = this.currencyRepository.save(ProductCurrency.builder()
              .code("USD")
              .decimalPrecision(2.0)
              .name("US Dollar")
              .symbol("$")
              .build());
          for (int i = 0; i < 50; i++) {
            ProductDTO product = this.productService.createProduct(
                ProductGenerator.generateProductDTO(productCurrency.getId()));
            this.userService.createUser(UserGenerator.createRandomUser());
            this.inventoryService.create(new InventoryDTO(product.getId(), 100L));
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    } finally {
      assert connection != null;
      connection.close();
    }

  }

  private boolean isUserTableEmpty(Connection connection) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) "
        + "FROM public.p_admin ");
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return resultSet.getInt(1) == 0;
  }

  private AdminDTO createDumbAdmin() {
    AdminDTO adminDTO = new AdminDTO();
    adminDTO.setUsername("admin");
    adminDTO.setName("System Admin");
    adminDTO.setPassword("admin");

    ProfileDetailsDTO profileDetailsDTO = new ProfileDetailsDTO();

    profileDetailsDTO.setFirstName("System");
    profileDetailsDTO.setLastName("Admin");
    profileDetailsDTO.setEmail("admin@procart.com");

    adminDTO.setProfileDetailsDTO(profileDetailsDTO);

    return adminDTO;
  }
}
