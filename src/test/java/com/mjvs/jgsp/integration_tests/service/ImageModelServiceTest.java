package com.mjvs.jgsp.integration_tests.service;

import com.mjvs.jgsp.helpers.exception.ImageModelAlreadyDeletedException;
import com.mjvs.jgsp.helpers.exception.ImageModelNotFoundException;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.repository.ImageModelRepository;
import com.mjvs.jgsp.service.ImageModelService;
import com.mjvs.jgsp.service.UserService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.mjvs.jgsp.JgspApplicationTests.prepareLoggedUser;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Ignore
public class ImageModelServiceTest {

    @Autowired
    private ImageModelRepository imageModelRepository;

    @Autowired
    private ImageModelService imageModelService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    private ImageModel testImageModel = new ImageModel("picture.png", new byte[]{});


    @Transactional
    @Rollback(true)
    @Test(expected = ImageModelNotFoundException.class)
    public void deleteTestThrowsImageModelNotFoundException() throws ImageModelNotFoundException, ImageModelAlreadyDeletedException {
        final long wrongId = 123;
        imageModelService.delete(wrongId);
    }

    @Transactional
    @Rollback(true)
    @Test(expected = ImageModelAlreadyDeletedException.class)
    public void deleteTestThrowsImageModelAlreadyDeleted() throws ImageModelNotFoundException, ImageModelAlreadyDeletedException {
        testImageModel.setDeleted(true);
        testImageModel = imageModelRepository.save(testImageModel);

        imageModelService.delete(testImageModel.getId());
    }

    @Transactional
    @Rollback(true)
    @Test()
    public void deleteTestSuccess() throws ImageModelNotFoundException, ImageModelAlreadyDeletedException {
        testImageModel.setDeleted(false);
        ImageModel imageModel1 = imageModelRepository.save(testImageModel);

        imageModelService.delete(imageModel1.getId());

        ImageModel imageModel2 = imageModelRepository.findById(imageModel1.getId());
        assertTrue(imageModel2.isDeleted());
    }

    @Transactional
    @Rollback(true)
    @Test
    public void saveTestSuccess() throws Exception {
        prepareLoggedUser(userService, passwordEncoder, authenticationManager);

        MultipartFile image = new MockMultipartFile("picture.png", "some string for conversion in bytes".getBytes());
        ImageModel imageModel = imageModelService.save(image);
        Passenger passenger = (Passenger) userService.getLoggedUser();

        assertEquals(passenger.getUsername(), imageModel.getName());
        assertEquals(passenger.getIdConfirmation(), imageModel.getId());
    }

    @Transactional
    @Rollback(true)
    @Test
    public void saveTestUnsuccessEmptyByteArray() throws Exception {
        //prepareLoggedUser(userService, passwordEncoder, authenticationManager);

        final String password = "aaa";
        Passenger passenger = new Passenger("aaa", passwordEncoder.encode(password), UserType.PASSENGER, UserStatus.ACTIVATED, "Aaa", "Aaa", "email_address", "address", PassengerType.OTHER);
        try {
            userService.save(passenger);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken userInfo = new UsernamePasswordAuthenticationToken(passenger.getUsername(), password);
        Authentication authentication = authenticationManager.authenticate(userInfo);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MultipartFile image = new MockMultipartFile("picture.png", new byte[]{});
        ImageModel imageModel = imageModelService.save(image);
        assertNull(imageModel);
    }

    @Transactional
    @Rollback(true)
    @Test
    public void saveTestUnsuccessWithNullMultipartFile() throws Exception {
        //prepareLoggedUser(userService, passwordEncoder, authenticationManager);

        MultipartFile image = null;
        ImageModel imageModel = imageModelService.save(image);
        assertNull(imageModel);
    }


}