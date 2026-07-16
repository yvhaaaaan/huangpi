"use strict";
Page({
  onSignupTap() {
    wx.navigateTo({ url: "/pages/activity/signup" });
  },
  onCollectTap() {
    wx.showToast({ title: "已收藏活动", icon: "success" });
  },
  onMapTap() {
    wx.switchTab({ url: "/pages/map/index" });
  },
  onShareAppMessage() {
    return { title: "油茶文化体验活动", path: "/pages/activity/detail", imageUrl: "/static/real-activity.jpg" };
  }
});
