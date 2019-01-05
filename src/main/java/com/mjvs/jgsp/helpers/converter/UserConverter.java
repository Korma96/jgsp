package com.mjvs.jgsp.helpers.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.mjvs.jgsp.dto.UserFrontendDTO;
import com.mjvs.jgsp.model.User;

public class UserConverter {
	
	public static List<UserFrontendDTO> ConvertUserToUserFrontendDTOs(List<User> users)
    {
        return users.stream()
                .map(user -> new UserFrontendDTO(user))
                .collect(Collectors.toList());
    }

}
