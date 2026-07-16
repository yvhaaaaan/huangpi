import { getProducts, merchantProfile } from '../../utils/merchant-data'
import { logout, requireAuth } from '../../utils/role-router'

Page({
  data: {
    merchant: merchantProfile,
    activePanel: 'todo',
    stats: { published: 0, pending: 0, draft: 0 },
    messages: [] as Array<{ id: string; type: string; title: string; desc: string; time: string; unread: boolean }>,
    todos: [] as Array<{ id: string; title: string; desc: string; level: string; productId?: string }>,
  },

  onShow(): void {
    if (!requireAuth(['merchant'])) return
    this.loadData()
  },
  onPullDownRefresh(): void { this.loadData(); setTimeout(() => wx.stopPullDownRefresh(), 350) },

  onPanelTap(event: WechatMiniprogram.TouchEvent): void {
    this.setData({ activePanel: event.currentTarget.dataset.panel })
  },

  loadData(): void {
    const products = getProducts()
    const rejected = products.filter(item => item.status === 'rejected')
    const pending = products.filter(item => item.status === 'pending')
    const submitted = wx.getStorageSync('merchantLastSubmit') as { title?: string; time?: string } | undefined
    this.setData({
      stats: {
        published: products.filter(item => item.status === 'published').length,
        pending: pending.length,
        draft: products.filter(item => item.status === 'draft').length,
      },
      messages: [
        ...(submitted && submitted.title ? [{ id: 'submitted', type: '提交', title: '内容已提交审核', desc: `“${submitted.title}”已进入审核流程`, time: submitted.time || '刚刚', unread: true }] : []),
        { id: 'reject', type: '审核', title: '内容审核未通过', desc: '“客家手工米果”需要补充生产信息', time: '昨天 11:45', unread: true },
        { id: 'publish', type: '发布', title: '内容已成功发布', desc: '“黄陂客家传统油茶”已在用户端展示', time: '07-12 16:20', unread: false },
      ],
      todos: [
        ...rejected.map(item => ({ id: item.id, title: `修改“${item.title}”`, desc: item.rejectReason || '请根据审核意见修改后重新提交', level: 'urgent', productId: item.id })),
        ...pending.map(item => ({ id: item.id, title: `等待“${item.title}”审核`, desc: '平台审核完成后会通过消息通知', level: 'normal' })),
      ],
    })
  },

  onTodoTap(event: WechatMiniprogram.TouchEvent): void {
    const productId = event.currentTarget.dataset.productId as string | undefined
    if (!productId) { wx.showToast({ title: '当前无需操作', icon: 'none' }); return }
    wx.setStorageSync('editProductId', productId)
    wx.redirectTo({ url: '/pages/merchant/product-edit/index' })
  },

  onMessageTap(event: WechatMiniprogram.TouchEvent): void {
    const id = event.currentTarget.dataset.id as string
    if (id === 'reject') {
      wx.setStorageSync('editProductId', 'p3')
      wx.redirectTo({ url: '/pages/merchant/product-edit/index' })
      return
    }
    wx.showToast({ title: '已读', icon: 'none' })
  },

  onLogout(): void {
    wx.showModal({
      title: '退出登录',
      content: '退出后需要重新登录商家账号。',
      confirmColor: '#a3433a',
      success: result => { if (result.confirm) logout() },
    })
  },

  onNavTap(event: WechatMiniprogram.TouchEvent): void {
    if (event.currentTarget.dataset.page === 'add') wx.redirectTo({ url: '/pages/merchant/product-edit/index' })
  },
})
