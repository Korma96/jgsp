package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.ImageModelNotFoundException;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.ImageModel;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.repository.ImageModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImageModelServiceImpl implements ImageModelService {

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
    public void delete(Long id) throws ImageModelNotFoundException {
        //ImageModel imageModel = imageModelRepository.findById(id);
        //imageModelRepository.delete(imageModel);
        ImageModel imageModel = imageModelRepository.findById(id);
        if(imageModel == null) throw new ImageModelNotFoundException(String.format("ImageModel with id (%d) was not found" +
                                                                                    " in database.", id));

        imageModel.setDeleted(true);
        imageModelRepository.save(imageModel);
    }

    //@Async
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void save(MultipartFile image) throws Exception {
        User loggedUser = userService.getLoggedUser();
        String fileName = loggedUser.getUsername();

        ImageModel newImageModel = new ImageModel(fileName, image.getBytes());
        newImageModel = imageModelRepository.save(newImageModel);

        Passenger loggedPassenger = (Passenger) loggedUser;
        loggedPassenger.setConfirmation(newImageModel.getId());
        userService.save(loggedPassenger);

        System.out.println("File successfully uploaded to : " + fileName);

    }

}
