export type ReviewStatus = 'pending' | 'approved' | 'rejected'

export interface ReviewItem {
  id: string
  type: string
  title: string
  owner: string
  submittedAt: string
  image: string
  summary: string
  status: ReviewStatus
  statusLabel: string
}

const reviewItems: ReviewItem[] = [
  { id: 'r1', type: '油茶', title: '山茶油伴手礼', owner: '黄陂油茶工坊', submittedAt: '今天 09:10', image: '/static/real-tea.jpg', summary: '小瓶装冷榨山茶油，适合作为黄陂特色伴手礼。', status: 'pending', statusLabel: '待审核' },
  { id: 'r2', type: '丝苗米', title: '黄陂丝苗米', owner: '黄陂镇优质稻合作社', submittedAt: '昨天 17:26', image: '/static/real-product.jpg', summary: '本地种植加工，米粒细长、饭香自然，申请在特色产品页展示。', status: 'pending', statusLabel: '待审核' },
  { id: 'r3', type: '商家资料', title: '黄陂茶山农产品店', owner: '陈志明', submittedAt: '07-12 15:40', image: '/static/real-merchant.jpg', summary: '申请更新商家地址、联系电话和门店图片。', status: 'approved', statusLabel: '已通过' },
  { id: 'r4', type: '油茶', title: '油茶体验礼盒', owner: '黄陂油茶工坊', submittedAt: '07-11 10:18', image: '/static/real-product.jpg', summary: '商品图片存在联系方式水印，不符合展示要求。', status: 'rejected', statusLabel: '已驳回' },
]

export const getReviewItems = (): ReviewItem[] => reviewItems.map(item => ({ ...item }))
