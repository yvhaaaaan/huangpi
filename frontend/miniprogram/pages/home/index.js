"use strict";
Page({
  data: {
    shortcuts: [
      { title: "油茶文化", desc: "工艺故事", icon: "茶", url: "/pages/tea/detail", mode: "navigateTo" },
      { title: "特色产品", desc: "本地好物", icon: "物", url: "/pages/specialty/index", mode: "switchTab" },
      { title: "客家文旅", desc: "古村路线", icon: "旅", url: "/pages/culture/index", mode: "navigateTo" },
      { title: "非遗展示", desc: "体验工坊", icon: "艺", url: "/pages/intangible/index", mode: "navigateTo" },
      { title: "地图导览", desc: "点位导航", icon: "图", url: "/pages/map/index", mode: "switchTab" },
      { title: "活动资讯", desc: "报名参与", icon: "动", url: "/pages/activity/index", mode: "switchTab" }
    ],
    specialties: [
      { id: "oil-tea-gift", categoryName: "油茶", title: "黄陂油茶礼盒", desc: "精选本地油茶原料，适合作为伴手礼和团队采购。", merchant: "黄陂油茶工坊", address: "游客服务中心旁", image: "/static/real-merchant.jpg" },
      { id: "simiao-rice", categoryName: "丝苗米", title: "黄陂丝苗米", desc: "黄陂重点特色农产品，米粒细长、饭香自然。", merchant: "黄陂镇优质稻合作社", address: "农产品展示中心", image: "/static/real-product.jpg" },
      { id: "hakka-snack", categoryName: "客家食品", title: "客家手作米果", desc: "传统手工制作，保留黄陂乡土风味。", merchant: "客家古村合作社", address: "客家古村入口", image: "/static/specialty-2.jpg" }
    ],
    points: [
      { id: 1, title: "油茶体验点", desc: "观看油茶制作流程，参与品鉴和打卡。", tag: "油茶特色", distance: "距游客中心 800m", duration: "60 分钟", image: "/static/real-place.jpg" },
      { id: 2, title: "客家古村落", desc: "串联村史展陈、传统建筑和慢行路线。", tag: "客家文旅", distance: "距上站 1.2km", duration: "70 分钟", image: "/static/real-hero.jpg" },
      { id: 3, title: "非遗体验工坊", desc: "近距离观看传统技艺，也可预约体验课。", tag: "非遗体验", distance: "距上站 600m", duration: "50 分钟", image: "/static/real-workshop.jpg" }
    ],
    heritageItems: [
      { id: 1, title: "油茶制作技艺", desc: "炒料、研磨、冲煮与品鉴", image: "/static/real-tea.jpg" },
      { id: 2, title: "油茶入课堂", desc: "研学课程与乡土文化教育", image: "/static/real-classroom.jpg" }
    ],
    activities: [
      { id: 1, status: "报名中", type: "油茶体验", title: "油茶文化体验活动", time: "10月12日 09:30", place: "黄陂镇文化广场", image: "/static/real-activity.jpg" },
      { id: 2, status: "招募中", type: "研学导览", title: "客家古村半日研学", time: "10月26日 10:00", place: "游客服务中心", image: "/static/real-classroom.jpg" }
    ]
  },
  onSearchTap() {
    wx.navigateTo({ url: "/pages/common/search" });
  },
  onNavTap(event) {
    const dataset = event.currentTarget.dataset;
    if (!dataset.url)
      return;
    if (dataset.mode === "switchTab") {
      wx.switchTab({ url: dataset.url });
      return;
    }
    wx.navigateTo({ url: dataset.url });
  },
  onCollectTap() {
    wx.showToast({ title: "已加入收藏", icon: "success" });
  },
  onMapTap() {
    wx.switchTab({ url: "/pages/map/index" });
  },
  onSignupTap() {
    wx.navigateTo({ url: "/pages/activity/signup" });
  },
  onStopTap() {
  },
  onShareAppMessage() {
    return {
      title: "黄陂镇特色产业客家文旅非遗",
      path: "/pages/home/index",
      imageUrl: "/static/real-hero.jpg"
    };
  },
  onPullDownRefresh() {
    wx.stopPullDownRefresh();
  }
});
