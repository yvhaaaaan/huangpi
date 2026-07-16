"use strict";
var import_merchant_data = require("../../utils/merchant-data");
var import_role_router = require("../../utils/role-router");
Page({
  data: {
    merchant: import_merchant_data.merchantProfile,
    activePanel: "todo",
    stats: { published: 0, pending: 0, draft: 0 },
    messages: [],
    todos: []
  },
  onShow() {
    if (!(0, import_role_router.requireAuth)(["merchant"]))
      return;
    this.loadData();
  },
  onPullDownRefresh() {
    this.loadData();
    setTimeout(() => wx.stopPullDownRefresh(), 350);
  },
  onPanelTap(event) {
    this.setData({ activePanel: event.currentTarget.dataset.panel });
  },
  loadData() {
    const products = (0, import_merchant_data.getProducts)();
    const rejected = products.filter((item) => item.status === "rejected");
    const pending = products.filter((item) => item.status === "pending");
    const submitted = wx.getStorageSync("merchantLastSubmit");
    this.setData({
      stats: {
        published: products.filter((item) => item.status === "published").length,
        pending: pending.length,
        draft: products.filter((item) => item.status === "draft").length
      },
      messages: [
        ...submitted && submitted.title ? [{ id: "submitted", type: "提交", title: "内容已提交审核", desc: `“${submitted.title}”已进入审核流程`, time: submitted.time || "刚刚", unread: true }] : [],
        { id: "reject", type: "审核", title: "内容审核未通过", desc: "“客家手工米果”需要补充生产信息", time: "昨天 11:45", unread: true },
        { id: "publish", type: "发布", title: "内容已成功发布", desc: "“黄陂客家传统油茶”已在用户端展示", time: "07-12 16:20", unread: false }
      ],
      todos: [
        ...rejected.map((item) => ({ id: item.id, title: `修改“${item.title}”`, desc: item.rejectReason || "请根据审核意见修改后重新提交", level: "urgent", productId: item.id })),
        ...pending.map((item) => ({ id: item.id, title: `等待“${item.title}”审核`, desc: "平台审核完成后会通过消息通知", level: "normal" }))
      ]
    });
  },
  onTodoTap(event) {
    const productId = event.currentTarget.dataset.productId;
    if (!productId) {
      wx.showToast({ title: "当前无需操作", icon: "none" });
      return;
    }
    wx.setStorageSync("editProductId", productId);
    wx.redirectTo({ url: "/pages/merchant/product-edit/index" });
  },
  onMessageTap(event) {
    const id = event.currentTarget.dataset.id;
    if (id === "reject") {
      wx.setStorageSync("editProductId", "p3");
      wx.redirectTo({ url: "/pages/merchant/product-edit/index" });
      return;
    }
    wx.showToast({ title: "已读", icon: "none" });
  },
  onLogout() {
    wx.showModal({
      title: "退出登录",
      content: "退出后需要重新登录商家账号。",
      confirmColor: "#a3433a",
      success: (result) => {
        if (result.confirm)
          (0, import_role_router.logout)();
      }
    });
  },
  onNavTap(event) {
    if (event.currentTarget.dataset.page === "add")
      wx.redirectTo({ url: "/pages/merchant/product-edit/index" });
  }
});
