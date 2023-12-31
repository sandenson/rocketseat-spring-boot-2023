package com.sandenson.todolist.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping
    public ResponseEntity create(@RequestBody UserModel model) {
        var user = this.userRepository.findByUsername(model.getUsername());

        if (user != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        var hashedPassword = BCrypt.withDefaults().hashToString(12, model.getPassword().toCharArray());
        model.setPassword(hashedPassword);

        var createdUser = this.userRepository.save(model);
        return ResponseEntity.ok(createdUser);
    }
}
