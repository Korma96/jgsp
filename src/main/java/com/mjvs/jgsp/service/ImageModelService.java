package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.ImageModelAlreadyDeleted;
import com.mjvs.jgsp.helpers.exception.ImageModelNotFoundException;
import com.mjvs.jgsp.model.ImageModel;
import org.springframework.web.multipart.MultipartFile;

public interface ImageModelService {

    ImageModel getImageModel(Long id);

    void delete(Long id) throws ImageModelNotFoundException, ImageModelAlreadyDeleted;

    ImageModel save(MultipartFile image) throws Exception;

    ImageModel save(ImageModel imageModel);
}
