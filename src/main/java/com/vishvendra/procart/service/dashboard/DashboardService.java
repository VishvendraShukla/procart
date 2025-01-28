package com.vishvendra.procart.service.dashboard;

import com.vishvendra.procart.model.DashboardCardData;
import com.vishvendra.procart.repository.ChargeRepository;
import com.vishvendra.procart.repository.ProductRepository;
import com.vishvendra.procart.repository.UserRepository;
import com.vishvendra.procart.service.charge.ChargeSpecifications;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("dashboardService")
@RequiredArgsConstructor
public class DashboardService {

  private final ProductRepository productRepository;
  private final ChargeRepository chargeRepository;
  private final UserRepository userRepository;

  public List<DashboardCardData> getDashboardCardData() {
    return List.of(
        getProductCardData(),
        getChargeCardData(),
        getUserCardData());
  }

  private DashboardCardData getProductCardData() {
    return new DashboardCardData(
        "Total Products",
        String.valueOf(productRepository.count()),
        "Package",
        "bg-blue-500");
  }

  private DashboardCardData getChargeCardData() {
    return new DashboardCardData(
        "Active Charges",
        String.valueOf(chargeRepository.count(ChargeSpecifications.isDeletedFalse())),
        "DollarSign",
        "bg-green-500");
  }

  private DashboardCardData getUserCardData() {
    return new DashboardCardData(
        "Total Users",
        String.valueOf(userRepository.count()),
        "Users",
        "bg-purple-500");
  }


}
