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
var user_exports = {};
__export(user_exports, {
  addFavorite: () => addFavorite,
  getActivities: () => getActivities,
  getActivityDetail: () => getActivityDetail,
  getCurrentUser: () => getCurrentUser,
  getHomeData: () => getHomeData,
  getMapPoints: () => getMapPoints,
  getMyFavorites: () => getMyFavorites,
  getMySignups: () => getMySignups,
  getRoutes: () => getRoutes,
  removeFavorite: () => removeFavorite,
  searchContents: () => searchContents,
  signupActivity: () => signupActivity
});
module.exports = __toCommonJS(user_exports);
var import_request = require("../utils/request");
const getCurrentUser = () => (0, import_request.request)({ path: "/api/me" });
const getHomeData = () => (0, import_request.request)({ path: "/api/home" });
const searchContents = (keyword, type = "", page = 1) => (0, import_request.request)({ path: "/api/search", data: { keyword, type, page } });
const getMapPoints = () => (0, import_request.request)({ path: "/api/map/points" });
const getRoutes = () => (0, import_request.request)({ path: "/api/routes" });
const getActivities = (page = 1) => (0, import_request.request)({ path: "/api/activities", data: { page } });
const getActivityDetail = (id) => (0, import_request.request)({ path: `/api/activities/${encodeURIComponent(id)}` });
const signupActivity = (id, payload) => (0, import_request.request)({ path: `/api/activities/${encodeURIComponent(id)}/signups`, method: "POST", data: payload });
const getMySignups = () => (0, import_request.request)({ path: "/api/me/signups" });
const getMyFavorites = () => (0, import_request.request)({ path: "/api/me/favorites" });
const addFavorite = (targetType, targetId) => (0, import_request.request)({ path: "/api/favorites", method: "POST", data: { targetType, targetId } });
const removeFavorite = (id) => (0, import_request.request)({ path: `/api/favorites/${encodeURIComponent(id)}`, method: "DELETE" });
