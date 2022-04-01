package com.example.restful.service;

import com.example.restful.entity.DbUser;
import com.example.restful.entity.DbWarehouse;
import com.example.restful.payload.UserDto;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.UserRepository;
import com.example.restful.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    public List<DbUser> getUsers(Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        return userRepository.findAllByActive(true, pageable).getContent();
    }

    public List<DbUser> getUsersWarehouse(Integer warehouseId, Integer page) {
        Pageable pageable = PageRequest.of(page-1, 10);
        return userRepository.findByWarehouse_Id(warehouseId,true, pageable).getContent();
    }

    public Result postUser(@Valid UserDto userDto) {
        if (!userRepository.existsByEmail(userDto.getEmail())) {
            DbUser user = new DbUser();
            return saveUserInformation(user, userDto, "Saved user information");
        } else {
            return new Result("The email you entered is busy", false);
        }
    }

    public Result updateUser(Integer id, UserDto userDto) {
        Optional<DbUser> optionalDbUser = userRepository.findById(id);
        if (optionalDbUser.isPresent() && optionalDbUser.get().isActive()) {
            if (!userRepository.existsByEmailAndIdNot(userDto.getEmail(), id)) {
                DbUser user = optionalDbUser.get();
                return saveUserInformation(user, userDto, "Update user information");
            } else {
                return new Result("The email you entered is busy", false);
            }
        } else {
            return new Result("No user matching the id you entered", false);
        }
    }

    public Result deleteUser(Integer id) {
        Optional<DbUser> optionalDbUser = userRepository.findById(id);
        if (optionalDbUser.isPresent() && optionalDbUser.get().isActive()) {
            DbUser user = optionalDbUser.get();
            user.setActive(false);
            return saveUser(user, "Delete user information");
        } else {
            return new Result("No user matching the id you entered", false);
        }
    }

    private Result saveUserInformation(DbUser user, UserDto userDto, String text) {
        List<DbWarehouse> warehouseList = new ArrayList<>();
        for (Integer warehouseId : userDto.getWarehousesId()) {
            Optional<DbWarehouse> optionalDbWarehouse = warehouseRepository.findById(warehouseId);
            if (optionalDbWarehouse.isPresent()) {
                warehouseList.add(optionalDbWarehouse.get());
            } else {
                return new Result("No warehouse matching the ID you entered", false);
            }
        }
        user.setFirstname(userDto.getFirstName());
        user.setLastname(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setCode(codeGeneration());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setDbWarehouses(warehouseList);
        return saveUser(user, text);
    }

    private Result saveUser(DbUser user, String text) {
        try {
            userRepository.save(user);
            return new Result(text, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Error dbUser table", false);
        }
    }

    private String codeGeneration() {
        DbUser user = userRepository.findTopByOrderByIdDesc();
        int code = (user != null) ? Integer.parseInt(user.getCode()) + 1 : 1;
        return String.valueOf(code);
    }
}
