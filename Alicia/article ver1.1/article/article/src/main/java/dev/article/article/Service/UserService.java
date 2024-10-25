package dev.article.article.Service;

import dev.article.article.Entity.UserEntity;
import dev.article.article.Repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserEntityRepository userEntityRepository;
    public UserEntity createUser(String username, String password, int accessLvl, String userId){
        System.out.println("Creating user with parameters:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Access Level: " + accessLvl);
        System.out.println("User ID: " + userId);

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(username);
        userEntity.setUserPassword(password);
        userEntity.setUserId(userId);
        userEntity.setUserAccessLvl(accessLvl);
        userEntityRepository.save(userEntity);
        return userEntity;
    }

    public String generateId(){
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear()%100;
        int month = currentDate.getMonthValue();

        // Get the last ID from the users table
        String lastId = userEntityRepository.findLastUserId();

        // Extract the last 4 digits from the lastId and parse as integer
        int last4Digits = Integer.parseInt(lastId.substring(lastId.length() - 4));

        // Increment the last 4 digits by one
        last4Digits++;

        // Generate the type string
        return String.format("%02d%02d%04d", year, month, last4Digits);
    }

    public UserEntity getUserByUserId(String userId) {
        return userEntityRepository.findByUserId(userId);
    }

    public List<UserEntity> getAllUsers() {
        return userEntityRepository.findAll();
    }

    public UserEntity updateUserAccessLvl(String selectUserId, int selectUserAccess) {
        UserEntity user = userEntityRepository.findByUserId(selectUserId);
        user.setUserAccessLvl(selectUserAccess);
        return userEntityRepository.save(user);
    }

    public UserEntity updateUserPassword(String npwd, String userId) {
        System.out.println(npwd);
        System.out.println(userId);
        UserEntity user = userEntityRepository.findByUserId(userId);
        user.setUserPassword(npwd);
        return userEntityRepository.save(user);
    }
}
