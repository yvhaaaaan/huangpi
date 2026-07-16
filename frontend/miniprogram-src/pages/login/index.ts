import { AuthSession, getAuthSession, loginByWechat } from '../../utils/auth'
import { enterRoleHome } from '../../utils/role-router'

Page({
  data: {
    statusBarHeight: 20,
    navBarHeight: 44,
    agreed: false,
    submitting: false,
  },

  onLoad(): void {
    const system = wx.getSystemInfoSync()
    this.setData({ statusBarHeight: system.statusBarHeight || 20 })
    const session = getAuthSession()
    if (session) setTimeout(() => enterRoleHome(session), 100)
  },

  onOpenAccount(): void {
    if (!this.data.agreed) { wx.showToast({ title: '请先阅读并同意相关协议', icon: 'none' }); return }
    wx.navigateTo({ url: '/pages/login/account/index?agreed=1' })
  },
  onToggleAgreement(): void { this.setData({ agreed: !this.data.agreed }) },
  onAgreementTap(): void { wx.showModal({ title: '用户协议', content: '用户协议内容将在正式上线前接入。', showCancel: false }) },
  onPrivacyTap(): void { wx.showModal({ title: '隐私政策', content: '隐私政策内容将在正式上线前接入。', showCancel: false }) },

  async onWechatLogin(): Promise<void> {
    if (this.data.submitting) return
    if (!this.data.agreed) { wx.showToast({ title: '请先阅读并同意相关协议', icon: 'none' }); return }
    await this.completeLogin(loginByWechat, '微信登录失败，请稍后重试')
  },

  async completeLogin(login: () => Promise<AuthSession>, errorMessage: string): Promise<void> {
    this.setData({ submitting: true })
    try {
      const session = await login()
      getApp<IAppOption>().globalData.session = session
      wx.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => enterRoleHome(session), 350)
    } catch (_error) {
      wx.showToast({ title: errorMessage, icon: 'none' })
    } finally {
      this.setData({ submitting: false })
    }
  },
})
