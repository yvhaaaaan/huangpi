import { getReviewItems, ReviewItem, ReviewStatus } from '../../../utils/admin-data'
import { requireAuth } from '../../../utils/role-router'

Page({
  data: {
    activeStatus: 'pending',
    filters: [
      { key: 'pending', label: '待审核' },
      { key: 'all', label: '全部' },
      { key: 'approved', label: '已通过' },
      { key: 'rejected', label: '已驳回' },
    ],
    items: [] as ReviewItem[],
    filteredItems: [] as ReviewItem[],
  },

  onShow(): void {
    if (!requireAuth(['admin'])) return
    if (!this.data.items.length) this.setData({ items: getReviewItems() }, () => this.applyFilter())
  },

  onFilterTap(event: WechatMiniprogram.TouchEvent): void {
    this.setData({ activeStatus: event.currentTarget.dataset.status }, () => this.applyFilter())
  },

  applyFilter(): void {
    const status = this.data.activeStatus
    this.setData({ filteredItems: this.data.items.filter(item => status === 'all' || item.status === status) })
  },

  updateStatus(id: string, status: ReviewStatus, statusLabel: string): void {
    const items = this.data.items.map(item => item.id === id ? { ...item, status, statusLabel } : item)
    this.setData({ items }, () => this.applyFilter())
  },

  onApproveTap(event: WechatMiniprogram.TouchEvent): void {
    const id = event.currentTarget.dataset.id as string
    wx.showModal({ title: '确认通过', content: '通过后该内容将在用户端展示。', success: result => { if (result.confirm) { this.updateStatus(id, 'approved', '已通过'); wx.showToast({ title: '审核已通过', icon: 'success' }) } } })
  },

  onRejectTap(event: WechatMiniprogram.TouchEvent): void {
    const id = event.currentTarget.dataset.id as string
    wx.showModal({ title: '确认驳回', content: '驳回后商家可根据审核意见修改并重新提交。', confirmColor: '#a3433a', success: result => { if (result.confirm) { this.updateStatus(id, 'rejected', '已驳回'); wx.showToast({ title: '已驳回', icon: 'none' }) } } })
  },

  onNavTap(event: WechatMiniprogram.TouchEvent): void { if (event.currentTarget.dataset.page === 'home') wx.redirectTo({ url: '/pages/admin/index' }) },
})
