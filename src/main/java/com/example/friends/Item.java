package com.example.friends;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taixiang on 2015/12/16.
 */
public class Item {
    private int portraitId; // 头像
    private String nickName; // 昵称
    private String content; // 说说
    private String createdAt; // 发布时间
    private boolean show;//展示点赞
    private List<CommentItem> comments = new ArrayList<>();//评论
    private List<String> likes = new ArrayList<>();//点赞人数
    private List<UserImage> images = new ArrayList<>();

    public int getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(int portraitId) {
        this.portraitId = portraitId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentItem> getComments() {
        return comments;
    }

    public void setComments(List<CommentItem> comments) {
        this.comments = comments;
    }

    public List<UserImage> getImages() {
        return images;
    }

    public void setImages(List<UserImage> images) {
        this.images = images;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return "Item{" +
                "portraitId=" + portraitId +
                ", nickName='" + nickName + '\'' +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", show=" + show +
                ", comments=" + comments +
                ", likes=" + likes +
                ", images=" + images +
                '}';
    }
}
