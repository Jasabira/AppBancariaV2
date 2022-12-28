package com.example.AppBancariaV2;

import com.example.AppBancariaV2.Entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
