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
var request_exports = {};
__export(request_exports, {
  ApiError: () => ApiError,
  handleUnauthorized: () => handleUnauthorized,
  request: () => request
});
module.exports = __toCommonJS(request_exports);
var import_api = require("../config/api");
class ApiError extends Error {
  constructor(message, code = -1, statusCode, traceId) {
    super(message);
    this.name = "ApiError";
    this.code = code;
    this.statusCode = statusCode;
    this.traceId = traceId;
  }
}
const SESSION_KEY = "auth.session.v2";
let redirectingToLogin = false;
const getToken = () => {
  const session = wx.getStorageSync(SESSION_KEY);
  return session && session.token ? session.token : "";
};
const handleUnauthorized = () => {
  wx.removeStorageSync(SESSION_KEY);
  wx.removeStorageSync("auth.session.v1");
  wx.removeStorageSync("userProfile");
  getApp().globalData.session = null;
  if (redirectingToLogin)
    return;
  redirectingToLogin = true;
  wx.reLaunch({
    url: "/pages/login/index",
    complete: () => {
      redirectingToLogin = false;
    }
  });
};
const request = (options) => {
  const token = options.auth === false ? "" : getToken();
  const header = __spreadValues({
    "content-type": "application/json"
  }, options.header);
  if (token)
    header.Authorization = `Bearer ${token}`;
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${(0, import_api.getApiBaseUrl)()}${options.path}`,
      method: options.method || "GET",
      data: options.data,
      header,
      timeout: import_api.API_CONFIG.timeout,
      success: (response) => {
        const body = response.data;
        const bodyCode = body && body.code;
        const bodyMessage = body && body.message;
        const traceId = body && body.traceId;
        if (response.statusCode === 401 || bodyCode === 40100) {
          handleUnauthorized();
          reject(new ApiError(bodyMessage || "登录状态已失效", bodyCode || 40100, response.statusCode, traceId));
          return;
        }
        if (response.statusCode < 200 || response.statusCode >= 300) {
          reject(new ApiError(bodyMessage || `请求失败 (${response.statusCode})`, bodyCode || -1, response.statusCode, traceId));
          return;
        }
        if (!body || body.code !== 0) {
          reject(new ApiError(bodyMessage || "接口返回异常", bodyCode || -1, response.statusCode, traceId));
          return;
        }
        resolve(body.data);
      },
      fail: (error) => reject(new ApiError(error.errMsg || "网络连接失败"))
    });
  });
};
