import { getAuthSession } from './utils/auth'
import { getRoleHome } from './utils/role-router'

App<IAppOption>({
  globalData: { session: null },

  onLaunch(): void {
    this.globalData.session = getAuthSession()
  },

  onShow(): void {
    setTimeout(() => {
      const pages = getCurrentPages()
      const route = pages.length ? pages[pages.length - 1].route : ''
      if (!route) return
      const session = getAuthSession()
      this.globalData.session = session
      const isAuthPage = route === 'pages/login/index' || route === 'pages/login/account/index'
      if (!session && !isAuthPage) wx.reLaunch({ url: '/pages/login/index' })
      if (!session || isAuthPage) return
      const isMerchantPage = route.startsWith('pages/merchant/')
      const isAdminPage = route.startsWith('pages/admin/')
      if (session.user.role === 'merchant' && !isMerchantPage) wx.reLaunch({ url: getRoleHome('merchant') })
      if (session.user.role === 'admin' && !isAdminPage) wx.reLaunch({ url: getRoleHome('admin') })
      if (session.user.role === 'user' && (isMerchantPage || isAdminPage)) wx.reLaunch({ url: getRoleHome('user') })
    }, 500)
  },
})
