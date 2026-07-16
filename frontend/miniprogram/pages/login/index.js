"use strict";
var import_auth = require("../../utils/auth");
var import_role_router = require("../../utils/role-router");
Page({
  data: {
    statusBarHeight: 20,
    navBarHeight: 44,
    agreed: false,
    submitting: false
  },
  onLoad() {
    const system = wx.getSystemInfoSync();
    this.setData({ statusBarHeight: system.statusBarHeight || 20 });
    const session = (0, import_auth.getAuthSession)();
    if (session)
      setTimeout(() => (0, import_role_router.enterRoleHome)(session), 100);
  },
  onOpenAccount() {
    if (!this.data.agreed) {
      wx.showToast({ title: "请先阅读并同意相关协议", icon: "none" });
      return;
    }
    wx.navigateTo({ url: "/pages/login/account/index?agreed=1" });
  },
  onToggleAgreement() {
    this.setData({ agreed: !this.data.agreed });
  },
  onAgreementTap() {
    wx.showModal({ title: "用户协议", content: "用户协议内容将在正式上线前接入。", showCancel: false });
  },
  onPrivacyTap() {
    wx.showModal({ title: "隐私政策", content: "隐私政策内容将在正式上线前接入。", showCancel: false });
  },
  async onWechatLogin() {
    if (this.data.submitting)
      return;
    if (!this.data.agreed) {
      wx.showToast({ title: "请先阅读并同意相关协议", icon: "none" });
      return;
    }
    await this.completeLogin(import_auth.loginByWechat, "微信登录失败，请稍后重试");
  },
  async completeLogin(login, errorMessage) {
    this.setData({ submitting: true });
    try {
      const session = await login();
      getApp().globalData.session = session;
      wx.showToast({ title: "登录成功", icon: "success" });
      setTimeout(() => (0, import_role_router.enterRoleHome)(session), 350);
    } catch (_error) {
      wx.showToast({ title: errorMessage, icon: "none" });
    } finally {
      this.setData({ submitting: false });
    }
  }
});
