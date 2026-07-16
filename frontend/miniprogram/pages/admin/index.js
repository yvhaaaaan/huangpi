"use strict";
var import_admin_data = require("../../utils/admin-data");
var import_role_router = require("../../utils/role-router");
Page({
  data: {
    adminName: "",
    stats: { pending: 0, approvedToday: 0, merchants: 12 },
    pendingItems: []
  },
  onShow() {
    const session = (0, import_role_router.requireAuth)(["admin"]);
    if (!session)
      return;
    const items = (0, import_admin_data.getReviewItems)();
    this.setData({
      adminName: session.user.nickname,
      stats: { pending: items.filter((item) => item.status === "pending").length, approvedToday: 3, merchants: 12 },
      pendingItems: items.filter((item) => item.status === "pending").slice(0, 3)
    });
  },
  onReviewTap() {
    wx.redirectTo({ url: "/pages/admin/review/index" });
  },
  onNavTap(event) {
    if (event.currentTarget.dataset.page === "review")
      this.onReviewTap();
  },
  onLogout() {
    wx.showModal({
      title: "退出登录",
      content: "退出后需要重新登录管理员账号。",
      confirmColor: "#a3433a",
      success: (result) => {
        if (result.confirm)
          (0, import_role_router.logout)();
      }
    });
  }
});
