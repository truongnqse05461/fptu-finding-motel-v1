package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel,String>{

}