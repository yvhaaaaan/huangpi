"use strict";
var import_auth = require("./utils/auth");
var import_role_router = require("./utils/role-router");
App({
  globalData: { session: null },
  onLaunch() {
    this.globalData.session = (0, import_auth.getAuthSession)();
  },
  onShow() {
    setTimeout(() => {
      const pages = getCurrentPages();
      const route = pages.length ? pages[pages.length - 1].route : "";
      if (!route)
        return;
      const session = (0, import_auth.getAuthSession)();
      this.globalData.session = session;
      const isAuthPage = route === "pages/login/index" || route === "pages/login/account/index";
      if (!session && !isAuthPage) {
        wx.reLaunch({ url: "/pages/login/index" });
        return;
      }
      if (!session || isAuthPage)
        return;
      const isMerchantPage = route.startsWith("pages/merchant/");
      const isAdminPage = route.startsWith("pages/admin/");
      if (session.user.role === "merchant" && !isMerchantPage)
        wx.reLaunch({ url: (0, import_role_router.getRoleHome)("merchant") });
      if (session.user.role === "admin" && !isAdminPage)
        wx.reLaunch({ url: (0, import_role_router.getRoleHome)("admin") });
      if (session.user.role === "user" && (isMerchantPage || isAdminPage))
        wx.reLaunch({ url: (0, import_role_router.getRoleHome)("user") });
    }, 0);
  }
});
