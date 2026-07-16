"use strict";
var import_auth = require("../../../utils/auth");
var import_role_router = require("../../../utils/role-router");
Page({
  data: { account: "", password: "", accountFocused: false, passwordFocused: false, showPassword: false, submitting: false },
  onLoad(options) {
    if (options.agreed !== "1") {
      wx.reLaunch({ url: "/pages/login/index" });
      return;
    }
    const session = (0, import_auth.getAuthSession)();
    if (session)
      setTimeout(() => (0, import_role_router.enterRoleHome)(session), 100);
  },
  onAccountInput(event) {
    this.setData({ account: event.detail.value.trim() });
  },
  onPasswordInput(event) {
    this.setData({ password: event.detail.value });
  },
  onAccountFocus() {
    this.setData({ accountFocused: true });
  },
  onAccountBlur() {
    this.setData({ accountFocused: false });
  },
  onPasswordFocus() {
    this.setData({ passwordFocused: true });
  },
  onPasswordBlur() {
    this.setData({ passwordFocused: false });
  },
  onTogglePassword() {
    this.setData({ showPassword: !this.data.showPassword });
  },
  async onLogin() {
    if (this.data.submitting)
      return;
    if (!this.data.account) {
      wx.showToast({ title: "请输入账号", icon: "none" });
      return;
    }
    if (!this.data.password) {
      wx.showToast({ title: "请输入密码", icon: "none" });
      return;
    }
    this.setData({ submitting: true });
    try {
      const session = await (0, import_auth.loginByAccount)(this.data.account, this.data.password);
      getApp().globalData.session = session;
      wx.showToast({ title: "登录成功", icon: "success" });
      setTimeout(() => (0, import_role_router.enterRoleHome)(session), 350);
    } catch (_error) {
      wx.showToast({ title: "账号或密码错误", icon: "none" });
    } finally {
      this.setData({ submitting: false });
    }
  }
});
