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
var auth_exports = {};
__export(auth_exports, {
  getRemoteSession: () => getRemoteSession,
  loginWithAccount: () => loginWithAccount,
  loginWithWechat: () => loginWithWechat,
  logoutRemoteSession: () => logoutRemoteSession
});
module.exports = __toCommonJS(auth_exports);
var import_request = require("../utils/request");
const loginWithWechat = (code) => (0, import_request.request)({
  path: "/api/auth/wechat-login",
  method: "POST",
  data: { code },
  auth: false
});
const loginWithAccount = (account, password) => (0, import_request.request)({
  path: "/api/auth/account-login",
  method: "POST",
  data: { account, password },
  auth: false
});
const getRemoteSession = () => (0, import_request.request)({ path: "/api/auth/session" });
const logoutRemoteSession = () => (0, import_request.request)({ path: "/api/auth/logout", method: "POST" });
