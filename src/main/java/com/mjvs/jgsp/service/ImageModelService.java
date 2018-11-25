package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.ImageModel;
import org.springframework.web.multipart.MultipartFile;

public interface ImageModelService {

    ImageModel getImageModel(Long id);

    void delete(Long id);

    void save(MultipartFile image) throws Exception;
}
