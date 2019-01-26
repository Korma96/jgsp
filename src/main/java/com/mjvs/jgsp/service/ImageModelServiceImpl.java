package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.ImageModelAlreadyDeletedException;
import com.mjvs.jgsp.helpers.exception.ImageModelNotFoundException;
import com.mjvs.jgsp.model.ImageModel;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.repository.ImageModelRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class ImageModelServiceImpl implements ImageModelService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private ImageModelRepository imageModelRepository;

    @Autowired
    private UserService userService;


    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public ImageModel getImageModel(Long id) {
        return imageModelRepository.findById(id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void delete(Long id) throws ImageModelNotFoundException, ImageModelAlreadyDeletedException {
        //ImageModel imageModel = imageModelRepository.findById(id);
        //imageModelRepository.delete(imageModel);
        ImageModel imageModel = imageModelRepository.findById(id);

        String message;
        if(imageModel == null) {
            message = String.format("ImageModel with id (%d) was not found in database.", id);
            logger.error(message);
            throw new ImageModelNotFoundException(message);
        }

       delete(imageModel);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void delete(ImageModel imageModel) throws ImageModelAlreadyDeletedException {
        String message;

        if(imageModel.isDeleted()) {
            message = String.format("ImageModel, with id (%d), is already deleted.", imageModel.getId());
            logger.error(message);
            throw new ImageModelAlreadyDeletedException(message);
        }

        imageModel.setDeleted(true);
        imageModelRepository.save(imageModel);
    }

    //@Async
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public ImageModel save(MultipartFile image) throws Exception {
        if(image != null) {
            if(image.getBytes().length > 0) {
                User loggedUser = userService.getLoggedUser();
                String fileName = loggedUser.getUsername();
                List<ImageModel> imageModels = imageModelRepository.findByNameAndDeleted(fileName, false);
                if(imageModels != null) {
                    if(!imageModels.isEmpty())
                        imageModels.stream().forEach(imageModel -> {
                            try {
                                delete(imageModel);
                            } catch (ImageModelAlreadyDeletedException imageModelAlreadyDeleted) {
                                imageModelAlreadyDeleted.printStackTrace();
                            }
                        });
                }

                ImageModel newImageModel = new ImageModel(fileName, image.getBytes());

                newImageModel = imageModelRepository.save(newImageModel);

                Passenger loggedPassenger = (Passenger) loggedUser;
                loggedPassenger.setIdConfirmation(newImageModel.getId());
                userService.save(loggedPassenger);

                System.out.println("File successfully uploaded to : " + fileName);

                return newImageModel;
            }
        }

        return null;
    }

    @Override
    public ImageModel save(ImageModel imageModel) {
        return imageModelRepository.save(imageModel);
    }

}
