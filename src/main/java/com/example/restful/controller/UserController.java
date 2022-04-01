package com.example.restful.controller;

import com.example.restful.entity.DbUser;
import com.example.restful.payload.UserDto;
import com.example.restful.payload.result.Result;
import com.example.restful.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public List<DbUser> getUsers(@RequestParam(defaultValue = "0") Integer page){
        return userService.getUsers(page);
    }

    @GetMapping(path = "/warehouseId/{warehouseId}")
    public List<DbUser> getUsersWarehouse(@PathVariable Integer warehouseId, @RequestParam(defaultValue = "1") Integer page){
        return userService.getUsersWarehouse(warehouseId, page);
    }

    @PostMapping
    public Result postUser(@Valid @RequestBody UserDto userDto){
        return userService.postUser(userDto);
    }

    @PutMapping(path = "/{id}")
    public Result updateUser(@PathVariable Integer id, @Valid @RequestBody UserDto userDto){
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping(path = "/{id}")
    public Result deleteUser(@PathVariable Integer id){
        return userService.deleteUser(id);
    }
}
