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
var merchant_exports = {};
__export(merchant_exports, {
  closeMerchantProduct: () => closeMerchantProduct,
  createMerchantProduct: () => createMerchantProduct,
  getMerchantDashboard: () => getMerchantDashboard,
  getMerchantMessages: () => getMerchantMessages,
  getMerchantProduct: () => getMerchantProduct,
  getMerchantProducts: () => getMerchantProducts,
  getMerchantProfile: () => getMerchantProfile,
  getMerchantTodos: () => getMerchantTodos,
  submitMerchantProduct: () => submitMerchantProduct,
  submitMerchantProfile: () => submitMerchantProfile,
  updateMerchantProduct: () => updateMerchantProduct,
  updateMerchantProfile: () => updateMerchantProfile
});
module.exports = __toCommonJS(merchant_exports);
var import_request = require("../utils/request");
const getMerchantDashboard = () => (0, import_request.request)({ path: "/api/merchant/dashboard" });
const getMerchantProfile = () => (0, import_request.request)({ path: "/api/merchant/profile" });
const updateMerchantProfile = (payload) => (0, import_request.request)({ path: "/api/merchant/profile", method: "PUT", data: payload });
const submitMerchantProfile = () => (0, import_request.request)({ path: "/api/merchant/profile/submit", method: "POST" });
const getMerchantProducts = (page = 1) => (0, import_request.request)({ path: "/api/merchant/products", data: { page } });
const getMerchantProduct = (id) => (0, import_request.request)({ path: `/api/merchant/products/${encodeURIComponent(id)}` });
const createMerchantProduct = (payload) => (0, import_request.request)({ path: "/api/merchant/products", method: "POST", data: payload });
const updateMerchantProduct = (id, payload) => (0, import_request.request)({ path: `/api/merchant/products/${encodeURIComponent(id)}`, method: "PUT", data: payload });
const submitMerchantProduct = (id) => (0, import_request.request)({ path: `/api/merchant/products/${encodeURIComponent(id)}/submit`, method: "POST" });
const closeMerchantProduct = (id) => (0, import_request.request)({ path: `/api/merchant/products/${encodeURIComponent(id)}/close`, method: "POST" });
const getMerchantMessages = () => (0, import_request.request)({ path: "/api/merchant/messages" });
const getMerchantTodos = () => (0, import_request.request)({ path: "/api/merchant/todos" });
