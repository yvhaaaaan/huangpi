"use strict";
const productDetails = {
  "oil-tea-gift": {
    id: "oil-tea-gift",
    title: "黄陂油茶礼盒",
    categoryName: "油茶",
    status: "已发布",
    detail: "采用本地油茶原料，保留客家传统风味，并附冲泡说明。页面只提供产品展示、商家联系和地图导览，不提供购物车、支付、订单和物流。",
    merchant: "黄陂油茶工坊",
    address: "黄陂镇游客服务中心旁",
    businessHours: "09:00-18:00",
    image: "/static/real-merchant.jpg",
    images: ["/static/real-merchant.jpg", "/static/real-tea.jpg", "/static/specialty-1.jpg"]
  },
  "simiao-rice": {
    id: "simiao-rice",
    title: "黄陂丝苗米",
    categoryName: "丝苗米",
    status: "已发布",
    detail: "选用本地种植的丝苗米，突出产地、种植和加工信息。油茶与丝苗米在平台中均为一级重点品类，使用同一套展示、审核和商家联系流程。",
    merchant: "黄陂镇优质稻合作社",
    address: "黄陂镇农产品展示中心",
    businessHours: "08:30-17:30",
    image: "/static/real-product.jpg",
    images: ["/static/real-product.jpg", "/static/real-classroom.jpg"]
  },
  "hakka-snack": {
    id: "hakka-snack",
    title: "客家手作米果",
    categoryName: "客家食品",
    status: "已发布",
    detail: "选用本地米料制作，经过传统工序成型，适合作为地方风味展示和研学体验内容。",
    merchant: "客家古村合作社",
    address: "客家古村入口",
    businessHours: "09:00-17:30",
    image: "/static/specialty-2.jpg",
    images: ["/static/specialty-2.jpg", "/static/real-workshop.jpg"]
  },
  "heritage-gift": {
    id: "heritage-gift",
    title: "非遗纹样纪念品",
    categoryName: "文创伴手礼",
    status: "已发布",
    detail: "将黄陂客家文化与非遗纹样转化为便于携带的纪念品，适合文旅展示和研学纪念。",
    merchant: "非遗文创中心",
    address: "黄陂镇非遗工坊",
    businessHours: "09:00-18:00",
    image: "/static/real-workshop.jpg",
    images: ["/static/real-workshop.jpg", "/static/real-classroom.jpg"]
  }
};
Page({
  data: {
    product: productDetails["oil-tea-gift"]
  },
  onLoad(options) {
    const id = options.id || "oil-tea-gift";
    const product = productDetails[id] || productDetails["oil-tea-gift"];
    this.setData({ product });
    wx.setNavigationBarTitle({ title: product.title });
  },
  onCollectTap() {
    wx.showToast({ title: "已收藏", icon: "success" });
  },
  onMapTap() {
    wx.switchTab({ url: "/pages/map/index" });
  },
  onNavTap() {
    wx.showToast({ title: "导航能力待接入", icon: "none" });
  },
  onContactTap() {
    wx.showToast({ title: "已复制联系方式", icon: "success" });
  },
  onShareAppMessage() {
    const product = this.data.product;
    return { title: product.title, path: `/pages/specialty/detail?id=${product.id}`, imageUrl: product.image };
  }
});
