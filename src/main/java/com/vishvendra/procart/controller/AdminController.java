package com.vishvendra.procart.controller;

import com.vishvendra.procart.mapper.AdminMapper;
import com.vishvendra.procart.model.AdminDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

  private final AdminMapper adminMapper;

  @GetMapping
  public void test(@RequestBody AdminDTO adminDTO) {
    adminMapper.toEntity(adminDTO.getProfileDetailsDTO());
    adminMapper.toEntity(adminDTO);
  }

}
