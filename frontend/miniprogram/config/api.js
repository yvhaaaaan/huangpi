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
var api_exports = {};
__export(api_exports, {
  API_CONFIG: () => API_CONFIG,
  getApiBaseUrl: () => getApiBaseUrl
});
module.exports = __toCommonJS(api_exports);
const API_CONFIG = {
  // Replace with the HTTPS domain configured in WeChat Mini Program settings.
  baseUrl: "https://api.example.com",
  useMock: true,
  timeout: 1e4
};
const API_BASE_URL_STORAGE_KEY = "api.baseUrl.override";
const getApiBaseUrl = () => {
  const override = wx.getStorageSync(API_BASE_URL_STORAGE_KEY);
  return (override || API_CONFIG.baseUrl).replace(/\/$/, "");
};
