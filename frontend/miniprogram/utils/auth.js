"use strict";
var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
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
var auth_exports = {};
__export(auth_exports, {
  clearAuthSession: () => clearAuthSession,
  getAuthSession: () => getAuthSession,
  loginByAccount: () => loginByAccount,
  loginByWechat: () => loginByWechat,
  notifyBackendLogout: () => notifyBackendLogout,
  saveAuthSession: () => saveAuthSession,
  validateAuthSession: () => validateAuthSession
});
module.exports = __toCommonJS(auth_exports);
var import_auth = require("../api/auth");
var import_api = require("../config/api");
const SESSION_KEY = "auth.session.v2";
const SESSION_DURATION = 7 * 24 * 60 * 60 * 1e3;
const getAuthSession = () => {
  const session = wx.getStorageSync(SESSION_KEY);
  if (!session || !session.token || !session.user || !session.user.role || session.expiresAt <= Date.now()) {
    if (session)
      wx.removeStorageSync(SESSION_KEY);
    return null;
  }
  return session;
};
const saveAuthSession = (session) => {
  wx.setStorageSync(SESSION_KEY, session);
};
const clearAuthSession = () => {
  wx.removeStorageSync(SESSION_KEY);
  wx.removeStorageSync("auth.session.v1");
  wx.removeStorageSync("userProfile");
  wx.removeStorageSync("editProductId");
  wx.removeStorageSync("merchantLastSubmit");
};
const createMockSession = (account, nickname) => {
  const isMerchant = account === "merchant";
  const isAdmin = account === "admin";
  const role = isAdmin ? "admin" : isMerchant ? "merchant" : "user";
  return {
    token: `demo-token-${Date.now()}`,
    expiresAt: Date.now() + SESSION_DURATION,
    user: __spreadValues({
      id: isAdmin ? "admin-user-001" : isMerchant ? "merchant-user-001" : "wechat-user-001",
      nickname,
      role
    }, isMerchant ? { merchantId: "merchant-demo-001" } : {})
  };
};
const loginByAccount = async (account, password) => {
  if (!import_api.API_CONFIG.useMock) {
    const session2 = await (0, import_auth.loginWithAccount)(account, password);
    saveAuthSession(session2);
    return session2;
  }
  if (!["merchant", "admin"].includes(account) || password !== "123456") {
    throw new Error("INVALID_CREDENTIALS");
  }
  const nickname = account === "admin" ? "黄陂镇文旅管理员" : "黄陂特色产品商家";
  const session = createMockSession(account, nickname);
  saveAuthSession(session);
  return session;
};
const loginByWechat = async () => {
  const code = await new Promise((resolve, reject) => {
    wx.login({
      success: (result) => result.code ? resolve(result.code) : reject(new Error("WECHAT_CODE_MISSING")),
      fail: (error) => reject(error)
    });
  });
  if (!import_api.API_CONFIG.useMock) {
    const session2 = await (0, import_auth.loginWithWechat)(code);
    saveAuthSession(session2);
    return session2;
  }
  const session = createMockSession("wechat", "微信用户");
  saveAuthSession(session);
  return session;
};
const notifyBackendLogout = () => {
  if (!import_api.API_CONFIG.useMock)
    void (0, import_auth.logoutRemoteSession)().catch(() => void 0);
};
const validateAuthSession = async () => {
  const localSession = getAuthSession();
  if (!localSession || import_api.API_CONFIG.useMock)
    return localSession;
  try {
    const remoteSession = await (0, import_auth.getRemoteSession)();
    saveAuthSession(remoteSession);
    return remoteSession;
  } catch (_error) {
    return getAuthSession();
  }
};
