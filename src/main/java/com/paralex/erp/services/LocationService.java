package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.AddLocationDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.dtos.UpdateLocationDto;
import com.paralex.erp.entities.LocationEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.LocationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class LocationService {
    private final LocationRepository locationRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;
    private final UserService userService;

    // INFO must have at least one location o
    public Optional<LocationEntity> findLocationNearestTo(@NotNull double latitude, @NotNull double longitude) {
        Point point = new Point(longitude, latitude);
        Distance maxDistance = new Distance(10000, Metrics.KILOMETERS); // Adjust the distance as needed

        List<LocationEntity> locations = locationRepository.findByLocationNear(point, maxDistance, 1, 0);
        return locations.isEmpty() ? Optional.empty() : Optional.of(locations.get(0));
    }

    public void addLocations(@NotNull List<AddLocationDto> addLocationDtoList) {
        // Authenticate the user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        var userEmail = auth.getName();
        var userEntity = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        locationRepository.saveAll(addLocationDtoList.stream()
                .map(item -> LocationEntity.builder()
                        .name(item.getName())
                        .location(new Point(item.getLatitude(), item.getLongitude()))
                        .status(item.getStatus())
                        .amount(item.getAmount())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }

    public void updateLocation(
            @NotNull @NotEmpty @NotBlank String id,
            @ValidateUpdateItem(
                    keyList = { "id", "time", "creatorId" },
                    modelClass = UpdateLocationDto.class)
            @NotNull @NotEmpty List<UpdateItemDto> changes) {
        final var criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<LocationEntity> update = criteriaBuilder.createCriteriaUpdate(LocationEntity.class);
        final Root<LocationEntity> root = update.from(LocationEntity.class);

        changes.forEach(item -> update.set(item.getKey(), item.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update).executeUpdate();
    }

    public void deleteLocation(@NotNull String id) {
        locationRepository.deleteById(id);
    }

    public List<LocationEntity> getLocations(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize());

        return locationRepository.findAll(pageable)
                .getContent();
    }
}
