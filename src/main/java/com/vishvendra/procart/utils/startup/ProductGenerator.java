package com.vishvendra.procart.utils.startup;

import com.vishvendra.procart.model.ProductCurrencyDTO;
import com.vishvendra.procart.model.ProductDTO;
import java.math.BigDecimal;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductGenerator {

  private static final String[] PRODUCT_NAMES = {"Laptop", "Smartphone", "Tablet", "Monitor",
      "Headphones", "Keyboard", "Mouse", "Smartwatch", "Camera", "Speaker"};
  private static final String[] DESCRIPTIONS = {"Apple M2 15-inch", "Samsung Galaxy S22",
      "iPad Air 2024", "LG UltraWide 34-inch", "Bose QuietComfort 45", "Logitech MX Keys",
      "Razer DeathAdder", "Apple Watch Series 8", "Canon EOS 90D", "JBL Flip 5"};
  private static final Random random = new Random();

  public static ProductDTO generateProductDTO(Long currencyId) {
    String name =
        PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)] + " " + (random.nextInt(1000) + 100);
    String description = DESCRIPTIONS[random.nextInt(DESCRIPTIONS.length)];
    String sku = "SKU" + (random.nextInt(100000) + 10000);
    BigDecimal price = BigDecimal.valueOf(100 + (random.nextInt(1000) + 100) / 10.0);
    String imageUrl =
        "https://example.com/images/" + name.toLowerCase().replace(" ", "-") + ".jpg";
    long quantity = 100;

    ProductDTO productDTO = new ProductDTO();
    productDTO.setName(name);
    productDTO.setDescription(description);
    productDTO.setSku(sku);
    productDTO.setPrice(price);
    productDTO.setImageUrl(imageUrl);
    productDTO.setQuantity(quantity);

    ProductCurrencyDTO currency = new ProductCurrencyDTO();
    currency.setId(currencyId);
    productDTO.setCurrency(currency);
    return productDTO;

  }
}

