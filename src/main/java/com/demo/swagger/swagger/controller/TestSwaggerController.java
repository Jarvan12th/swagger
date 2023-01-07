package com.demo.swagger.swagger.controller;

import com.demo.swagger.swagger.entity.Result;
import com.demo.swagger.swagger.entity.User;
import com.demo.swagger.swagger.utils.ResultGenerator;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TestSwaggerController {
    static Map<Integer, User> userMap = Collections.synchronizedMap(new HashMap<>());

    static {
        userMap.put(1, new User(1, "newbee1", "111111"));
        userMap.put(2, new User(2, "newbee2", "222222"));
    }

    @ApiOperation(value = "Get User List", notes = "Get user list")
    @GetMapping("/users")
    public Result<List<User>> getUserList() {
        return ResultGenerator.generateSuccessResultWithData(new ArrayList<>(userMap.values()));
    }

    @ApiOperation(value = "Add New User", notes = "Add new user")
    @ApiImplicitParam(name = "user", value = "user entity", required = true, dataType = "User")
    @PostMapping("/users")
    public Result<Boolean> postUser(@RequestBody User user) {
        userMap.put(user.getId(), user);
        return ResultGenerator.generateSuccessResultWithData(true);
    }

    @ApiOperation(value = "Get User", notes = "Get user according to id")
    @ApiImplicitParam(name = "id", value = "User id", required = true, dataType = "int")
    @GetMapping("/users/{id}")
    public Result<User> getUser(@PathVariable Integer id) {
        return ResultGenerator.generateSuccessResultWithData(userMap.get(id));
    }

    @ApiOperation(value = "Update User", notes = "Update user info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "User id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "user", value = "User info", required = true, dataType = "User")
    })
    @PutMapping("/users/{id}")
    public Result<Boolean> putUser(@PathVariable Integer id, @RequestBody User user) {
        userMap.put(id, new User(id, user.getName(), user.getPassword()));
        return ResultGenerator.generateSuccessResultWithData(true);
    }

    @ApiOperation(value = "Delete User", notes = "Delete user")
    @ApiImplicitParam(name = "id", value = "user id", required = true, dataType = "int")
    @DeleteMapping("/users/{id}")
    public Result<Boolean> deleteUser(@PathVariable Integer id) {
        userMap.remove(id);
        return ResultGenerator.generateSuccessResultWithData(true);
    }
}
