import { getReviewItems, ReviewItem } from '../../utils/admin-data'
import { logout, requireAuth } from '../../utils/role-router'

Page({
  data: {
    adminName: '',
    stats: { pending: 0, approvedToday: 0, merchants: 12 },
    pendingItems: [] as ReviewItem[],
  },

  onShow(): void {
    const session = requireAuth(['admin'])
    if (!session) return
    const items = getReviewItems()
    this.setData({
      adminName: session.user.nickname,
      stats: { pending: items.filter(item => item.status === 'pending').length, approvedToday: 3, merchants: 12 },
      pendingItems: items.filter(item => item.status === 'pending').slice(0, 3),
    })
  },

  onReviewTap(): void { wx.redirectTo({ url: '/pages/admin/review/index' }) },
  onNavTap(event: WechatMiniprogram.TouchEvent): void { if (event.currentTarget.dataset.page === 'review') this.onReviewTap() },

  onLogout(): void {
    wx.showModal({
      title: '退出登录',
      content: '退出后需要重新登录管理员账号。',
      confirmColor: '#a3433a',
      success: result => { if (result.confirm) logout() },
    })
  },
})
