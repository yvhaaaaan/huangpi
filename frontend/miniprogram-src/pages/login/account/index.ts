import { AuthSession, getAuthSession, loginByAccount } from '../../../utils/auth'
import { enterRoleHome } from '../../../utils/role-router'

Page({
  data: { account: '', password: '', accountFocused: false, passwordFocused: false, showPassword: false, submitting: false },

  onLoad(options: Record<string, string>): void {
    if (options.agreed !== '1') { wx.reLaunch({ url: '/pages/login/index' }); return }
    const session = getAuthSession()
    if (session) setTimeout(() => enterRoleHome(session), 100)
  },

  onAccountInput(event: WechatMiniprogram.CustomEvent<{ value: string }>): void { this.setData({ account: event.detail.value.trim() }) },
  onPasswordInput(event: WechatMiniprogram.CustomEvent<{ value: string }>): void { this.setData({ password: event.detail.value }) },
  onAccountFocus(): void { this.setData({ accountFocused: true }) },
  onAccountBlur(): void { this.setData({ accountFocused: false }) },
  onPasswordFocus(): void { this.setData({ passwordFocused: true }) },
  onPasswordBlur(): void { this.setData({ passwordFocused: false }) },
  onTogglePassword(): void { this.setData({ showPassword: !this.data.showPassword }) },

  async onLogin(): Promise<void> {
    if (this.data.submitting) return
    if (!this.data.account) { wx.showToast({ title: '请输入账号', icon: 'none' }); return }
    if (!this.data.password) { wx.showToast({ title: '请输入密码', icon: 'none' }); return }
    this.setData({ submitting: true })
    try {
      const session: AuthSession = await loginByAccount(this.data.account, this.data.password)
      getApp<IAppOption>().globalData.session = session
      wx.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => enterRoleHome(session), 350)
    } catch (_error) {
      wx.showToast({ title: '账号或密码错误', icon: 'none' })
    } finally {
      this.setData({ submitting: false })
    }
  },
})
