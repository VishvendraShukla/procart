package com.vishvendra.procart.service.dashboard;

import com.vishvendra.procart.entities.User;
import com.vishvendra.procart.mapper.UserMapper;
import com.vishvendra.procart.model.DashboardCardData;
import com.vishvendra.procart.model.DashboardUserData;
import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.repository.ChargeRepository;
import com.vishvendra.procart.repository.ProductRepository;
import com.vishvendra.procart.repository.UserRepository;
import com.vishvendra.procart.service.charge.ChargeSpecifications;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service("dashboardService")
@RequiredArgsConstructor
public class DashboardService {

  private final ProductRepository productRepository;
  private final ChargeRepository chargeRepository;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public List<DashboardCardData> getDashboardCardData() {
    return List.of(
        getProductCardData(),
        getChargeCardData(),
        getUserCardData());
  }

  public PageResultResponse<DashboardUserData> getDashboardUserData(PageRequest pageRequest) {
    Page<User> users = this.userRepository.findAll(pageRequest);
    return mapToDTOList(users);
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

  private PageResultResponse<DashboardUserData> mapToDTOList(Page<User> users) {
    List<DashboardUserData> userDTOList = users.map(user -> {
      DashboardUserData userData = new DashboardUserData();
      userData.setUsername(user.getUsername());
      if (Objects.nonNull(user.getProfileDetails())) {
        if (Objects.nonNull(user.getProfileDetails().getEmail())) {
          userData.setEmail(user.getProfileDetails().getEmail());
        }
        if (Objects.nonNull(user.getProfileDetails().getPhone())) {
          userData.setPhone(user.getProfileDetails().getPhone());
        }
        if (Objects.nonNull(user.getProfileDetails().getWhatsApp())) {
          userData.setWhatsApp(user.getProfileDetails().getWhatsApp());
        }
        if (Objects.nonNull(user.getProfileDetails().getAddress())) {
          userData.setHasAddress(Boolean.TRUE);
          userData.setAddress(user.getProfileDetails().getAddress());
        } else {
          userData.setHasAddress(Boolean.FALSE);
        }
      }
      return userData;
    }).toList();
    ;

    return PageResultResponse.of(
        userDTOList,
        users.getSize(),
        users.getTotalElements(),
        users.getNumber(), users.isLast());
  }


}
