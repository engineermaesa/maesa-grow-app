package com.fauzighozali.mgamobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Course {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("course_id")
    @Expose
    private Integer course_id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("is_going")
    @Expose
    private Integer isGoing;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("test")
    @Expose
    private List<Test> test;
    @SerializedName("pre_test")
    @Expose
    private List<PreTest> preTest;
    @SerializedName("pre_score")
    @Expose
    private Integer preScore;
    @SerializedName("post_score")
    @Expose
    private Integer postScore;
    @SerializedName("pre_status")
    @Expose
    private String preStatus;
    @SerializedName("post_status")
    @Expose
    private String postStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getIsGoing() {
        return isGoing;
    }

    public void setIsGoing(Integer isGoing) {
        this.isGoing = isGoing;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Test> getTest() {
        return test;
    }

    public void setTest(List<Test> test) {
        this.test = test;
    }

    public List<PreTest> getPreTest() {
        return preTest;
    }

    public void setPreTest(List<PreTest> preTest) {
        this.preTest = preTest;
    }

    public Integer getPreScore() {
        return preScore;
    }

    public void setPreScore(Integer preScore) {
        this.preScore = preScore;
    }

    public String getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus;
    }

    public Integer getPostScore() {
        return postScore;
    }

    public void setPostScore(Integer postScore) {
        this.postScore = postScore;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }
}
