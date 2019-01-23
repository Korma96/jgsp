package com.mjvs.jgsp.repository;

import org.springframework.data.repository.Repository;

import com.mjvs.jgsp.model.ImageModel;

public interface ImageModelRepository extends Repository<ImageModel, Long> {

    ImageModel save(ImageModel imageModel);

    ImageModel findById(Long id);

    ImageModel findByNameAndDeleted(String name, boolean deleted);

    void delete(ImageModel imageModel);

}