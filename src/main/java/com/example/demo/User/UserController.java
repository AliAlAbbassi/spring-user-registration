package com.example.demo.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.annotation.Resource;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController implements CommandLineRunner {
    @Autowired
    private UserRepo userRepo;

    // inject the actual template
    @Autowired
    private RedisTemplate<String, String> template;

    // inject the template as ListOperations
    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOps;

    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long user) {
        return this.userRepo.findById(user).get();
    }

    @RequestMapping(value = "/{user}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long user) {
        this.userRepo.deleteById(user);
    }

    @PostMapping(path = "/register")
    public @ResponseBody String createUser(@RequestParam String username, @RequestParam String email,
            @RequestParam String password) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        String sha256hex = new String(Hex.encode(hash));
        User n = new User(username, sha256hex, email);
        this.userRepo.save(n);
        return "Saved";
    }

    @PostMapping(path = "/login")
    public @ResponseBody User login(@RequestParam String usernameOrEmail, @RequestParam String password) {
        User user;
        if (usernameOrEmail.contains("@")) {
            user = this.userRepo.findByEmailAddress(usernameOrEmail);
        } else {
            user = this.userRepo.findByUsername(usernameOrEmail);
        }
        byte[] ye = Hex.decode(user.getPassword());

        if (Hex.toHexString(ye).equals(password)) {
            this.listOps.leftPush("userId", user.getId() + "");
            return user;
        }
        return null;
    }

    @PostMapping(path = "/logout")
    public void logout() {
        this.listOps.leftPop("userId");
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return this.userRepo.findAll();
    }

    @Override
    public void run(String... args) throws Exception {
        // TODO Auto-generated method stub

    }

    // @RequestMapping(value = "/{user}/customers", method = RequestMethod.GET)
    // List<Customer> getUserCustomers(@PathVariable Long user) {
    // // ...
    // }
}
