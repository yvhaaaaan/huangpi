"use strict";
var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __export = (target, all) => {
  for (var name in all)
    __defProp(target, name, { get: all[name], enumerable: true });
};
var __copyProps = (to, from, except, desc) => {
  if (from && typeof from === "object" || typeof from === "function") {
    for (let key of __getOwnPropNames(from))
      if (!__hasOwnProp.call(to, key) && key !== except)
        __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
  }
  return to;
};
var __toCommonJS = (mod) => __copyProps(__defProp({}, "__esModule", { value: true }), mod);
var role_router_exports = {};
__export(role_router_exports, {
  enterRoleHome: () => enterRoleHome,
  getRoleHome: () => getRoleHome,
  logout: () => logout,
  requireAuth: () => requireAuth
});
module.exports = __toCommonJS(role_router_exports);
var import_auth = require("./auth");
const getRoleHome = (role) => {
  if (role === "merchant")
    return "/pages/merchant/index";
  if (role === "admin")
    return "/pages/admin/index";
  return "/pages/home/index";
};
const enterRoleHome = (session) => {
  wx.reLaunch({ url: getRoleHome(session.user.role) });
};
const requireAuth = (allowedRoles) => {
  const session = (0, import_auth.getAuthSession)();
  if (!session) {
    wx.reLaunch({ url: "/pages/login/index" });
    return null;
  }
  if (allowedRoles && !allowedRoles.includes(session.user.role)) {
    wx.reLaunch({ url: getRoleHome(session.user.role) });
    return null;
  }
  return session;
};
const logout = () => {
  (0, import_auth.notifyBackendLogout)();
  (0, import_auth.clearAuthSession)();
  wx.reLaunch({ url: "/pages/login/index" });
};
