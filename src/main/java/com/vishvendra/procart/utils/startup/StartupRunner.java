package com.vishvendra.procart.utils.startup;

import com.vishvendra.procart.model.AdminDTO;
import com.vishvendra.procart.model.ProfileDetailsDTO;
import com.vishvendra.procart.service.AdminService;
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

  public StartupRunner(AdminService adminService) {
    this.adminService = adminService;
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
