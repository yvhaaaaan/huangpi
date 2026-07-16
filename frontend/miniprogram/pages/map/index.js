"use strict";
const center = {
  latitude: 24.3056,
  longitude: 115.8858
};
const allPoints = [
  {
    id: 1,
    title: "游客服务中心",
    type: "服务设施",
    latitude: 24.3056,
    longitude: 115.8858,
    desc: "路线咨询、活动签到、文创市集和游客服务。",
    address: "黄陂镇游客服务中心",
    duration: "建议停留 15 分钟",
    image: "/static/real-place.jpg",
    width: 34,
    height: 34,
    iconPath: "/static/map-pin.png",
    callout: { content: "游客服务中心", color: "#244b38", fontSize: 13, borderRadius: 8, bgColor: "#ffffff", padding: 8, display: "ALWAYS" }
  },
  {
    id: 2,
    title: "油茶体验点",
    type: "油茶特色",
    latitude: 24.3091,
    longitude: 115.8796,
    desc: "观看油茶制作流程，参与品鉴和研学打卡。",
    address: "黄陂镇油茶体验区",
    duration: "建议停留 60 分钟",
    image: "/static/real-tea.jpg",
    width: 34,
    height: 34,
    iconPath: "/static/map-pin.png",
    callout: { content: "油茶体验点", color: "#244b38", fontSize: 13, borderRadius: 8, bgColor: "#ffffff", padding: 8, display: "ALWAYS" }
  },
  {
    id: 3,
    title: "客家古村落",
    type: "客家文旅",
    latitude: 24.3128,
    longitude: 115.8919,
    desc: "传统建筑、村史展陈和客家慢行路线。",
    address: "黄陂镇客家古村片区",
    duration: "建议停留 70 分钟",
    image: "/static/real-hero.jpg",
    width: 34,
    height: 34,
    iconPath: "/static/map-pin.png",
    callout: { content: "客家古村落", color: "#244b38", fontSize: 13, borderRadius: 8, bgColor: "#ffffff", padding: 8, display: "ALWAYS" }
  },
  {
    id: 4,
    title: "非遗体验工坊",
    type: "非遗体验",
    latitude: 24.3004,
    longitude: 115.8928,
    desc: "手作体验、非遗讲解、文创展示和预约课堂。",
    address: "黄陂镇非遗工坊",
    duration: "建议停留 50 分钟",
    image: "/static/real-workshop.jpg",
    width: 34,
    height: 34,
    iconPath: "/static/map-pin.png",
    callout: { content: "非遗体验工坊", color: "#244b38", fontSize: 13, borderRadius: 8, bgColor: "#ffffff", padding: 8, display: "ALWAYS" }
  },
  {
    id: 5,
    title: "特色产品展示点",
    type: "特色产品",
    latitude: 24.2988,
    longitude: 115.8814,
    desc: "展示油茶、丝苗米、客家食品和文创伴手礼。",
    address: "黄陂镇文创市集",
    duration: "建议停留 30 分钟",
    image: "/static/real-product.jpg",
    width: 34,
    height: 34,
    iconPath: "/static/map-pin.png",
    callout: { content: "特色产品展示点", color: "#244b38", fontSize: 13, borderRadius: 8, bgColor: "#ffffff", padding: 8, display: "ALWAYS" }
  },
  {
    id: 6,
    title: "油茶文化体验活动点",
    type: "活动地点",
    latitude: 24.3031,
    longitude: 115.8756,
    desc: "油茶文化体验、亲子课堂和研学活动集合点。",
    address: "黄陂镇文化广场",
    duration: "按活动安排",
    image: "/static/real-activity.jpg",
    width: 34,
    height: 34,
    iconPath: "/static/map-pin.png",
    callout: { content: "活动地点", color: "#244b38", fontSize: 13, borderRadius: 8, bgColor: "#ffffff", padding: 8, display: "ALWAYS" }
  }
];
function filterPoints(type) {
  if (type === "全部")
    return allPoints;
  return allPoints.filter((point) => point.type === type);
}
Page({
  data: {
    keyword: "",
    center,
    scale: 14,
    showUserLocation: false,
    activeType: "全部",
    types: ["全部", "油茶特色", "特色产品", "客家文旅", "非遗体验", "活动地点", "服务设施"],
    visibleMarkers: allPoints,
    selectedPoint: allPoints[0],
    polyline: [
      {
        points: [
          { latitude: allPoints[0].latitude, longitude: allPoints[0].longitude },
          { latitude: allPoints[1].latitude, longitude: allPoints[1].longitude },
          { latitude: allPoints[2].latitude, longitude: allPoints[2].longitude },
          { latitude: allPoints[3].latitude, longitude: allPoints[3].longitude },
          { latitude: allPoints[4].latitude, longitude: allPoints[4].longitude }
        ],
        color: "#2e6b4e",
        width: 5,
        dottedLine: false,
        arrowLine: true
      }
    ]
  },
  onKeywordInput(event) {
    const keyword = event.detail.value.trim();
    const activeType = this.data.activeType;
    const base = filterPoints(activeType);
    const visibleMarkers = keyword ? base.filter((point) => `${point.title}${point.desc}${point.type}`.includes(keyword)) : base;
    this.setData({ keyword, visibleMarkers, selectedPoint: visibleMarkers[0] || null });
  },
  onTypeTap(event) {
    const type = event.currentTarget.dataset.type;
    if (!type)
      return;
    const visibleMarkers = filterPoints(type);
    this.setData({ activeType: type, visibleMarkers, selectedPoint: visibleMarkers[0] || null });
  },
  onMarkerTap(event) {
    const markerId = event.detail.markerId;
    const selectedPoint = allPoints.find((point) => point.id === markerId) || allPoints[0];
    this.setData({ selectedPoint, center: { latitude: selectedPoint.latitude, longitude: selectedPoint.longitude }, scale: 16 });
  },
  onPointTap(event) {
    const id = Number(event.currentTarget.dataset.id);
    const selectedPoint = allPoints.find((point) => point.id === id) || allPoints[0];
    this.setData({ selectedPoint, center: { latitude: selectedPoint.latitude, longitude: selectedPoint.longitude }, scale: 16 });
  },
  onLocateTap() {
    wx.getLocation({
      type: "gcj02",
      success: (res) => {
        this.setData({ center: { latitude: res.latitude, longitude: res.longitude }, showUserLocation: true, scale: 15 });
      },
      fail: () => {
        wx.showToast({ title: "定位失败，可继续浏览点位", icon: "none" });
      }
    });
  },
  onResetTap() {
    this.setData({ center, scale: 14, activeType: "全部", visibleMarkers: allPoints, selectedPoint: allPoints[0] });
  },
  onRouteTap() {
    this.setData({ activeType: "全部", visibleMarkers: allPoints, selectedPoint: allPoints[0], center, scale: 14 });
    wx.showToast({ title: "已显示推荐路线", icon: "success" });
  },
  onDetailTap() {
    const point = this.data.selectedPoint;
    if (!point)
      return;
    if (point.type === "特色产品") {
      wx.navigateTo({ url: "/pages/specialty/detail?id=simiao-rice" });
      return;
    }
    if (point.type === "非遗体验") {
      wx.navigateTo({ url: "/pages/intangible/detail" });
      return;
    }
    if (point.type === "活动地点") {
      wx.navigateTo({ url: "/pages/activity/detail" });
      return;
    }
    wx.navigateTo({ url: "/pages/culture/detail" });
  },
  onOpenLocationTap() {
    const point = this.data.selectedPoint;
    if (!point)
      return;
    wx.openLocation({
      latitude: point.latitude,
      longitude: point.longitude,
      name: point.title,
      address: point.address,
      scale: 16,
      fail: () => wx.showToast({ title: "导航打开失败", icon: "none" })
    });
  }
});
