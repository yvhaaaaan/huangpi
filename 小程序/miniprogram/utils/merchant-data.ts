export type ContentStatus = 'published' | 'pending' | 'draft' | 'rejected' | 'closed'

export interface ProductItem {
  id: string
  title: string
  category: string
  summary: string
  image: string
  status: ContentStatus
  statusLabel: string
  updatedAt: string
  views: number
  rejectReason?: string
}

const products: ProductItem[] = [
  { id: 'p1', title: '黄陂客家传统油茶', category: '油茶', summary: '本地山茶籽与客家传统工艺制作，茶香醇厚。', image: '/static/merchant/product.jpg', status: 'published', statusLabel: '已发布', updatedAt: '07-12 16:20', views: 368 },
  { id: 'p2', title: '黄陂丝苗米', category: '丝苗米', summary: '本地种植加工，米粒细长、饭香自然。', image: '/static/real-product.jpg', status: 'pending', statusLabel: '待审核', updatedAt: '07-13 09:10', views: 0 },
  { id: 'p3', title: '客家手工米果', category: '客家食品', summary: '使用本地糯米制作，保留传统手工风味。', image: '/static/merchant/workshop.jpg', status: 'rejected', statusLabel: '已驳回', updatedAt: '07-11 11:45', views: 46, rejectReason: '商品介绍中缺少生产信息，请补充后重新提交。' },
  { id: 'p4', title: '油茶体验礼盒', category: '油茶', summary: '包含茶料、茶具与冲泡说明的体验组合。', image: '/static/merchant/merchant.jpg', status: 'draft', statusLabel: '草稿', updatedAt: '07-10 18:30', views: 0 },
]

export const merchantProfile = {
  name: '黄陂镇特色农产品合作社',
  owner: '陈店长',
  phone: '13800138000',
  address: '广东省兴宁市黄陂镇振兴路 18 号',
  hours: '08:30-18:00',
  intro: '展示黄陂油茶、丝苗米和客家食品，提供产品介绍、产地参观和研学接待服务。',
  image: '/static/merchant/merchant.jpg',
}

export const getProducts = (): ProductItem[] => products.map(item => ({ ...item }))
export const getProductById = (id: string): ProductItem | undefined => products.find(item => item.id === id)
