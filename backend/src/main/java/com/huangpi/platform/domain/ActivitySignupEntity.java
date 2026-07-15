package com.huangpi.platform.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "activity_signups", uniqueConstraints = @UniqueConstraint(
        name = "uk_activity_signup_user", columnNames = {"activity_id", "user_id"}))
public class ActivitySignupEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private ActivityEntity activity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private int peopleCount;

    @Column(length = 500)
    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActivitySignupStatus status;

    public Long getId() { return id; }
    public ActivityEntity getActivity() { return activity; }
    public void setActivity(ActivityEntity activity) { this.activity = activity; }
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public int getPeopleCount() { return peopleCount; }
    public void setPeopleCount(int peopleCount) { this.peopleCount = peopleCount; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public ActivitySignupStatus getStatus() { return status; }
    public void setStatus(ActivitySignupStatus status) { this.status = status; }
}
