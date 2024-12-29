package com.vishvendra.procart.repository;

import com.vishvendra.procart.entities.Cart;
import com.vishvendra.procart.entities.CartStatus;
import com.vishvendra.procart.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends BaseRepository<Cart> {

  Optional<Cart> findByUser(final User user);

  @Query("SELECT c FROM Cart c WHERE c.user = :user and c.cartStatus = :cartStatus")
  Optional<Cart> findActiveCartByUser(@Param("user") final User user,
      @Param("cartStatus") final CartStatus cartStatus);
}
