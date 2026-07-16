import { getProductById } from '../../../utils/merchant-data'
import { requireAuth } from '../../../utils/role-router'

Page({
  data: {
    id: '', title: '', category: '', summary: '', address: '', phone: '', editMode: false,
    images: [] as string[], categories: ['请选择品类', '油茶', '丝苗米', '客家食品', '农副产品', '文创伴手礼'], categoryIndex: 0,
  },

  onShow(): void {
    if (!requireAuth(['merchant'])) return
    const editProductId = wx.getStorageSync('editProductId') as string
    if (!editProductId) {
      if (this.data.editMode) this.resetForm()
      return
    }
    wx.removeStorageSync('editProductId')
    this.loadProduct(editProductId)
  },

  loadProduct(id: string): void {
    const item = getProductById(id)
    if (!item) return
    const categoryIndex = Math.max(0, this.data.categories.indexOf(item.category))
    this.setData({
      id: item.id,
      title: item.title,
      category: item.category,
      categoryIndex,
      summary: item.summary,
      images: [item.image],
      address: '广东省兴宁市黄陂镇振兴路 18 号',
      phone: '13800138000',
      editMode: true,
    })
    wx.setNavigationBarTitle({ title: '修改产品' })
  },

  resetForm(): void {
    this.setData({ id: '', title: '', category: '', summary: '', address: '', phone: '', images: [], categoryIndex: 0, editMode: false })
    wx.setNavigationBarTitle({ title: '新增产品' })
  },

  onInput(event: WechatMiniprogram.CustomEvent<{ value: string }>): void {
    const field = event.currentTarget.dataset.field as string
    this.setData({ [field]: event.detail.value })
  },
  onCategoryChange(event: WechatMiniprogram.CustomEvent<{ value: string }>): void {
    const categoryIndex = Number(event.detail.value)
    this.setData({ categoryIndex, category: categoryIndex === 0 ? '' : this.data.categories[categoryIndex] })
  },
  onChooseImage(): void {
    wx.chooseMedia({ count: 3 - this.data.images.length, mediaType: ['image'], success: result => this.setData({ images: [...this.data.images, ...result.tempFiles.map(file => file.tempFilePath)] }) })
  },
  onRemoveImage(event: WechatMiniprogram.TouchEvent): void {
    const index = Number(event.currentTarget.dataset.index)
    this.setData({ images: this.data.images.filter((_, itemIndex) => itemIndex !== index) })
  },
  validate(): boolean {
    if (!this.data.title.trim()) { wx.showToast({ title: '请填写产品名称', icon: 'none' }); return false }
    if (!this.data.category) { wx.showToast({ title: '请选择产品品类', icon: 'none' }); return false }
    if (!this.data.summary.trim()) { wx.showToast({ title: '请填写产品介绍', icon: 'none' }); return false }
    if (!this.data.images.length) { wx.showToast({ title: '请上传至少一张图片', icon: 'none' }); return false }
    return true
  },
  onSaveDraft(): void {
    wx.showToast({ title: '草稿已保存', icon: 'success' })
    setTimeout(() => { this.resetForm(); wx.redirectTo({ url: '/pages/merchant/index' }) }, 500)
  },
  onSubmit(): void {
    if (!this.validate()) return
    wx.showModal({
      title: '提交审核',
      content: '提交后内容将进入平台审核，审核结果会显示在“我的”消息中。',
      success: result => {
        if (!result.confirm) return
        wx.setStorageSync('merchantLastSubmit', { title: this.data.title, time: '刚刚' })
        wx.showToast({ title: '已提交审核', icon: 'success' })
        setTimeout(() => { this.resetForm(); wx.redirectTo({ url: '/pages/merchant/index' }) }, 600)
      },
    })
  },
  onNavTap(event: WechatMiniprogram.TouchEvent): void {
    if (event.currentTarget.dataset.page === 'mine') wx.redirectTo({ url: '/pages/merchant/index' })
  },
})
