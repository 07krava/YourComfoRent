package com.dmitriykravchuk.project.yourcomforent.repository;

import com.dmitriykravchuk.project.yourcomforent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
