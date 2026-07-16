"use strict";
var import_role_router = require("../../utils/role-router");
Page({
  data: {
    nickname: "",
    role: "user",
    roleLabel: "普通用户",
    avatarText: "用",
    favoriteCount: 8,
    signupCount: 3
  },
  onShow() {
    const session = (0, import_role_router.requireAuth)();
    if (!session)
      return;
    this.setData({
      nickname: session.user.nickname,
      role: session.user.role,
      roleLabel: session.user.role === "admin" ? "政府管理员" : session.user.role === "merchant" ? "认证商家" : "普通用户",
      avatarText: session.user.nickname.substring(0, 1)
    });
  },
  onNavigateTap(event) {
    const url = event.currentTarget.dataset.url;
    if (url)
      wx.navigateTo({ url });
  },
  onToastTap() {
    wx.showToast({ title: "功能待接入", icon: "none" });
  },
  onLogout() {
    wx.showModal({
      title: "退出登录",
      content: "退出后需要重新登录才能使用小程序。",
      confirmColor: "#a3433a",
      success: (result) => {
        if (result.confirm)
          (0, import_role_router.logout)();
      }
    });
  }
});
