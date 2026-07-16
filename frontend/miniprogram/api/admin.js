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
var admin_exports = {};
__export(admin_exports, {
  approveAdminReview: () => approveAdminReview,
  closeAdminReview: () => closeAdminReview,
  createAdminCategory: () => createAdminCategory,
  getAdminCategories: () => getAdminCategories,
  getAdminDashboard: () => getAdminDashboard,
  getAdminMerchants: () => getAdminMerchants,
  getAdminReview: () => getAdminReview,
  getAdminReviews: () => getAdminReviews,
  getAuditLogs: () => getAuditLogs,
  rejectAdminReview: () => rejectAdminReview,
  updateAdminCategory: () => updateAdminCategory
});
module.exports = __toCommonJS(admin_exports);
var import_request = require("../utils/request");
const getAdminDashboard = () => (0, import_request.request)({ path: "/api/admin/dashboard" });
const getAdminReviews = (query = {}) => (0, import_request.request)({ path: "/api/admin/reviews", data: query });
const getAdminReview = (id) => (0, import_request.request)({ path: `/api/admin/reviews/${encodeURIComponent(id)}` });
const approveAdminReview = (id, comment = "") => (0, import_request.request)({ path: `/api/admin/reviews/${encodeURIComponent(id)}/approve`, method: "POST", data: { comment } });
const rejectAdminReview = (id, reason) => (0, import_request.request)({ path: `/api/admin/reviews/${encodeURIComponent(id)}/reject`, method: "POST", data: { reason } });
const closeAdminReview = (id, reason = "") => (0, import_request.request)({ path: `/api/admin/reviews/${encodeURIComponent(id)}/close`, method: "POST", data: { reason } });
const getAdminMerchants = (page = 1) => (0, import_request.request)({ path: "/api/admin/merchants", data: { page } });
const getAdminCategories = () => (0, import_request.request)({ path: "/api/admin/product-categories" });
const createAdminCategory = (payload) => (0, import_request.request)({ path: "/api/admin/product-categories", method: "POST", data: payload });
const updateAdminCategory = (id, payload) => (0, import_request.request)({ path: `/api/admin/product-categories/${encodeURIComponent(id)}`, method: "PUT", data: payload });
const getAuditLogs = (page = 1) => (0, import_request.request)({ path: "/api/admin/audit-logs", data: { page } });
