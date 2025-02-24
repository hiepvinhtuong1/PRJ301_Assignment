package com.hipdev.LeaveManagement.mapper;

import java.util.List;

/**
 *
 * @param <D> DTO type parameter
 * @param <E> Entity type parameter
 */
public interface EntityMapper<D,E> {

    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDto(List<E> entities);

    List<E> toEntity(List<D> dtos);
}
