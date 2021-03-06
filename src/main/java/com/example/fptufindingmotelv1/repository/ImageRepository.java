package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel,String> {

    @Query(value = "select new ImageModel(im.id, im.fileContent, im.fileType) from ImageModel im " +
            "where im.id = :imageId")
    ImageModel getImageById(String imageId);

    @Query(value = "select new ImageModel(im.id, im.fileContent, im.fileType) from ImageModel im " +
            "where im.post.id = :postId")
    List<ImageModel> getImageByPostId(String postId);

    @Transactional
    @Modifying
    @Query(value = "delete im from IMAGE im " +
            "where im.POST_ID = :postId ", nativeQuery = true)
    void deleteImagesByPost(String postId);
}
