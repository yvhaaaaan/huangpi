"use strict";
Page({
  data: {
    items: [
      { id: 1, title: "油茶制作技艺", desc: "从炒料、研磨到冲煮，完整体验传统油茶制作。", master: "本地讲解员", duration: "60 分钟", image: "/static/activity.jpg" },
      { id: 2, title: "客家手作课堂", desc: "结合地方纹样完成一件手作纪念品。", master: "非遗工坊", duration: "90 分钟", image: "/static/profile.jpg" }
    ]
  },
  onItemTap() {
    wx.navigateTo({ url: "/pages/intangible/detail" });
  }
});
