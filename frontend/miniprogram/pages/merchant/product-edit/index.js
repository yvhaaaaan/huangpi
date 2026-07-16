"use strict";
var import_merchant_data = require("../../../utils/merchant-data");
var import_role_router = require("../../../utils/role-router");
Page({
  data: {
    id: "",
    title: "",
    category: "",
    summary: "",
    address: "",
    phone: "",
    editMode: false,
    images: [],
    categories: ["请选择品类", "油茶", "丝苗米", "客家食品", "农副产品", "文创伴手礼"],
    categoryIndex: 0
  },
  onShow() {
    if (!(0, import_role_router.requireAuth)(["merchant"]))
      return;
    const editProductId = wx.getStorageSync("editProductId");
    if (!editProductId) {
      if (this.data.editMode)
        this.resetForm();
      return;
    }
    wx.removeStorageSync("editProductId");
    this.loadProduct(editProductId);
  },
  loadProduct(id) {
    const item = (0, import_merchant_data.getProductById)(id);
    if (!item)
      return;
    const categoryIndex = Math.max(0, this.data.categories.indexOf(item.category));
    this.setData({
      id: item.id,
      title: item.title,
      category: item.category,
      categoryIndex,
      summary: item.summary,
      images: [item.image],
      address: "广东省兴宁市黄陂镇振兴路 18 号",
      phone: "13800138000",
      editMode: true
    });
    wx.setNavigationBarTitle({ title: "修改产品" });
  },
  resetForm() {
    this.setData({ id: "", title: "", category: "", summary: "", address: "", phone: "", images: [], categoryIndex: 0, editMode: false });
    wx.setNavigationBarTitle({ title: "新增产品" });
  },
  onInput(event) {
    const field = event.currentTarget.dataset.field;
    this.setData({ [field]: event.detail.value });
  },
  onCategoryChange(event) {
    const categoryIndex = Number(event.detail.value);
    this.setData({ categoryIndex, category: categoryIndex === 0 ? "" : this.data.categories[categoryIndex] });
  },
  onChooseImage() {
    wx.chooseMedia({ count: 3 - this.data.images.length, mediaType: ["image"], success: (result) => this.setData({ images: [...this.data.images, ...result.tempFiles.map((file) => file.tempFilePath)] }) });
  },
  onRemoveImage(event) {
    const index = Number(event.currentTarget.dataset.index);
    this.setData({ images: this.data.images.filter((_, itemIndex) => itemIndex !== index) });
  },
  validate() {
    if (!this.data.title.trim()) {
      wx.showToast({ title: "请填写产品名称", icon: "none" });
      return false;
    }
    if (!this.data.category) {
      wx.showToast({ title: "请选择产品品类", icon: "none" });
      return false;
    }
    if (!this.data.summary.trim()) {
      wx.showToast({ title: "请填写产品介绍", icon: "none" });
      return false;
    }
    if (!this.data.images.length) {
      wx.showToast({ title: "请上传至少一张图片", icon: "none" });
      return false;
    }
    return true;
  },
  onSaveDraft() {
    wx.showToast({ title: "草稿已保存", icon: "success" });
    setTimeout(() => {
      this.resetForm();
      wx.redirectTo({ url: "/pages/merchant/index" });
    }, 500);
  },
  onSubmit() {
    if (!this.validate())
      return;
    wx.showModal({
      title: "提交审核",
      content: "提交后内容将进入平台审核，审核结果会显示在“我的”消息中。",
      success: (result) => {
        if (!result.confirm)
          return;
        wx.setStorageSync("merchantLastSubmit", { title: this.data.title, time: "刚刚" });
        wx.showToast({ title: "已提交审核", icon: "success" });
        setTimeout(() => {
          this.resetForm();
          wx.redirectTo({ url: "/pages/merchant/index" });
        }, 600);
      }
    });
  },
  onNavTap(event) {
    if (event.currentTarget.dataset.page === "mine")
      wx.redirectTo({ url: "/pages/merchant/index" });
  }
});
