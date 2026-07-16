"use strict";
Page({
  data: { name: "", phone: "", count: "1", remark: "" },
  onNameInput(event) {
    this.setData({ name: event.detail.value });
  },
  onPhoneInput(event) {
    this.setData({ phone: event.detail.value });
  },
  onCountInput(event) {
    this.setData({ count: event.detail.value });
  },
  onRemarkInput(event) {
    this.setData({ remark: event.detail.value });
  },
  onBackTap() {
    wx.navigateBack();
  },
  onSubmitTap() {
    if (!this.data.name || !this.data.phone || !this.data.count) {
      wx.showToast({ title: "请填写必填项", icon: "none" });
      return;
    }
    if (!/^1\d{10}$/.test(this.data.phone)) {
      wx.showToast({ title: "手机号格式错误", icon: "none" });
      return;
    }
    wx.navigateTo({ url: "/pages/activity/success" });
  }
});
