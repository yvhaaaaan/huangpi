"use strict";
var __defProp = Object.defineProperty;
var __defProps = Object.defineProperties;
var __getOwnPropDescs = Object.getOwnPropertyDescriptors;
var __getOwnPropSymbols = Object.getOwnPropertySymbols;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __propIsEnum = Object.prototype.propertyIsEnumerable;
var __defNormalProp = (obj, key, value) => key in obj ? __defProp(obj, key, { enumerable: true, configurable: true, writable: true, value }) : obj[key] = value;
var __spreadValues = (a, b) => {
  for (var prop in b || (b = {}))
    if (__hasOwnProp.call(b, prop))
      __defNormalProp(a, prop, b[prop]);
  if (__getOwnPropSymbols)
    for (var prop of __getOwnPropSymbols(b)) {
      if (__propIsEnum.call(b, prop))
        __defNormalProp(a, prop, b[prop]);
    }
  return a;
};
var __spreadProps = (a, b) => __defProps(a, __getOwnPropDescs(b));
var import_admin_data = require("../../../utils/admin-data");
var import_role_router = require("../../../utils/role-router");
Page({
  data: {
    activeStatus: "pending",
    filters: [
      { key: "pending", label: "待审核" },
      { key: "all", label: "全部" },
      { key: "approved", label: "已通过" },
      { key: "rejected", label: "已驳回" }
    ],
    items: [],
    filteredItems: []
  },
  onShow() {
    if (!(0, import_role_router.requireAuth)(["admin"]))
      return;
    if (!this.data.items.length)
      this.setData({ items: (0, import_admin_data.getReviewItems)() }, () => this.applyFilter());
  },
  onFilterTap(event) {
    this.setData({ activeStatus: event.currentTarget.dataset.status }, () => this.applyFilter());
  },
  applyFilter() {
    const status = this.data.activeStatus;
    this.setData({ filteredItems: this.data.items.filter((item) => status === "all" || item.status === status) });
  },
  updateStatus(id, status, statusLabel) {
    const items = this.data.items.map((item) => item.id === id ? __spreadProps(__spreadValues({}, item), { status, statusLabel }) : item);
    this.setData({ items }, () => this.applyFilter());
  },
  onApproveTap(event) {
    const id = event.currentTarget.dataset.id;
    wx.showModal({ title: "确认通过", content: "通过后该内容将在用户端展示。", success: (result) => {
      if (result.confirm) {
        this.updateStatus(id, "approved", "已通过");
        wx.showToast({ title: "审核已通过", icon: "success" });
      }
    } });
  },
  onRejectTap(event) {
    const id = event.currentTarget.dataset.id;
    wx.showModal({ title: "确认驳回", content: "驳回后商家可根据审核意见修改并重新提交。", confirmColor: "#a3433a", success: (result) => {
      if (result.confirm) {
        this.updateStatus(id, "rejected", "已驳回");
        wx.showToast({ title: "已驳回", icon: "none" });
      }
    } });
  },
  onNavTap(event) {
    if (event.currentTarget.dataset.page === "home")
      wx.redirectTo({ url: "/pages/admin/index" });
  }
});
