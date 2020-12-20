package com.example.TopScore.Web;

import com.example.TopScore.DataAccess.Entities.UserEntity;
import com.example.TopScore.Exception.UserNotFoundException;
import com.example.TopScore.Services.Interfaces.IUserService;
import com.example.TopScore.Shared.ApiResponse.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/user")
public class UserController {

    private IUserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public UserController(IUserService userService, ModelMapper modelMapper)
    {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") Long userId) {
        return convertToDto(userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId)));
    }


    private UserDto convertToDto(UserEntity user) {
        return modelMapper.map(user, UserDto.class);
    }

    private UserEntity convertToEntity(UserDto userDto) throws ParseException {
        UserEntity user = modelMapper.map(userDto, UserEntity.class);
        if (userDto.getPlayer() != null) {
            user.setNormalizedPlayer(userDto.getPlayer().toUpperCase());
        }
        return user;
    }
}