package com.huangpi.platform.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "travel_routes")
public class TravelRouteEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 500)
    private String summary;

    @Column(length = 40)
    private String duration;

    @Column(length = 80)
    private String suitable;

    @Column(length = 500)
    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "travel_route_points", joinColumns = @JoinColumn(name = "route_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "map_point_id", nullable = false)
    private List<Long> mapPointIds = new ArrayList<>();

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private int sortOrder;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getSuitable() { return suitable; }
    public void setSuitable(String suitable) { this.suitable = suitable; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public List<Long> getMapPointIds() { return mapPointIds; }
    public void setMapPointIds(List<Long> mapPointIds) { this.mapPointIds = new ArrayList<>(mapPointIds); }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
