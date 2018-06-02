package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.DistributionChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributionChannelRepository extends JpaRepository<DistributionChannel, Long> {
}
