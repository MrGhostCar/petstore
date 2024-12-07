package com.home.petstore.util;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

/** Extends the ModelMapper with a feature, to convert any List. */
public class ExtendedModelMapper extends ModelMapper {
    /**
     * Each element in source List is converted to the target class, then returns the converted
     * elements in a new List.
     *
     * @param source The source List to be converted.
     * @param targetClass The target .class reference.
     * @return A new List, containing the converted elements.
     * @param <S> Source type, derived from the List<S> param.
     * @param <T> Target type, derived from the targetClass .class reference.
     */
    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream()
                .map(element -> this.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
