"use strict";
Page({
  data: {
    keyword: "",
    hotKeywords: ["油茶", "丝苗米", "非遗", "客家古村", "路线"],
    results: [
      { id: 1, type: "油茶", title: "油茶文化详情", desc: "了解黄陂油茶的制作流程和文化故事。", url: "/pages/tea/detail", image: "/static/real-tea.jpg" },
      { id: 2, type: "特色产品", title: "黄陂油茶礼盒", desc: "查看油茶产品、商家和产地信息。", url: "/pages/specialty/detail?id=oil-tea-gift", image: "/static/real-merchant.jpg" },
      { id: 3, type: "特色产品", title: "黄陂丝苗米", desc: "黄陂一级重点品类，查看产品和产地信息。", url: "/pages/specialty/detail?id=simiao-rice", image: "/static/real-product.jpg" },
      { id: 4, type: "文旅", title: "客家古村路线", desc: "查看古村打卡路线和推荐停留点。", url: "/pages/culture/detail", image: "/static/real-hero.jpg" },
      { id: 5, type: "活动", title: "油茶文化体验活动", desc: "报名参与油茶制作与品鉴体验。", url: "/pages/activity/detail", image: "/static/real-activity.jpg" }
    ]
  },
  onKeywordInput(event) {
    this.setData({ keyword: event.detail.value });
  },
  onHotTap(event) {
    const keyword = event.currentTarget.dataset.keyword;
    if (keyword)
      this.setData({ keyword });
  },
  onResultTap(event) {
    const url = event.currentTarget.dataset.url;
    if (url)
      wx.navigateTo({ url });
  }
});
