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
var files_exports = {};
__export(files_exports, {
  deleteFile: () => deleteFile,
  getFileInfo: () => getFileInfo,
  uploadImage: () => uploadImage
});
module.exports = __toCommonJS(files_exports);
var import_api = require("../config/api");
var import_request = require("../utils/request");
const SESSION_KEY = "auth.session.v2";
const uploadImage = (filePath, businessType = "merchant_product") => {
  const session = wx.getStorageSync(SESSION_KEY);
  const token = session && session.token ? session.token : "";
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: `${(0, import_api.getApiBaseUrl)()}/api/files`,
      filePath,
      name: "file",
      formData: { businessType },
      header: token ? { Authorization: `Bearer ${token}` } : {},
      timeout: import_api.API_CONFIG.timeout,
      success: (response) => {
        try {
          const body = JSON.parse(response.data);
          if (response.statusCode === 401 || body.code === 40100) {
            (0, import_request.handleUnauthorized)();
            reject(new import_request.ApiError(body.message || "登录状态已失效", body.code, response.statusCode, body.traceId));
            return;
          }
          if (response.statusCode < 200 || response.statusCode >= 300 || body.code !== 0) {
            reject(new import_request.ApiError(body.message || "图片上传失败", body.code, response.statusCode, body.traceId));
            return;
          }
          resolve(body.data);
        } catch (_error) {
          reject(new import_request.ApiError("图片上传返回格式错误"));
        }
      },
      fail: (error) => reject(new import_request.ApiError(error.errMsg || "图片上传失败"))
    });
  });
};
const getFileInfo = (id) => {
  return (0, import_request.request)({ path: `/api/files/${encodeURIComponent(id)}` });
};
const deleteFile = (id) => {
  return (0, import_request.request)({ path: `/api/files/${encodeURIComponent(id)}`, method: "DELETE" });
};
