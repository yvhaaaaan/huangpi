"use strict";
Page({
  data: {
    activeStatus: "全部",
    statuses: ["全部", "未开始", "报名中", "已报满", "已结束"],
    activities: [
      { id: 1, status: "报名中", type: "油茶体验", title: "油茶文化体验活动", desc: "从原料认识到现场品鉴，完整体验黄陂油茶文化。", time: "10月12日 09:30", place: "黄陂镇文化广场", limit: 30, image: "/static/real-activity.jpg" },
      { id: 2, status: "报名中", type: "非遗课堂", title: "客家非遗手作课堂", desc: "跟随本地匠人完成一件可带走的手作纪念品。", time: "10月18日 14:00", place: "非遗工坊", limit: 20, image: "/static/real-workshop.jpg" },
      { id: 3, status: "未开始", type: "研学导览", title: "客家古村半日研学", desc: "面向亲子和学生团队，串联村史、建筑和民俗。", time: "10月26日 10:00", place: "游客服务中心", limit: 40, image: "/static/real-classroom.jpg" }
    ]
  },
  onStatusTap(event) {
    const status = event.currentTarget.dataset.status;
    if (status)
      this.setData({ activeStatus: status });
  },
  onNavigateTap(event) {
    const url = event.currentTarget.dataset.url;
    if (url)
      wx.navigateTo({ url });
  },
  onCollectTap() {
    wx.showToast({ title: "已收藏活动", icon: "success" });
  },
  onShareAppMessage() {
    return { title: "黄陂镇文旅活动报名", path: "/pages/activity/index", imageUrl: "/static/real-activity.jpg" };
  }
});
