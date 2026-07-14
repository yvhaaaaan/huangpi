import { requireAuth, logout } from '../../utils/role-router'

Page({
  data: {
    nickname: '',
    role: 'user',
    roleLabel: '普通用户',
    avatarText: '用',
    favoriteCount: 8,
    signupCount: 3,
  },

  onShow(): void {
    const session = requireAuth()
    if (!session) return
    this.setData({
      nickname: session.user.nickname,
      role: session.user.role,
      roleLabel: session.user.role === 'admin' ? '政府管理员' : session.user.role === 'merchant' ? '认证商家' : '普通用户',
      avatarText: session.user.nickname.substring(0, 1),
    })
  },

  onNavigateTap(event: WechatMiniprogram.TouchEvent): void {
    const url = event.currentTarget.dataset.url as string | undefined
    if (url) wx.navigateTo({ url })
  },

  onToastTap(): void { wx.showToast({ title: '功能待接入', icon: 'none' }) },

  onLogout(): void {
    wx.showModal({
      title: '退出登录',
      content: '退出后需要重新登录才能使用小程序。',
      confirmColor: '#a3433a',
      success: result => { if (result.confirm) logout() },
    })
  },
})
