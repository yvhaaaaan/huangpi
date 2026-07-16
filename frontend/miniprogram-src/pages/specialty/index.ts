Page({
  data: {
    activeCategory: '全部',
    categories: ['全部', '油茶', '丝苗米', '客家食品', '农副产品', '文创伴手礼'],
    products: [
      { id: 'oil-tea-gift', categoryName: '油茶', status: '已发布', title: '黄陂油茶礼盒', desc: '精选本地油茶原料，口感醇厚，适合作为伴手礼和团队采购。', merchant: '黄陂油茶工坊', address: '游客服务中心旁', contact: '138****2026', image: '/static/real-merchant.jpg' },
      { id: 'simiao-rice', categoryName: '丝苗米', status: '已发布', title: '黄陂丝苗米', desc: '本地种植加工，米粒细长、饭香自然，适合家庭日常食用。', merchant: '黄陂镇优质稻合作社', address: '农产品展示中心', contact: '136****5188', image: '/static/real-product.jpg' },
      { id: 'hakka-snack', categoryName: '客家食品', status: '已发布', title: '客家手作米果', desc: '传统手工制作，保留黄陂乡土风味。', merchant: '客家古村合作社', address: '客家古村入口', contact: '', image: '/static/specialty-2.jpg' },
      { id: 'heritage-gift', categoryName: '文创伴手礼', status: '已发布', title: '非遗纹样纪念品', desc: '提取地方纹样和乡土元素设计的文创纪念品。', merchant: '非遗文创中心', address: '非遗工坊', contact: '136****7788', image: '/static/real-workshop.jpg' },
    ],
  },

  onFilterTap(event: WechatMiniprogram.TouchEvent): void {
    const category = event.currentTarget.dataset.category as string | undefined
    if (category) this.setData({ activeCategory: category })
  },

  onNavigateTap(event: WechatMiniprogram.TouchEvent): void {
    const url = event.currentTarget.dataset.url as string | undefined
    if (url) wx.navigateTo({ url })
  },

  onMapTap(): void {
    wx.switchTab({ url: '/pages/map/index' })
  },

  onContactTap(): void {
    wx.showToast({ title: '已复制联系方式', icon: 'success' })
  },
})
