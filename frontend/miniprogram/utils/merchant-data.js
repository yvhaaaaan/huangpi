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
var merchant_data_exports = {};
__export(merchant_data_exports, {
  getProductById: () => getProductById,
  getProducts: () => getProducts,
  merchantProfile: () => merchantProfile
});
module.exports = __toCommonJS(merchant_data_exports);
const products = [
  { id: "p1", title: "黄陂客家传统油茶", category: "油茶", summary: "本地山茶籽与客家传统工艺制作，茶香醇厚。", image: "/static/real-product.jpg", status: "published", statusLabel: "已发布", updatedAt: "07-12 16:20", views: 368 },
  { id: "p2", title: "黄陂丝苗米", category: "丝苗米", summary: "本地种植加工，米粒细长、饭香自然。", image: "/static/real-product.jpg", status: "pending", statusLabel: "待审核", updatedAt: "07-13 09:10", views: 0 },
  { id: "p3", title: "客家手工米果", category: "客家食品", summary: "使用本地糯米制作，保留传统手工风味。", image: "/static/real-workshop.jpg", status: "rejected", statusLabel: "已驳回", updatedAt: "07-11 11:45", views: 46, rejectReason: "商品介绍中缺少生产信息，请补充后重新提交。" },
  { id: "p4", title: "油茶体验礼盒", category: "油茶", summary: "包含茶料、茶具与冲泡说明的体验组合。", image: "/static/real-merchant.jpg", status: "draft", statusLabel: "草稿", updatedAt: "07-10 18:30", views: 0 }
];
const merchantProfile = {
  name: "黄陂镇特色农产品合作社",
  owner: "陈店长",
  phone: "13800138000",
  address: "广东省兴宁市黄陂镇振兴路 18 号",
  hours: "08:30-18:00",
  intro: "展示黄陂油茶、丝苗米和客家食品，提供产品介绍、产地参观和研学接待服务。",
  image: "/static/real-merchant.jpg"
};
const getProducts = () => products.map((item) => __spreadValues({}, item));
const getProductById = (id) => products.find((item) => item.id === id);
